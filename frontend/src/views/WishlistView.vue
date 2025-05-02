<template>
  <div class="wishlist-container">
    <div class="back-button" @click="goBack">
      <i class="back-arrow">←</i>
      <span>Back</span>
    </div>

    <div class="wishlist-header">
      <h1 class="wishlist-title">
        ВАШ СПИСОК ЖЕЛАНИЙ
        <span class="wishlist-count">{{ wishlistData?.items?.length || 0 }} ТОВАРЫ</span>
      </h1>
      <div class="wishlist-actions">
        <button class="select-button">ВЫБРАТЬ</button>
        <button class="menu-button">≡</button>
      </div>
    </div>

    <div v-if="loading" class="wishlist-loading">Загрузка...</div>

    <div v-else-if="error" class="wishlist-error">
      {{ error }}
    </div>

    <div v-else-if="!isAuthenticated" class="wishlist-error">
      Пожалуйста, войдите в систему для просмотра списка желаний
    </div>

    <div v-else class="wishlist-items">
      <div v-for="item in wishlistData?.items" :key="item.id" class="wishlist-item">
        <div class="item-image-container">
          <img :src="getImageUrl(item.productId)" alt="Product image" class="item-image" />
          <button class="remove-button" @click="removeFromWishlist(item.productId)">✕</button>
        </div>
        <div class="item-details">
          <p class="item-name">{{ item.name }}</p>
          <p class="item-price">{{ formatPrice(item.price) }} KZT</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import api from '@/api/api'
import { jwtDecode } from 'jwt-decode'
import { useRouter } from 'vue-router'

// Состояние компонента
const router = useRouter()
const wishlistData = ref(null)
const loading = ref(false)
const error = ref(null)
const userData = ref(null)

// Функция для получения и декодирования JWT токена
const getDecodedToken = () => {
  try {
    const token = localStorage.getItem('jwtToken')
    if (!token) return null

    return jwtDecode(token)
  } catch (err) {
    console.error('Ошибка при декодировании токена:', err)
    return null
  }
}

// Проверка аутентификации пользователя
const isAuthenticated = computed(() => {
  const token = localStorage.getItem('jwtToken')
  if (!token) return false

  try {
    // Проверяем срок действия токена
    const decoded = jwtDecode(token)
    const currentTime = Date.now() / 1000

    // Обновляем данные пользователя, если они еще не установлены
    if (!userData.value) {
      userData.value = decoded
    }

    return decoded.exp > currentTime
  } catch (err) {
    return false
  }
})

// Получение email пользователя из JWT токена
const getUserEmail = () => {
  const decoded = getDecodedToken()
  return decoded?.email || null
}

// Получение ID пользователя из JWT токена
const getUserId = () => {
  const decoded = getDecodedToken()
  return decoded?.id || decoded?.sub || null
}

// Настройка заголовков авторизации для запросов
const getAuthHeaders = () => {
  const token = localStorage.getItem('jwtToken')
  return {
    Authorization: token ? `Bearer ${token}` : '',
  }
}

// Загрузка списка желаний
const fetchWishlist = async () => {
  if (!isAuthenticated.value) {
    error.value = 'Требуется авторизация'
    return
  }

  loading.value = true
  error.value = null

  try {
    const email = getUserEmail()

    const response = await api.get(`/api/wishlist`, {
      params: { email },
      headers: getAuthHeaders(),
    })

    wishlistData.value = response.data
  } catch (err) {
    console.error('Ошибка при загрузке списка желаний:', err)
    if (err.response?.status === 401) {
      error.value = 'Сессия истекла. Пожалуйста, войдите снова'
    } else {
      error.value = 'Не удалось загрузить список желаний'
    }
  } finally {
    loading.value = false
  }
}

// Удаление товара из списка желаний
const removeFromWishlist = async (productId) => {
  if (!isAuthenticated.value) return

  try {
    const email = getUserEmail()

    await api.delete('/api/wishlist/delete', {
      data: { email, productId },
      headers: getAuthHeaders(),
    })

    // Обновляем список после удаления
    fetchWishlist()
  } catch (err) {
    console.error('Ошибка при удалении товара:', err)
    error.value = 'Не удалось удалить товар из списка желаний'
  }
}

// Перемещение товара в корзину
const moveToCart = async (productId) => {
  if (!isAuthenticated.value) return

  try {
    const email = getUserEmail()

    await api.post(
      '/api/wishlist/move-to-cart',
      { email, productId },
      { headers: getAuthHeaders() },
    )

    // Обновляем список после перемещения
    fetchWishlist()
  } catch (err) {
    console.error('Ошибка при перемещении товара в корзину:', err)
    error.value = 'Не удалось переместить товар в корзину'
  }
}

// Форматирование цены
const formatPrice = (price) => {
  return new Intl.NumberFormat('ru-KZ').format(price)
}

// Получение URL изображения
const getImageUrl = (productId) => {
  // Здесь должна быть логика получения изображения по ID продукта
  return `/images/products/${productId}.jpg`
}

// Загрузка списка желаний при монтировании компонента
onMounted(() => {
  // Проверяем аутентификацию и загружаем данные
  if (isAuthenticated.value) {
    userData.value = getDecodedToken()
    fetchWishlist()
  }
})

function goBack() {
  router.back()
}
</script>

<style scoped>
.wishlist-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  font-family: 'Tinos', serif;
}

.wishlist-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.wishlist-title {
  font-size: 24px;
  font-weight: bold;
  text-transform: uppercase;
}

.wishlist-count {
  font-size: 14px;
  font-weight: normal;
  margin-left: 10px;
}

.wishlist-actions {
  display: flex;
  align-items: center;
}

.select-button {
  background: none;
  border: none;
  font-weight: bold;
  text-transform: uppercase;
  margin-right: 10px;
  cursor: pointer;
}

.menu-button {
  background: none;
  border: none;
  font-size: 18px;
  cursor: pointer;
}

.wishlist-loading,
.wishlist-error {
  text-align: center;
  padding: 40px;
  font-size: 16px;
}

.wishlist-error {
  color: #e53935;
}

.wishlist-items {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
}

.wishlist-item {
  display: flex;
  flex-direction: column;
}

.item-image-container {
  position: relative;
  background-color: #f5f5f5;
  aspect-ratio: 1 / 1;
  margin-bottom: 10px;
}

.item-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.remove-button {
  position: absolute;
  top: 10px;
  right: 10px;
  width: 24px;
  height: 24px;
  background: rgba(255, 255, 255, 0.8);
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-size: 14px;
  border-radius: 50%;
}

.item-details {
  padding: 5px 0;
}

.item-name {
  font-size: 14px;
  margin-bottom: 5px;
}

.item-price {
  font-weight: bold;
  font-size: 14px;
}

@media (max-width: 768px) {
  .wishlist-items {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 480px) {
  .wishlist-items {
    grid-template-columns: 1fr;
  }
}

.back-button {
  display: flex;
  align-items: center;
  cursor: pointer;
  margin-bottom: 20px;
  max-width: 1200px;
  margin-left: auto;
  margin-right: auto;
  padding: 0 20px;
  top: 30px;
  position: relative;
}

.back-arrow {
  font-size: 24px;
  margin-right: 5px;
}
</style>
