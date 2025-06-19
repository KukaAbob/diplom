<template>
  <div class="purchases-container">
    <div class="purchases-header">
      <h2>My purchases</h2>
    </div>
    <div v-if="loading" class="loading-state">
      <p>Loading orders...</p>
    </div>
    <div v-else-if="error" class="error-state">
      <h3>Error loading orders</h3>
      <p>{{ error }}</p>
      <button @click="fetchOrders" class="retry-button">Try again</button>
    </div>
    <div class="empty-state" v-else-if="!orders.length">
      <div class="empty-icon">
        <img src="@/assets/img/icons/profile.svg" alt="Buy online" />
      </div>
      <h3>There are no orders</h3>
      <p>You have yet to make an online purchase.</p>
      <p>Take a look at our latest arrivals!</p>
      <div class="action-button">
        <button @click="goToNewArrivals">See new arrivals</button>
      </div>
    </div>
    <div class="purchases-list" v-else>
      <div v-for="order in orders" :key="order.id" class="order-item" @click="toggleDetails(order)">
        <div class="order-header">
          <div class="order-info">
            <div class="order-number">Order #{{ order.id }}</div>
            <div class="order-date">{{ formatDate(order.createdAt) }}</div>
          </div>
          <div class="order-status" :class="order.status?.toLowerCase()">
            {{ order.status || 'Unknown' }}
          </div>
        </div>
        <div class="order-products" v-if="order.products && order.products.length > 0">
          <div v-for="product in order.products" :key="product.id" class="product-item">
            <div class="product-image">
              <img :src="product.image || '/placeholder-image.jpg'" :alt="product.name || 'Product'" />
            </div>
            <div class="product-details">
              <div class="product-name">{{ product.name || 'Unknown Product' }}</div>
              <div class="product-price">{{ formatPrice(product.price || 0) }}</div>
              <div class="product-quantity">Quantity: {{ product.quantity || 0 }}</div>
            </div>
          </div>
        </div>
        <div v-else class="no-products">
          <p>No product details available for this order.</p>
        </div>
        <div class="order-footer">
          <div class="order-total">Total: {{ formatPrice(calculateOrderTotal(order)) }}</div>
        </div>
        <div v-if="selectedOrder === order" class="order-details">
          <p>Additional details for Order #{{ order.id }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import api from '@/api/api'

export default {
  data() {
    return {
      orders: [],
      loading: true,
      error: null,
      selectedOrder: null
    }
  },
  computed: {
    hasPurchases() {
      return this.orders.length > 0
    }
  },
  methods: {
    goToNewArrivals() {
      this.$router.push('/general')
    },
    formatDate(dateString) {
      if (!dateString) return 'Unknown Date'
      try {
        const date = new Date(dateString)
        return date.toLocaleDateString('en-US', {
          year: 'numeric',
          month: 'long',
          day: 'numeric'
        })
      } catch (error) {
        return 'Invalid Date'
      }
    },
    formatPrice(price) {
      const numPrice = Number(price) || 0
      return '$' + numPrice.toFixed(2)
    },
    calculateOrderTotal(order) {
      // Если у заказа есть готовое поле total, используем его
      if (order.total && typeof order.total === 'number') {
        return order.total
      }
      
      // Иначе вычисляем из продуктов
      if (!order.products || !Array.isArray(order.products)) {
        return 0
      }
      return order.products.reduce((total, product) => {
        const price = Number(product.price) || 0
        const quantity = Number(product.quantity) || 0
        return total + (price * quantity)
      }, 0)
    },
    async fetchOrders() {
      this.loading = true
      this.error = null
      this.orders = [] // Очищаем данные перед загрузкой
      
      try {
        const token = localStorage.getItem('jwtToken')
        if (!token) {
          this.error = 'Authentication required'
          return
        }
        
        const tokenPayload = JSON.parse(atob(token.split('.')[1]))
        const email = tokenPayload.email
        
        const response = await api.get(`/api/order/user?email=${email}`, {
          headers: {
            Authorization: `Bearer ${token}`
          }
        })
        
        // СНАЧАЛА проверяем и обрабатываем полученные данные
        const rawOrders = response.data
        
        if (!Array.isArray(rawOrders)) {
          console.warn('Server response is not an array:', rawOrders)
          this.orders = []
          return
        }
        
        // ЗАТЕМ валидируем и нормализуем каждый заказ
        const validatedOrders = []
        
        for (let i = 0; i < rawOrders.length; i++) {
          const order = rawOrders[i]
          
          // Проверяем обязательные поля
          if (!order || typeof order !== 'object') {
            console.warn('Invalid order found (not an object):', order)
            continue
          }
          
          // Нормализуем данные заказа
          const normalizedOrder = {
            id: order.id || `order-${order.userId}-${i}`, // Генерируем ID если его нет
            status: order.status || 'Unknown',
            createdAt: order.date || order.createdAt || new Date().toISOString(),
            total: Number(order.total) || 0,
            userId: order.userId,
            executed: order.executed,
            products: []
          }
          
          // Обрабатываем продукты из orderItems
          if (order.orderItems && Array.isArray(order.orderItems)) {
            normalizedOrder.products = order.orderItems.filter(item => {
              // Проверяем, что элемент имеет минимально необходимые данные
              return item && (item.productId || item.price)
            }).map((item, index) => ({
              id: item.productId || `temp-${Date.now()}-${index}`,
              productId: item.productId,
              name: item.name || `Product ${item.productId}`, // Пока нет названия
              price: Number(item.price) || 0,
              quantity: Number(item.quantity) || 1,
              image: item.image || '/placeholder-image.jpg'
            }))
          }
          
          validatedOrders.push(normalizedOrder)
        }
        
        // ТОЛЬКО после полной валидации присваиваем данные
        this.orders = validatedOrders
        
        console.log('Successfully loaded orders:', this.orders.length)
        
      } catch (error) {
        console.error('Error fetching orders:', error)
        this.error = 'Failed to load orders'
        this.orders = []
      } finally {
        this.loading = false
      }
    },
    toggleDetails(order) {
      this.selectedOrder = this.selectedOrder === order ? null : order
    }
  },
  mounted() {
    this.fetchOrders()
  }
}
</script>

<style scoped>
.purchases-container {
  padding: 20px 0;
}
.purchases-header {
  margin-bottom: 30px;
}
.empty-state {
  text-align: center;
  padding: 40px 0;
}
.empty-icon {
  margin-bottom: 20px;
}
.empty-icon img {
  width: 100px;
}
.action-button {
  margin-top: 30px;
}
.action-button button {
  padding: 12px 30px;
  border: 1px solid #000;
  background: white;
  border-radius: 30px;
  cursor: pointer;
  transition: all 0.3s ease;
}
.action-button button:hover {
  background: #000;
  color: white;
}
.loading-state {
  text-align: center;
  padding: 40px 0;
}
.error-state {
  text-align: center;
  padding: 40px 0;
  color: #c62828;
}
.retry-button {
  padding: 12px 30px;
  border: 1px solid #c62828;
  background: white;
  color: #c62828;
  border-radius: 30px;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-top: 15px;
}
.retry-button:hover {
  background: #c62828;
  color: white;
}
.order-item {
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  margin-bottom: 20px;
  overflow: hidden;
  cursor: pointer;
}
.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  background-color: #f9f9f9;
  border-bottom: 1px solid #e0e0e0;
}
.order-number {
  font-weight: bold;
  font-size: 16px;
}
.order-date {
  color: #666;
  font-size: 14px;
  margin-top: 5px;
}
.order-status {
  padding: 5px 10px;
  border-radius: 4px;
  font-size: 14px;
  font-weight: 500;
}
.order-status.delivered {
  background-color: #e8f5e9;
  color: #2e7d32;
}
.order-status.in_progress {
  background-color: #fff8e1;
  color: #ff8f00;
}
.order-status.processing {
  background-color: #fff8e1;
  color: #ff8f00;
}
.order-status.shipped {
  background-color: #e3f2fd;
  color: #1976d2;
}
.order-status.cancelled {
  background-color: #ffebee;
  color: #c62828;
}
.order-products {
  padding: 15px 20px;
}
.no-products {
  padding: 15px 20px;
  text-align: center;
  color: #666;
  font-style: italic;
}
.product-item {
  display: flex;
  margin-bottom: 15px;
  padding-bottom: 15px;
  border-bottom: 1px solid #f0f0f0;
}
.product-item:last-child {
  margin-bottom: 0;
  padding-bottom: 0;
  border-bottom: none;
}
.product-image {
  width: 80px;
  height: 80px;
  margin-right: 15px;
}
.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 4px;
}
.product-details {
  flex: 1;
}
.product-name {
  font-weight: 500;
  margin-bottom: 5px;
}
.product-price {
  color: #333;
  margin-bottom: 5px;
}
.product-quantity {
  color: #666;
  font-size: 14px;
}
.order-footer {
  padding: 15px 20px;
  background-color: #f9f9f9;
  border-top: 1px solid #e0e0e0;
  text-align: right;
}
.order-total {
  font-weight: bold;
  font-size: 16px;
}
.order-details {
  padding: 15px 20px;
  background-color: #f0f0f0;
  border-top: 1px solid #e0e0e0;
}
</style>