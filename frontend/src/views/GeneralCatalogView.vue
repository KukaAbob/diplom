<template>
  <div class="products-page">
    <!-- Gender filter buttons -->
    <div class="gender-filter">
      <router-link
        :to="{
          path: '/general',
          query: { gender: 'all', category: activeFilter, collection: activeCollection },
        }"
        class="gender-btn"
        :class="{ active: activeGender === 'all' }"
      >
        Все
      </router-link>
      <router-link
        :to="{
          path: '/general',
          query: { gender: 'male', category: activeFilter, collection: activeCollection },
        }"
        class="gender-btn"
        :class="{ active: activeGender === 'male' }"
      >
        Мужское
      </router-link>
      <router-link
        :to="{
          path: '/general',
          query: { gender: 'female', category: activeFilter, collection: activeCollection },
        }"
        class="gender-btn"
        :class="{ active: activeGender === 'female' }"
      >
        Женское
      </router-link>
    </div>

    <div class="collection-filter">
      <router-link
        :to="{
          path: '/general',
          query: { gender: activeGender, category: activeFilter, collection: 'all' },
        }"
        class="collection-btn"
        :class="{ active: activeCollection === 'all' }"
      >
        Все
      </router-link>
      <router-link
        :to="{
          path: '/general',
          query: { gender: activeGender, category: activeFilter, collection: 'new' },
        }"
        class="collection-btn"
        :class="{ active: activeCollection === 'new' }"
      >
        Новинки
      </router-link>
      <router-link
        :to="{
          path: '/general',
          query: { gender: activeGender, category: activeFilter, collection: 'pacific' },
        }"
        class="collection-btn"
        :class="{ active: activeCollection === 'pacific' }"
      >
        Pacific Republic
      </router-link>
      <router-link
        :to="{
          path: '/general',
          query: { gender: activeGender, category: activeFilter, collection: 'stwd' },
        }"
        class="collection-btn"
        :class="{ active: activeCollection === 'stwd' }"
      >
        STWD
      </router-link>
    </div>

    <!-- Category filter buttons -->
    <div class="filter-buttons">
      <router-link
        :to="{
          path: '/general',
          query: { gender: activeGender, category: 'all', collection: activeCollection },
        }"
        class="filter-btn"
        :class="{ dark: activeFilter === 'all' }"
      >
        Просмотреть все
      </router-link>
      <router-link
        :to="{
          path: '/general',
          query: { gender: activeGender, category: 'jeans', collection: activeCollection },
        }"
        class="filter-btn"
        :class="{ dark: activeFilter === 'jeans' }"
      >
        Джинсы
      </router-link>
      <router-link
        :to="{
          path: '/general',
          query: { gender: activeGender, category: 'pants', collection: activeCollection },
        }"
        class="filter-btn"
        :class="{ dark: activeFilter === 'pants' }"
      >
        Брюки
      </router-link>
      <router-link
        :to="{
          path: '/general',
          query: { gender: activeGender, category: 'hoodies', collection: activeCollection },
        }"
        class="filter-btn"
        :class="{ dark: activeFilter === 'hoodies' }"
      >
        Толстовки
      </router-link>
      <router-link
        :to="{
          path: '/general',
          query: { gender: activeGender, category: 'jackets', collection: activeCollection },
        }"
        class="filter-btn"
        :class="{ dark: activeFilter === 'jackets' }"
      >
        Куртки
      </router-link>
      <router-link
        :to="{
          path: '/general',
          query: { gender: activeGender, category: 'tshirts', collection: activeCollection },
        }"
        class="filter-btn"
        :class="{ dark: activeFilter === 'tshirts' }"
      >
        Футболки
      </router-link>
      <router-link
        :to="{
          path: '/general',
          query: { gender: activeGender, category: 'polo', collection: activeCollection },
        }"
        class="filter-btn"
        :class="{ dark: activeFilter === 'polo' }"
      >
        Поло
      </router-link>
      <router-link
        :to="{
          path: '/general',
          query: { gender: activeGender, category: 'shorts', collection: activeCollection },
        }"
        class="filter-btn"
        :class="{ dark: activeFilter === 'shorts' }"
      >
        Шорты
      </router-link>
      <router-link
        :to="{
          path: '/general',
          query: { gender: activeGender, category: 'shirts', collection: activeCollection },
        }"
        class="filter-btn"
        :class="{ dark: activeFilter === 'shirts' }"
      >
        Рубашки
      </router-link>
      <router-link
        :to="{
          path: '/general',
          query: { gender: activeGender, category: 'sport', collection: activeCollection },
        }"
        class="filter-btn"
        :class="{ dark: activeFilter === 'sport' }"
      >
        Спортивные костюмы
      </router-link>
      <router-link
        :to="{
          path: '/general',
          query: { gender: activeGender, category: 'accessories', collection: activeCollection },
        }"
        class="filter-btn"
        :class="{ dark: activeFilter === 'accessories' }"
      >
        Аксессуары
      </router-link>
      <router-link
        :to="{
          path: '/general',
          query: { gender: activeGender, category: 'bags', collection: activeCollection },
        }"
        class="filter-btn"
        :class="{ dark: activeFilter === 'bags' }"
      >
        Сумки
      </router-link>
      <router-link
        :to="{
          path: '/general',
          query: { gender: activeGender, category: 'dress', collection: activeCollection },
        }"
        class="filter-btn"
        :class="{ dark: activeFilter === 'dress' }"
      >
        Платья
      </router-link>
      <router-link
        :to="{
          path: '/general',
          query: { gender: activeGender, category: 'coat', collection: activeCollection },
        }"
        class="filter-btn"
        :class="{ dark: activeFilter === 'coat' }"
      >
        Пальто
      </router-link>
      <router-link
        :to="{
          path: '/general',
          query: { gender: activeGender, category: 'cardigan', collection: activeCollection },
        }"
        class="filter-btn"
        :class="{ dark: activeFilter === 'cardigan' }"
      >
        Свитеры и кардиганы
      </router-link>
      <router-link
        :to="{
          path: '/general',
          query: { gender: activeGender, category: 'skirts', collection: activeCollection },
        }"
        class="filter-btn"
        :class="{ dark: activeFilter === 'skirts' }"
      >
        Юбки
      </router-link>
      <router-link
        :to="{
          path: '/general',
          query: { gender: activeGender, category: 'bijouterie', collection: activeCollection },
        }"
        class="filter-btn"
        :class="{ dark: activeFilter === 'bijouterie' }"
      >
        Бижутерия
      </router-link>
      <router-link
        :to="{
          path: '/general',
          query: { gender: activeGender, category: 'shoes', collection: activeCollection },
        }"
        class="filter-btn"
        :class="{ dark: activeFilter === 'shoes' }"
      >
        Обувь
      </router-link>
    </div>

    <div class="products-grid" v-if="filteredProducts.length > 0">
      <div class="product-card" v-for="product in filteredProducts" :key="product.id">
        <!-- Содержимое карточки товара не изменяется -->
        <div class="product-image">
          <img
            :src="getProductMainImage(product.id)"
            :alt="product.name"
            @click="selectProduct(product)"
          />
          <button class="favorite-btn" @click="toggleWishlist(product.id, $event)">
            <svg
              width="24"
              height="24"
              viewBox="0 0 24 24"
              fill="none"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"
                stroke="black"
                fill="none"
                :fill="isInWishlist(product.id) ? 'red' : 'none'"
                :stroke="isInWishlist(product.id) ? 'red' : 'black'"
              />
            </svg>
          </button>
        </div>
        <div class="product-info">
          <h3>{{ product.name }}</h3>
          <p class="product-price">{{ product.price }} KZT</p>
          <div class="product-colors">
            <div
              v-for="colorName in getUniqueColors(product)"
              :key="colorName"
              class="color-dot"
              :style="{ backgroundColor: getColorValue(colorName) }"
              :title="colorName"
            ></div>
          </div>
        </div>
      </div>
    </div>
    <div v-else-if="loading" class="loading-message">
      <p>Загрузка...</p>
    </div>
    <div v-else class="no-products">
      <p>Товары не найдены</p>
    </div>

    <!-- Детальная модальная информация о товаре -->
    <div class="product-modal-overlay" v-if="selectedProduct" @click.self="closeProductDetails">
      <div class="product-details-modal">
        <button class="close-modal-btn" @click="closeProductDetails">×</button>

        <div class="product-modal-content">
          <!-- Галерея изображений товара слева -->
          <div class="product-gallery">
            <div class="main-image-container">
              <img :src="mainImage" :alt="selectedProduct.name" class="main-product-image" />
            </div>
            <div class="thumbnail-images">
              <div
                v-for="(image, index) in productImages[selectedProduct.id]"
                :key="index"
                class="thumbnail-image"
                :class="{ active: mainImage === image }"
                @click="selectMainImage(image)"
              >
                <img :src="image" :alt="`${selectedProduct.name} - изображение ${index + 1}`" />
              </div>
            </div>
          </div>

          <!-- Информация о товаре справа -->
          <div class="product-info-container">
            <div class="product-header">
              <h2 class="product-title">{{ selectedProduct.name }}</h2>
              <button
                class="modal-favorite-btn"
                @click="toggleWishlist(selectedProduct.id)"
                v-if="selectedProduct"
              >
                <svg
                  width="24"
                  height="24"
                  viewBox="0 0 24 24"
                  fill="none"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <path
                    d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"
                    stroke="black"
                    fill="none"
                    :fill="selectedProduct && isInWishlist(selectedProduct.id) ? 'red' : 'none'"
                    :stroke="selectedProduct && isInWishlist(selectedProduct.id) ? 'red' : 'black'"
                  />
                </svg>
              </button>
            </div>
            <p class="product-modal-price">{{ selectedProduct.price }} KZT</p>

            <div class="product-collection" v-if="selectedProduct.collection">
              <p>Коллекция: {{ selectedProduct.collection }}</p>
            </div>

            <!-- Выбор цвета -->
            <div class="color-selection">
              <h3>Цвет</h3>
              <div class="color-options">
                <div
                  v-for="colorName in uniqueColors"
                  :key="colorName"
                  class="color-option"
                  :class="{ selected: selectedColor === colorName }"
                  :style="{ backgroundColor: getColorValue(colorName) }"
                  :title="colorName"
                  @click="selectColor(colorName)"
                ></div>
              </div>
              <p class="selected-color-name" v-if="selectedColor">{{ selectedColor }}</p>
            </div>

            <!-- Выбор размера -->
            <div class="size-selection">
              <h3>Размер</h3>
              <div class="size-options">
                <button
                  v-for="size in availableSizes"
                  :key="size"
                  class="size-option"
                  :class="{
                    selected: selectedSize === size,
                    unavailable: !isSizeAvailable(size),
                  }"
                  @click="selectSize(size)"
                  :disabled="!isSizeAvailable(size)"
                >
                  {{ size }}
                </button>
              </div>
            </div>

            <!-- Информация о наличии -->
            <div class="stock-info" v-if="currentVariant">
              <p v-if="currentVariant.stock > 0" class="in-stock">
                В наличии: {{ currentVariant.stock }} шт.
              </p>
              <p v-else class="out-of-stock">Нет в наличии</p>
            </div>

            <div
              class="quantity-selector"
              v-if="selectedProduct && selectedColor && selectedSize && currentVariant"
            >
              <h3>Количество</h3>
              <div class="quantity-controls">
                <button
                  class="quantity-btn"
                  @click="updateQuantity(cartQuantity - 1)"
                  :disabled="isMinQuantity"
                >
                  -
                </button>
                <input
                  type="number"
                  v-model="cartQuantity"
                  class="quantity-input"
                  min="1"
                  :max="currentVariant.stock"
                  @input="updateQuantity($event.target.value)"
                />
                <button
                  class="quantity-btn"
                  @click="updateQuantity(cartQuantity + 1)"
                  :disabled="isMaxQuantity"
                >
                  +
                </button>
              </div>
            </div>

            <!-- Кнопка добавления в корзину -->
            <button
              class="add-to-cart-btn"
              :class="{ loading: addToCartStatus.loading, success: addToCartStatus.success }"
              :disabled="!canAddToCart || addToCartStatus.loading"
              @click="addToCart"
            >
              <span v-if="addToCartStatus.loading">Добавление...</span>
              <span v-else-if="addToCartStatus.success">Добавлено ✓</span>
              <span v-else-if="canAddToCart">Добавить в корзину</span>
              <span v-else>Выберите размер и цвет</span>
            </button>

            <div class="cart-error" v-if="addToCartStatus.error">
              {{ addToCartStatus.error }}
            </div>

            <!-- Аккордеоны с дополнительной информацией -->
            <div class="product-accordions">
              <div class="accordion-item">
                <div class="accordion-header" @click="toggleAccordion('description')">
                  <h3>Описание</h3>
                  <span class="accordion-icon">{{
                    expandedSection === 'description' ? '−' : '+'
                  }}</span>
                </div>
                <div class="accordion-content" v-if="expandedSection === 'description'">
                  <p>{{ selectedProduct.description || 'Описание отсутствует' }}</p>
                </div>
              </div>

              <div class="accordion-item">
                <div class="accordion-header" @click="toggleAccordion('details')">
                  <h3>Характеристики</h3>
                  <span class="accordion-icon">{{
                    expandedSection === 'details' ? '−' : '+'
                  }}</span>
                </div>
                <div class="accordion-content" v-if="expandedSection === 'details'">
                  <div class="product-specs">
                    <div class="spec-item" v-if="selectedProduct.material">
                      <span class="spec-name">Материал:</span>
                      <span class="spec-value">{{ selectedProduct.material }}</span>
                    </div>
                    <div class="spec-item" v-if="selectedProduct.category">
                      <span class="spec-name">Категория:</span>
                      <span class="spec-value">{{ selectedProduct.category }}</span>
                    </div>
                    <div class="spec-item" v-if="selectedProduct.gender">
                      <span class="spec-name">Пол:</span>
                      <span class="spec-value">{{
                        selectedProduct.gender === 'male' ? 'Мужской' : 'Женский'
                      }}</span>
                    </div>
                    <div class="spec-item" v-if="selectedProduct.style">
                      <span class="spec-name">Стиль:</span>
                      <span class="spec-value">{{ selectedProduct.style }}</span>
                    </div>
                    <div class="spec-item" v-if="selectedProduct.season">
                      <span class="spec-name">Сезон:</span>
                      <span class="spec-value">{{ selectedProduct.season }}</span>
                    </div>
                    <div class="spec-item" v-if="selectedProduct.articleNumber">
                      <span class="spec-name">Артикул:</span>
                      <span class="spec-value">{{ selectedProduct.articleNumber }}</span>
                    </div>
                  </div>
                </div>
              </div>

              <div class="accordion-item">
                <div class="accordion-header" @click="toggleAccordion('care')">
                  <h3>Уход за изделием</h3>
                  <span class="accordion-icon">{{ expandedSection === 'care' ? '−' : '+' }}</span>
                </div>
                <div class="accordion-content" v-if="expandedSection === 'care'">
                  <p>{{ selectedProduct.careInstructions || 'Информация об уходе отсутствует' }}</p>
                </div>
              </div>

              <div class="accordion-item">
                <div class="accordion-header" @click="toggleAccordion('delivery')">
                  <h3>Доставка и оплата</h3>
                  <span class="accordion-icon">{{
                    expandedSection === 'delivery' ? '−' : '+'
                  }}</span>
                </div>
                <div class="accordion-content" v-if="expandedSection === 'delivery'">
                  <p>
                    Доставка доступна по всему Казахстану. Оплата возможна наличными при получении,
                    банковской картой или онлайн.
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<!-- Add to the script setup section -->
<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import api from '@/api/api'
import { jwtDecode } from 'jwt-decode'

const route = useRoute()
const router = useRouter()

const cartQuantity = ref(1)
const addToCartStatus = ref({ loading: false, success: false, error: null })

// Wishlist state
const wishlistItems = ref([])
const wishlistStatus = ref({ loading: false, success: false, error: null })

// Состояние списка товаров
const products = ref([])
const loading = ref(true)
const productImages = ref({}) // Хранение изображений для всех продуктов

const getUserEmailFromToken = () => {
  try {
    const token = localStorage.getItem('jwtToken')
    if (!token) return null

    const decodedToken = jwtDecode(token)
    return decodedToken.email || decodedToken.sub || null
  } catch (error) {
    console.error('Error decoding JWT token:', error)
    return null
  }
}

// Check if a product is in wishlist
const isInWishlist = (productId) => {
  return wishlistItems.value.includes(productId)
}

// Toggle product in wishlist
// Toggle product in wishlist
const toggleWishlist = async (productId, event) => {
  if (event) {
    event.stopPropagation() // Prevent opening product details when clicking heart
  }

  const email = getUserEmailFromToken()
  if (!email) {
    // Redirect to login if no email/token found
    router.push('/login?redirect=' + encodeURIComponent(route.fullPath))
    return
  }

  try {
    wishlistStatus.value.loading = true
    wishlistStatus.value.error = null

    // Проверяем, есть ли товар уже в списке желаний
    const inWishlist = isInWishlist(productId)

    if (inWishlist) {
      // Если товар уже в списке желаний, удаляем его
      await api.post('/api/wishlist/delete', {
        email: email,
        productId: productId,
      })
      // Удаляем из локального списка
      wishlistItems.value = wishlistItems.value.filter((id) => id !== productId)
    } else {
      // Если товара нет в списке желаний, добавляем его
      await api.post('/api/wishlist/add', {
        email: email,
        productId: productId,
      })
      // Добавляем в локальный список
      wishlistItems.value.push(productId)
    }

    wishlistStatus.value.success = true
    // Reset after 2 seconds
    setTimeout(() => {
      wishlistStatus.value.success = false
    }, 2000)
  } catch (error) {
    console.error('Ошибка обновления списка желаний:', error)
    wishlistStatus.value.error = error.response?.data?.message || 'Ошибка обновления списка желаний'
  } finally {
    wishlistStatus.value.loading = false
  }
}

const addToCart = async () => {
  if (!currentVariant.value || currentVariant.value.stock <= 0 || !selectedProduct.value) {
    return
  }

  const email = getUserEmailFromToken()
  if (!email) {
    // Redirect to login if no email/token found
    router.push('/login?redirect=' + encodeURIComponent(route.fullPath))
    return
  }

  try {
    addToCartStatus.value.loading = true
    addToCartStatus.value.error = null

    const response = await api.post('/api/cart/add', {
      email: email,
      productId: selectedProduct.value.id,
      quantity: cartQuantity.value,
    })

    addToCartStatus.value.success = true

    // Reset after 3 seconds
    setTimeout(() => {
      addToCartStatus.value.success = false
    }, 3000)
  } catch (error) {
    console.error('Error adding item to cart:', error)
    addToCartStatus.value.error =
      error.response?.data?.message || 'Ошибка добавления товара в корзину'
  } finally {
    addToCartStatus.value.loading = false
  }
}

const updateQuantity = (value) => {
  const newQuantity = parseInt(value, 10)

  if (isNaN(newQuantity) || newQuantity < 1) {
    cartQuantity.value = 1
    return
  }

  if (currentVariant.value && newQuantity > currentVariant.value.stock) {
    cartQuantity.value = currentVariant.value.stock
    return
  }

  cartQuantity.value = newQuantity
}

// Check if maximum quantity is reached
const isMaxQuantity = computed(() => {
  return currentVariant.value && cartQuantity.value >= currentVariant.value.stock
})

// Check if minimum quantity is reached
const isMinQuantity = computed(() => {
  return cartQuantity.value <= 1
})

// Инициализация фильтров из query параметров
const activeFilter = ref(route.query.category || 'all')
const activeGender = ref(route.query.gender || 'all')
const activeCollection = ref(route.query.collection || 'all') // Новый параметр для коллекций

// Маппинг кодов фильтров на категории в базе данных
const categoryMapping = {
  all: null, // Просмотреть все (null означает, что фильтрации нет)
  jeans: 'Джинсы',
  pants: 'Брюки',
  hoodies: 'Толстовки',
  jackets: 'Куртки',
  tshirts: 'Футболки',
  polo: 'Поло',
  shorts: 'Шорты',
  shoes: 'Обувь',
  shirts: 'Рубашки',
  sport: 'Спортивный костюм',
  accessories: 'Аксессуары',
  bags: 'Сумки',
  dress: 'Платья',
  coat: 'Пальто',
  cardigan: 'Свитеры и кардиганы',
  skirts: 'Юбки',
  bijouterie: 'Бижутерия',
}

const collectionMapping = {
  all: null, // Все коллекции (null означает, что фильтрации нет)
  new: 'Новинки',
  pacific: 'Pacific Republic',
  stwd: 'STWD',
}

// Состояние детального просмотра товара
const selectedProduct = ref(null)
const selectedColor = ref(null)
const selectedSize = ref(null)
const mainImage = ref(null)
const expandedSection = ref(null)

// Отслеживание изменений URL для обновления фильтров
watch(
  () => route.query,
  (newQuery) => {
    activeFilter.value = newQuery.category || 'all'
    activeGender.value = newQuery.gender || 'all'
    activeCollection.value = newQuery.collection || 'all'
  },
  { immediate: true },
)

// Получение всех товаров
const fetchProducts = async () => {
  try {
    loading.value = true
    const response = await api.get('/api/product/all')

    console.log('Загруженные товары:', response.data)

    // Сохраняем все товары
    products.value = response.data

    // Предзагрузка изображений для всех продуктов
    for (const product of products.value) {
      await fetchProductImages(product.id)
    }
  } catch (error) {
    console.error('Ошибка загрузки товаров', error)
  } finally {
    loading.value = false
  }
}

// Получение изображений для товара
const fetchProductImages = async (productId) => {
  try {
    const response = await api.get(`/api/images/all/${productId}`)
    productImages.value[productId] = response.data
      .map((imageObj) => {
        const base64Data = imageObj.oid || imageObj.base64 || imageObj.image || imageObj
        if (!base64Data) return null
        return base64Data.startsWith('data:image')
          ? base64Data
          : `data:image/jpeg;base64,${base64Data}`
      })
      .filter(Boolean)
  } catch (error) {
    console.error(`Ошибка загрузки изображений для товара ${productId}`, error)
    productImages.value[productId] = []
  }
}

// Отфильтрованные товары
const filteredProducts = computed(() => {
  let result = products.value

  // Фильтрация по полу
  if (activeGender.value !== 'all') {
    result = result.filter((product) => product.gender?.toLowerCase() === activeGender.value)
  }

  // Фильтрация по категории
  if (activeFilter.value !== 'all') {
    const categoryName = categoryMapping[activeFilter.value]
    if (categoryName) {
      result = result.filter((product) => product.category.name === categoryName)
    }
  }

  if (activeCollection.value !== 'all') {
    const collectionName = collectionMapping[activeCollection.value]
    if (collectionName) {
      result = result.filter((product) => product.collection === collectionName)
    }
  }

  return result
})

// Методы для работы с изображениями и выбором товара
const getProductMainImage = (productId) => {
  const images = productImages.value[productId] || []
  return images.length > 0 ? images[0] : '/placeholder-image.jpg'
}

const getUniqueColors = (product) => {
  const uniqueColorNames = [...new Set(product.variants?.map((v) => v.color) || [])]
  return uniqueColorNames // Возвращаем просто массив названий цветов
}

const getColorValue = (colorName) => {
  const colorMap = {
    'Черный': '#000000',
    'Белый': '#FFFFFF',
    'Коричневый': '#964B00',
  }
  
  return colorMap[colorName] || '#6B7280' // По умолчанию серый цвет, если цвет не найден
}

const selectProduct = (product) => {
  selectedProduct.value = product

  if (product.variants && product.variants.length > 0) {
    selectedColor.value = null  // Изменить с product.variants[0].color на null
    selectedSize.value = null
  }

  const productImageList = productImages.value[product.id] || []
  if (productImageList.length > 0) {
    mainImage.value = productImageList[0]
  }

  // Открываем первую секцию аккордеона по умолчанию
  expandedSection.value = 'description'

  // Блокируем прокрутку страницы при открытом модальном окне
  document.body.style.overflow = 'hidden'
}

const closeProductDetails = () => {
  selectedProduct.value = null
  selectedColor.value = null
  selectedSize.value = null
  mainImage.value = null
  expandedSection.value = null

  // Возвращаем прокрутку страницы
  document.body.style.overflow = ''
}

// Computed свойства для вариантов товара
const uniqueColors = computed(() => {
  return [...new Set(selectedProduct.value?.variants?.map((v) => v.color) || [])]
})

// Замените это computed свойство в вашем коде:
const availableSizes = computed(() => {
  if (!selectedColor.value) {
    // Если цвет не выбран, показываем все доступные размеры без дубликатов
    return [...new Set(selectedProduct.value?.variants?.map((v) => v.size) || [])]
  }
  // Если цвет выбран, показываем размеры для этого цвета без дубликатов
  return [...new Set(
    selectedProduct.value?.variants
      ?.filter((v) => v.color === selectedColor.value)
      .map((v) => v.size) || []
  )]
})

const currentVariant = computed(() => {
  return selectedProduct.value?.variants?.find(
    (v) => v.color === selectedColor.value && v.size === selectedSize.value,
  )
})

const canAddToCart = computed(() => {
  return currentVariant.value && currentVariant.value.stock > 0
})

// Методы для работы с деталями товара
const selectColor = (color) => {
  selectedColor.value = color
  
  // Сбрасываем размер, если он больше не доступен для выбранного цвета
  if (selectedSize.value && !availableSizes.value.includes(selectedSize.value)) {
    selectedSize.value = null
  }
}

const selectSize = (size) => {
  selectedSize.value = size
}

const isSizeAvailable = (size) => {
  if (!selectedColor.value) {
    // Если цвет не выбран, проверяем наличие размера для любого цвета
    return selectedProduct.value?.variants?.some(
      (v) => v.size === size && v.stock > 0
    )
  }
  return selectedProduct.value?.variants?.some(
    (v) => v.color === selectedColor.value && v.size === size && v.stock > 0
  )
}

const selectMainImage = (image) => {
  mainImage.value = image
}

const toggleAccordion = (section) => {
  expandedSection.value = expandedSection.value === section ? null : section
}

// Инициализация при монтировании компонента
onMounted(() => {
  fetchProducts()
  fetchWishlist() // Fetch user's wishlist on page load

  // Если есть параметры в URL, устанавливаем соответствующие фильтры
  if (route.query.category) {
    activeFilter.value = route.query.category
  }

  if (route.query.gender) {
    activeGender.value = route.query.gender
  }

  if (route.query.collection) {
    activeCollection.value = route.query.collection
  }
})
</script>

<style scoped>
.products-page {
  max-width: 1400px;
  margin: 0 auto;
  padding: 40px 20px;
  margin-top: 3%;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 30px;
}

.product-card {
  border: 1px solid #eee;
  border-radius: 5px;
  overflow: hidden;
  transition: all 0.3s;
  cursor: pointer;
}

.product-card:hover {
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.product-image {
  position: relative;
  height: 300px;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.favorite-btn {
  position: absolute;
  top: 10px;
  right: 10px;
  background: rgba(255, 255, 255, 0.8);
  border: none;
  border-radius: 50%;
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s ease-in-out;
  z-index: 2;
}

.favorite-btn:hover {
  background: rgba(255, 255, 255, 1);
  transform: scale(1.1);
}

@keyframes heartbeat {
  0% {
    transform: scale(1);
  }
  25% {
    transform: scale(1.2);
  }
  50% {
    transform: scale(1);
  }
  75% {
    transform: scale(1.2);
  }
  100% {
    transform: scale(1);
  }
}

.favorite-btn svg {
  transition: all 0.3s ease;
}

.favorite-btn:active svg {
  animation: heartbeat 0.6s ease-in-out;
}

.product-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.modal-favorite-btn {
  background: none;
  border: none;
  cursor: pointer;
  padding: 5px;
  border-radius: 50%;
  transition: all 0.2s ease;
}

.modal-favorite-btn:hover {
  background: rgba(0, 0, 0, 0.05);
}

/* Make sure product title has proper spacing */
.product-title {
  margin-right: 15px;
  flex: 1;
}

.product-info {
  padding: 15px;
}

.product-info h3 {
  margin: 0 0 10px 0;
  font-size: 16px;
}

.product-price {
  font-weight: bold;
  margin-bottom: 10px;
}

.product-colors {
  display: flex;
  gap: 5px;
}

.color-dot {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background-color: #ddd;
  border: 1px solid #ccc;
}

.loading-message,
.no-products {
  text-align: center;
  padding: 50px;
  font-size: 18px;
  color: #777;
}

/* Стили для модального окна с деталями товара */
.product-details-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
}

.modal-content {
  position: relative;
  background: white;
  max-width: 1200px;
  width: 90%;
  max-height: 90vh;
  overflow-y: auto;
  padding: 30px;
  display: flex;
  gap: 50px;
  z-index: 1001;
}

.close-btn {
  position: absolute;
  top: 15px;
  right: 15px;
  font-size: 24px;
  background: none;
  border: none;
  cursor: pointer;
}

/* Стили для отображения деталей товара (из предыдущего кода) */
.product-gallery {
  display: flex;
  gap: 20px;
  flex: 1;
}

.product-images {
  display: flex;
  flex-direction: column;
  gap: 10px;
  max-width: 80px;
}

.product-images img,
.main-image img {
  width: 100%;
  height: auto;
  object-fit: cover;
  cursor: pointer;
}

.main-image {
  position: relative;
  flex: 1;
  max-width: 600px;
}

.product-details {
  flex: 1;
  max-width: 450px;
}

.product-details h1 {
  margin-bottom: 10px;
  font-size: 22px;
  color: #333;
}

.product-price {
  font-size: 22px;
  font-weight: bold;
  margin-bottom: 10px;
  color: #333;
}

.product-code {
  color: #777;
  margin-bottom: 20px;
  font-size: 14px;
}

.size-selection {
  margin-bottom: 20px;
}

.size-label {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  font-size: 14px;
}

.size-guide {
  color: #777;
  text-decoration: none;
  font-size: 12px;
}

.size-buttons {
  display: flex;
  gap: 10px;
}

.size-btn {
  border: 1px solid #ddd;
  background: white;
  padding: 10px 15px;
  cursor: pointer;
  transition: all 0.3s;
  font-size: 14px;
}

.size-btn:hover {
  background: #f5f5f5;
}

.add-to-cart-btn {
  width: 100%;
  background: #00c853;
  color: white;
  border: none;
  padding: 15px;
  font-size: 16px;
  cursor: pointer;
  margin-top: 20px;
  transition: background 0.3s;
}

.add-to-cart-btn:hover {
  background: #00a041;
}

.product-info-accordion {
  margin-top: 30px;
}

.accordion-item {
  border-top: 1px solid #ddd;
}

.accordion-item button {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  border: none;
  padding: 15px 0;
  cursor: pointer;
  font-size: 16px;
}

.accordion-content {
  padding: 15px 0;
  font-size: 14px;
  color: #333;
}

.accordion-icon {
  font-size: 24px;
  color: #777;
}

.color-selection,
.size-selection {
  margin-bottom: 20px;
}

.color-buttons,
.size-buttons {
  display: flex;
  gap: 10px;
}

.color-btn,
.size-btn {
  border: 1px solid #ddd;
  background: white;
  padding: 10px 15px;
  cursor: pointer;
  transition: all 0.3s;
  font-size: 14px;
}

.color-btn.selected,
.size-btn.selected {
  background: #f0f0f0;
  border-color: #333;
}

.color-btn:disabled,
.size-btn:disabled {
  color: #ccc;
  cursor: not-allowed;
}

.stock-info {
  margin-top: 10px;
  font-size: 14px;
  color: #666;
}

.add-to-cart-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
}

/* Адаптивный дизайн для мобильных устройств */
@media (max-width: 768px) {
  .modal-content {
    flex-direction: column;
    gap: 20px;
    overflow-y: auto;
  }

  .product-gallery {
    flex-direction: column-reverse;
  }

  .product-images {
    flex-direction: row;
    max-width: 100%;
    overflow-x: auto;
  }

  .product-images img {
    max-width: 60px;
  }

  .main-image {
    max-width: 100%;
  }

  .product-details {
    max-width: 100%;
  }
}

/* Стили для кнопок фильтрации */
.filter-buttons {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 30px;
  overflow-x: auto;
  padding-bottom: 10px;
}

.filter-btn {
  padding: 10px 20px;
  border-radius: 25px;
  border: none;
  background-color: #f5f5f5;
  color: #333;
  font-size: 14px;
  cursor: pointer;
  white-space: nowrap;
  text-decoration: none;
  transition: all 0.3s ease;
}

.filter-btn:hover,
.filter-btn:focus {
  /* Убираем дефолтную подсветку при наведении и фокусе */
  text-decoration: none;
  outline: none;
}

.filter-btn:hover {
  background-color: #e0e0e0;
}

.filter-btn.dark {
  background-color: #000;
  color: #fff;
}

.filter-btn.dark:hover {
  background-color: #333;
}

/* Изменение отступа для сетки товаров после добавления кнопок */
.products-grid {
  margin-top: 20px;
}

@media (max-width: 768px) {
  .filter-buttons {
    padding-bottom: 5px;
    margin-bottom: 20px;
  }

  .filter-btn {
    padding: 8px 15px;
    font-size: 13px;
  }
}

.gender-filter {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.gender-btn {
  padding: 10px 20px;
  margin: 0 10px;
  background-color: #f5f5f5;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  /* Убираем стили ссылок */
  text-decoration: none;
  color: #333;
  display: inline-block;
}

.gender-btn:hover,
.gender-btn:focus {
  /* Убираем дефолтную подсветку при наведении и фокусе */
  text-decoration: none;
  outline: none;
}

.gender-btn.active {
  background-color: #333;
  color: white;
}

.collection-filter {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.collection-btn {
  padding: 8px 16px;
  border: 1px solid #ccc;
  border-radius: 4px;
  cursor: pointer;
  text-decoration: none;
  color: #333;
  transition: all 0.3s ease;
}

.collection-btn.active {
  background-color: #333;
  color: white;
  border-color: #333;
}

.collection-btn:hover {
  background-color: #f0f0f0;
}

.collection-btn.active:hover {
  background-color: #222;
}

/* Стили для модального окна товара */
.product-modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  overflow-y: auto;
  padding: 20px;
}

.product-details-modal {
  position: relative;
  background-color: white;
  width: 100%;
  max-width: 1000px;
  max-height: 90vh;
  border-radius: 8px;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.3);
  overflow-y: auto;
}

.close-modal-btn {
  position: absolute;
  top: 15px;
  right: 15px;
  background: none;
  border: none;
  font-size: 28px;
  font-weight: bold;
  cursor: pointer;
  z-index: 10;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  background-color: rgba(255, 255, 255, 0.8);
  transition: all 0.3s;
}

.close-modal-btn:hover {
  background-color: #f1f1f1;
}

.product-modal-content {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  margin-top: 200px;
}

.product-gallery {
  flex: 1;
  min-width: 300px;
  padding: 20px;
  display: flex;
  flex-direction: column;
}

.main-image-container {
  height: 400px;
  margin-bottom: 20px;
  border: 1px solid #e5e5e5;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.main-product-image {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}

.thumbnail-images {
  display: flex;
  gap: 10px;
  overflow-x: auto;
  padding-bottom: 10px;
}

.thumbnail-image {
  width: 70px;
  height: 70px;
  border: 1px solid #e5e5e5;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s;
}

.thumbnail-image img {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}

.thumbnail-image.active {
  border-color: #333;
}

.thumbnail-image:hover {
  border-color: #999;
}

/* Информация о товаре */
.product-info-container {
  flex: 1;
  min-width: 300px;
  padding: 20px;
}

.product-title {
  font-size: 24px;
  margin: 0 0 10px;
}

.product-modal-price {
  font-size: 22px;
  font-weight: bold;
  margin-bottom: 20px;
}

.product-collection {
  margin-bottom: 20px;
  color: #666;
}

/* Выбор цвета */
.color-selection {
  margin-bottom: 20px;
}

.color-selection h3 {
  margin-bottom: 10px;
  font-size: 16px;
  font-weight: 500;
}

.color-options {
  display: flex;
  gap: 10px;
  margin-bottom: 5px;
}

.color-option {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  cursor: pointer;
  transition: all 0.3s;
  border: 2px solid transparent;
}

.color-option.selected {
  border-color: #333;
  box-shadow:
    0 0 0 2px white,
    0 0 0 4px #333;
}

.selected-color-name {
  margin: 5px 0;
  font-size: 14px;
  color: #666;
}

/* Выбор размера */
.size-selection {
  margin-bottom: 20px;
}

.size-selection h3 {
  margin-bottom: 10px;
  font-size: 16px;
  font-weight: 500;
}

.size-options {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.size-option {
  width: 50px;
  height: 35px;
  border: 1px solid #ddd;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  background: white;
  transition: all 0.3s;
}

.size-option.selected {
  border-color: #333;
  background: #333;
  color: white;
}

.size-option:hover:not(.unavailable) {
  border-color: #999;
}

.size-option.unavailable {
  opacity: 0.4;
  cursor: not-allowed;
  text-decoration: line-through;
}

/* Информация о наличии */
.stock-info {
  margin-bottom: 20px;
}

.in-stock {
  color: green;
}

.out-of-stock {
  color: red;
}

/* Кнопка добавления в корзину */
.add-to-cart-btn {
  width: 100%;
  padding: 12px 20px;
  background-color: #333;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
  margin-bottom: 20px;
}

.add-to-cart-btn:hover:not(:disabled) {
  background-color: #222;
}

.add-to-cart-btn:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

/* Аккордеоны с информацией */
.product-accordions {
  margin-top: 30px;
}

.accordion-item {
  border-bottom: 1px solid #e5e5e5;
}

.accordion-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 0;
  cursor: pointer;
}

.accordion-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 500;
}

.accordion-icon {
  font-size: 20px;
  font-weight: bold;
}

.accordion-content {
  padding: 0 0 15px;
}

/* Характеристики товара */
.product-specs {
  display: grid;
  grid-template-columns: 1fr;
  gap: 10px;
}

.spec-item {
  display: flex;
  justify-content: space-between;
  border-bottom: 1px dashed #e5e5e5;
  padding-bottom: 5px;
}

.spec-name {
  font-weight: 500;
}

/* Адаптивность для мобильных устройств */
@media (max-width: 768px) {
  .product-modal-content {
    flex-direction: column;
  }

  .main-image-container {
    height: 300px;
  }

  .product-gallery,
  .product-info-container {
    min-width: 100%;
  }
}

/* Стили для отображения цветов товара на карточке */
.product-colors {
  display: flex;
  gap: 5px;
}

.color-dot {
  width: 15px;
  height: 15px;
  border-radius: 50%;
  border: 1px solid #ddd;
}
</style>
