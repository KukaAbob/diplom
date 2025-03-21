import { defineStore } from 'pinia'
import api from '@/api/api' // Подключаем API-инстанс
import { jwtDecode } from 'jwt-decode' // Исправлен импорт

export const useCartStore = defineStore('cart', {
  state: () => ({
    items: [],
    totalPrice: 0,
    loading: false,
  }),

  actions: {
    async fetchCart() {
      this.loading = true
      try {
        const token = localStorage.getItem('jwtToken')

        if (!token) {
          // Если нет токена, устанавливаем пустую корзину
          this.items = []
          this.totalPrice = 0
          this.loading = false
          return
        }

        const userEmail = this.getEmailFromToken(token)
        console.log(userEmail)

        const response = await api.get(`/api/cart?email=${userEmail}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        })

        // Убедимся, что items всегда является массивом
        this.items = response.data.items || []
        this.totalPrice = response.data.totalPrice || 0
      } catch (error) {
        console.error('Ошибка при получении корзины:', error)
        // При ошибке также устанавливаем пустую корзину
        this.items = []
        this.totalPrice = 0
      } finally {
        this.loading = false
      }
    },

    async addToCart({ productId, quantity = 1 }) {
      try {
        const token = localStorage.getItem('jwtToken')
        if (!token) return

        const userEmail = this.getEmailFromToken(token)

        await api.post(
          '/api/cart/add',
          {
            email: userEmail,
            productId,
            quantity,
          },
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          },
        )

        // Обновляем состояние корзины после добавления товара
        await this.fetchCart()
      } catch (error) {
        console.error('Ошибка при добавлении товара в корзину:', error)
      }
    },

    async removeFromCart(itemId) {
      try {
        const token = localStorage.getItem('jwtToken')
        if (!token) return

        const userEmail = this.getEmailFromToken(token)

        await api.delete(`/api/cart/item/${itemId}?email=${userEmail}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        })

        // Обновляем состояние корзины после удаления товара
        await this.fetchCart()
      } catch (error) {
        console.error('Ошибка при удалении товара из корзины:', error)
      }
    },

    async clearCart() {
      try {
        const token = localStorage.getItem('jwtToken')
        if (!token) return

        const userEmail = this.getEmailFromToken(token)

        await api.delete(`/api/cart?email=${userEmail}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        })

        // Обновляем состояние корзины после очистки
        this.items = []
        this.totalPrice = 0
      } catch (error) {
        console.error('Ошибка при очистке корзины:', error)
      }
    },

    getEmailFromToken(token) {
      try {
        const decoded = jwtDecode(token) // Использование jwtDecode вместо jwt_decode
        console.log(decoded)
        return decoded.email || ''
      } catch (error) {
        console.error('Ошибка декодирования токена:', error)
        return ''
      }
    },
  },
})
