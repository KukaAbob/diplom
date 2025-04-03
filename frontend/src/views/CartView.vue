<template>
  <div class="cart-container">
    <div class="back-button" @click="goBack">
      <i class="back-arrow">←</i>
      <span>Back</span>
    </div>

    <div v-if="cartStore.loading || loading" class="loading">
      <div class="loader"></div>
    </div>
    <div v-else>
      <div v-if="cartStore.items && cartStore.items.length > 0">
        <h1 class="cart-title">КОРЗИНА ({{ cartStore.items.length }})</h1>

        <div class="cart-content">
          <div class="cart-items">
            <!-- First Item: Brown Jeans -->
            <div class="cart-item">
              <div class="item-image">
                <img src="@/assets/img/banner.jpg" alt="Джинсы D92 прямого кроя" />
              </div>
              <div class="item-details">
                <h3>Джинсы D92 прямого кроя</h3>
                <p>36 | Длинные | Коричневый</p>
                <p class="item-price">18 990.00 KZT</p>
                <div class="quantity-control">
                  <button class="quantity-btn">−</button>
                  <span class="quantity">1</span>
                  <button class="quantity-btn">+</button>
                </div>
              </div>
              <div class="item-actions">
                <button class="wishlist-btn">
                  <i class="heart-icon">♡</i>
                </button>
                <button class="remove-btn">
                  <i class="trash-icon">🗑</i>
                </button>
              </div>
            </div>

            <!-- Second Item: Hair Bands -->
            <div class="cart-item">
              <div class="item-image">
                <img
                  src="@/assets/img/banner.jpg"
                  alt="Набор из двух эластичных повязок на голову"
                />
              </div>
              <div class="item-details">
                <h3>Набор из двух эластичных повязок на голову</h3>
                <p>Единый размер | Небесно-голубой</p>
                <p class="item-price">4 290.00 KZT</p>
                <div class="quantity-control">
                  <button class="quantity-btn">−</button>
                  <span class="quantity">1</span>
                  <button class="quantity-btn">+</button>
                </div>
              </div>
              <div class="item-actions">
                <button class="wishlist-btn">
                  <i class="heart-icon">♡</i>
                </button>
                <button class="remove-btn">
                  <i class="trash-icon">🗑</i>
                </button>
              </div>
            </div>

            <!-- Third Item: Leather Sandals -->
            <div class="cart-item">
              <div class="item-image">
                <img src="@/assets/img/banner.jpg" alt="Кожаные сандалии с ремешками" />
              </div>
              <div class="item-details">
                <h3>Кожаные сандалии с ремешками</h3>
                <p>41 | КОРИЧНЕВЫЙ</p>
                <p class="item-price">21 590.00 KZT</p>
                <div class="quantity-control">
                  <button class="quantity-btn">−</button>
                  <span class="quantity">1</span>
                  <button class="quantity-btn">+</button>
                </div>
              </div>
              <div class="item-actions">
                <button class="wishlist-btn">
                  <i class="heart-icon">♡</i>
                </button>
                <button class="remove-btn">
                  <i class="trash-icon">🗑</i>
                </button>
              </div>
            </div>
          </div>

          <!-- Order Information -->
          <div class="order-info">
            <h2 class="info-title">ИНФОРМАЦИЯ</h2>
            <div class="info-row">
              <span>Товаров: 3</span>
              <span>44 870.00 KZT</span>
            </div>
            <div class="info-row">
              <span>Доставка на дом</span>
              <span class="free-delivery">Бесплатно!</span>
            </div>
            <div class="info-row total">
              <span>ИТОГО</span>
              <span>44 870.00 KZT</span>
            </div>
            <div class="total-taxes">
              <span>С учетом налогов</span>
            </div>

            <button class="checkout-btn">ОФОРМИТЬ ЗАКАЗ</button>

            <div class="promo-code">
              <span class="promo-icon">✓</span>
              <span>У тебя есть промокод?</span>
              <i class="chevron-icon">›</i>
            </div>

            <div class="gift-options">
              <i class="gift-icon">🎁</i>
              <span>Выбери варианты подарка далее</span>
            </div>
          </div>
        </div>
      </div>

      <!-- Empty Cart -->
      <div v-else class="empty-cart">
        <h1 class="cart-title">ВАША КОРЗИНА ПУСТА</h1>
        <p>Вы также можете восстановить свою корзину для покупок при входе в систему.</p>
        <button class="login-btn">ВОЙТИ В АККАУНТ</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useCartStore } from '@/stores/cart'
import { useRouter } from 'vue-router'

const cartStore = useCartStore()
const router = useRouter()
const loading = ref(false)

onMounted(async () => {
  await loadCart()
})

async function loadCart() {
  loading.value = true
  try {
    await cartStore.fetchCart()
  } catch (error) {
    console.error('Ошибка при загрузке корзины:', error)
  } finally {
    loading.value = false
  }
}

function removeFromCart(itemId) {
  cartStore.removeFromCart(itemId)
}

async function increaseQuantity(item) {
  await cartStore.addToCart({ productId: item.productId, quantity: 1 })
}

async function decreaseQuantity(item) {
  if (item.quantity <= 1) {
    removeFromCart(item.id)
    return
  }
  await cartStore.addToCart({ productId: item.productId, quantity: -1 })
}

async function addToWishlist(item) {
  try {
    const token = localStorage.getItem('jwtToken')
    if (!token) {
      alert('Необходимо войти в систему')
      router.push('/auth')
      return
    }

    const userEmail = cartStore.getEmailFromToken(token)
    await api.post(
      '/api/wishlist/add',
      {
        email: userEmail,
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
