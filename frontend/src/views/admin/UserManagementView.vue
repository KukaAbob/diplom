<template>
  <div class="user-management">
    <el-card v-loading="loading">
      <template #header>
        <div class="card-header">
          <span>Список пользователей</span>
          <el-input
            v-model="search"
            placeholder="Поиск пользователей"
            style="width: 300px"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
        </div>
      </template>
      
      <!-- Простая таблица с данными -->
      <el-table :data="filteredUsers" stripe v-if="users.length > 0">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="Username" width="150" />
        <el-table-column prop="email" label="Email" width="200" />
        <el-table-column prop="phone" label="Phone" width="120" />
        <el-table-column prop="role" label="Role" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.role === 'USER' ? 'success' : 'danger'">
              {{ scope.row.role }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
      
      <div v-else-if="!loading" style="text-align: center; padding: 20px; color: #999;">
        Нет данных для отображения
      </div>
      
      <!-- Отладочная информация -->
      <div style="margin-top: 20px; padding: 10px; background: #f5f5f5;">
        <h4>Отладочная информация:</h4>
        <p>Количество пользователей: {{ users.length }}</p>
        <p>Тип данных: {{ typeof users }}</p>
        <p>Это массив: {{ Array.isArray(users) }}</p>
        <pre style="max-height: 200px; overflow: auto;">{{ JSON.stringify(users, null, 2) }}</pre>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import api from '@/api/api'

const users = ref([])
const loading = ref(false)
const search = ref('')

const filteredUsers = computed(() => {
  if (!search.value) return users.value
  
  const searchLower = search.value.toLowerCase()
  return users.value.filter(user => 
    user.username?.toLowerCase().includes(searchLower) ||
    user.email?.toLowerCase().includes(searchLower)
  )
})

const fetchUsers = async () => {
  loading.value = true
  try {
    const response = await api.get('/api/dev/all')
    
    console.log('Полный ответ:', response)
    
    // Принудительно конвертируем в обычный массив без Proxy
    let data = response.data || response
    
    // Если это Proxy объект, извлекаем данные
    if (data && typeof data === 'object' && !Array.isArray(data)) {
      if (data.target && Array.isArray(data.target)) {
        data = data.target
      }
    }
    
    // Конвертируем в обычный JSON и обратно, чтобы убрать Proxy
    if (Array.isArray(data)) {
      const jsonString = JSON.stringify(data)
      users.value = JSON.parse(jsonString)
    } else {
      users.value = []
    }
    
    console.log('Финальные данные (без Proxy):', users.value)
    console.log('Тип данных:', typeof users.value)
    console.log('Это массив:', Array.isArray(users.value))
    
  } catch (error) {
    console.error('Ошибка:', error)
    ElMessage.error('Ошибка при загрузке пользователей')
    users.value = []
  } finally {
    loading.value = false
  }
}

onMounted(fetchUsers)
</script>

<style scoped>
.user-management {
  min-height: 400px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>