<template>
  <div class="product-list-page">
    <h1>Мужская одежда</h1>

    <div v-if="loading" class="loading">
      <p>Загрузка...</p>
    </div>

    <div v-else-if="error" class="error">
      <p>Произошла ошибка при загрузке товаров: {{ error }}</p>
    </div>

    <div v-else class="products-grid">
      <div
        v-for="product in maleProducts"
        :key="product.id"
        class="product-card"
        @click="goToProductDetail(product.id)"
      >
        <div class="product-image">
          <img :src="product.imageUrl || '/placeholder-image.jpg'" :alt="product.name" />
        </div>
        <div class="product-info">
          <h3>{{ product.name }}</h3>
          <p class="product-price">{{ product.price }} KZT</p>
          <p class="product-colors">
            Доступные цвета:
            <span v-for="(color, index) in getUniqueColors(product)" :key="color">
              {{ color }}{{ index < getUniqueColors(product).length - 1 ? ', ' : '' }}
            </span>
          </p>
        </div>
      </div>
    </div>

    <div v-if="maleProducts.length === 0 && !loading" class="no-products">
      <p>Товары не найдены</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import api from '@/api/api'

const router = useRouter()
const allProducts = ref([])
const loading = ref(true)
const error = ref(null)

// Fetch all products and filter by gender
const fetchProducts = async () => {
  loading.value = true
  error.value = null

  try {
    const response = await api.get('/api/product/all')
    allProducts.value = response.data
    loading.value = false
  } catch (err) {
    console.error('Ошибка при загрузке товаров:', err)
    error.value = err.message || 'Произошла ошибка при загрузке товаров'
    loading.value = false
  }
}

// Filter products by gender (MALE)
const maleProducts = computed(() => {
  return allProducts.value.filter((product) => product.gender === 'FEMALE')
})

// Get unique colors for a product
const getUniqueColors = (product) => {
  if (!product.variants || product.variants.length === 0) return []
  return [...new Set(product.variants.map((variant) => variant.color))]
}

// Navigate to product detail page
const goToProductDetail = (productId) => {
  router.push(`/product/${productId}`)
}

onMounted(fetchProducts)
</script>

<style scoped>
.product-list-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
}

h1 {
  margin-bottom: 30px;
  font-size: 28px;
  color: #333;
  text-align: center;
}

.loading,
.error,
.no-products {
  text-align: center;
  padding: 40px;
  font-size: 18px;
  color: #666;
}

.error {
  color: #e53935;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 30px;
}

.product-card {
  border: 1px solid #eee;
  border-radius: 4px;
  overflow: hidden;
  transition:
    transform 0.3s,
    box-shadow 0.3s;
  cursor: pointer;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.product-image {
  height: 300px;
  overflow: hidden;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s;
}

.product-card:hover .product-image img {
  transform: scale(1.05);
}

.product-info {
  padding: 15px;
}

.product-info h3 {
  margin: 0 0 10px;
  font-size: 16px;
  color: #333;
}

.product-price {
  font-weight: bold;
  font-size: 18px;
  margin-bottom: 10px;
  color: #333;
}

.product-colors {
  font-size: 14px;
  color: #666;
}
</style>
