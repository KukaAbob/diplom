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
import { ref, reactive, computed, onMounted } from 'vue'
import api from '@/api/api'
import { jwtDecode } from 'jwt-decode'

export default {
  name: 'DetailsView',
  setup() {
    const userData = ref(null)
    const profileData = reactive({
      email: '',
      phone: '',
      username: '',
      address: '',
    })
    const isLoading = ref(false)
    const errorMessage = ref('')
    const successMessage = ref('')

    const userId = computed(() => {
      try {
        const token = localStorage.getItem('jwtToken')
        if (!token) {
          console.error('JWT token not found in localStorage')
          return null
        }
        const decoded = jwtDecode(token)
        return decoded.id
      } catch (err) {
        console.error('Failed to decode JWT token:', err)
        return null
      }
    })

    const loadUserData = async () => {
      if (!userId.value) {
        errorMessage.value = 'User ID not available. Please log in again.'
        return
      }
      try {
        isLoading.value = true
        const resp = await api.get(`/api/profile/${userId.value}`)
        userData.value = resp.data

        Object.assign(profileData, {
          email: userData.value.email || '',
          phone: userData.value.phone || '',
          username: userData.value.username || '',
          address: userData.value.address || '',
        })
      } catch (err) {
        console.error('Failed to load user data:', err)
        errorMessage.value = 'Failed to load user data: ' + (err.response?.data || err.message)
        if (err.response?.status === 401) {
          // router.push('/login')
        }
      } finally {
        isLoading.value = false
      }
    }

    const saveChanges = async () => {
      isLoading.value = true
      errorMessage.value = ''
      successMessage.value = ''

      try {
        const updateData = {
          email: profileData.email,
          phone: profileData.phone,
          username: profileData.username,
          address: profileData.address,
        }
        const resp = await api.put('/api/profile/update', updateData)
        console.log('Server response:', resp.data)
        successMessage.value = 'Profile updated successfully!'
      } catch (err) {
        console.error('Failed to update profile:', err)
        if (err.response) {
          errorMessage.value = 'Server error: ' + (err.response.data || err.response.status)
        } else if (err.request) {
          errorMessage.value = 'No response from server. Check your connection.'
        } else {
          errorMessage.value = 'Request error: ' + err.message
        }
      } finally {
        isLoading.value = false
      }
    }

    onMounted(loadUserData)

    return {
      userData,
      profileData,
      isLoading,
      errorMessage,
      successMessage,
      userId,
      loadUserData,
      saveChanges,
    }
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
