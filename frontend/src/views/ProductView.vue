<template>
  <div class="product-container" v-if="product">
    <div class="product-image">
      <img :src="product.imagePath || defaultImage" :alt="product.name" />
    </div>
    <div class="product-details">
      <h1>{{ product.name }}</h1>
      <p class="description">{{ product.description }}</p>
      <p class="price">{{ product.price }} ₽</p>
      <p class="status" :class="{ 'out-of-stock': product.productStatus !== 'IN_STOCK' }">
        {{ product.productStatus === 'IN_STOCK' ? 'В наличии' : 'Нет в наличии' }}
      </p>
      <button :disabled="product.productStatus !== 'IN_STOCK'" class="add-to-cart">
        Добавить в корзину
      </button>

      <div class="accordion">
        <div class="accordion-item">
          <button @click="toggleAccordion('composition')">Состав, уход, происхождение</button>
          <div v-if="expandedSection === 'composition'" class="accordion-content">
            <p>Состав: 100% хлопок</p>
            <p>Уход: Машинная стирка при 30°C</p>
            <p>Происхождение: Сделано в России</p>
          </div>
        </div>
        <div class="accordion-item">
          <button @click="toggleAccordion('shipping')">Доставка и возврат</button>
          <div v-if="expandedSection === 'shipping'" class="accordion-content">
            <p>Доставка: 2-5 рабочих дней</p>
            <p>Возврат: В течение 14 дней с момента получения</p>
          </div>
        </div>
      </div>
    </div>
  </div>
  <p v-else>Загрузка...</p>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import api from '@/api/api'

const route = useRoute()
const product = ref(null)
const defaultImage = '/img/default-product.jpg'
const expandedSection = ref(null)

const fetchProduct = async () => {
  try {
    const response = await api.get(`/api/product/${route.params.id}`)
    product.value = response.data
  } catch (error) {
    console.error('Ошибка загрузки товара', error)
  }
}

const toggleAccordion = (section) => {
  expandedSection.value = expandedSection.value === section ? null : section
}

onMounted(fetchProduct)
</script>

<style scoped>
.product-container {
  display: flex;
  gap: 20px;
  padding: 20px;
}
.product-image img {
  max-width: 300px;
  border-radius: 8px;
}
.product-details {
  flex: 1;
}
.price {
  font-size: 1.5em;
  font-weight: bold;
}
.status {
  font-weight: bold;
}
.out-of-stock {
  color: red;
}
.add-to-cart {
  padding: 10px;
  margin-top: 10px;
  background: green;
  color: white;
  border: none;
  cursor: pointer;
}
.add-to-cart:disabled {
  background: gray;
  cursor: not-allowed;
}
.accordion-item {
  margin-top: 10px;
}
.accordion button {
  width: 100%;
  padding: 10px;
  text-align: left;
  background: #f5f5f5;
  border: none;
  cursor: pointer;
}
.accordion-content {
  padding: 10px;
  background: #fff;
  border: 1px solid #ddd;
}
</style>
