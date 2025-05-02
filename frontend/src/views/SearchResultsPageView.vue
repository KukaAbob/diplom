<template>
  <div class="search-results-page">
    <!-- Форма поиска на странице результатов -->
    <div class="search-form">
      <form @submit.prevent="updateSearch" class="search-container">
        <input
          type="text"
          v-model="searchQuery"
          placeholder="Поиск по товару, коллекции..."
          class="search-input"
        />
        <button type="submit" class="search-button">
          <img src="@/assets/img/icons/logo2.svg" alt="Поиск" class="search-icon" />
        </button>
      </form>
    </div>

    <!-- Информация о результатах поиска -->
    <div class="search-info">
      <h1>Результаты поиска: "{{ searchQuery }}"</h1>
      <div v-if="filteredProducts.length" class="result-count">
        Найдено товаров: {{ filteredProducts.length }}
      </div>
    </div>

    <!-- Загрузка и ошибки -->
    <div v-if="isLoading" class="search-loading">
      <div class="loader"></div>
      <p>Загружаем товары...</p>
    </div>

    <div v-else-if="error" class="search-error">
      <p>{{ error }}</p>
      <button @click="fetchAllProducts" class="retry-button">Попробовать снова</button>
    </div>

    <!-- Результаты поиска -->
    <div v-else-if="filteredProducts.length === 0 && searchQuery" class="no-results">
      <h2>По вашему запросу ничего не найдено</h2>
      <p>Попробуйте изменить поисковый запрос или посмотреть популярные товары</p>
    </div>

    <!-- Список товаров -->
    <div v-else class="products-grid">
      <div
        v-for="product in filteredProducts"
        :key="product.id"
        class="product-card"
        @click="goToProduct(product)"
      >
        <!-- Здесь должно быть изображение товара -->
        <div class="product-image-placeholder"></div>
        <div class="product-info">
          <h3 class="product-name">{{ product.name }}</h3>
          <div class="product-category">{{ product.category }}</div>
          <div class="product-price">{{ product.price.toLocaleString() }} ₽</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

const searchQuery = ref('')
const allProducts = ref([]) // Хранит все продукты
const isLoading = ref(false)
const error = ref(null)

// Отфильтрованные товары на основе поискового запроса
const filteredProducts = computed(() => {
  if (!searchQuery.value.trim()) {
    return allProducts.value
  }

  const query = searchQuery.value.toLowerCase().trim()

  return allProducts.value.filter((product) => {
    const nameMatch = product.name.toLowerCase().includes(query)
    const categoryMatch = product.category.toLowerCase().includes(query)
    const descriptionMatch = product.description?.toLowerCase().includes(query) || false

    return nameMatch || categoryMatch || descriptionMatch
  })
})

// Получаем поисковый запрос из URL при создании компонента
onMounted(() => {
  searchQuery.value = route.query.q || ''

  // Загружаем все товары один раз при монтировании компонента
  fetchAllProducts()
})

// Следим за изменениями параметров URL
watch(
  () => route.query.q,
  (newQuery) => {
    searchQuery.value = newQuery || ''
  },
)

// Получение всех товаров
async function fetchAllProducts() {
  isLoading.value = true
  error.value = null

  try {
    // Получаем все товары с сервера
    const response = await fetch('/api/product/all')

    if (!response.ok) {
      throw new Error(`Ошибка получения данных: ${response.status}`)
    }

    // Сохраняем все товары
    allProducts.value = await response.json()
  } catch (err) {
    console.error('Ошибка при загрузке товаров:', err)
    error.value = err.message
  } finally {
    isLoading.value = false
  }
}

// Перейти на страницу товара
function goToProduct(product) {
  router.push(`/product/${product.id}`)
}

// Обновить поисковый запрос и URL
function updateSearch() {
  if (searchQuery.value.trim()) {
    router.replace({
      path: '/search',
      query: { q: searchQuery.value.trim() },
    })
  } else {
    // Если запрос пустой, остаемся на странице поиска, но убираем параметр q
    router.replace({
      path: '/search',
      query: {},
    })
  }
}
</script>

<style scoped>
.search-results-page {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.search-form {
  margin-bottom: 30px;
}

.search-container {
  position: relative;
  max-width: 600px;
  margin: 0 auto;
}

.search-input {
  width: 100%;
  padding: 12px 45px 12px 15px;
  border: 1px solid #ccc;
  border-radius: 25px;
  font-size: 16px;
  outline: none;
}

.search-button {
  position: absolute;
  right: 5px;
  top: 50%;
  transform: translateY(-50%);
  border: none;
  background: transparent;
  cursor: pointer;
  padding: 8px;
}

.search-icon {
  width: 20px;
  height: 20px;
}

.search-info {
  margin-bottom: 20px;
}

.search-info h1 {
  font-size: 24px;
  margin-bottom: 10px;
}

.result-count {
  color: #666;
  font-size: 14px;
}

.search-loading,
.search-error,
.no-results {
  text-align: center;
  padding: 40px 0;
}

.loader {
  border: 4px solid #f3f3f3;
  border-radius: 50%;
  border-top: 4px solid #3498db;
  width: 40px;
  height: 40px;
  animation: spin 1s linear infinite;
  margin: 0 auto 20px;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.retry-button {
  padding: 8px 16px;
  background: #3498db;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 15px;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
}

.product-card {
  border: 1px solid #eee;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition:
    transform 0.2s,
    box-shadow 0.2s;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.product-image-placeholder {
  height: 200px;
  background-color: #f5f5f5;
}

.product-info {
  padding: 15px;
}

.product-name {
  margin: 0 0 8px;
  font-size: 16px;
}

.product-category {
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
}

.product-price {
  font-weight: bold;
  color: #e91e63;
  font-size: 18px;
}
</style>
