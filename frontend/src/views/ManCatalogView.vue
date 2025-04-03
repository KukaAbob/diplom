<template>
  <div class="products-page">
    <div class="products-grid" v-if="filteredProducts.length > 0">
      <div class="product-card" v-for="product in filteredProducts" :key="product.id">
        <div class="product-image">
          <img
            :src="getProductMainImage(product.id)"
            :alt="product.name"
            @click="selectProduct(product)"
          />
          <button class="favorite-btn">
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
              />
            </svg>
          </button>
        </div>
        <div class="product-info">
          <h3>{{ product.name }}</h3>
          <p class="product-price">{{ product.price }} KZT</p>
          <div class="product-colors">
            <div
              v-for="color in getUniqueColors(product)"
              :key="color"
              class="color-dot"
              :title="color"
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

    <!-- Детальная информация о товаре -->
    <div class="product-details-modal" v-if="selectedProduct">
      <div class="modal-overlay" @click="closeProductDetails"></div>
      <div class="modal-content">
        <button class="close-btn" @click="closeProductDetails">×</button>

        <div class="product-gallery">
          <div class="product-images">
            <img
              v-for="(image, index) in productImages"
              :key="index"
              :src="image"
              :alt="selectedProduct.name"
              @click="selectMainImage(image)"
            />
          </div>
          <div class="main-image">
            <img
              :src="mainImage || (productImages.length > 0 ? productImages[0] : '')"
              :alt="selectedProduct.name"
            />
            <button class="favorite-btn">
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
                />
              </svg>
            </button>
          </div>
        </div>

        <div class="product-details">
          <h1>{{ selectedProduct.name }}</h1>
          <p class="product-price">{{ currentVariant?.price || selectedProduct.price }} KZT</p>
          <p class="product-code">{{ selectedColor }} - Артикул: {{ selectedProduct.id }}</p>

          <div class="color-selection">
            <div class="color-label">Цвет</div>
            <div class="color-buttons">
              <button
                v-for="color in uniqueColors"
                :key="color"
                :class="['color-btn', { selected: selectedColor === color }]"
                @click="selectColor(color)"
              >
                {{ color }}
              </button>
            </div>
          </div>

          <div class="size-selection">
            <div class="size-label">
              <span>Размер</span>
              <a href="#" class="size-guide">Таблица размеров</a>
            </div>
            <div class="size-buttons">
              <button
                v-for="size in availableSizes"
                :key="size"
                :class="['size-btn', { selected: selectedSize === size }]"
                @click="selectSize(size)"
                :disabled="!isSizeAvailable(size)"
              >
                {{ size }}
              </button>
            </div>
          </div>

          <button class="add-to-cart-btn" :disabled="!canAddToCart">ДОБАВИТЬ В КОРЗИНУ</button>

          <div v-if="currentVariant" class="stock-info">
            <p>Доступно: {{ currentVariant.stock }} шт.</p>
          </div>

          <div class="product-info-accordion">
            <div class="accordion-item">
              <button @click="toggleAccordion('composition')">
                Состав, уход и происхождение
                <span class="accordion-icon">{{
                  expandedSection === 'composition' ? '−' : '+'
                }}</span>
              </button>
              <div v-if="expandedSection === 'composition'" class="accordion-content">
                <p>{{ selectedProduct.description }}</p>
                <p>Состав: 100% хлопок</p>
                <p>Уход: Машинная стирка при 30°C</p>
                <p>Происхождение: Сделано в России</p>
              </div>
            </div>

            <div class="accordion-item">
              <button @click="toggleAccordion('delivery')">
                Доставка и возврат
                <span class="accordion-icon">{{ expandedSection === 'delivery' ? '−' : '+' }}</span>
              </button>
              <div v-if="expandedSection === 'delivery'" class="accordion-content">
                <p>Получение в магазине БЕСПЛАТНО</p>
                <p>Стандартная доставка на дом БЕСПЛАТНО</p>
                <p>При заказе от 23,990,00 KZT</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import api from '@/api/api'

// Состояние списка товаров
const products = ref([])
const loading = ref(true)
const productImages = ref({}) // Хранение изображений для всех продуктов

// Состояние детального просмотра товара
const selectedProduct = ref(null)
const selectedColor = ref(null)
const selectedSize = ref(null)
const mainImage = ref(null)
const expandedSection = ref(null)

// Получение всех товаров и фильтрация по полу
const fetchProducts = async () => {
  try {
    loading.value = true
    const response = await api.get('/api/product/all')

    // Фильтруем только мужские товары
    products.value = response.data.filter((product) => product.gender === 'MALE')

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

// Получение основного изображения товара для карточки
const getProductMainImage = (productId) => {
  const images = productImages.value[productId] || []
  return images.length > 0 ? images[0] : '/placeholder-image.jpg'
}

// Получение уникальных цветов для товара (для отображения в карточке)
const getUniqueColors = (product) => {
  return [...new Set(product.variants?.map((v) => v.color) || [])]
}

// Отображение товаров, отфильтрованных по полу
const filteredProducts = computed(() => {
  return products.value
})

// Выбор товара для детального просмотра
const selectProduct = (product) => {
  selectedProduct.value = product

  // Устанавливаем начальные значения для деталей товара
  if (product.variants && product.variants.length > 0) {
    selectedColor.value = product.variants[0].color
    selectedSize.value = null
  }

  // Устанавливаем изображения для просмотра
  const productImageList = productImages.value[product.id] || []
  if (productImageList.length > 0) {
    mainImage.value = productImageList[0]
  }
}

// Закрытие детального просмотра
const closeProductDetails = () => {
  selectedProduct.value = null
  selectedColor.value = null
  selectedSize.value = null
  mainImage.value = null
  expandedSection.value = null
}

// Computed свойства для вариантов товара
const uniqueColors = computed(() => {
  return [...new Set(selectedProduct.value?.variants?.map((v) => v.color) || [])]
})

const availableSizes = computed(() => {
  return (
    selectedProduct.value?.variants
      ?.filter((v) => v.color === selectedColor.value)
      .map((v) => v.size) || []
  )
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

  // Сбрасываем размер, если ранее выбранный размер недоступен для этого цвета
  if (selectedSize.value && !availableSizes.value.includes(selectedSize.value)) {
    selectedSize.value = null
  }
}

const selectSize = (size) => {
  selectedSize.value = size
}

const isSizeAvailable = (size) => {
  return selectedProduct.value?.variants?.some(
    (v) => v.color === selectedColor.value && v.size === size && v.stock > 0,
  )
}

const selectMainImage = (image) => {
  mainImage.value = image
}

const toggleAccordion = (section) => {
  expandedSection.value = expandedSection.value === section ? null : section
}

// Инициализация при монтировании компонента
onMounted(fetchProducts)
</script>

<style scoped>
.products-page {
  max-width: 1400px;
  margin: 0 auto;
  padding: 40px 20px;
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
  background: white;
  border: none;
  border-radius: 50%;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
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
</style>
