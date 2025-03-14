import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080/', // Укажите ваш бэкенд
  headers: {
    'Content-Type': 'application/json',
  },
})

// Добавляем токен в заголовки перед каждым запросом
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('jwtToken')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

export default api
