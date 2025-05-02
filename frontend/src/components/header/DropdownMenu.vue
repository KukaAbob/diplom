<template>
  <div class="dropdown-menu" :class="{ 'menu-open': isOpen }">
    <div class="menu-content">
      <!-- Кнопка закрытия -->
      <button @click="$emit('close')" class="close-button">
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
      <SearchBar />
      <!-- Категории -->
      <CategoryList />
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
</template>

<script setup>
import { defineProps, defineEmits } from 'vue'
import CategoryList from '@/components/header/CategoryList.vue'
import SearchBar from '@/components/header/SearchBar.vue'

defineProps({
  isOpen: {
    type: Boolean,
    default: false,
  },
})

defineEmits(['close'])
</script>

<style scoped>
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

.icon {
  width: 25px;
  height: 25px;
}

.logo {
  max-width: 250px;
  height: auto;
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
}
</style>
