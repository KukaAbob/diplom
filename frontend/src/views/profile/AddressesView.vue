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

    <div class="empty-state" v-if="!hasAddress && !isEditing">
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
    <div class="addresses-list" v-else-if="hasAddress">
      <div class="address-card">
        <h3>{{ address.name }}</h3>
        <p>{{ address.street }}</p>
        <p>{{ address.city }}, {{ address.zipCode }}</p>
        <p>{{ address.country }}</p>
        <div class="address-actions">
          <button class="edit-btn" @click="editAddress">Edit</button>
          <button class="delete-btn" @click="confirmDeleteAddress">Delete</button>
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
import { jwtDecode } from 'jwt-decode' // Предполагается, что у вас есть библиотека для декодирования JWT

export default {
  data() {
    return {
      userData: null,
      address: {
        street: '',
        city: '',
        zipCode: '',
        country: '',
      },
      currentAddress: {
        street: '',
        city: '',
        zipCode: '',
        country: '',
      },
      isLoading: false,
      errorMessage: '',
      successMessage: '',
      isEditing: false,
      editingExisting: false,
      showDeleteConfirm: false,
    }
  },
  computed: {
    hasAddress() {
      return this.address.street && this.address.city
    },
    // Format address as a single string for the DTO
    formattedAddressString() {
      const addr = this.currentAddress
      return `${addr.street}, ${addr.city}, ${addr.zipCode}, ${addr.country}`
    },
    // Получение userId из JWT токена
    userId() {
      try {
        const token = localStorage.getItem('jwtToken') // или другое имя ключа, где хранится ваш JWT
        if (!token) {
          console.error('JWT token not found in localStorage')
          return null
        }

        const decodedToken = jwtDecode(token)
        return decodedToken.id // в зависимости от структуры вашего JWT
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

        // Check if user has an address in the response
        if (this.userData && this.userData.address) {
          this.parseAddressString(this.userData.address)
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

    // Parse an address string back into components (useful when fetching existing data)
    parseAddressString(addressString) {
      if (!addressString) return

      try {
        // This is a simple parsing example - adjust based on your address format
        const parts = addressString.split(':')
        if (parts.length >= 2) {
          // const name = parts[0].trim(); // Name is not used in the address object
          const components = parts[1].split(',').map((item) => item.trim())

          this.address = {
            street: components[0] || '',
            city: components[1] || '',
            zipCode: components[2] || '',
            country: components[3] || '',
          }
        }
      } catch (error) {
        console.error('Error parsing address string:', error)
      }
    },

    startEditing() {
      this.isEditing = true
      this.editingExisting = false
      this.currentAddress = {
        street: '',
        city: '',
        zipCode: '',
        country: '',
      }
    },

    editAddress() {
      this.isEditing = true
      this.editingExisting = true
      this.currentAddress = { ...this.address }
    },

    cancelEditing() {
      this.isEditing = false
    },

    async saveAddress() {
      this.isLoading = true
      this.errorMessage = ''
      this.successMessage = ''

      try {
        // Create the DTO object according to the UpdateUserRequest structure
        const updateData = {
          username: this.userData.username,
          email: this.userData.email,
          phone: this.userData.phone,
          address: this.formattedAddressString,
        }

        // Make the PUT request to update the user data with the new address
        await api.put('/api/profile/update', updateData)

        // Update local data
        this.address = { ...this.currentAddress }
        this.successMessage = 'Address updated successfully'
        this.isEditing = false
      } catch (error) {
        console.error('Failed to update address:', error)
        this.errorMessage =
          'Failed to update address: ' + (error.response?.data?.message || error.message)
      } finally {
        this.isLoading = false
      }
    },

    confirmDeleteAddress() {
      this.showDeleteConfirm = true
    },

    async deleteAddress() {
      this.isLoading = true
      this.errorMessage = ''

      try {
        // Create the DTO object with empty address
        const updateData = {
          username: this.userData.username,
          email: this.userData.email,
          phone: this.userData.phone,
          address: '',
        }

        // Send the update with empty address to effectively delete it
        await api.put('/api/profile/update', updateData)

        // Clear local address data
        this.address = {
          street: '',
          city: '',
          zipCode: '',
          country: '',
        }
        this.successMessage = 'Address deleted successfully'
        this.showDeleteConfirm = false
      } catch (error) {
        console.error('Failed to delete address:', error)
        this.errorMessage =
          'Failed to delete address: ' + (error.response?.data?.message || error.message)
      } finally {
        this.isLoading = false
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
