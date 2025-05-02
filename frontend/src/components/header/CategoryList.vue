<template>
  <div class="categories">
    <h2 class="category-header">НОВИНКИ</h2>
    <h2 class="category-header" @click="toggleClothesCategory">ОДЕЖДА</h2>
    <transition name="list">
      <ul v-show="isClothesCategoryOpen" class="category-list">
        <li class="category-item">ДЖИНСЫ</li>
        <li class="category-item">БРЮКИ</li>
        <li class="category-item">ТОЛСТОВКИ</li>
        <li class="category-item">КУРТКИ</li>
        <li class="category-item">ФУТБОЛКИ</li>
        <li class="category-item">ПОЛО</li>
        <li class="category-item">ШОРТЫ</li>
        <li class="category-item">ОБУВЬ</li>
      </ul>
    </transition>
    <h2 class="category-header">СПОРТ</h2>
    <h2 class="category-header">ОБУВЬ</h2>
    <h2 class="category-header">АКСЕССУАРЫ</h2>
    <h2 class="category-header">СУМКИ</h2>
  </div>
</template>

<script setup>
import { ref } from 'vue'

// Состояние для категории "ОДЕЖДА"
const isClothesCategoryOpen = ref(false)

// Функция для переключения состояния категории
function toggleClothesCategory(event) {
  event.stopPropagation() // Предотвращаем всплытие события
  isClothesCategoryOpen.value = !isClothesCategoryOpen.value
}
</script>

<style scoped>
.categories {
  padding: 0 10px;
  margin-left: 15%;
}

.category-header {
  font-size: 18px;
  font-weight: bold;
  margin: 20px 0;
  cursor: pointer;
  user-select: none;
}

.category-header:hover {
  opacity: 0.8;
}

.category-list {
  list-style: none;
  padding: 0;
  margin: 0 0 30px 0;
  overflow: hidden;
}

.category-item {
  padding: 8px 0;
  font-size: 16px;
  cursor: pointer;
}

/* Стили для анимации выпадающего списка */
.list-enter-active,
.list-leave-active {
  transition: all 0.5s ease;
  max-height: 500px;
  opacity: 1;
  overflow: hidden;
}

.list-enter-from,
.list-leave-to {
  max-height: 0;
  opacity: 0;
  overflow: hidden;
}
</style>
