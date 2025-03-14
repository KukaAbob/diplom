<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import api from '@/api/api' // Импортируем API-инстанс
import { jwtDecode } from 'jwt-decode'

const router = useRouter()
const user = ref(null)
const loading = ref(true)
const error = ref(null)

// Функция получения id пользователя из токена
const getUserIdFromToken = () => {
  const token = localStorage.getItem('jwtToken')
  console.log(token)
  if (!token) return null
  try {
    const decoded = jwtDecode(token)
    return decoded.id // id должен быть в payload токена
  } catch (err) {
    console.error('Ошибка декодирования токена', err)
    return null
  }
}

// Функция загрузки профиля
const fetchUserProfile = async () => {
  const token = localStorage.getItem('jwtToken')
  const userId = getUserIdFromToken()

  if (!token || !userId) {
    router.push('/registration') // Если токена или id нет, отправляем на регистрацию
    return
  }

  try {
    const response = await api.get(`/api/profile/${userId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
    user.value = response.data
  } catch (err) {
    error.value = 'Ошибка загрузки профиля!'
    console.error(err)
  } finally {
    loading.value = false
  }
}

// Выход из аккаунта
const logout = () => {
  localStorage.removeItem('jwtToken')
  router.push('/')
}

// Загружаем данные при монтировании
onMounted(fetchUserProfile)
</script>

<template>
  <div class="profile-container">
    <h1>Профиль</h1>

    <div v-if="loading">Загрузка...</div>
    <div v-else-if="error">{{ error }}</div>
    <div v-else>
      <div class="profile-info">
        <p><strong>Имя:</strong> {{ user.firstName }}</p>
        <p><strong>Фамилия:</strong> {{ user.lastName }}</p>
        <p><strong>Email:</strong> {{ user.email }}</p>
        <p><strong>Телефон:</strong> {{ user.phone }}</p>
      </div>

      <button class="logout-button" @click="logout">Выйти</button>
    </div>
  </div>
</template>

<style scoped>
.profile-container {
  max-width: 600px;
  margin: auto;
  padding: 20px;
  text-align: center;
}

.profile-info {
  text-align: left;
  background: #f9f9f9;
  padding: 15px;
  border-radius: 10px;
  margin-bottom: 20px;
}

.logout-button {
  background: red;
  color: white;
  border: none;
  padding: 10px 20px;
  font-size: 16px;
  cursor: pointer;
  border-radius: 5px;
}
</style>
