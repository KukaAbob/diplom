<template>
  <div class="checkout-container">
    <!-- Прогресс оформления заказа -->
    <div class="progress-container">
      <div class="step-container">
        <div :class="['step-circle', currentStep >= 1 ? 'step-active' : 'step-inactive']">1</div>
        <div
          :class="['step-line', currentStep >= 2 ? 'step-line-active' : 'step-line-inactive']"
        ></div>
        <div :class="['step-circle', currentStep >= 2 ? 'step-active' : 'step-inactive']">2</div>
        <div
          :class="['step-line', currentStep >= 3 ? 'step-line-active' : 'step-line-inactive']"
        ></div>
        <div :class="['step-circle', currentStep >= 3 ? 'step-active' : 'step-inactive']">3</div>
      </div>
      <div class="step-labels">
        <div class="step-label">Адрес</div>
        <div class="step-label">Оплата</div>
        <div class="step-label">Подтверждение</div>
      </div>
    </div>

    <!-- Отображение текущего шага -->
    <div class="checkout-card">
      <!-- Шаг 1: Выбор адреса -->
      <div v-if="currentStep === 1" class="step-content">
        <h2 class="step-title">Выберите адрес доставки</h2>

        <div v-if="loading" class="loading-spinner">
          <div class="spinner"></div>
        </div>

        <div v-else>
          <!-- Существующие адреса -->
          <div v-if="addresses.length > 0" class="address-list">
            <div
              v-for="address in addresses"
              :key="address.id"
              :class="[
                'address-item',
                selectedAddress && selectedAddress.id === address.id ? 'address-selected' : '',
              ]"
              @click="selectAddress(address)"
            >
              <div class="address-details">
                <div class="address-country-city">{{ address.country }}, {{ address.city }}</div>
                <div>{{ address.street }}</div>
                <div>{{ address.zipCode }}</div>
              </div>
              <div
                v-if="selectedAddress && selectedAddress.id === address.id"
                class="address-check"
              >
                ✓
              </div>
            </div>
          </div>
          <div v-else class="no-items-message">У вас пока нет сохраненных адресов</div>

          <!-- Форма добавления нового адреса -->
          <div class="new-address-section">
            <button v-if="!showAddressForm" class="btn-outline" @click="toggleAddressForm">
              + Добавить новый адрес
            </button>

            <div v-if="showAddressForm" class="new-address-form">
              <h3>Новый адрес</h3>
              <div class="form-group">
                <label for="country">Страна</label>
                <input
                  type="text"
                  id="country"
                  v-model="newAddress.country"
                  placeholder="Страна"
                  class="form-input"
                />
              </div>
              <div class="form-group">
                <label for="city">Город</label>
                <input
                  type="text"
                  id="city"
                  v-model="newAddress.city"
                  placeholder="Город"
                  class="form-input"
                />
              </div>
              <div class="form-group">
                <label for="street">Улица, дом</label>
                <input
                  type="text"
                  id="street"
                  v-model="newAddress.street"
                  placeholder="Улица, дом"
                  class="form-input"
                />
              </div>
              <div class="form-group">
                <label for="zipCode">Почтовый индекс</label>
                <input
                  type="text"
                  id="zipCode"
                  v-model="newAddress.zipCode"
                  placeholder="Почтовый индекс"
                  class="form-input"
                />
              </div>
              <div class="form-actions">
                <button class="btn-secondary" @click="toggleAddressForm">Отмена</button>
                <button class="btn-primary" @click="saveNewAddress" :disabled="!isNewAddressValid">
                  Сохранить
                </button>
              </div>
            </div>
          </div>
        </div>

        <div class="step-actions">
          <button class="btn-primary" @click="goToStep(2)" :disabled="!selectedAddress">
            Продолжить
          </button>
        </div>
      </div>

      <!-- Шаг 2: Выбор способа оплаты -->
      <div v-if="currentStep === 2" class="step-content">
        <h2 class="step-title">Выберите способ оплаты</h2>

        <div v-if="loading" class="loading-spinner">
          <div class="spinner"></div>
        </div>

        <div v-else>
          <!-- Существующие карты -->
          <div v-if="paymentMethods.length > 0" class="payment-list">
            <div
              v-for="payment in paymentMethods"
              :key="payment.id"
              :class="[
                'payment-item',
                selectedPayment && selectedPayment.id === payment.id ? 'payment-selected' : '',
              ]"
              @click="selectPayment(payment)"
            >
              <div class="payment-details">
                <div class="card-number">•••• •••• •••• {{ payment.cardNumber.slice(-4) }}</div>
                <div class="card-expiry">
                  Действует до: {{ formatExpiryDate(payment.expiryDate) }}
                </div>
              </div>
              <div
                v-if="selectedPayment && selectedPayment.id === payment.id"
                class="payment-check"
              >
                ✓
              </div>
            </div>
          </div>
          <div v-else class="no-items-message">У вас пока нет сохраненных карт</div>

          <!-- Форма добавления новой карты -->
          <div class="new-payment-section">
            <button v-if="!showPaymentForm" class="btn-outline" @click="togglePaymentForm">
              + Добавить новую карту
            </button>

            <div v-if="showPaymentForm" class="new-payment-form">
              <h3>Новая карта</h3>
              <div class="form-group">
                <label for="cardNumber">Номер карты</label>
                <input
                  type="text"
                  id="cardNumber"
                  v-model="newPayment.cardNumber"
                  placeholder="1234 5678 9012 3456"
                  class="form-input"
                  maxlength="16"
                />
              </div>
              <div class="form-row">
                <div class="form-group">
                  <label for="expiryMonth">Месяц</label>
                  <select id="expiryMonth" v-model="newPayment.expiryMonth" class="form-input">
                    <option
                      v-for="month in 12"
                      :key="month"
                      :value="month < 10 ? '0' + month : month.toString()"
                    >
                      {{ month < 10 ? '0' + month : month }}
                    </option>
                  </select>
                </div>
                <div class="form-group">
                  <label for="expiryYear">Год</label>
                  <select id="expiryYear" v-model="newPayment.expiryYear" class="form-input">
                    <option
                      v-for="year in 10"
                      :key="year"
                      :value="(currentYear + year - 1).toString()"
                    >
                      {{ currentYear + year - 1 }}
                    </option>
                  </select>
                </div>
                <div class="form-group">
                  <label for="cvvCode">CVV</label>
                  <input
                    type="text"
                    id="cvvCode"
                    v-model="newPayment.cvvCode"
                    placeholder="123"
                    class="form-input"
                    maxlength="3"
                  />
                </div>
              </div>
              <div class="form-actions">
                <button class="btn-secondary" @click="togglePaymentForm">Отмена</button>
                <button class="btn-primary" @click="saveNewPayment" :disabled="!isNewPaymentValid">
                  Сохранить
                </button>
              </div>
            </div>
          </div>
        </div>

        <div class="step-actions">
          <button class="btn-secondary" @click="goToStep(1)">Назад</button>
          <button class="btn-primary" @click="goToStep(3)" :disabled="!selectedPayment">
            Продолжить
          </button>
        </div>
      </div>

      <!-- Шаг 3: Подтверждение заказа -->
      <div v-if="currentStep === 3" class="step-content">
        <h2 class="step-title">Подтверждение заказа</h2>

        <div class="confirmation-content">
          <div class="confirmation-section">
            <h3 class="section-title">Адрес доставки</h3>
            <div v-if="selectedAddress" class="confirmation-details">
              <div>{{ selectedAddress.country }}, {{ selectedAddress.city }}</div>
              <div>{{ selectedAddress.street }}</div>
              <div>{{ selectedAddress.zipCode }}</div>
            </div>
          </div>

          <div class="confirmation-section">
            <h3 class="section-title">Способ оплаты</h3>
            <div v-if="selectedPayment" class="confirmation-details">
              <div class="card-number">
                •••• •••• •••• {{ selectedPayment.cardNumber.slice(-4) }}
              </div>
              <div class="card-expiry">
                Действует до: {{ formatExpiryDate(selectedPayment.expiryDate) }}
              </div>
            </div>
          </div>

          <div class="confirmation-section">
            <h3 class="section-title">Итого</h3>
            <div class="confirmation-total">
              <div class="total-row">
                <span>Товары:</span>
                <span>{{ formatPrice(orderData.subtotal) }}</span>
              </div>
              <div class="total-row">
                <span>Доставка:</span>
                <span>{{ formatPrice(orderData.delivery) }}</span>
              </div>
              <div class="total-row total-sum">
                <span>Итого к оплате:</span>
                <span>{{ formatPrice(orderData.total) }}</span>
              </div>
            </div>
          </div>
        </div>

        <div class="step-actions">
          <button class="btn-secondary" @click="goToStep(2)">Назад</button>
          <button class="btn-primary" @click="confirmOrder">Оплатить</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted, reactive } from 'vue'
import { useRouter } from 'vue-router'
import api from '@/api/api'
import { jwtDecode } from 'jwt-decode' // Добавляем импорт библиотеки для декодирования JWT

export default {
  name: 'Checkout',
  setup() {
    const router = useRouter()

    // Функция для получения ID пользователя из JWT токена
    const getUserIdFromToken = () => {
      try {
        // Получаем токен из localStorage
        const token = localStorage.getItem('jwtToken')
        if (!token) {
          console.error('JWT токен не найден')
          router.push('/login') // Перенаправляем на страницу входа, если токен отсутствует
          return null
        }
        
        // Декодируем токен для получения payload
        const decodedToken = jwtDecode(token)
        
        // Получаем ID пользователя из payload
        // Примечание: в зависимости от структуры вашего токена, 
        // путь к ID может отличаться (например, sub, userId, id и т.д.)
        const userId = decodedToken.id
        
        if (!userId) {
          console.error('ID пользователя не найден в токене')
          router.push('/login')
          return null
        }
        
        return userId
      } catch (error) {
        console.error('Ошибка при декодировании JWT токена:', error)
        router.push('/login')
        return null
      }
    }

    // Общие состояния
    const currentStep = ref(1)
    const loading = ref(false)
    const userId = ref(getUserIdFromToken()) // Получаем ID из JWT вместо статического значения

    // Данные для шага 1: Адреса
    const addresses = ref([])
    const selectedAddress = ref(null)
    const showAddressForm = ref(false)
    const newAddress = reactive({
      country: '',
      city: '',
      street: '',
      zipCode: '',
    })

    // Данные для шага 2: Оплата
    const paymentMethods = ref([])
    const selectedPayment = ref(null)
    const showPaymentForm = ref(false)
    const newPayment = reactive({
      cardNumber: '',
      expiryMonth: '',
      expiryYear: '',
      cvvCode: '',
    })
    const currentYear = new Date().getFullYear()

    // Данные для шага 3: Подтверждение
    const orderData = reactive({
      subtotal: 12500,
      delivery: 500,
      total: 13000,
    })

    // Вычисляемые свойства
    const isNewAddressValid = computed(() => {
      return newAddress.country && newAddress.city && newAddress.street && newAddress.zipCode
    })

    const isNewPaymentValid = computed(() => {
      const cardNumberValid = newPayment.cardNumber && newPayment.cardNumber.length === 16
      const expiryValid = newPayment.expiryMonth && newPayment.expiryYear
      const cvvValid = newPayment.cvvCode && newPayment.cvvCode.length === 3

      return cardNumberValid && expiryValid && cvvValid
    })

    // Методы для навигации между шагами
    const goToStep = (step) => {
      if (step === 1) {
        currentStep.value = 1
      } else if (step === 2) {
        if (selectedAddress.value) {
          currentStep.value = 2
          fetchPaymentMethods()
        }
      } else if (step === 3) {
        if (selectedAddress.value && selectedPayment.value) {
          currentStep.value = 3
        }
      }
    }

    // Методы для шага 1: Адреса
    const fetchAddresses = async () => {
      // Проверяем актуальность ID пользователя перед запросом
      if (!userId.value) {
        userId.value = getUserIdFromToken()
        if (!userId.value) return // Если ID не получен, прерываем выполнение
      }
      
      loading.value = true
      try {
        const response = await api.get(`api/address/user/${userId.value}`)
        addresses.value = response.data
        if (addresses.value.length > 0) {
          selectedAddress.value = addresses.value[0]
        }
      } catch (error) {
        console.error('Ошибка при получении адресов:', error)
        // Если получили ошибку 401 или 403, перенаправляем на страницу входа
        if (error.response && (error.response.status === 401 || error.response.status === 403)) {
          router.push('/login')
        }
      } finally {
        loading.value = false
      }
    }

    const selectAddress = (address) => {
      selectedAddress.value = address
    }

    const toggleAddressForm = () => {
      showAddressForm.value = !showAddressForm.value
      if (showAddressForm.value) {
        Object.assign(newAddress, {
          country: '',
          city: '',
          street: '',
          zipCode: '',
        })
      }
    }

    const saveNewAddress = async () => {
      if (!isNewAddressValid.value) return
      
      // Проверяем актуальность ID пользователя перед запросом
      if (!userId.value) {
        userId.value = getUserIdFromToken()
        if (!userId.value) return
      }

      loading.value = true
      try {
        const addressData = {
          ...newAddress,
          user: {
            id: userId.value,
          },
        }

        const response = await api.post('api/address/create', addressData)
        const newAddressData = response.data
        addresses.value.push(newAddressData)
        selectedAddress.value = newAddressData
        showAddressForm.value = false
      } catch (error) {
        console.error('Ошибка при создании адреса:', error)
        // Если получили ошибку 401 или 403, перенаправляем на страницу входа
        if (error.response && (error.response.status === 401 || error.response.status === 403)) {
          router.push('/login')
        }
      } finally {
        loading.value = false
      }
    }

    // Методы для шага 2: Оплата
    const fetchPaymentMethods = async () => {
      // Проверяем актуальность ID пользователя перед запросом
      if (!userId.value) {
        userId.value = getUserIdFromToken()
        if (!userId.value) return
      }
      
      loading.value = true
      try {
        const response = await api.get(`api/payment/user/${userId.value}`)
        paymentMethods.value = response.data
        if (paymentMethods.value.length > 0) {
          selectedPayment.value = paymentMethods.value[0]
        }
      } catch (error) {
        console.error('Ошибка при получении способов оплаты:', error)
        // Если получили ошибку 401 или 403, перенаправляем на страницу входа
        if (error.response && (error.response.status === 401 || error.response.status === 403)) {
          router.push('/login')
        }
      } finally {
        loading.value = false
      }
    }

    const selectPayment = (payment) => {
      selectedPayment.value = payment
    }

    const togglePaymentForm = () => {
      showPaymentForm.value = !showPaymentForm.value
      if (showPaymentForm.value) {
        Object.assign(newPayment, {
          cardNumber: '',
          expiryMonth: '',
          expiryYear: '',
          cvvCode: '',
        })
      }
    }

    const saveNewPayment = async () => {
      if (!isNewPaymentValid.value) return
      
      // Проверяем актуальность ID пользователя перед запросом
      if (!userId.value) {
        userId.value = getUserIdFromToken()
        if (!userId.value) return
      }

      loading.value = true
      try {
        // Формируем дату истечения срока действия в формате YYYY-MM-DD
        const expiryDate = `${newPayment.expiryYear}-${newPayment.expiryMonth}-31`

        const paymentData = {
          cardNumber: newPayment.cardNumber,
          expiryDate: expiryDate,
          cvvCode: newPayment.cvvCode,
          user: {
            id: userId.value,
          },
        }

        const response = await api.post('api/payment/create', paymentData)
        const newPaymentData = response.data
        paymentMethods.value.push(newPaymentData)
        selectedPayment.value = newPaymentData
        showPaymentForm.value = false
      } catch (error) {
        console.error('Ошибка при создании способа оплаты:', error)
        // Если получили ошибку 401 или 403, перенаправляем на страницу входа
        if (error.response && (error.response.status === 401 || error.response.status === 403)) {
          router.push('/login')
        }
      } finally {
        loading.value = false
      }
    }

    // Методы для шага 3: Подтверждение
    const confirmOrder = async () => {
      // Проверяем актуальность ID пользователя перед запросом
      if (!userId.value) {
        userId.value = getUserIdFromToken()
        if (!userId.value) return
      }
      
      loading.value = true
      try {
        // Здесь можно реализовать отправку заказа на сервер
        const orderDetails = {
          userId: userId.value,
          addressId: selectedAddress.value.id,
          paymentId: selectedPayment.value.id,
          total: orderData.total,
          // Другие необходимые данные заказа
        }
        
        // Отправка данных заказа на сервер
        // await api.post('api/orders/create', orderDetails)
        
        alert('Заказ успешно оплачен!')
        // Перенаправление на страницу успешного оформления заказа
        router.push('/order-success')
      } catch (error) {
        console.error('Ошибка при оформлении заказа:', error)
        alert('Произошла ошибка при оформлении заказа. Пожалуйста, попробуйте снова.')
      } finally {
        loading.value = false
      }
    }

    // Вспомогательные методы
    const formatExpiryDate = (dateString) => {
      const date = new Date(dateString)
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const year = date.getFullYear()
      return `${month}/${year}`
    }

    const formatPrice = (price) => {
      return new Intl.NumberFormat('ru-RU', {
        style: 'currency',
        currency: 'KZT',
        minimumFractionDigits: 0,
      }).format(price)
    }

    // При создании компонента проверяем наличие токена и загружаем адреса
    onMounted(() => {
      // Проверка наличия токена и перенаправление, если его нет
      if (!localStorage.getItem('jwtToken')) {
        router.push('/login')
        return
      }
      
      fetchAddresses()
    })

    // Возвращаем все необходимые реактивные данные и методы
    return {
      // Общие состояния
      currentStep,
      loading,
      userId,

      // Данные для шага 1: Адреса
      addresses,
      selectedAddress,
      showAddressForm,
      newAddress,

      // Данные для шага 2: Оплата
      paymentMethods,
      selectedPayment,
      showPaymentForm,
      newPayment,
      currentYear,

      // Данные для шага 3: Подтверждение
      orderData,

      // Вычисляемые свойства
      isNewAddressValid,
      isNewPaymentValid,

      // Методы
      goToStep,
      fetchAddresses,
      selectAddress,
      toggleAddressForm,
      saveNewAddress,
      fetchPaymentMethods,
      selectPayment,
      togglePaymentForm,
      saveNewPayment,
      confirmOrder,
      formatExpiryDate,
      formatPrice,
    }
  },
}
</script>

<style>
.checkout-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 32px 16px;
  font-family: 'Roboto', sans-serif;
}

/* Прогресс оформления заказа */
.progress-container {
  margin-bottom: 32px;
}

.step-container {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 8px;
}

.step-circle {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  transition: all 0.3s ease;
}

.step-active {
  background-color: #2563eb;
  color: white;
}

.step-inactive {
  background-color: #d1d5db;
  color: #374151;
}

.step-line {
  height: 3px;
  width: 80px;
  transition: all 0.3s ease;
}

.step-line-active {
  background-color: #2563eb;
}

.step-line-inactive {
  background-color: #d1d5db;
}

.step-labels {
  display: flex;
  justify-content: space-around;
  text-align: center;
}

.step-label {
  width: 120px;
  font-size: 14px;
  color: #4b5563;
}

/* Карточка оформления заказа */
.checkout-card {
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  padding: 24px;
}

.step-content {
  min-height: 400px;
  display: flex;
  flex-direction: column;
}

.step-title {
  font-size: 22px;
  font-weight: 600;
  margin-bottom: 24px;
  color: #111827;
}

/* Общие стили для списков и элементов */
.address-list,
.payment-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-bottom: 24px;
}

.address-item,
.payment-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.address-item:hover,
.payment-item:hover {
  border-color: #93c5fd;
  background-color: #f0f9ff;
}

.address-selected,
.payment-selected {
  border-color: #2563eb;
  background-color: #eff6ff;
}

.address-check,
.payment-check {
  color: #2563eb;
  font-size: 20px;
  font-weight: bold;
}

.no-items-message {
  padding: 16px;
  text-align: center;
  color: #6b7280;
  font-style: italic;
  margin-bottom: 24px;
}

/* Стили для форм */
.new-address-section,
.new-payment-section {
  margin-bottom: 24px;
}

.new-address-form,
.new-payment-form {
  margin-top: 16px;
  padding: 16px;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  background-color: #f9fafb;
}

.new-address-form h3,
.new-payment-form h3 {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 16px;
  color: #111827;
}

.form-group {
  margin-bottom: 16px;
}

.form-row {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
}

.form-row .form-group {
  flex: 1;
  margin-bottom: 0;
}

label {
  display: block;
  margin-bottom: 4px;
  font-size: 14px;
  color: #4b5563;
}

.form-input {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #d1d5db;
  border-radius: 6px;
  font-size: 16px;
}

.form-input:focus {
  outline: none;
  border-color: #2563eb;
  box-shadow: 0 0 0 1px #93c5fd;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 16px;
}

/* Стили для подтверждения заказа */
.confirmation-content {
  flex: 1;
}

.confirmation-section {
  margin-bottom: 24px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 12px;
  color: #374151;
}

.confirmation-details {
  padding: 12px;
  background-color: #f9fafb;
  border-radius: 6px;
  border: 1px solid #e5e7eb;
}

.confirmation-total {
  padding: 12px;
  background-color: #f9fafb;
  border-radius: 6px;
  border: 1px solid #e5e7eb;
}

.total-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
}

.total-sum {
  margin-top: 12px;
  padding-top: 12px;
  border-top: 1px solid #e5e7eb;
  font-weight: 600;
  font-size: 18px;
}

/* Стили для карточек оплаты */
.card-number {
  font-weight: 500;
  margin-bottom: 4px;
}

.card-expiry {
  font-size: 14px;
  color: #6b7280;
}

/* Стили для кнопок */
.step-actions {
  display: flex;
  justify-content: space-between;
  margin-top: auto;
  padding-top: 24px;
}

.btn-primary {
  background-color: #2563eb;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 6px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.btn-primary:hover {
  background-color: #1d4ed8;
}

.btn-primary:disabled {
  background-color: #93c5fd;
  cursor: not-allowed;
}

.btn-secondary {
  background-color: white;
  color: #4b5563;
  border: 1px solid #d1d5db;
  padding: 10px 20px;
  border-radius: 6px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-secondary:hover {
  background-color: #f3f4f6;
  border-color: #9ca3af;
}

.btn-outline {
  background-color: transparent;
  color: #2563eb;
  border: 1px solid #2563eb;
  padding: 10px 20px;
  border-radius: 6px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-outline:hover {
  background-color: #eff6ff;
}

/* Стили для индикатора загрузки */
.loading-spinner {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 200px;
}

.spinner {
  width: 40px;
  height: 40px;
  border: 4px solid rgba(37, 99, 235, 0.1);
  border-left-color: #2563eb;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
</style>
