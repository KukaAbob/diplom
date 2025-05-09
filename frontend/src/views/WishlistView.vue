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

    <div v-else class="wishlist-container-main">
      <div v-if="actionSuccessMessage" class="success-message">
        {{ actionSuccessMessage }}
      </div>

      <div class="wishlist-items">
        <div v-for="item in wishlistData?.items" :key="item.id" class="wishlist-item">
          <div class="item-image-container">
            <img
              v-if="itemImages[item.productId]"
              :src="itemImages[item.productId]"
              alt="Product image"
              class="item-image"
            />
            <div v-else class="image-placeholder">Загрузка...</div>
            <button
              class="remove-button"
              @click="removeFromWishlist(item.productId)"
              :disabled="loading && currentActionItemId === item.productId"
            >
              <span
                v-if="
                  loading && currentActionItemId === item.productId && currentAction === 'remove'
                "
                class="spinner"
              ></span>
              <span v-else>✕</span>
            </button>
          </div>
          <div class="item-details">
            <p class="item-name">{{ item.productName }}</p>
            <p
              class="item-status"
              :class="item.productStatus === 'IN_STOCK' ? 'in-stock' : 'out-of-stock'"
            >
              {{ item.productStatus === 'IN_STOCK' ? 'В наличии' : 'Нет в наличии' }}
            </p>
            <p class="item-price">{{ formatPrice(item.price) }} KZT</p>
            <button
              class="add-to-cart-button"
              @click="addToCart(item.productId)"
              :disabled="
                (loading && currentActionItemId === item.productId) ||
                item.productStatus !== 'IN_STOCK'
              "
            >
              <span
                v-if="
                  loading && currentActionItemId === item.productId && currentAction === 'addToCart'
                "
                class="spinner"
              ></span>
              <span v-else>В КОРЗИНУ</span>
            </button>
          </div>
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
const itemImages = ref({})
const currentActionItemId = ref(null)
const currentAction = ref(null)
const actionSuccessMessage = ref('')

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
    return response.data // Возвращаем данные для цепочки промисов
  } catch (err) {
    console.error('Ошибка при загрузке списка желаний:', err)
    if (err.response?.status === 401) {
      error.value = 'Сессия истекла. Пожалуйста, войдите снова'
    } else {
      error.value = 'Не удалось загрузить список желаний'
    }
    return null
  } finally {
    loading.value = false
  }
}

// Удаление товара из списка желаний
const removeFromWishlist = async (productId) => {
  if (!isAuthenticated.value) return

  try {
    const email = getUserEmail()

    // Отображаем состояние загрузки для этого конкретного товара
    loading.value = true
    currentActionItemId.value = productId
    currentAction.value = 'remove'

    // Отправляем запрос на удаление
    await api.delete('/api/wishlist/delete', {
      data: { email, productId },
      headers: getAuthHeaders(),
    })

    // Обновляем сообщение об успешном удалении
    actionSuccessMessage.value = 'Товар успешно удален из списка желаний'
    setTimeout(() => {
      actionSuccessMessage.value = ''
    }, 3000)

    // Обновляем список после удаления
    await fetchWishlist()

    // Загружаем изображения для оставшихся товаров
    await fetchProductImages()

    // Логируем успешное удаление
    console.log(`Товар с ID ${productId} успешно удален из списка желаний`)
  } catch (err) {
    console.error('Ошибка при удалении товара:', err)
    error.value = 'Не удалось удалить товар из списка желаний'
  } finally {
    loading.value = false
    currentActionItemId.value = null
    currentAction.value = null
  }
}

// Добавление товара в корзину и удаление из списка желаний
const addToCart = async (productId) => {
  if (!isAuthenticated.value) return

  try {
    const email = getUserEmail()

    // Отображаем состояние загрузки для этого конкретного товара
    loading.value = true
    currentActionItemId.value = productId
    currentAction.value = 'addToCart'

    // Отправляем запрос на добавление в корзину
    await api.post(
      '/api/cart/add',
      { email, productId, quantity: 1 }, // Добавляем 1 единицу товара
      { headers: getAuthHeaders() },
    )

    // После успешного добавления в корзину удаляем из списка желаний
    await api.delete('/api/wishlist/delete', {
      data: { email, productId },
      headers: getAuthHeaders(),
    })

    // Обновляем сообщение об успешном действии
    actionSuccessMessage.value = 'Товар успешно добавлен в корзину и удален из списка желаний'
    setTimeout(() => {
      actionSuccessMessage.value = ''
    }, 3000)

    // Обновляем список желаний после удаления
    await fetchWishlist()

    // Загружаем изображения для оставшихся товаров
    await fetchProductImages()

    console.log(`Товар с ID ${productId} успешно добавлен в корзину и удален из списка желаний`)
  } catch (err) {
    console.error('Ошибка при добавлении товара в корзину:', err)
    error.value = 'Не удалось добавить товар в корзину'
  } finally {
    loading.value = false
    currentActionItemId.value = null
    currentAction.value = null
  }
}

// Форматирование цены
const formatPrice = (price) => {
  return new Intl.NumberFormat('ru-KZ').format(price)
}

// Загрузка изображений для продуктов
const fetchProductImages = async () => {
  if (!wishlistData.value?.items || wishlistData.value.items.length === 0) return

  try {
    // Очищаем существующие изображения, если список был изменен
    itemImages.value = {}

    // Получаем первый продукт из списка для отображения его изображения
    const firstProductId = wishlistData.value.items[0].productId

    // Загружаем изображение для первого продукта
    await loadProductImage(firstProductId)

    // Загружаем изображения для остальных продуктов в фоновом режиме
    for (const item of wishlistData.value.items) {
      if (item.productId !== firstProductId) {
        loadProductImage(item.productId)
      }
    }
  } catch (err) {
    console.error('Ошибка при загрузке изображений:', err)
  }
}

// Загрузка изображения для отдельного продукта
const loadProductImage = async (productId) => {
  try {
    // Запрашиваем изображение в формате base64 через API с правильным путем
    const response = await api.get(`/api/images/all/${productId}`, {
      headers: getAuthHeaders(),
    })

    // Проверяем, что в ответе есть изображение в формате base64
    if (response.data) {
      // Формируем data URL для отображения изображения
      // Сервер возвращает base64 строку без префикса
      itemImages.value[productId] = `data:image/jpeg;base64,${response.data}`
    } else {
      console.warn(`Изображение для продукта ID ${productId} не найдено`)
      itemImages.value[productId] = null
    }
  } catch (err) {
    console.error(`Ошибка при загрузке изображения для продукта ID ${productId}:`, err)
    itemImages.value[productId] = null
  }
}

// Загрузка списка желаний при монтировании компонента
onMounted(() => {
  // Проверяем аутентификацию и загружаем данные
  if (isAuthenticated.value) {
    userData.value = getDecodedToken()
    fetchWishlist().then(() => {
      // После загрузки списка желаний загружаем изображения
      fetchProductImages()
    })
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
}

.back-button {
  display: flex;
  align-items: center;
  cursor: pointer;
  margin-bottom: 20px;
}

.back-arrow {
  font-size: 20px;
  margin-right: 5px;
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
}

.wishlist-count {
  margin-left: 10px;
  font-size: 16px;
  color: #666;
}

.wishlist-actions {
  display: flex;
  gap: 15px;
}

.select-button,
.menu-button {
  padding: 8px 15px;
  border: 1px solid #ddd;
  background: #fff;
  cursor: pointer;
}

.menu-button {
  font-size: 18px;
}

.wishlist-loading,
.wishlist-error {
  text-align: center;
  padding: 30px;
  color: #666;
}

.wishlist-items {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
}

.wishlist-item {
  border: 1px solid #eee;
  border-radius: 8px;
  overflow: hidden;
}

.item-image-container {
  position: relative;
  height: 200px;
  background: #f5f5f5;
}

.item-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.image-placeholder {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  color: #999;
}

.remove-button {
  position: absolute;
  top: 10px;
  right: 10px;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.8);
  border: none;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
}

.item-details {
  padding: 15px;
}

.item-name {
  font-weight: 500;
  margin-bottom: 8px;
}

.item-price {
  font-weight: bold;
  color: #333;
}

.item-status {
  font-size: 12px;
  margin-bottom: 5px;
}

.in-stock {
  color: #28a745;
}

.out-of-stock {
  color: #dc3545;
}

.success-message {
  background-color: #d4edda;
  color: #155724;
  padding: 15px;
  margin-bottom: 20px;
  border-radius: 4px;
  text-align: center;
}

.spinner {
  display: inline-block;
  width: 12px;
  height: 12px;
  border: 2px solid rgba(0, 0, 0, 0.1);
  border-top-color: #333;
  border-radius: 50%;
  animation: spin 1s infinite linear;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.wishlist-container-main {
  width: 100%;
}

.add-to-cart-button {
  margin-top: 10px;
  padding: 8px 15px;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 600;
  transition: background-color 0.3s;
  width: 100%;
}

.add-to-cart-button:hover {
  background-color: #45a049;
}

.add-to-cart-button:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

.spinner {
  display: inline-block;
  width: 15px;
  height: 15px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  border-top-color: #ffffff;
  animation: spin 1s ease-in-out infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}
</style>
