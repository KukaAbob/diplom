<template>
  <!-- Если пользователь авторизован -->
  <div v-if="isAuthenticated" class="profile-menu" @click="toggleProfileMenu" ref="profileMenu">
    <div class="icons">
      <img src="@/assets/img/icons/profile.svg" alt="Профиль" class="icon" />
    </div>
    <transition name="fade">
      <div v-show="isProfileMenuOpen" class="profile-dropdown">
        <div class="profile-header">
          <div class="greeting">
            <h3>Привет!</h3>
            <p class="user-email">{{ userEmail }}</p>
          </div>
          <button class="close-btn" @click.stop="isProfileMenuOpen = false">×</button>
        </div>
        <div class="profile-menu-items">
          <router-link to="/profile" class="profile-menu-item" @click="isProfileMenuOpen = false">
            <div class="menu-icon">
              <img src="@/assets/img/icons/profile.svg" alt="Профиль" />
            </div>
            <span>Мой профиль</span>
            <span class="arrow">›</span>
          </router-link>
          <router-link to="/orders" class="profile-menu-item" @click="isProfileMenuOpen = false">
            <div class="menu-icon">
              <img src="@/assets/img/icons/profile.svg" alt="Заказы" />
            </div>
            <span>Мои заказы</span>
            <span class="arrow">›</span>
          </router-link>
          <router-link to="/wishlist" class="profile-menu-item" @click="isProfileMenuOpen = false">
            <div class="menu-icon">
              <img src="@/assets/img/icons/profile.svg" alt="Избранное" />
            </div>
            <span>Избранное</span>
            <span class="arrow">›</span>
          </router-link>
          <router-link to="/address" class="profile-menu-item" @click="isProfileMenuOpen = false">
            <div class="menu-icon">
              <img src="@/assets/img/icons/profile.svg" alt="Адреса" />
            </div>
            <span>Адреса доставки</span>
            <span class="arrow">›</span>
          </router-link>
          <router-link to="/settings" class="profile-menu-item" @click="isProfileMenuOpen = false">
            <div class="menu-icon">
              <img src="@/assets/img/icons/profile.svg" alt="Настройки" />
            </div>
            <span>Настройки</span>
            <span class="arrow">›</span>
          </router-link>
          <div class="logout-container">
            <button @click="handleLogout" class="logout-btn">Выйти</button>
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
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, defineProps, defineEmits } from 'vue'

const props = defineProps({
  isAuthenticated: {
    type: Boolean,
    default: false,
  },
  userEmail: {
    type: String,
    default: '',
  },
})

const emit = defineEmits(['logout'])

const isProfileMenuOpen = ref(false)
const profileMenu = ref(null)

function toggleProfileMenu() {
  isProfileMenuOpen.value = !isProfileMenuOpen.value
}

function handleLogout() {
  isProfileMenuOpen.value = false
  emit('logout')
}

// Close menu when clicking outside
function handleClickOutside(event) {
  if (profileMenu.value && !profileMenu.value.contains(event.target)) {
    isProfileMenuOpen.value = false
  }
}

onMounted(() => {
  document.addEventListener('click', handleClickOutside)
})

onBeforeUnmount(() => {
  document.removeEventListener('click', handleClickOutside)
})
</script>

<style scoped>
.profile-menu {
  position: relative;
  cursor: pointer;
}

.icons {
  display: flex;
  align-items: center;
  justify-content: center;
}

.icon {
  width: 25px;
  height: 25px;
}

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

/* Profile Menu Styles */
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
  word-break: break-word;
  max-width: 250px;
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

/* Animation styles */
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

/* Responsive adjustments */
@media (max-width: 480px) {
  .profile-dropdown {
    width: 280px;
    right: -10px;
  }

  .profile-menu-item {
    padding: 12px 15px;
  }

  .user-email {
    max-width: 180px;
  }
}
</style>
