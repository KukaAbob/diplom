<template>
  <div class="details-container">
    <div class="details-header">
      <h2>Personal details</h2>
    </div>

    <div v-if="errorMessage" class="error-message">
      {{ errorMessage }}
    </div>

    <div v-if="successMessage" class="success-message">
      {{ successMessage }}
    </div>

    <div class="details-form">
      <div class="form-group">
        <label>Email</label>
        <input type="email" v-model="profileData.email" placeholder="Enter your email address" />
      </div>
      <div class="form-group">
        <label>Phone number</label>
        <input type="tel" v-model="profileData.phone" placeholder="Enter your phone number" />
      </div>
      <div class="action-button">
        <button @click="saveChanges" :disabled="isLoading">
          {{ isLoading ? 'Saving...' : 'Save changes' }}
        </button>
      </div>
    </div>

    <!-- Отладочная информация -->
    <div v-if="debugMode" class="debug-panel">
      <h3>Debug Information</h3>
      <pre>{{ JSON.stringify(profileData, null, 2) }}</pre>
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
      profileData: {
        email: '',
        phone: '',
        username: '',
        address: '', // Сохраняем адрес, но не будем его менять
      },
      isLoading: false,
      errorMessage: '',
      successMessage: '',
    }
  },
  computed: {
    // Получение userId из JWT токена
    userId() {
      try {
        const token = localStorage.getItem('jwtToken') // или другое имя ключа, где хранится ваш JWT
        if (!token) {
          console.error('JWT token not found in localStorage')
          return null
        }

        const decodedToken = jwtDecode(token)
        return decodedToken.id // Используем поле "id" из вашего payload
      } catch (error) {
        console.error('Failed to decode JWT token:', error)
        return null
      }
    },
  },
  mounted() {
    this.loadUserData()
  },
  methods: {
    async loadUserData() {
      if (!this.userId) {
        this.errorMessage = 'User ID not available. Please log in again.'
        return
      }

      try {
        this.isLoading = true

        // Загружаем данные профиля по ID из JWT токена
        const response = await api.get(`/api/profile/${this.userId}`)
        this.userData = response.data

        // Заполняем profileData данными с сервера
        this.profileData = {
          email: this.userData.email || '',
          phone: this.userData.phone || '',
          username: this.userData.username || '',
          address: this.userData.address || '', // Сохраняем адрес, но не будем его менять
        }

        console.log('Loaded user data:', this.profileData)
      } catch (error) {
        console.error('Failed to load user data:', error)
        this.errorMessage = 'Failed to load user data: ' + (error.response?.data || error.message)

        // Если ошибка связана с авторизацией, можно перенаправить на страницу входа
        if (error.response?.status === 401) {
          // this.$router.push('/login');
        }
      } finally {
        this.isLoading = false
      }
    },

    async saveChanges() {
      this.isLoading = true
      this.errorMessage = ''
      this.successMessage = ''

      try {
        console.log('Sending profile data:', this.profileData)

        // Отправляем обновленные данные email и phone, сохраняя существующий адрес
        const updateData = {
          email: this.profileData.email,
          phone: this.profileData.phone,
          username: this.profileData.username,
          address: this.profileData.address, // Передаем существующий адрес
        }

        // Отправляем данные на сервер
        const response = await api.put('/api/profile/update', updateData)
        console.log('Server response:', response.data)
        this.successMessage = 'Profile updated successfully!'
      } catch (error) {
        console.error('Failed to update profile:', error)
        if (error.response) {
          console.error('Error response data:', error.response.data)
          this.errorMessage = 'Server error: ' + (error.response.data || error.response.status)
        } else if (error.request) {
          this.errorMessage = 'No response from server. Check your connection.'
        } else {
          this.errorMessage = 'Request error: ' + error.message
        }
      } finally {
        this.isLoading = false
      }
    },
  },
}
</script>

<style scoped>
.details-container {
  padding: 20px 0;
}
.details-header {
  margin-bottom: 30px;
}
.details-form {
  max-width: 500px;
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
.action-button {
  margin-top: 30px;
}
.action-button button {
  padding: 12px 30px;
  background: #000;
  color: white;
  border: none;
  border-radius: 30px;
  cursor: pointer;
}
.action-button button:disabled {
  background: #999;
  cursor: not-allowed;
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
.debug-panel {
  margin-top: 30px;
  padding: 15px;
  background-color: #f5f5f5;
  border: 1px solid #ddd;
  border-radius: 4px;
}
.debug-panel pre {
  white-space: pre-wrap;
  font-family: monospace;
}
</style>
