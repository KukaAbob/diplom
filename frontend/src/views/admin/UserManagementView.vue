<template>
  <div class="user-management">
    <!-- Заголовок и поиск -->
    <div class="header-card">
      <!-- Индикатор загрузки -->
      <div v-if="loading" class="loading">
        <div class="spinner"></div>
        <p>Загрузка пользователей...</p>
      </div>

      <div v-else>
        <div class="card-header">
          <h2>Список пользователей</h2>
          <div class="search-container">
            <input 
              v-model="search" 
              placeholder="Поиск пользователей" 
              class="search-input"
              type="text"
            />
            <svg class="search-icon" viewBox="0 0 24 24">
              <path d="M15.5 14h-.79l-.28-.27A6.471 6.471 0 0 0 16 9.5 6.5 6.5 0 1 0 9.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z"/>
            </svg>
          </div>
        </div>

        <!-- HTML таблица -->
        <div v-if="filteredUsers && filteredUsers.length > 0" class="table-container">
          <table class="users-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Username</th>
                <th>Email</th>
                <th>Phone</th>
                <th>Role</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="user in filteredUsers" :key="user.id" class="table-row">
                <td>{{ user.id }}</td>
                <td class="username">{{ user.username }}</td>
                <td class="email">{{ user.email }}</td>
                <td class="phone">{{ user.phone || '—' }}</td>
                <td>
                  <span :class="['role-badge', getRoleClass(user.role)]">
                    {{ user.role }}
                  </span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- Сообщение об отсутствии данных -->
        <div v-else class="no-data">
          <p v-if="search">Пользователи не найдены по запросу "{{ search }}"</p>
          <p v-else>Нет данных для отображения</p>
        </div>
      </div>
    </div>

    <!-- Отладочная информация (закомментирована) -->
    <!-- <div class="debug-info">
      <h4>Отладочная информация:</h4>
      <p>Количество пользователей: {{ users.length }}</p>
      <p>Тип данных: {{ typeof users }}</p>
      <p>Это массив: {{ Array.isArray(users) }}</p>
      <pre v-if="users.length > 0">{{ JSON.stringify(users[0], null, 2) }}</pre>
    </div> -->
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import api from '@/api/api'

const users = ref([])
const loading = ref(false)
const search = ref('')

const filteredUsers = computed(() => {
  if (!search.value || !Array.isArray(users.value)) return users.value
 
  const searchLower = search.value.toLowerCase()
  return users.value.filter((user) => {
    if (!user) return false
    return (
      user.username?.toLowerCase().includes(searchLower) ||
      user.email?.toLowerCase().includes(searchLower)
    )
  })
})

// Функция для определения класса роли
const getRoleClass = (role) => {
  switch (role) {
    case 'USER': return 'role-user'
    case 'ADMIN': return 'role-admin'
    case 'MODERATOR': return 'role-moderator'
    default: return 'role-default'
  }
}

const fetchUsers = async () => {
  loading.value = true
  try {
    const response = await api.get('/api/dev/all')
    console.log('Полный ответ API:', response)
   
    // Получаем данные из ответа
    let data = response.data
   
    // Если данные находятся в другом поле
    if (!Array.isArray(data) && data.users) {
      data = data.users
    }
   
    // Проверяем, что это массив
    if (Array.isArray(data)) {
      users.value = data
      console.log('Пользователи загружены:', users.value.length)
      if (users.value.length > 0) {
        console.log('Первый пользователь:', users.value[0])
      }
    } else {
      console.error('Данные не являются массивом:', typeof data, data)
      users.value = []
      alert('Получены некорректные данные от сервера')
    }
   
  } catch (error) {
    console.error('Ошибка при загрузке:', error)
    alert('Ошибка при загрузке пользователей')
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
  padding: 20px;
}

/* Заголовок карточки */
.header-card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid #ebeef5;
  background-color: #fafafa;
}

.card-header h2 {
  margin: 0;
  color: #303133;
  font-size: 18px;
  font-weight: 600;
}

/* Поиск */
.search-container {
  position: relative;
  width: 300px;
}

.search-input {
  width: 100%;
  padding: 8px 12px 8px 40px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  font-size: 14px;
  transition: border-color 0.2s;
}

.search-input:focus {
  outline: none;
  border-color: #409eff;
}

.search-icon {
  position: absolute;
  left: 12px;
  top: 50%;
  transform: translateY(-50%);
  width: 16px;
  height: 16px;
  fill: #c0c4cc;
  pointer-events: none;
}

/* Стили для загрузки */
.loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  color: #666;
}

.spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #409eff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 16px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* Контейнер таблицы */
.table-container {
  overflow-x: auto;
}

/* Стили HTML таблицы */
.users-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 14px;
}

.users-table th,
.users-table td {
  padding: 12px 16px;
  text-align: left;
  border-bottom: 1px solid #ebeef5;
}

.users-table th {
  background-color: #f8f9fa;
  font-weight: 600;
  color: #303133;
  border-bottom: 2px solid #ebeef5;
}

.users-table tbody tr:hover {
  background-color: #f5f7fa;
}

.users-table tbody tr:nth-child(even) {
  background-color: #fafafa;
}

.users-table tbody tr:nth-child(even):hover {
  background-color: #f0f2f5;
}

/* Специальные стили для колонок */
.username {
  font-weight: 500;
  color: #303133;
}

.email {
  color: #606266;
}

.phone {
  color: #909399;
}

/* Стили для ролей */
.role-badge {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
  text-align: center;
  display: inline-block;
  min-width: 60px;
}

.role-user {
  background-color: #f0f9ff;
  color: #67c23a;
  border: 1px solid #b3e19d;
}

.role-admin {
  background-color: #fef0f0;
  color: #f56c6c;
  border: 1px solid #fbc4c4;
}

.role-moderator {
  background-color: #fdf6ec;
  color: #e6a23c;
  border: 1px solid #f5dab1;
}

.role-default {
  background-color: #f4f4f5;
  color: #909399;
  border: 1px solid #d3d4d6;
}

/* Сообщение об отсутствии данных */
.no-data {
  text-align: center;
  padding: 60px 20px;
  color: #999;
}

.no-data p {
  font-size: 16px;
  margin: 0;
}

/* Отладочная информация */
.debug-info {
  margin-top: 20px;
  padding: 10px;
  background: #f5f5f5;
  border-radius: 4px;
}

.debug-info pre {
  max-height: 200px;
  overflow: auto;
  background: white;
  padding: 10px;
  border-radius: 4px;
  font-size: 12px;
}

/* Адаптивность */
@media (max-width: 768px) {
  .card-header {
    flex-direction: column;
    gap: 16px;
    align-items: stretch;
  }
  
  .search-container {
    width: 100%;
  }
  
  .users-table {
    font-size: 12px;
  }
  
  .users-table th,
  .users-table td {
    padding: 8px 12px;
  }
}

@media (max-width: 480px) {
  .user-management {
    padding: 10px;
  }
  
  .users-table th,
  .users-table td {
    padding: 6px 8px;
  }
}
</style>