<template>
  <div class="product-table">
    <!-- Индикатор загрузки -->
    <div v-if="loading" class="loading">
      <div class="spinner"></div>
      <p>Загрузка продуктов...</p>
    </div>

    <!-- HTML таблица -->
    <div v-else-if="products && products.length > 0" class="table-container">
      <table class="products-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Название</th>
            <th>Описание</th>
            <th>Цена</th>
            <th>Категория</th>
            <th>Статус</th>
            <th>Пол</th>
            <th>Действия</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="product in products" :key="product.id" class="table-row">
            <td>{{ product.id }}</td>
            <td class="product-name">{{ product.name }}</td>
            <td class="product-description">{{ product.description }}</td>
            <td class="price">
              <span v-if="product.price">{{ product.price }} ₸</span>
              <span v-else>—</span>
            </td>
            <td>
              <span v-if="product.category">{{ product.category.name }}</span>
              <span v-else>—</span>
            </td>
            <td>
              <span :class="['status', getStatusClass(product.productStatus)]">
                {{ getStatusText(product.productStatus) }}
              </span>
            </td>
            <td>{{ getGenderText(product.gender) }}</td>
            <td class="actions">
              <button 
                class="delete-btn" 
                @click="deleteProduct(product.id, product.name)"
                :disabled="loading"
                title="Удалить продукт"
              >
                <svg class="delete-icon" viewBox="0 0 24 24">
                  <path d="M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z"/>
                </svg>
                Удалить
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Сообщение об отсутствии данных -->
    <div v-else class="no-data">
      <p>Продукты не найдены</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '@/api/api'

const products = ref([])
const loading = ref(false)

// Функции для форматирования данных
const getStatusClass = (status) => {
  switch (status) {
    case 'IN_STOCK': return 'status-success'
    case 'OUT_OF_STOCK': return 'status-danger'
    case 'DISCONTINUED': return 'status-warning'
    default: return 'status-default'
  }
}

const getStatusText = (status) => {
  switch (status) {
    case 'IN_STOCK': return 'В наличии'
    case 'OUT_OF_STOCK': return 'Нет в наличии'
    case 'DISCONTINUED': return 'Снят с производства'
    default: return status
  }
}

const getGenderText = (gender) => {
  switch (gender) {
    case 'MALE': return 'Мужской'
    case 'FEMALE': return 'Женский'
    case 'UNISEX': return 'Унисекс'
    default: return gender || '—'
  }
}

const deleteProduct = async (productId, productName) => {
  // Подтверждение удаления
  const confirmed = confirm(`Вы уверены, что хотите удалить продукт "${productName}"?`)
  
  if (!confirmed) {
    return
  }

  try {
    console.log(`Удаляем продукт с ID: ${productId}`)
    
    // Отправляем DELETE запрос
    await api.delete(`/api/dev/product/${productId}`)
    
    // Удаляем продукт из локального массива
    products.value = products.value.filter(product => product.id !== productId)
    
    console.log(`Продукт "${productName}" успешно удален`)
    alert(`Продукт "${productName}" успешно удален`)
    
  } catch (error) {
    console.error('Ошибка при удалении продукта:', error)
    alert('Ошибка при удалении продукта: ' + (error.message || 'Неизвестная ошибка'))
  }
}

const fetchProducts = async () => {
  loading.value = true
  try {
    console.log('Начинаем загрузку продуктов...')
    const response = await api.get('/api/dev/product/all')
    console.log('Полный ответ API:', response)
   
    // Инициализируем пустым массивом по умолчанию
    products.value = []
   
    // Проверяем структуру ответа
    if (response && response.data) {
      let data = response.data
      console.log('response.data:', data, 'тип:', typeof data)
     
      // Если данные в другом поле (например, response.data.products)
      if (!Array.isArray(data) && typeof data === 'object') {
        if (data.products && Array.isArray(data.products)) {
          data = data.products
        } else if (data.data && Array.isArray(data.data)) {
          data = data.data
        }
      }
     
      // Убеждаемся, что это массив
      if (Array.isArray(data)) {
        // Проверяем каждый элемент массива
        const validProducts = data.filter(item =>
          item && typeof item === 'object' && item.id
        )
       
        products.value = validProducts
        console.log('Продукты успешно загружены:', products.value.length)
        
        if (products.value.length > 0) {
          console.log('Первый продукт:', products.value[0])
        }
      } else {
        console.error('Данные не являются массивом:', typeof data, data)
        alert('Получен неправильный формат данных')
      }
    } else {
      console.error('Пустой ответ от сервера:', response)
      alert('Пустой ответ от сервера')
    }
   
  } catch (error) {
    console.error('Ошибка при загрузке продуктов:', error)
    alert('Ошибка при загрузке продуктов: ' + (error.message || 'Неизвестная ошибка'))
    products.value = []
  } finally {
    loading.value = false
  }
}

onMounted(fetchProducts)
</script>

<style scoped>
.product-table {
  min-height: 400px;
  padding: 20px;
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
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

/* Стили HTML таблицы */
.products-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 14px;
}

.products-table th,
.products-table td {
  padding: 12px 16px;
  text-align: left;
  border-bottom: 1px solid #ebeef5;
}

.products-table th {
  background-color: #fafafa;
  font-weight: 600;
  color: #303133;
  border-bottom: 2px solid #ebeef5;
}

.products-table tbody tr:hover {
  background-color: #f5f7fa;
}

.products-table tbody tr:nth-child(even) {
  background-color: #fafafa;
}

.products-table tbody tr:nth-child(even):hover {
  background-color: #f0f2f5;
}

/* Специальные стили для колонок */
.product-name {
  font-weight: 500;
  color: #303133;
  min-width: 150px;
}

.product-description {
  max-width: 200px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.price {
  font-weight: 600;
  color: #409eff;
  text-align: right;
}

/* Стили для статусов */
.status {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
  text-align: center;
  display: inline-block;
  min-width: 80px;
}

.status-success {
  background-color: #f0f9ff;
  color: #67c23a;
  border: 1px solid #b3e19d;
}

.status-danger {
  background-color: #fef0f0;
  color: #f56c6c;
  border: 1px solid #fbc4c4;
}

.status-warning {
  background-color: #fdf6ec;
  color: #e6a23c;
  border: 1px solid #f5dab1;
}

.status-default {
  background-color: #f4f4f5;
  color: #909399;
  border: 1px solid #d3d4d6;
}

/* Сообщение об отсутствии данных */
.no-data {
  text-align: center;
  padding: 60px 20px;
  color: #999;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.no-data p {
  font-size: 16px;
  margin: 0;
}

/* Стили для кнопки удаления */
.actions {
  text-align: center;
  padding: 8px 12px;
}

.delete-btn {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 6px 12px;
  background-color: #f56c6c;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
  min-width: 80px;
  justify-content: center;
}

.delete-btn:hover:not(:disabled) {
  background-color: #f04b4b;
  transform: translateY(-1px);
  box-shadow: 0 2px 4px rgba(245, 108, 108, 0.3);
}

.delete-btn:active:not(:disabled) {
  transform: translateY(0);
  box-shadow: 0 1px 2px rgba(245, 108, 108, 0.3);
}

.delete-btn:disabled {
  background-color: #c0c4cc;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

.delete-icon {
  width: 14px;
  height: 14px;
  fill: currentColor;
}

/* Адаптивность */
@media (max-width: 768px) {
  .products-table {
    font-size: 12px;
  }
  
  .products-table th,
  .products-table td {
    padding: 8px 12px;
  }
  
  .product-description {
    max-width: 120px;
  }
  
  .delete-btn {
    padding: 4px 8px;
    font-size: 11px;
    min-width: 60px;
  }
  
  .delete-btn span {
    display: none;
  }
}
</style>