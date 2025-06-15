<template>
  <div class="order-management">
    <el-card v-loading="loading">
      <template #header>
        <div class="card-header">
          <span>Управление заказами</span>
          <el-select v-model="statusFilter" placeholder="Фильтр по статусу" clearable>
            <el-option
              v-for="status in statusOptions"
              :key="status.value"
              :label="status.label"
              :value="status.value"
            />
          </el-select>
        </div>
      </template>

      <el-table :data="filteredOrders" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="user.username" label="Покупатель" />
        <el-table-column prop="total" label="Сумма" width="120">
          <template #default="{ row }">
            {{ row.total }} ₽
          </template>
        </el-table-column>
        <el-table-column prop="status" label="Статус" width="150">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="Дата заказа" width="180">
          <template #default="{ row }">
            {{ new Date(row.createdAt).toLocaleDateString() }}
          </template>
        </el-table-column>
        <el-table-column label="Действия" width="200" fixed="right">
          <template #default="{ row }">
            <el-button
              size="small"
              :type="row.status === 'PENDING' ? 'success' : 'primary'"
              :disabled="row.status === 'COMPLETED' || row.status === 'CANCELLED'"
              @click="handleStatusChange(row)"
            >
              {{ getActionButtonText(row.status) }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import api from '@/api/api'

const orders = ref([])
const loading = ref(false)
const statusFilter = ref('')

const statusOptions = [
  { value: 'PENDING', label: 'Ожидает' },
  { value: 'PROCESSING', label: 'В обработке' },
  { value: 'COMPLETED', label: 'Завершен' },
  { value: 'CANCELLED', label: 'Отменен' }
]

const filteredOrders = computed(() => {
  if (!statusFilter.value) return orders.value
  return orders.value.filter(order => order.status === statusFilter.value)
})

const getStatusType = (status) => {
  const types = {
    'PENDING': 'warning',
    'PROCESSING': 'info',
    'COMPLETED': 'success',
    'CANCELLED': 'danger'
  }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = {
    'PENDING': 'Ожидает',
    'PROCESSING': 'В обработке',
    'COMPLETED': 'Завершен',
    'CANCELLED': 'Отменен'
  }
  return texts[status] || status
}

const getActionButtonText = (status) => {
  const texts = {
    'PENDING': 'Принять в обработку',
    'PROCESSING': 'Завершить',
    'COMPLETED': 'Завершен',
    'CANCELLED': 'Отменен'
  }
  return texts[status] || 'Изменить статус'
}

const handleStatusChange = async (order) => {
  try {
    const newStatus = order.status === 'PENDING' ? 'PROCESSING' : 'COMPLETED'
    await api.put(`/api/dev/order/${order.id}/status`, { status: newStatus })
    ElMessage.success('Статус заказа обновлен')
    fetchOrders()
  } catch (error) {
    ElMessage.error('Ошибка при обновлении статуса заказа')
  }
}

const fetchOrders = async () => {
  loading.value = true
  try {
    const response = await api.get('/api/dev/order/all')
    orders.value = response.data
  } catch (error) {
    ElMessage.error('Ошибка при загрузке заказов')
  } finally {
    loading.value = false
  }
}

onMounted(fetchOrders)
</script>

<style scoped>
.order-management {
  min-height: 400px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>