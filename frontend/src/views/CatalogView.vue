<script setup>
import { ref, onMounted } from 'vue'
import api from '@/api/api' // Подключаем API-инстанс

const products = ref([])
const loading = ref(true)
const error = ref(null)

// Функция загрузки товаров
const fetchProducts = async () => {
  try {
    const response = await api.get('/api/product/all') // Запрос с токеном
    products.value = response.data
  } catch (err) {
    error.value = 'Ошибка загрузки товаров!'
    console.error(err)
  } finally {
    loading.value = false
  }
}

// Загружаем товары при монтировании
onMounted(fetchProducts)
</script>

<template>
  <div class="catalog">
    <h1>Каталог товаров</h1>

    <p v-if="loading">Загрузка...</p>
    <p v-if="error" class="error">{{ error }}</p>

    <div v-if="products.length" class="product-list">
      <div v-for="product in products" :key="product.id" class="product-card">
        <div class="product-header">
          <h3>{{ product.name }}</h3>
          <span class="category">{{ product.category.name }}</span>
        </div>
        <p class="description">{{ product.description }}</p>
        <p class="price">{{ product.price }} ₽</p>
        <button class="btn">Добавить в корзину</button>
      </div>
    </div>

    <p v-else-if="!loading">Товары не найдены</p>
  </div>
</template>

<style scoped>
.catalog {
  max-width: 1200px;
  margin: 20px auto;
  padding: 10px;
  text-align: center;
}

.product-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.product-card {
  border: 1px solid #ddd;
  border-radius: 10px;
  padding: 15px;
  text-align: left;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  background: #fff;
}

.product-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.category {
  font-size: 12px;
  color: #777;
  background: #f3f3f3;
  padding: 3px 7px;
  border-radius: 5px;
}

.description {
  font-size: 14px;
  color: #555;
  margin: 5px 0;
}

.price {
  font-size: 18px;
  font-weight: bold;
  color: #e44d26;
}

.btn {
  background: #ff6600;
  color: white;
  border: none;
  padding: 10px 15px;
  border-radius: 5px;
  cursor: pointer;
  margin-top: 10px;
  width: 100%;
}

.btn:hover {
  background: #e65c00;
}
</style>
