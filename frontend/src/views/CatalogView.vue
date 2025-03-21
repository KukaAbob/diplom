<script setup>
import { ref, onMounted } from 'vue'
import api from '@/api/api'

const products = ref([])
const loading = ref(true)
const error = ref(null)
const selectedProduct = ref(null) // Выбранный товар для деталей
const selectedVariant = ref(null) // Выбранный вариант (размер/цвет)

// Загружаем список товаров
const fetchProducts = async () => {
  try {
    const response = await api.get('/api/product/all')
    products.value = response.data
  } catch (err) {
    error.value = 'Ошибка загрузки товаров!'
    console.error(err)
  } finally {
    loading.value = false
  }
}

// Загружаем детали конкретного товара
const fetchProductDetails = async (id) => {
  try {
    const response = await api.get(`/api/product/${id}`)
    selectedProduct.value = response.data
    selectedVariant.value = null // Сбрасываем выбор варианта
  } catch (err) {
    console.error('Ошибка загрузки деталей товара:', err)
  }
}

// Обработчик выбора варианта
const selectVariant = (variant) => {
  selectedVariant.value = variant
}

// Добавить в корзину
const addToCart = () => {
  if (!selectedVariant.value) {
    alert('Выберите размер и цвет!')
    return
  }
  console.log('Добавлено в корзину:', selectedVariant.value)
}

onMounted(fetchProducts)
</script>

<template>
  <div class="catalog">
    <h1>Каталог товаров</h1>

    <p v-if="loading">Загрузка...</p>
    <p v-if="error" class="error">{{ error }}</p>

    <div v-if="products.length" class="product-list">
      <div v-for="product in products" :key="product.id" class="product-card" @click="fetchProductDetails(product.id)">
        <div class="product-header">
          <h3>{{ product.name }}</h3>
          <span class="category">{{ product.category }}</span>
        </div>
        <p class="description">{{ product.description }}</p>
        <p class="price">{{ product.price }} ₽</p>
      </div>
    </div>

    <p v-else-if="!loading">Товары не найдены</p>

    <!-- Окно деталей товара -->
    <div v-if="selectedProduct" class="modal">
      <div class="modal-content">
        <h2>{{ selectedProduct.name }}</h2>
        <p>{{ selectedProduct.description }}</p>
        <p class="price">{{ selectedProduct.price }} ₽</p>

        <!-- Выбор варианта (размер и цвет) -->
        <div v-if="selectedProduct.variants && selectedProduct.variants.length">
          <h4>Выберите вариант:</h4>
          <div class="variants">
            <button
              v-for="variant in selectedProduct.variants"
              :key="variant.id"
              @click="selectVariant(variant)"
              :class="{ selected: selectedVariant && selectedVariant.id === variant.id }"
            >
              {{ variant.size }} - {{ variant.color }}
            </button>
          </div>
        </div>

        <button class="btn" @click="addToCart">Добавить в корзину</button>
        <button class="close-btn" @click="selectedProduct = null">Закрыть</button>
      </div>
    </div>
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
  cursor: pointer;
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

.variants {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-top: 10px;
}

.variants button {
  padding: 8px 12px;
  border: 1px solid #ccc;
  background: white;
  cursor: pointer;
}

.variants button.selected {
  background: #ff6600;
  color: white;
}

/* Модальное окно */
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background: white;
  padding: 20px;
  border-radius: 10px;
  width: 400px;
  text-align: center;
}

.close-btn {
  background: #ccc;
  border: none;
  padding: 10px 15px;
  border-radius: 5px;
  cursor: pointer;
  margin-top: 10px;
}

.close-btn:hover {
  background: #bbb;
}
</style>
