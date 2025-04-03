<template>
  <header class="header">
    <button @click="toggleDropdownMenu" class="menu-button">
      <span v-if="!isDropdownMenuOpen">☰</span>
      <span v-else>✕</span>
    </button>

    <!-- Выпадающее меню -->
    <div class="dropdown-menu" :class="{ 'menu-open': isDropdownMenuOpen }">
      <div class="menu-content">
        <!-- Кнопка закрытия -->
        <button @click="toggleDropdownMenu" class="close-button">
          <span>×</span>
        </button>

        <!-- Лого -->
        <div class="logo-container-1">
          <img src="@/assets/img/logo.png" alt="logo" class="logo" />
        </div>

        <!-- Кнопки справа -->
        <div class="top-buttons">
          <RouterLink to="/wishlist" class="wishlist-button">
            <img src="@/assets/img/icons/logo1.svg" alt="Избранное" class="icon" />
          </RouterLink>
          <RouterLink to="/cart" class="cart-button">
            <img src="@/assets/img/icons/logo2.svg" alt="Корзина" class="icon" />
          </RouterLink>
        </div>

        <!-- Поисковое поле -->
        <div class="search-container">
          <input type="text" placeholder="Поиск по товару, коллекции..." class="search-input" />
          <button class="search-button">
            <img src="@/assets/img/icons/logo2.svg" alt="Поиск" class="search-icon" />
          </button>
        </div>

        <!-- Категории -->
        <div class="categories">
          <h2 class="category-header">НОВИНКИ</h2>

          <RouterLink to="/catalog">
            <h2 class="category-header">ОДЕЖДА</h2>
          </RouterLink>
          <ul class="category-list">
            <li class="category-item">ДЖИНСЫ</li>
            <li class="category-item">БРЮКИ</li>
            <li class="category-item">ТОЛСТОВКИ</li>
            <li class="category-item">КУРТКИ</li>
            <li class="category-item">ФУТБОЛКИ</li>
            <li class="category-item">ПОЛО</li>
            <li class="category-item">ШОРТЫ</li>
            <li class="category-item">ОБУВЬ</li>
          </ul>
          <h2 class="category-header">СПОРТ</h2>
          <h2 class="category-header">ОБУВЬ</h2>
          <h2 class="category-header">АКСЕССУАРЫ</h2>
          <h2 class="category-header">СУМКИ</h2>
        </div>
      </div>

      <!-- Правая часть с изображением -->
      <div class="banner-container">
        <div class="banner-text">
          <span>УЗНАТЬ БОЛЬШЕ</span>
          <h2>НОВИНКИ</h2>
        </div>
        <img src="@/assets/img/banner-reg.jpg" alt="Banner" class="banner-image" />
      </div>
    </div>

    <RouterLink to="/" class="logo-container-2">
      <div class="logo-container-2">
        <img src="@/assets/img/logo.png" alt="logo" class="logo" />
      </div>
    </RouterLink>

    <div class="actions">
      <search></search>
      <input type="text" placeholder="Search" class="search" />

      <!-- Если пользователь авторизован -->
      <div
        v-if="isAuthenticated"
        class="profile-menu"
        @click="isProfileMenuOpen = !isProfileMenuOpen"
        ref="profileMenu"
      >
        <div class="icons">
          <img src="@/assets/img/icons/profile.svg" alt="Профиль" class="icon" />
        </div>
        <transition name="fade">
          <div v-show="isProfileMenuOpen" class="profile-dropdown">
            <!-- Профиль меню содержимое (оставлено без изменений) -->
            <div class="profile-header">
              <div class="greeting">
                <h3>Hello</h3>
                <p class="user-email">{{ userEmail }}</p>
              </div>
              <button class="close-btn" @click.stop="isProfileMenuOpen = false">×</button>
            </div>
            <div class="profile-menu-items">
              <RouterLink to="/profile/purchases" class="profile-menu-item">
                <div class="menu-icon">
                  <img src="@/assets/img/icons/profile.svg" alt="Purchases" />
                </div>
                <span>My purchases</span>
                <span class="arrow">›</span>
              </RouterLink>

              <RouterLink to="/profile/returns" class="profile-menu-item">
                <div class="menu-icon">
                  <img src="@/assets/img/icons/profile.svg" alt="Returns" />
                </div>
                <span>Returns</span>
                <span class="arrow">›</span>
              </RouterLink>

              <RouterLink to="/profile/details" class="profile-menu-item">
                <div class="menu-icon">
                  <img src="@/assets/img/icons/profile.svg" alt="Personal details" />
                </div>
                <span>Personal details</span>
                <span class="arrow">›</span>
              </RouterLink>

              <RouterLink to="/profile/addresses" class="profile-menu-item">
                <div class="menu-icon">
                  <img src="@/assets/img/icons/profile.svg" alt="Addresses" />
                </div>
                <span>Addresses</span>
                <span class="arrow">›</span>
              </RouterLink>

              <RouterLink to="/profile/payment" class="profile-menu-item">
                <div class="menu-icon">
                  <img src="@/assets/img/icons/profile.svg" alt="Payment methods" />
                </div>
                <span>Payment methods</span>
                <span class="arrow">›</span>
              </RouterLink>

              <RouterLink to="/profile/newsletter" class="profile-menu-item">
                <div class="menu-icon">
                  <img src="@/assets/img/icons/profile.svg" alt="Newsletter" />
                </div>
                <span>Newsletter</span>
                <span class="arrow">›</span>
              </RouterLink>

              <RouterLink to="/profile/service" class="profile-menu-item">
                <div class="menu-icon">
                  <img src="@/assets/img/icons/profile.svg" alt="Customer service" />
                </div>
                <span>Customer service</span>
                <span class="arrow">›</span>
              </RouterLink>

              <RouterLink to="/profile/privacy" class="profile-menu-item">
                <div class="menu-icon">
                  <img src="@/assets/img/icons/profile.svg" alt="Политика конфиденциальности" />
                </div>
                <span>Политика конфиденциальности</span>
                <span class="arrow">›</span>
              </RouterLink>
              <div class="logout-container">
                <button @click="logout" class="logout-btn">Log out</button>
              </div>
            </div>
          </div>
        </transition>
      </div>

      <!-- Если пользователь не авторизован -->
      <RouterLink v-else to="/auth" class="cart-button">
        <div class="icons">
          <img src="@/assets/img/icons/profile.svg" alt="Профиль" class="icon" />
        </div>
      </RouterLink>

      <RouterLink to="/wishlist" class="cart-button">
        <div class="icons">
          <img src="@/assets/img/icons/wishlist.svg" alt="Список желаний" class="icon" />
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
import { ref, onMounted, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
const router = useRouter()
const isAuthenticated = ref(false)
const isMenuOpen = ref(false)
const isProfileMenuOpen = ref(false)
const userEmail = ref('')
const profileMenu = ref(null)
let closeTimeout = null

// Добавляем переменную для управления состоянием выпадающего меню
const isDropdownMenuOpen = ref(false)

// Функция для декодирования JWT токена
function parseJwt(token) {
  try {
    const base64Url = token.split('.')[1]
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/')
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split('')
        .map(function (c) {
          return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)
        })
        .join(''),
    )
    return JSON.parse(jsonPayload)
  } catch (error) {
    console.error('Error parsing JWT:', error)
    return null
  }
}

// Функция для проверки валидности токена
function isTokenValid(token) {
  try {
    const decodedToken = parseJwt(token)
    if (!decodedToken) return false
    // Проверка срока действия токена
    const currentTime = Date.now() / 1000
    return decodedToken.exp > currentTime
  } catch (error) {
    console.error('Error validating token:', error)
    return false
  }
}

// Проверка токена при загрузке компонента
onMounted(() => {
  checkAuthentication()
  // Добавляем обработчик клика вне меню профиля
  document.addEventListener('click', handleClickOutside)
})

// Функция для проверки аутентификации
function checkAuthentication() {
  const token = localStorage.getItem('jwtToken')
  if (token && isTokenValid(token)) {
    isAuthenticated.value = true
    // Получаем email из токена
    const decodedToken = parseJwt(token)
    if (decodedToken && decodedToken.email) {
      userEmail.value = decodedToken.email
    } else {
      // Если email нет в токене, используем из localStorage или значение по умолчанию
      userEmail.value = localStorage.getItem('userEmail') || 'user@example.com'
    }
  } else {
    // Если токен отсутствует или недействителен, удаляем его
    localStorage.removeItem('jwtToken')
    localStorage.removeItem('userEmail')
    isAuthenticated.value = false
  }
}

onBeforeUnmount(() => {
  document.removeEventListener('click', handleClickOutside)
  cancelCloseMenu()
})

function handleClickOutside(event) {
  if (profileMenu.value && !profileMenu.value.contains(event.target)) {
    isProfileMenuOpen.value = false
  }
}

function handleMouseLeave() {
  scheduleCloseMenu()
}

function scheduleCloseMenu() {
  closeTimeout = setTimeout(() => {
    isMenuOpen.value = false
  }, 300)
}

function cancelCloseMenu() {
  if (closeTimeout) {
    clearTimeout(closeTimeout)
    closeTimeout = null
  }
}

function logout() {
  localStorage.removeItem('jwtToken')
  localStorage.removeItem('userEmail')
  isAuthenticated.value = false
  isProfileMenuOpen.value = false
  router.push('/')
}

// Добавляем функцию для переключения состояния выпадающего меню
function toggleDropdownMenu() {
  isDropdownMenuOpen.value = !isDropdownMenuOpen.value

  // Блокируем/разблокируем скролл на основной странице
  if (isDropdownMenuOpen.value) {
    document.body.style.overflow = 'hidden'
  } else {
    document.body.style.overflow = 'auto'
  }
}
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

.menu-button {
  border: none;
  background: transparent;
  height: auto;
  width: auto;
  font-size: 24px;
  padding: 5px;
  cursor: pointer;
}

.menu {
  position: relative;
}

/* Стили для выпадающего меню */
.dropdown-menu {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100vh;
  background-color: #fff;
  z-index: 1001;
  display: flex;
  visibility: hidden;
  opacity: 0;
  transition: all 0.3s ease;
}

.menu-open {
  visibility: visible;
  opacity: 1;
}

.menu-content {
  width: 50%;
  height: 100%;
  margin: 20px;
  margin-top: 30px;
  margin-right: 50px;
  position: relative;
  overflow-y: auto;
}

.close-button {
  position: absolute;
  left: 35px;
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  width: auto;
  height: auto;
  padding: 0;
}

.logo-container-1 {
  display: flex;
  justify-content: left;
  padding-top: 20px;
  margin-bottom: 20px;
  margin-left: 15%;
}

.logo-container-2 {
  display: flex;
  justify-content: left;
  padding-top: 20px;
  margin-bottom: 20px;
}

.top-buttons {
  position: absolute;
  top: 45px;
  right: 20px;
  display: flex;
  gap: 15px;
}

.wishlist-button,
.cart-button {
  background: none;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  text-decoration: none;
  height: auto;
  width: auto;
}

.search-container {
  position: relative;
  margin: 30px 0;
  margin-left: 15%;
  width: 900px;
}

.search-input {
  width: 100%;
  padding: 10px;
  border: none;
  border-bottom: 1px solid #ccc;
  outline: none;
  font-size: 16px;
}

.search-button {
  position: absolute;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  cursor: pointer;
  height: auto;
  width: auto;
}

.search-icon {
  width: 20px;
  height: 20px;
}

.categories {
  padding: 0 10px;
  margin-left: 15%;
}

.category-header {
  font-size: 18px;
  font-weight: bold;
  margin: 20px 0;
  cursor: pointer;
}

.category-list {
  list-style: none;
  padding: 0;
  margin: 0 0 30px 0;
}

.category-item {
  padding: 8px 0;
  font-size: 16px;
  cursor: pointer;
}

.new-tag {
  font-size: 12px;
  color: #000;
  padding: 2px 5px;
  margin-left: 5px;
}

.stradilooks {
  display: flex;
  align-items: center;
  margin-top: 20px;
}

.phone-icon {
  width: 24px;
  height: 24px;
  margin-left: 10px;
}

/* Banner styles */
.banner-container {
  width: 50%;
  height: 100%;
  position: relative;
  overflow: hidden;
}

.banner-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.banner-text {
  position: absolute;
  bottom: 40px;
  left: 40px;
  color: white;
}

.banner-text span {
  display: block;
  font-size: 14px;
  margin-bottom: 8px;
}

.banner-text h2 {
  font-size: 28px;
  margin: 0;
}

/* Profile Menu Styles */
.profile-menu {
  position: relative;
  cursor: pointer;
}

.profile-dropdown {
  position: absolute;
  top: 50px;
  right: -20px;
  width: 350px;
  background: white;
  border-radius: 8px;
  box-shadow: 0px 4px 12px rgba(0, 0, 0, 0.15);
  overflow: hidden;
  z-index: 1001;
}

.profile-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.greeting h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 500;
}

.user-email {
  margin: 5px 0 0;
  font-size: 14px;
  color: #666;
}

.close-btn {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  padding: 0;
  width: auto;
  height: auto;
}

.profile-menu-items {
  padding: 10px 0;
}

.profile-menu-item {
  display: flex;
  align-items: center;
  padding: 15px 20px;
  text-decoration: none;
  color: #000;
  transition: background-color 0.2s;
}

.profile-menu-item:hover {
  background-color: #f5f5f5;
}

.menu-icon {
  width: 24px;
  height: 24px;
  margin-right: 15px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.menu-icon img {
  width: 20px;
  height: 20px;
}

.arrow {
  margin-left: auto;
  font-size: 20px;
}

.logout-container {
  padding: 15px 20px;
  border-top: 1px solid #f0f0f0;
  margin-top: 10px;
}

.logout-btn {
  width: 100%;
  background-color: #000;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 12px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.logout-btn:hover {
  background-color: #333;
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
  width: 190px;
  border-radius: 15px;
  font-size: 16px;
  padding-left: 15px;
}

/* Responsive adjustments */
@media (max-width: 768px) {
  .dropdown-menu {
    flex-direction: column;
  }

  .menu-content,
  .banner-container {
    width: 100%;
  }

  .banner-container {
    height: 300px;
  }

  .header {
    padding: 10px 20px;
  }

  .search {
    width: 150px;
  }
}
</style>
