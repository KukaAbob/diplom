<template>
  <header class="header">
    <div class="menu" @mouseenter="isMenuOpen = true" @mouseleave="handleMouseLeave">
      <button>☰ Меню</button>
      <transition name="fade">
        <div
          v-show="isMenuOpen"
          class="dropdown-menu"
          @mouseenter="cancelCloseMenu"
          @mouseleave="scheduleCloseMenu"
        >
          <div class="menu-content">
            <div class="categories">
              <h3>Предложения для тебя</h3>
              <div class="category-tags">
                <span>Куртки</span>
                <span>Толстовки</span>
                <span>Джинсы</span>
                <span>Брюки</span>
                <span>Футболки</span>
              </div>
              <ul>
                <li>&(AND)</li>
                <li>STWD</li>
                <li>Often</li>
              </ul>
            </div>
            <div class="subcategories">
              <ul>
                <li>Рубашки</li>
                <li>Спортивный костюм</li>
                <li>Шорты</li>
                <li>Аксессуары</li>
                <li>Сумки | Рюкзаки</li>
              </ul>
              <div class="special-offers">
                <h3>Специальные предложения</h3>
                <ul>
                  <li>Новинки <span class="new">New</span></li>
                  <li>Из смесового льна</li>
                  <li>Джинсы</li>
                  <li>Брюки</li>
                  <li>Толстовки</li>
                  <li>Куртки</li>
                  <li>Футболки</li>
                  <li>Трикотаж</li>
                  <li>Обувь</li>
                </ul>
                <p class="trend">Коллаборации <span class="tag">Trend</span></p>
                <p>Собери свой комплект со скидкой до -10%</p>
              </div>
            </div>
            <div class="recommendations">
              <h3>Рекомендуем тебе</h3>
              <div class="recommend-grid">
                <img
                  v-for="i in 9"
                  :key="i"
                  src="@/assets/img/category-item1.png"
                  alt="Рекомендация"
                />
              </div>
            </div>
          </div>
        </div>
      </transition>
    </div>

    <RouterLink to="/" class="logo-container">
      <div class="logo-container">
        <img src="@/assets/img/logo.png" alt="logo" class="logo" />
      </div>
    </RouterLink>

    <div class="actions">
      <input type="text" placeholder="Search" class="search" />

      <!-- Если есть токен, отправляем на /profile -->
      <RouterLink v-if="isAuthenticated" to="/profile" class="cart-button">
        <div class="icons">
          <img src="@/assets/img/icons/profile.svg" alt="Профиль" class="icon" />
        </div>
      </RouterLink>

      <!-- Если нет токена, отправляем на регистрацию -->
      <RouterLink v-else to="/auth" class="cart-button">
        <div class="icons">
          <img src="@/assets/img/icons/profile.svg" alt="Профиль" class="icon" />
        </div>
      </RouterLink>

      <RouterLink to="/cart" class="cart-button">
        <div class="icons">
          <img src="@/assets/img/icons/cart.svg" alt="Корзина" class="icon" />
        </div>
      </RouterLink>
    </div>
  </header>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const isAuthenticated = ref(false)

// Проверяем наличие токена
onMounted(() => {
  const token = localStorage.getItem('jwtToken')
  if (token) {
    isAuthenticated.value = true
  }
})
</script>

<style scoped>
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 50px;
  height: 80px;
  background: transparent;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 1000;
  box-sizing: border-box;
}

button {
  border: 1px solid #6b6161;
  background: transparent;
  height: 40px;
  width: 100px;
  border-radius: 15px;
  font-size: 16px;
  cursor: pointer;
}

.menu {
  position: relative;
}

.dropdown-menu {
  position: absolute;
  top: 50px;
  left: 0;
  width: 40vw;
  background: white;
  border: 1px solid #ccc;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.2);
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 15px;
  opacity: 0;
  transform: translateY(-10px);
  transition:
    opacity 0.3s ease,
    transform 0.3s ease;
}

.fade-enter-active,
.fade-leave-active {
  transition:
    opacity 0.3s ease,
    transform 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

.fade-enter-to,
.fade-leave-from {
  opacity: 1;
  transform: translateY(0);
}

.menu-content {
  display: flex;
  justify-content: space-between;
}

.categories,
.subcategories,
.recommendations {
  width: 30%;
}

.category-tags span {
  display: inline-block;
  background: #f4f4f4;
  padding: 5px 10px;
  margin: 5px;
  border-radius: 5px;
  cursor: pointer;
}

.special-offers .new {
  background: orange;
  padding: 2px 5px;
  border-radius: 3px;
  font-size: 12px;
}

.trend {
  color: blue;
  font-weight: bold;
}

.tag {
  background: blue;
  color: white;
  padding: 2px 5px;
  border-radius: 3px;
}

.recommend-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px;
}

.recommend-grid img {
  width: 100%;
  height: auto;
}

.logo-container {
  flex: 5;
  display: flex;
  justify-content: center;
}

.actions {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 15px;
}

.icon {
  width: 25px;
  height: 25px;
}

.logo {
  max-width: 250px;
  height: auto;
}

.search {
  padding: 5px;
  border: 1px solid #6b6161;
  background: transparent;
  height: 30px;
  width: 250px;
  border-radius: 15px;
  font-size: 16px;
  padding-left: 15px;
}
</style>
