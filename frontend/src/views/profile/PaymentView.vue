<template>
  <div class="payment-container">
    <div class="payment-header">
      <h2>Payment methods</h2>
    </div>

    <!-- Форма добавления карты -->
    <div class="add-card-form" v-if="showAddForm">
      <h3>Add Payment Method</h3>
      <form @submit.prevent="submitPaymentMethod">
        <div class="form-group">
          <label for="cardNumber">Card Number</label>
          <input
            type="text"
            id="cardNumber"
            v-model="newCard.cardNumber"
            placeholder="1234 5678 9012 3456"
            maxlength="19"
            @input="formatCardNumber"
            required
          />
        </div>
        <div class="form-row">
          <div class="form-group">
            <label for="expiryDate">Expiry Date</label>
            <input
              type="text"
              id="expiryDate"
              v-model="newCard.displayExpiryDate"
              placeholder="MM/YY"
              maxlength="5"
              @input="formatExpiryDate"
              required
            />
          </div>
          <div class="form-group">
            <label for="cvvCode">CVV</label>
            <input
              type="text"
              id="cvvCode"
              v-model="newCard.cvvCode"
              placeholder="123"
              maxlength="3"
              required
            />
          </div>
        </div>
        <div class="form-actions">
          <button type="button" class="cancel-btn" @click="cancelAddCard">Cancel</button>
          <button type="submit" class="save-btn">Save Card</button>
        </div>
      </form>
    </div>

    <!-- Пустое состояние -->
    <div class="empty-state" v-if="paymentMethods.length === 0 && !showAddForm">
      <div class="empty-icon">
        <img src="@/assets/img/icons/profile.svg" alt="Payment" />
      </div>
      <h3>No payment methods saved</h3>
      <p>You haven't saved any payment methods yet.</p>
      <div class="action-button">
        <button @click="showAddForm = true">Add payment method</button>
      </div>
    </div>

    <!-- Список карт -->
    <div class="payment-list" v-else-if="!showAddForm">
      <div class="payment-card" v-for="(method, index) in paymentMethods" :key="index">
        <div class="card-info">
          <div class="card-icon">
            <img :src="getCardIcon(method.cardNumber)" alt="Card" />
          </div>
          <div class="card-details">
            <h3>{{ getCardType(method.cardNumber) }}</h3>
            <p>•••• •••• •••• {{ getLastFour(method.cardNumber) }}</p>
            <p>Expires: {{ formatDisplayDate(method.expiryDate) }}</p>
          </div>
        </div>
        <div class="card-actions">
          <button class="delete-btn" @click="deletePaymentMethod(method.id)">Delete</button>
        </div>
      </div>
      <div class="action-button">
        <button @click="showAddForm = true">Add payment method</button>
      </div>
    </div>

    <!-- Всплывающие сообщения -->
    <div class="alert" v-if="alert.show" :class="alert.type">
      {{ alert.message }}
    </div>
  </div>
</template>

<script>
import api from '@/api/api'
import { jwtDecode } from 'jwt-decode'

export default {
  data() {
    return {
      paymentMethods: [],
      showAddForm: false,
      newCard: {
        cardNumber: '',
        expiryDate: '', // Формат для бэкенда: 'yyyy-MM-01'
        displayExpiryDate: '', // Формат для отображения: 'MM/YY'
        cvvCode: '',
      },
      alert: {
        show: false,
        message: '',
        type: 'success',
      },
    }
  },

  created() {
    this.fetchPaymentMethods()
  },

  methods: {
    // Получение списка карт пользователя
    async fetchPaymentMethods() {
      try {
        const userId = this.getUserId()
        if (!userId) {
          this.showAlert('User not authenticated', 'error')
          return
        }
        const response = await api.get(`/api/profile/${userId}`)
        // Исправление: получаем массив из поля payment
        this.paymentMethods = response.data.payment || []
      } catch (error) {
        console.error('Error fetching payment methods:', error)
        this.showAlert('Failed to load payment methods', 'error')
      }
    },

    // Добавление новой карты
    async submitPaymentMethod() {
      try {
        // Преобразование MM/YY в формат yyyy-MM-01
        const expiryDate = this.convertExpiryDateFormat(this.newCard.displayExpiryDate)

        // Подготовка данных карты
        const paymentData = {
          cardNumber: this.newCard.cardNumber.replace(/\s/g, ''),
          expiryDate: expiryDate, // В формате yyyy-MM-01
          cvvCode: this.newCard.cvvCode,
          user: {
            id: this.getUserId(),
          },
        }

        // Отправка запроса на бэкенд
        const response = await api.post('/api/payment/create', paymentData)

        // Обновление списка карт
        this.paymentMethods.push(response.data)
        this.showAlert('Payment method added successfully', 'success')
        this.resetForm()
      } catch (error) {
        console.error('Error adding payment method:', error)
        this.showAlert('Failed to add payment method', 'error')
      }
    },

    // Удаление карты
    async deletePaymentMethod(id) {
      if (confirm('Are you sure you want to delete this payment method?')) {
        try {
          await api.delete(`/api/payment/${id}`)
          this.paymentMethods = this.paymentMethods.filter((method) => method.id !== id)
          this.showAlert('Payment method deleted', 'success')
        } catch (error) {
          console.error('Error deleting payment method:', error)
          this.showAlert('Failed to delete payment method', 'error')
        }
      }
    },

    // Преобразование даты из формата MM/YY в формат yyyy-MM-01
    convertExpiryDateFormat(displayDate) {
      if (!displayDate || displayDate.length < 5) return ''

      const [month, shortYear] = displayDate.split('/')
      const fullYear = '20' + shortYear // Предполагаем, что все годы в 21 веке

      return `${fullYear}-${month}-01`
    },

    // Преобразование даты из формата yyyy-MM-dd в формат MM/YY для отображения
    formatDisplayDate(backendDate) {
      if (!backendDate) return ''

      try {
        const [year, month] = backendDate.split('-')
        const shortYear = year.slice(2)
        return `${month}/${shortYear}`
      } catch (error) {
        console.error('Error formatting date:', error)
        return backendDate
      }
    },

    // Получение ID пользователя из JWT токена
    getUserId() {
      try {
        const token = localStorage.getItem('jwtToken')
        if (!token) {
          console.error('JWT token not found in localStorage')
          return null
        }

        const decodedToken = jwtDecode(token)
        return decodedToken.id
      } catch (error) {
        console.error('Failed to decode JWT token:', error)
        return null
      }
    },

    resetForm() {
      this.newCard = {
        cardNumber: '',
        expiryDate: '',
        displayExpiryDate: '',
        cvvCode: '',
      }
      this.showAddForm = false
    },

    cancelAddCard() {
      this.resetForm()
    },

    formatCardNumber(e) {
      let value = e.target.value.replace(/\D/g, '')
      let formattedValue = ''

      for (let i = 0; i < value.length; i++) {
        if (i > 0 && i % 4 === 0) {
          formattedValue += ' '
        }
        formattedValue += value[i]
      }

      this.newCard.cardNumber = formattedValue
    },

    formatExpiryDate(e) {
      let value = e.target.value.replace(/\D/g, '')

      if (value.length > 2) {
        this.newCard.displayExpiryDate = value.substring(0, 2) + '/' + value.substring(2)
      } else {
        this.newCard.displayExpiryDate = value
      }
    },

    getCardType(cardNumber) {
      const firstDigit = cardNumber && cardNumber[0]
      if (firstDigit === '4') return 'Visa'
      if (firstDigit === '5') return 'MasterCard'
      if (firstDigit === '3') return 'American Express'
      return 'Card'
    },

    getCardIcon(cardNumber) {
      // Вернуть путь к иконке карты на основе типа карты
      return '@/assets/img/icons/credit-card.svg'
    },

    getLastFour(cardNumber) {
      if (!cardNumber) return '0000'
      const cleaned = cardNumber.replace(/\s/g, '')
      return cleaned.slice(-4)
    },

    showAlert(message, type = 'success') {
      this.alert = {
        show: true,
        message,
        type,
      }

      // Скрыть уведомление через 3 секунды
      setTimeout(() => {
        this.alert.show = false
      }, 3000)
    },
  },
}
</script>

<style scoped>
.payment-container {
  padding: 20px 0;
  position: relative;
}

.payment-header {
  margin-bottom: 30px;
}

.empty-state {
  text-align: center;
  padding: 40px 0;
}

.empty-icon {
  margin-bottom: 20px;
}

.empty-icon img {
  width: 100px;
}

.payment-card {
  border: 1px solid #eee;
  padding: 15px;
  margin-bottom: 15px;
  border-radius: 4px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-info {
  display: flex;
  align-items: center;
}

.card-icon {
  margin-right: 15px;
}

.card-icon img {
  width: 40px;
}

.card-actions button {
  margin-left: 10px;
  padding: 5px 10px;
  background: none;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
}

.card-actions .delete-btn {
  color: #ff3b30;
  border-color: #ff3b30;
}

.action-button {
  margin-top: 30px;
}

.action-button button {
  padding: 12px 30px;
  border: 1px solid #000;
  background: white;
  border-radius: 30px;
  cursor: pointer;
}

/* Стили для формы добавления карты */
.add-card-form {
  background: #f9f9f9;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 30px;
}

.add-card-form h3 {
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 15px;
}

.form-row {
  display: flex;
  gap: 15px;
}

.form-row .form-group {
  flex: 1;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: 500;
}

.form-group input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}

.cancel-btn {
  padding: 10px 20px;
  border: 1px solid #ddd;
  background: white;
  border-radius: 4px;
  cursor: pointer;
}

.save-btn {
  padding: 10px 20px;
  background: #000;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

/* Стили для уведомлений */
.alert {
  position: fixed;
  top: 20px;
  right: 20px;
  padding: 15px 20px;
  border-radius: 4px;
  color: white;
  z-index: 1000;
  animation:
    fadeIn 0.3s,
    fadeOut 0.3s 2.7s;
}

.alert.success {
  background: #34c759;
}

.alert.error {
  background: #ff3b30;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes fadeOut {
  from {
    opacity: 1;
  }
  to {
    opacity: 0;
  }
}
</style>
