<template>
  <div class="product-page" v-if="product">
    <div class="product-gallery">
      <div class="product-images">
        <img
          v-for="(image, index) in productImages"
          :key="index"
          :src="image"
          :alt="product.name"
          @click="selectMainImage(image)"
        />
      </div>
      <div class="main-image">
        <img
          :src="mainImage || (productImages.length > 0 ? productImages[0] : '')"
          :alt="product.name"
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
      <h1>{{ product.name }}</h1>
      <p class="product-price">{{ currentVariant?.price || product.price }} KZT</p>
      <p class="product-code">{{ selectedColor }} - Артикул: {{ product.id }}</p>

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
            <span class="accordion-icon">{{ expandedSection === 'composition' ? '−' : '+' }}</span>
          </button>
          <div v-if="expandedSection === 'composition'" class="accordion-content">
            <p>{{ product.description }}</p>
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
  <p v-else>Загрузка...</p>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import api from '@/api/api'

const route = useRoute()
const product = ref(null)
const productImages = ref([])
const mainImage = ref(null)
const expandedSection = ref(null)

// Variant selection state
const selectedColor = ref(null)
const selectedSize = ref(null)

const fetchProduct = async () => {
  try {
    const response = await api.get(`/api/product/${route.params.id}`)
    product.value = response.data

    // Set initial color and size if variants exist
    if (product.value.variants && product.value.variants.length > 0) {
      // Select first available color
      selectedColor.value = product.value.variants[0].color
    }

    // Fetch product images
    await fetchProductImages()
  } catch (error) {
    console.error('Ошибка загрузки товара', error)
  }
}

const fetchProductImages = async () => {
  try {
    const response = await api.get(`/api/images/all/${route.params.id}`)
    productImages.value = response.data
      .map((imageObj) => {
        const base64Data = imageObj.oid || imageObj.base64 || imageObj.image || imageObj
        if (!base64Data) return null
        return base64Data.startsWith('data:image')
          ? base64Data
          : `data:image/jpeg;base64,${base64Data}`
      })
      .filter(Boolean)

    if (productImages.value.length > 0) {
      mainImage.value = productImages.value[0]
    }
  } catch (error) {
    console.error('Ошибка загрузки изображений', error)
  }
}

// Computed properties for variants
const uniqueColors = computed(() => {
  return [...new Set(product.value?.variants?.map((v) => v.color) || [])]
})

const availableSizes = computed(() => {
  return (
    product.value?.variants?.filter((v) => v.color === selectedColor.value).map((v) => v.size) || []
  )
})

const currentVariant = computed(() => {
  return product.value?.variants?.find(
    (v) => v.color === selectedColor.value && v.size === selectedSize.value,
  )
})

const canAddToCart = computed(() => {
  return currentVariant.value && currentVariant.value.stock > 0
})

// Methods for selection
const selectColor = (color) => {
  selectedColor.value = color

  // Reset size if the previously selected size is not available for this color
  if (selectedSize.value && !availableSizes.value.includes(selectedSize.value)) {
    selectedSize.value = null
  }
}

const selectSize = (size) => {
  selectedSize.value = size
}

const isSizeAvailable = (size) => {
  return product.value?.variants?.some(
    (v) => v.color === selectedColor.value && v.size === size && v.stock > 0,
  )
}

const selectMainImage = (image) => {
  mainImage.value = image
}

const toggleAccordion = (section) => {
  expandedSection.value = expandedSection.value === section ? null : section
}

onMounted(fetchProduct)
</script>

<style scoped>
.product-page {
  display: flex;
  max-width: 1400px;
  max-width: 1500px;
  margin: 0 auto;
  padding: 40px 20px;
  margin-left: 20%;
  margin-right: 20%;
  gap: 80px;
  height: auto;
}

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

.favorite-btn {
  position: absolute;
  top: 20px;
  right: 20px;
  background: white;
  border: none;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
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
</style>
