<template>
  <div class="addresses-container">
    <div class="addresses-header">
      <h2>Addresses</h2>
    </div>

    <div v-if="errorMessage" class="error-message">
      {{ errorMessage }}
    </div>

    <div v-if="successMessage" class="success-message">
      {{ successMessage }}
    </div>

    <!-- Пустое состояние (нет адресов) -->
    <div class="empty-state" v-if="addresses.length === 0 && !isEditing">
      <div class="empty-icon">
        <img src="@/assets/img/icons/profile.svg" alt="Address" />
      </div>
      <h3>No addresses saved</h3>
      <p>You haven't saved any delivery addresses yet.</p>
      <div class="action-button">
        <button @click="startEditing">Add new address</button>
      </div>
    </div>

    <!-- Форма для редактирования адреса -->
    <div v-if="isEditing" class="address-form">
      <h3>{{ editingExisting ? 'Edit' : 'Add' }} Address</h3>
      <div class="form-group">
        <label>Street</label>
        <input type="text" v-model="currentAddress.street" placeholder="Enter street" />
      </div>
      <div class="form-group">
        <label>City</label>
        <input type="text" v-model="currentAddress.city" placeholder="Enter city" />
      </div>
      <div class="form-group">
        <label>Zip Code</label>
        <input type="text" v-model="currentAddress.zipCode" placeholder="Enter zip code" />
      </div>
      <div class="form-group">
        <label>Country</label>
        <input type="text" v-model="currentAddress.country" placeholder="Enter country" />
      </div>
      <div class="form-actions">
        <button class="cancel-btn" @click="cancelEditing">Cancel</button>
        <button class="save-btn" @click="saveAddress" :disabled="isLoading">
          {{ isLoading ? 'Saving...' : 'Save' }}
        </button>
      </div>
    </div>

    <!-- Отображение списка адресов -->
    <div class="addresses-list" v-else-if="addresses.length > 0">
      <div class="address-card" v-for="(addr, index) in addresses" :key="index">
        <p>{{ addr.street }}</p>
        <p>{{ addr.city }}, {{ addr.zipCode }}</p>
        <p>{{ addr.country }}</p>
        <div class="address-actions">
          <button class="edit-btn" @click="editAddress(addr, index)">Edit</button>
          <button class="delete-btn" @click="confirmDeleteAddress(addr.id)">Delete</button>
        </div>
      </div>
      <div class="action-button">
        <button @click="startEditing">Add new address</button>
      </div>
    </div>

    <!-- Подтверждение удаления -->
    <div v-if="showDeleteConfirm" class="delete-confirm-modal">
      <div class="modal-content">
        <h3>Delete Address</h3>
        <p>Are you sure you want to delete this address?</p>
        <div class="modal-actions">
          <button class="cancel-btn" @click="showDeleteConfirm = false">Cancel</button>
          <button class="delete-btn" @click="deleteAddress" :disabled="isLoading">
            {{ isLoading ? 'Deleting...' : 'Delete' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import api from '@/api/api'
import { jwtDecode } from 'jwt-decode'

export default {
  data() {
    return {
      userData: null,
      addresses: [], // Массив адресов
      currentAddress: {
        id: null,
        street: '',
        city: '',
        zipCode: '',
        country: '',
      },
      selectedAddressId: null,
      isLoading: false,
      errorMessage: '',
      successMessage: '',
      isEditing: false,
      editingExisting: false,
      editingIndex: -1,
      showDeleteConfirm: false,
    }
  },
  computed: {
    // Получение userId из JWT токена
    userId() {
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
  },
  created() {
    // Получаем данные пользователя при создании компонента
    this.fetchUserData()
  },
  methods: {
    async fetchUserData() {
      if (!this.userId) {
        this.errorMessage = 'User ID not available. Please log in again.'
        return
      }

      try {
        this.isLoading = true

        // Fetch user profile data from the API using JWT token ID
        const response = await api.get(`/api/profile/${this.userId}`)
        this.userData = response.data

        // Исправление: получаем массив из поля address
        this.addresses = response.data.address || []

        // Если массив адресов пуст, надо показать пустое состояние
        if (this.addresses.length === 0) {
          // Пустое состояние обрабатывается в шаблоне
        }
      } catch (error) {
        console.error('Failed to fetch user data:', error)
        this.errorMessage = 'Failed to load user data: ' + error.message

        // Если ошибка связана с авторизацией, можно перенаправить на страницу входа
        if (error.response?.status === 401) {
          // Перенаправление на страницу входа, если используется Vue Router
          // this.$router.push('/login');
        }
      } finally {
        this.isLoading = false
      }
    },

    startEditing() {
      this.isEditing = true
      this.editingExisting = false
      this.editingIndex = -1
      this.currentAddress = {
        id: null,
        street: '',
        city: '',
        zipCode: '',
        country: '',
      }
    },

    editAddress(address, index) {
      this.isEditing = true
      this.editingExisting = true
      this.editingIndex = index
      this.currentAddress = { ...address }
    },

    cancelEditing() {
      this.isEditing = false
      this.successMessage = ''
      this.errorMessage = ''
    },

    async saveAddress() {
      this.isLoading = true
      this.errorMessage = ''
      this.successMessage = ''

      try {
        const addressData = {
          ...this.currentAddress,
          user: {
            id: this.userId,
          },
        }

        let response
        if (this.editingExisting) {
          // Обновление существующего адреса
          response = await api.put(`/api/address/${this.currentAddress.id}`, addressData)

          // Обновляем в локальном массиве
          if (this.editingIndex >= 0) {
            this.addresses[this.editingIndex] = response.data
          }
        } else {
          // Создание нового адреса
          response = await api.post('/api/address/create', addressData)

          // Добавляем в локальный массив
          this.addresses.push(response.data)
        }

        this.successMessage = this.editingExisting
          ? 'Address updated successfully'
          : 'Address added successfully'
        this.isEditing = false
      } catch (error) {
        console.error('Failed to save address:', error)
        this.errorMessage =
          'Failed to save address: ' + (error.response?.data?.message || error.message)
      } finally {
        this.isLoading = false
      }
    },

    confirmDeleteAddress(addressId) {
      this.selectedAddressId = addressId
      this.showDeleteConfirm = true
    },

    async deleteAddress() {
      this.isLoading = true
      this.errorMessage = ''

      try {
        // Удаление адреса по ID
        await api.delete(`/api/address/${this.selectedAddressId}`)

        // Удаляем из локального массива
        this.addresses = this.addresses.filter((addr) => addr.id !== this.selectedAddressId)

        this.successMessage = 'Address deleted successfully'
        this.showDeleteConfirm = false
      } catch (error) {
        console.error('Failed to delete address:', error)
        this.errorMessage =
          'Failed to delete address: ' + (error.response?.data?.message || error.message)
      } finally {
        this.isLoading = false
        this.selectedAddressId = null
      }
    },
  },
}
</script>

<style scoped>
.addresses-container {
  padding: 20px 0;
}
.addresses-header {
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
.address-card {
  border: 1px solid #eee;
  padding: 15px;
  margin-bottom: 15px;
  border-radius: 4px;
}
.address-actions {
  margin-top: 10px;
}
.address-actions button {
  margin-right: 10px;
  padding: 5px 10px;
  background: none;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
}
.address-actions .delete-btn {
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
.error-message {
  padding: 10px;
  margin-bottom: 20px;
  background-color: #ffecec;
  color: #f44336;
  border: 1px solid #f44336;
  border-radius: 4px;
}
.success-message {
  padding: 10px;
  margin-bottom: 20px;
  background-color: #e7f6e7;
  color: #4caf50;
  border: 1px solid #4caf50;
  border-radius: 4px;
}
.address-form {
  max-width: 500px;
  margin-bottom: 30px;
}
.form-group {
  margin-bottom: 20px;
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
  margin-top: 30px;
  display: flex;
  gap: 10px;
}
.form-actions button {
  padding: 12px 30px;
  border-radius: 30px;
  cursor: pointer;
}
.form-actions .cancel-btn {
  background: white;
  border: 1px solid #ddd;
}
.form-actions .save-btn {
  background: #000;
  color: white;
  border: none;
}
.form-actions button:disabled {
  background: #999;
  cursor: not-allowed;
}
.delete-confirm-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}
.modal-content {
  background: white;
  padding: 30px;
  border-radius: 8px;
  max-width: 400px;
  width: 100%;
}
.modal-actions {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
.modal-actions button {
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
}
.modal-actions .cancel-btn {
  background: white;
  border: 1px solid #ddd;
}
.modal-actions .delete-btn {
  background: #ff3b30;
  color: white;
  border: none;
}
</style>
