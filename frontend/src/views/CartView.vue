<template>
  <div class="cart-container">
    <div class="back-button" @click="goBack">
      <i class="back-arrow">←</i>
      <span>Back</span>
    </div>

    <div v-if="loading" class="loading">
      <div class="loader"></div>
    </div>
    <div v-else>
      <div v-if="cart && cart.items && cart.items.length > 0">
        <h1 class="cart-title">КОРЗИНА ({{ cart.items.length }})</h1>

        <div class="cart-content">
          <div class="cart-items">
            <div v-for="item in cart.items" :key="item.id" class="cart-item">
              <div class="item-image">
                <!-- Используем base64 изображение, если доступно, иначе используем резервное изображение -->
                <img 
                  :src="getProductImage(item.productId)" 
                  :alt="item.productName" 
                />
              </div>
              <div class="item-details">
                <h3>{{ item.productName }}</h3>
                <p v-if="item.attributes">{{ item.attributes }}</p>
                <p class="item-price">{{ item.price.toFixed(2) }} KZT</p>
                <div class="quantity-control">
                  <button class="quantity-btn" @click="decreaseQuantity(item)">−</button>
                  <span class="quantity">{{ item.quantity }}</span>
                  <button class="quantity-btn" @click="increaseQuantity(item)">+</button>
                </div>
              </div>
              <div class="item-actions">
                <button class="wishlist-btn" @click="addToWishlist(item)">
                  <i class="heart-icon">♡</i>
                </button>
                <button class="remove-btn" @click="removeFromCart(item.id)">
                  <i class="trash-icon">🗑</i>
                </button>
              </div>
            </div>
          </div>

          <!-- Order Information -->
          <div class="order-info">
            <h2 class="info-title">ИНФОРМАЦИЯ</h2>
            <div class="info-row">
              <span>Товаров: {{ cart.items.length }}</span>
              <span>{{ cart.totalPrice.toFixed(2) }} KZT</span>
            </div>
            <div class="info-row">
              <span>Доставка на дом</span>
              <span class="free-delivery">Бесплатно!</span>
            </div>
            <div class="info-row total">
              <span>ИТОГО</span>
              <span>{{ cart.totalPrice.toFixed(2) }} KZT</span>
            </div>
            <div class="total-taxes">
              <span>С учетом налогов</span>
            </div>

            <button class="checkout-btn" @click="checkout">ОФОРМИТЬ ЗАКАЗ</button>
          </div>
        </div>
      </div>

      <!-- Empty Cart -->
      <div v-else class="empty-cart">
        <h1 class="cart-title">ВАША КОРЗИНА ПУСТА</h1>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { useRouter } from 'vue-router'
import api from '@/api/api'
import defaultImage from '@/assets/img/banner.jpg'

const router = useRouter()
const loading = ref(false)
const cart = ref(null)
const productImages = reactive({}) // Объект для хранения изображений по ID товара
const defaultImagePath = defaultImage

onMounted(async () => {
  await loadCart()
})

function getEmailFromToken() {
  const token = localStorage.getItem('jwtToken')
  if (!token) return null
  
  try {
    // Разбираем JWT токен (он состоит из трех частей, разделенных точками)
    const base64Url = token.split('.')[1]
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/')
    const payload = JSON.parse(window.atob(base64))
    
    return payload.email
  } catch (error) {
    console.error('Ошибка при получении email из токена:', error)
    return null
  }
}

// Функция для получения изображения товара в формате, пригодном для src
// Улучшенная функция получения изображения
function getProductImage(productId) {
  const images = productImages[productId] || []
  
  if (images.length > 0 && images[0]) {
    // Изображение должно уже быть в правильном формате благодаря обработке в loadProductImages
    return images[0]
  }
  
  return defaultImagePath
}

async function loadCart() {
  loading.value = true
  
  try {
    const email = getEmailFromToken()
    if (!email) {
      loading.value = false
      return
    }
    
    const response = await api.get(`/api/cart?email=${email}`, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('jwtToken')}`
      }
    })
    
    cart.value = response.data
    
    // Загрузка изображений для каждого товара в корзине
    if (cart.value && cart.value.items) {
      for (const item of cart.value.items) {
        await loadProductImages(item.productId)
      }
    }
  } catch (error) {
    console.error('Ошибка при загрузке корзины:', error)
  } finally {
    loading.value = false
  }
}

// Функция для загрузки изображений товара (ожидается ответ в формате base64)
// Функция загрузки изображений в корзине
async function loadProductImages(productId) {
  try {
    const response = await api.get(`/api/images/all/${productId}`, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('jwtToken')}`
      }
    })
    
    // Добавляем лог для просмотра данных от сервера
    console.log(`Ответ сервера для изображений товара ${productId}:`, response.data)
    
    if (response.data) {
      productImages[productId] = response.data
        .map((imageObj) => {
          // Более универсальная обработка разных форматов данных
          const base64Data = typeof imageObj === 'object'
            ? (imageObj.oid || imageObj.base64 || imageObj.image || imageObj)
            : imageObj
          
          // Добавим дополнительный лог для отладки обработки каждого изображения
          console.log(`Обработка изображения для товара ${productId}:`, {
            isObject: typeof imageObj === 'object',
            originalData: imageObj,
            extractedData: base64Data,
            hasPrefix: base64Data ? base64Data.startsWith('data:image') : false
          })
          
          if (!base64Data) return null
          return base64Data.startsWith('data:image')
            ? base64Data
            : `data:image/jpeg;base64,${base64Data}`
        })
        .filter(Boolean) // Фильтрация null/undefined значений
      
      // Добавим лог результата обработки
      console.log(`Обработанные изображения для товара ${productId}:`, productImages[productId])
    }
  } catch (error) {
    console.error(`Ошибка при загрузке изображений для товара ${productId}:`, error)
    productImages[productId] = []
  }
}

async function removeFromCart(itemId) {
  loading.value = true
  
  try {
    await api.delete(`/api/cart/delete/${itemId}`, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('jwtToken')}`
      }
    })
    
    // Обновляем корзину после удаления
    await loadCart()
  } catch (error) {
    console.error('Ошибка при удалении товара из корзины:', error)
  } finally {
    loading.value = false
  }
}

async function updateQuantity(item, change) {
  try {
    await api.put(`/api/cart/update/${item.id}`, 
      { quantity: item.quantity + change },
      {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('jwtToken')}`
        }
      }
    )
    
    // Обновляем корзину после изменений
    await loadCart()
  } catch (error) {
    console.error('Ошибка при обновлении количества:', error)
  }
}

async function increaseQuantity(item) {
  await updateQuantity(item, 1)
}

async function decreaseQuantity(item) {
  if (item.quantity <= 1) {
    await removeFromCart(item.id)
    return
  }
  
  await updateQuantity(item, -1)
}

async function addToWishlist(item) {
  try {
    const token = localStorage.getItem('jwtToken')
    if (!token) {
      alert('Необходимо войти в систему')
      router.push('/auth')
      return
    }

    const email = getEmailFromToken()
    if (!email) return
    
    await api.post(
      '/api/wishlist/add',
      {
        email: email,
        productId: item.productId,
      },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      },
    )
    alert('Товар добавлен в список желаний')
  } catch (error) {
    console.error('Ошибка при добавлении в список желаний:', error)
    alert('Произошла ошибка при добавлении в список желаний')
  }
}

function checkout() {
  router.push('/checkout')
}

function goToLogin() {
  router.push('/auth')
}

function goBack() {
  router.back()
}
</script>

<style scoped>
.cart-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  font-family: Arial, sans-serif;
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

.cart-title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 30px;
}

.cart-content {
  display: flex;
  gap: 30px;
}

.cart-items {
  flex: 3;
}

.cart-item {
  display: flex;
  padding: 20px 0;
  border-bottom: 1px solid #eee;
  position: relative;
}

.item-image {
  width: 120px;
  height: 160px;
  margin-right: 20px;
}

.item-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.item-details {
  flex-grow: 1;
}

.item-details h3 {
  margin: 0 0 10px;
  font-size: 16px;
  font-weight: normal;
}

.item-details p {
  margin: 5px 0;
  color: #666;
  font-size: 14px;
}

.item-price {
  font-weight: bold;
  margin-top: 10px !important;
}

.quantity-control {
  display: flex;
  align-items: center;
  margin-top: 15px;
}

.quantity-btn {
  width: 30px;
  height: 30px;
  border: 1px solid #ddd;
  background: white;
  font-size: 18px;
  cursor: pointer;
}

.quantity {
  padding: 0 15px;
}

.item-actions {
  display: flex;
  position: absolute;
  right: 10px;
  top: 20px;
}

.wishlist-btn,
.remove-btn {
  background: none;
  border: none;
  cursor: pointer;
  padding: 10px;
  font-size: 18px;
}

.order-info {
  flex: 1;
  background: #f9f9f9;
  padding: 20px;
  border-radius: 4px;
  height: fit-content;
}

.info-title {
  margin-top: 0;
  font-size: 18px;
  font-weight: bold;
}

.info-row {
  display: flex;
  justify-content: space-between;
  margin: 15px 0;
  font-size: 14px;
}

.free-delivery {
  color: green;
}

.total {
  font-weight: bold;
  font-size: 18px;
  margin-top: 20px;
  padding-top: 15px;
  border-top: 1px solid #ddd;
}

.total-taxes {
  display: flex;
  font-size: 12px;
  color: #777;
  margin-top: -10px;
  justify-content: flex-end;
}

.checkout-btn {
  width: 100%;
  padding: 15px;
  background: #88c8ff;
  border: none;
  border-radius: 30px;
  color: black;
  font-weight: bold;
  cursor: pointer;
  margin: 20px 0;
  font-size: 16px;
}

.promo-code,
.gift-options {
  display: flex;
  align-items: center;
  margin: 15px 0;
  color: #666;
  font-size: 14px;
}

.promo-code {
  justify-content: space-between;
}

.promo-icon {
  margin-right: 10px;
}

.gift-icon {
  margin-right: 10px;
}

.chevron-icon {
  font-size: 18px;
}

.empty-cart {
  text-align: center;
  padding: 50px 0;
}

.empty-cart p {
  margin: 20px 0 30px;
  color: #666;
}

.login-btn {
  padding: 12px 30px;
  border: 1px solid #000;
  background: white;
  font-weight: bold;
  cursor: pointer;
}

.loading {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 300px;
}

.loader {
  border: 4px solid #f3f3f3;
  border-top: 4px solid #3498db;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  animation: spin 2s linear infinite;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

@media (max-width: 768px) {
  .cart-content {
    flex-direction: column;
  }

  .order-info {
    margin-top: 20px;
  }
}
</style>
