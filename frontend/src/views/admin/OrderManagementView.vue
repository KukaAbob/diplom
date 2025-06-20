<template>
  <div class="order-management">
    <!-- Заголовок и фильтры -->
    <div class="header-card">
      <!-- Индикатор загрузки -->
      <div v-if="loading" class="loading">
        <div class="spinner"></div>
        <p>Загрузка заказов...</p>
      </div>

      <div v-else>
        <div class="card-header">
          <h2>Управление заказами</h2>
          <div class="filters-container">
            <el-select 
              v-model="statusFilter" 
              placeholder="Статус заказа" 
              clearable
              class="filter-select"
            >
              <el-option label="В процессе" value="IN_PROGRESS" />
              <el-option label="Завершен" value="COMPLETED" />
              <el-option label="Отменен" value="CANCELLED" />
              <el-option label="Ошибка" value="ERROR" />
            </el-select>
          </div>
        </div>

        <!-- Таблица заказов -->
        <div v-if="filteredOrders && filteredOrders.length > 0" class="table-container">
          <table class="orders-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Пользователь</th>
                <th>Адрес</th>
                <th>Дата</th>
                <th>Сумма</th>
                <th>Статус</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="order in filteredOrders" :key="order.id">
                <td>{{ order.id }}</td>
                <td>
                  <div class="user-info">
                    <strong>{{ order.username }}</strong>
                    <small>(ID: {{ order.userId }})</small>
                  </div>
                </td>
                <td>
                  <div class="address-info">
                    {{ order.addressString }}
                  </div>
                </td>
                <td>{{ formatDate(order.date) }}</td>
                <td class="price">{{ formatPrice(order.total) }} ₽</td>
                <td>
                  <span :class="['status-badge', getStatusClass(order.status)]">
                    {{ getStatusText(order.status) }}
                  </span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- Сообщение об отсутствии данных -->
        <div v-else class="no-data">
          <p v-if="statusFilter">Заказы не найдены с выбранным статусом</p>
          <p v-else>Нет заказов для отображения</p>
        </div>
      </div>
    </div>

    <!-- Модальное окно деталей заказа -->
    <el-dialog
      v-model="showDetailsModal"
      title="Детали заказа"
      width="60%"
    >
      <div v-if="selectedOrder" class="order-details">
        <div class="detail-section">
          <h3>Информация о заказе</h3>
          <p><strong>ID заказа:</strong> {{ selectedOrder.id }}</p>
          <p><strong>Пользователь:</strong> {{ selectedOrder.username }} (ID: {{ selectedOrder.userId }})</p>
          <p><strong>Дата:</strong> {{ formatDate(selectedOrder.date) }}</p>
          <p><strong>Статус:</strong> {{ getStatusText(selectedOrder.status) }}</p>
          <p><strong>Выполнен:</strong> {{ selectedOrder.executed ? 'Да' : 'Нет' }}</p>
          <p><strong>Общая сумма:</strong> {{ formatPrice(selectedOrder.total) }} ₽</p>
        </div>

        <div class="detail-section">
          <h3>Адрес доставки</h3>
          <p><strong>Адрес:</strong> {{ selectedOrder.addressString }}</p>
          <p><strong>ID адреса:</strong> {{ selectedOrder.addressId }}</p>
        </div>

        <div class="detail-section">
          <h3>Платеж</h3>
          <p><strong>ID платежа:</strong> {{ selectedOrder.paymentId }}</p>
        </div>

        <div class="detail-section">
          <h3>Товары в заказе</h3>
          <div v-if="selectedOrder.orderItems && selectedOrder.orderItems.length > 0">
            <el-table :data="selectedOrder.orderItems">
              <el-table-column prop="productId" label="ID товара" width="100" />
              <el-table-column prop="quantity" label="Количество" width="120" />
              <el-table-column prop="price" label="Цена за единицу">
                <template #default="{ row }">
                  {{ formatPrice(row.price) }} ₽
                </template>
              </el-table-column>
              <el-table-column label="Сумма">
                <template #default="{ row }">
                  {{ formatPrice(row.price * row.quantity) }} ₽
                </template>
              </el-table-column>
            </el-table>
          </div>
          <div v-else class="no-items">
            <p>В заказе нет товаров</p>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import api from '@/api/api'

const orders = ref([])
const loading = ref(false)
const updatingStatus = ref(null)
const statusFilter = ref('')
const showDetailsModal = ref(false)
const selectedOrder = ref(null)

// Фильтрация заказов по статусу
const filteredOrders = computed(() => {
  if (!statusFilter.value) return orders.value
  return orders.value.filter(order => order.status === statusFilter.value)
})

// Форматирование даты
const formatDate = (date) => {
  if (!date) return '—'
  return new Date(date).toLocaleString('ru-RU')
}

// Форматирование цены
const formatPrice = (price) => {
  if (!price) return '0'
  return Number(price).toLocaleString('ru-RU', { minimumFractionDigits: 2 })
}

// Получение класса для статуса
const getStatusClass = (status) => {
  switch (status) {
    case 'IN_PROGRESS': return 'status-progress'
    case 'COMPLETED': return 'status-completed'
    case 'CANCELLED': return 'status-cancelled'
    case 'ERROR': return 'status-error'
    default: return 'status-default'
  }
}

// Получение текста статуса
const getStatusText = (status) => {
  switch (status) {
    case 'IN_PROGRESS': return 'В процессе'
    case 'COMPLETED': return 'Завершен'
    case 'CANCELLED': return 'Отменен'
    case 'ERROR': return 'Ошибка'
    default: return status
  }
}

// Показать детали заказа
const showOrderDetails = (order) => {
  selectedOrder.value = order
  showDetailsModal.value = true
}

// Обновление статуса заказа
const updateOrderStatus = async (orderId, newStatus) => {
  try {
    updatingStatus.value = orderId
    
    await api.put(`/api/dev/order/${orderId}/status`, { status: newStatus })
    ElMessage.success('Статус заказа успешно обновлен')
    await fetchOrders() // Перезагружаем список заказов
  } catch (error) {
    console.error('Ошибка при обновлении статуса:', error)
    if (error.response?.status === 400) {
      ElMessage.error('Некорректный статус заказа')
    } else if (error.response?.status === 404) {
      ElMessage.error('Заказ не найден')
    } else {
      ElMessage.error('Ошибка при обновлении статуса заказа')
    }
  } finally {
    updatingStatus.value = null
  }
}

// Загрузка заказов
const fetchOrders = async () => {
  loading.value = true
  try {
    console.log('Загрузка заказов...')
    const response = await api.get('/api/dev/order/all')
    console.log('Ответ API заказов:', response)
    
    // Проверяем наличие content в ответе (для пагинации)
    const ordersData = response.data.content || response.data
    
    if (Array.isArray(ordersData)) {
      orders.value = ordersData.map((order) => ({
        id: order.id,
        userId: order.userId,
        username: order.username || `Пользователь ${order.userId}`,
        addressId: order.addressId,
        addressString: order.addressString || 'Адрес не указан',
        paymentId: order.paymentId,
        date: order.date,
        total: order.total || 0,
        status: order.status,
        executed: order.executed,
        orderItems: order.orderItems || []
      }))
      console.log(`Заказы успешно загружены: ${orders.value.length} шт.`)
    } else {
      console.error('Некорректный формат данных:', ordersData)
      ElMessage.error('Получены некорректные данные с сервера')
      orders.value = []
    }
  } catch (error) {
    console.error('Ошибка при загрузке заказов:', error)
    
    // Более подробная обработка ошибок
    if (error.response?.status === 502) {
      ElMessage.error('Сервер временно недоступен. Попробуйте позже.')
    } else if (error.response?.status === 404) {
      ElMessage.error('API не найден. Проверьте правильность URL.')
    } else if (error.response?.status === 500) {
      ElMessage.error('Внутренняя ошибка сервера')
    } else if (error.code === 'NETWORK_ERROR') {
      ElMessage.error('Ошибка сети. Проверьте подключение к интернету.')
    } else {
      ElMessage.error('Ошибка при загрузке заказов')
    }
    
    orders.value = []
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  console.log('Компонент загружен, начинаем загрузку заказов')
  fetchOrders()
})
</script>

<style scoped>
.order-management {
  padding: 20px;
}

.header-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.card-header h2 {
  margin: 0;
  color: #303133;
}

.filters-container {
  display: flex;
  gap: 10px;
}

.filter-select {
  width: 200px;
}

.loading {
  text-align: center;
  padding: 40px;
}

.spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #409eff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 20px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.table-container {
  overflow-x: auto;
}

.orders-table {
  width: 100%;
  border-collapse: collapse;
  background: white;
}

.orders-table th,
.orders-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #ebeef5;
}

.orders-table th {
  background: #f5f7fa;
  font-weight: 600;
  color: #606266;
}

.user-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.user-info small {
  color: #909399;
  font-size: 12px;
}

.address-info {
  color: #606266;
  font-size: 14px;
}

.price {
  font-weight: 600;
  color: #67c23a;
}

.status-badge {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
}

.status-progress {
  background: #e6f7ff;
  color: #1890ff;
}

.status-completed {
  background: #f6ffed;
  color: #52c41a;
}

.status-cancelled {
  background: #fff2f0;
  color: #ff4d4f;
}

.status-error {
  background: #fff1f0;
  color: #ff4d4f;
}

.status-default {
  background: #fafafa;
  color: #666;
}

.actions {
  display: flex;
  gap: 8px;
}

.no-data {
  text-align: center;
  padding: 40px;
  color: #909399;
}

.order-details {
  max-height: 500px;
  overflow-y: auto;
}

.detail-section {
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #ebeef5;
}

.detail-section:last-child {
  border-bottom: none;
}

.detail-section h3 {
  margin: 0 0 12px 0;
  color: #303133;
  font-size: 16px;
}

.detail-section p {
  margin: 8px 0;
  color: #606266;
}

.no-items {
  text-align: center;
  color: #909399;
  padding: 20px;
}
</style>