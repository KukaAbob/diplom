<template>
  <div class="purchases-container">
    <div class="purchases-header">
      <h2>My purchases</h2>
    </div>
    <div v-if="loading" class="loading-state">
      <p>Loading orders...</p>
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
      <div v-for="order in orders" :key="order.id" class="order-item">
        <div class="order-header">
          <div class="order-info">
            <div class="order-number">Order #{{ order.id }}</div>
            <div class="order-date">{{ formatDate(order.createdAt) }}</div>
          </div>
          <div class="order-status" :class="order.status.toLowerCase()">
            {{ order.status }}
          </div>
        </div>
        <div class="order-products">
          <div v-for="product in order.products" :key="product.id" class="product-item">
            <div class="product-image">
              <img :src="product.image" alt="Product" />
            </div>
            <div class="product-details">
              <div class="product-name">{{ product.name }}</div>
              <div class="product-price">{{ formatPrice(product.price) }}</div>
              <div class="product-quantity">Quantity: {{ product.quantity }}</div>
            </div>
          </div>
        </div>
        <div class="order-footer">
          <div class="order-total">Total: {{ formatPrice(calculateOrderTotal(order)) }}</div>
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
    }
  },
  computed: {
    hasPurchases() {
      return this.orders.length > 0
    },
  },
  methods: {
    goToNewArrivals() {
      this.$router.push('/general')
    },
    formatDate(dateString) {
      const date = new Date(dateString)
      return date.toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
      })
    },
    formatPrice(price) {
      return '$' + price.toFixed(2)
    },
    calculateOrderTotal(order) {
      return order.products.reduce((total, product) => {
        return total + product.price * product.quantity
      }, 0)
    },
    async fetchOrders() {
      this.loading = true
      try {
        const token = localStorage.getItem('jwtToken')

        if (!token) {
          this.error = 'Authentication required'
          this.loading = false
          return
        }

        // Декодируем JWT токен для получения email
        const tokenPayload = JSON.parse(atob(token.split('.')[1]))
        const email = tokenPayload.email

        // Отправляем запрос с параметром email
        const response = await api.get(`/api/order/user?email=${email}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        })

        console.log(response)
        this.orders = response.data
      } catch (error) {
        console.error('Error fetching orders:', error)
        this.error = 'Failed to load orders'
      } finally {
        this.loading = false
      }
    },
  },
  mounted() {
    this.fetchOrders()
  },
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
.order-item {
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  margin-bottom: 20px;
  overflow: hidden;
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
</style>
