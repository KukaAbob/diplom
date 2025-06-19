<template>
  <div class="product-table">
    <!-- Заголовок с кнопкой создания -->
    <div class="table-header">
      <h2>Управление продуктами</h2>
      <button 
        class="create-btn" 
        @click="openCreateModal"
        :disabled="loading"
        title="Создать новый продукт"
      >
        <svg class="create-icon" viewBox="0 0 24 24">
          <path d="M19 13h-6v6h-2v-6H5v-2h6V5h2v6h6v2z"/>
        </svg>
        Создать продукт
      </button>
    </div>

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
                class="edit-btn" 
                @click="editProduct(product)"
                :disabled="loading"
                title="Редактировать продукт"
              >
                <svg class="edit-icon" viewBox="0 0 24 24">
                  <path d="M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25zM20.71 7.04c.39-.39.39-1.02 0-1.41l-2.34-2.34c-.39-.39-1.02-.39-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z"/>
                </svg>
                Редактировать
              </button>
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
    <div v-else class="no-data">
      <p>Продукты не найдены</p>
      <button 
        class="create-btn secondary" 
        @click="openCreateModal"
        :disabled="loading"
      >
        <svg class="create-icon" viewBox="0 0 24 24">
          <path d="M19 13h-6v6h-2v-6H5v-2h6V5h2v6h6v2z"/>
        </svg>
        Создать первый продукт
      </button>
    </div>

    <!-- Модальное окно создания -->
    <div v-if="showCreateModal" class="modal-overlay" @click="closeCreateModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>Создать новый продукт</h3>
          <button class="close-btn" @click="closeCreateModal">
            <svg viewBox="0 0 24 24">
              <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"/>
            </svg>
          </button>
        </div>
        
        <form @submit.prevent="createProduct" class="edit-form">
          <div class="form-group">
            <label for="new-name">Название <span class="required">*</span>:</label>
            <input 
              id="new-name"
              v-model="newProduct.name" 
              type="text" 
              required 
              class="form-input"
              placeholder="Введите название продукта"
            >
          </div>
          
          <div class="form-group">
            <label for="new-description">Описание <span class="required">*</span>:</label>
            <textarea 
              id="new-description"
              v-model="newProduct.description" 
              class="form-textarea"
              rows="3"
              required
              placeholder="Введите описание продукта"
            ></textarea>
          </div>
          
          <div class="form-row">
            <div class="form-group">
              <label for="new-price">Цена <span class="required">*</span>:</label>
              <input 
                id="new-price"
                v-model="newProduct.price" 
                type="number" 
                step="0.01"
                min="0"
                required
                class="form-input"
                placeholder="0.00"
              >
            </div>
            
            <div class="form-group">
              <label for="new-collection">Коллекция:</label>
              <input 
                id="new-collection"
                v-model="newProduct.collection" 
                type="text" 
                class="form-input"
                placeholder="Введите название коллекции"
              >
            </div>
          </div>
          
          <div class="form-row">
            <div class="form-group">
              <label for="new-category">Категория <span class="required">*</span>:</label>
              <select id="new-category" v-model="newProduct.categoryId" class="form-select" required>
                <option value="">Выберите категорию</option>
                <option v-for="category in categories" :key="category.id" :value="category.id">
                  {{ category.name }}
                </option>
              </select>
            </div>
            
            <div class="form-group">
              <label for="new-status">Статус <span class="required">*</span>:</label>
              <select id="new-status" v-model="newProduct.productStatus" class="form-select" required>
                <option value="IN_STOCK">В наличии</option>
                <option value="OUT_OF_STOCK">Нет в наличии</option>
              </select>
            </div>
          </div>
          
          <div class="form-group">
            <label for="new-gender">Пол <span class="required">*</span>:</label>
            <select id="new-gender" v-model="newProduct.gender" class="form-select" required>
              <option value="MALE">Мужской</option>
              <option value="FEMALE">Женский</option>
            </select>
          </div>
          
          <div class="modal-actions">
            <button type="button" class="cancel-btn" @click="closeCreateModal">
              Отмена
            </button>
            <button type="submit" class="save-btn" :disabled="creating">
              <span v-if="creating">Создание...</span>
              <span v-else>Создать продукт</span>
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- Модальное окно редактирования -->
    <div v-if="showEditModal" class="modal-overlay" @click="closeEditModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>Редактировать продукт</h3>
          <button class="close-btn" @click="closeEditModal">
            <svg viewBox="0 0 24 24">
              <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"/>
            </svg>
          </button>
        </div>
        
        <form @submit.prevent="updateProduct" class="edit-form">
          <div class="form-group">
            <label for="name">Название:</label>
            <input 
              id="name"
              v-model="editingProduct.name" 
              type="text" 
              required 
              class="form-input"
            >
          </div>
          
          <div class="form-group">
            <label for="description">Описание:</label>
            <textarea 
              id="description"
              v-model="editingProduct.description" 
              class="form-textarea"
              rows="3"
            ></textarea>
          </div>
          
          <div class="form-row">
            <div class="form-group">
              <label for="price">Цена:</label>
              <input 
                id="price"
                v-model="editingProduct.price" 
                type="number" 
                step="0.01"
                min="0"
                class="form-input"
              >
            </div>
            
            <div class="form-group">
              <label for="collection">Коллекция:</label>
              <input 
                id="collection"
                v-model="editingProduct.collection" 
                type="text" 
                class="form-input"
              >
            </div>
          </div>
          
          <div class="form-row">
            <div class="form-group">
              <label for="status">Статус:</label>
              <select id="status" v-model="editingProduct.productStatus" class="form-select">
                <option value="IN_STOCK">В наличии</option>
                <option value="OUT_OF_STOCK">Нет в наличии</option>
              </select>
            </div>
            
            <div class="form-group">
              <label for="gender">Пол:</label>
              <select id="gender" v-model="editingProduct.gender" class="form-select">
                <option value="MALE">Мужской</option>
                <option value="FEMALE">Женский</option>
              </select>
            </div>
          </div>
          
          <div class="modal-actions">
            <button type="button" class="cancel-btn" @click="closeEditModal">
              Отмена
            </button>
            <button type="submit" class="save-btn" :disabled="updating">
              <span v-if="updating">Сохранение...</span>
              <span v-else>Сохранить</span>
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '@/api/api'

const products = ref([])
const categories = ref([])
const loading = ref(false)
const creating = ref(false)
const showCreateModal = ref(false)
const showEditModal = ref(false)
const updating = ref(false)

// Объект для создания нового продукта
const newProduct = ref({
  name: '',
  description: '',
  price: null,
  collection: '',
  categoryId: '',
  productStatus: 'IN_STOCK',
  gender: 'MALE'
})

// Объект для редактирования продукта
const editingProduct = ref({
  id: null,
  name: '',
  description: '',
  price: null,
  collection: '',
  category: null,
  productStatus: 'IN_STOCK',
  variants: [],
  gender: 'MALE'
})

const getStatusClass = (status) => {
  switch (status) {
    case 'IN_STOCK': return 'status-success'
    case 'OUT_OF_STOCK': return 'status-danger'
    default: return 'status-default'
  }
}

const getStatusText = (status) => {
  switch (status) {
    case 'IN_STOCK': return 'В наличии'
    case 'OUT_OF_STOCK': return 'Нет в наличии'
    default: return status
  }
}

const getGenderText = (gender) => {
  switch (gender) {
    case 'MALE': return 'Мужской'
    case 'FEMALE': return 'Женский'
    default: return gender || '—'
  }
}

// Функции для модального окна создания
const openCreateModal = () => {
  // Сбрасываем форму
  newProduct.value = {
    name: '',
    description: '',
    price: null,
    collection: '',
    categoryId: '',
    productStatus: 'IN_STOCK',
    gender: 'MALE'
  }
  showCreateModal.value = true
}

const closeCreateModal = () => {
  showCreateModal.value = false
  newProduct.value = {
    name: '',
    description: '',
    price: null,
    collection: '',
    categoryId: '',
    productStatus: 'IN_STOCK',
    gender: 'MALE'
  }
}

// Основная функция создания продукта
const createProduct = async () => {
  // Валидация обязательных полей
  if (!newProduct.value.name.trim()) {
    alert('Название продукта обязательно для заполнения')
    return
  }
  
  if (!newProduct.value.description.trim()) {
    alert('Описание продукта обязательно для заполнения')
    return
  }
  
  if (!newProduct.value.price || newProduct.value.price <= 0) {
    alert('Цена продукта должна быть больше 0')
    return
  }
  
  if (!newProduct.value.categoryId) {
    alert('Выберите категорию продукта')
    return
  }

  creating.value = true
  
  try {
    console.log('Создаем новый продукт...')
    
    // Подготавливаем данные для отправки согласно вашему API
    const productData = {
      name: newProduct.value.name.trim(),
      description: newProduct.value.description.trim(),
      price: parseFloat(newProduct.value.price),
      collection: newProduct.value.collection.trim() || null,
      category: {
        id: parseInt(newProduct.value.categoryId)
      },
      productStatus: newProduct.value.productStatus,
      gender: newProduct.value.gender,
      variants: [] // Пустой массив вариантов для начала
    }
    
    console.log('Отправляем данные:', productData)
    
    // Отправляем POST запрос на создание
    const response = await api.post('/api/dev/product/create', productData)
    
    console.log('Ответ сервера при создании:', response)
    
    // Добавляем новый продукт в локальный массив
    if (response.data) {
      products.value.push(response.data)
    }
    
    alert(`Продукт "${newProduct.value.name}" успешно создан`)
    closeCreateModal()
    
    // Перезагружаем список продуктов для синхронизации
    await fetchProducts()
    
  } catch (error) {
    console.error('Ошибка при создании продукта:', error)
    
    let errorMessage = 'Неизвестная ошибка'
    if (error.response?.data?.error) {
      errorMessage = error.response.data.error
    } else if (error.response?.data?.message) {
      errorMessage = error.response.data.message
    } else if (error.message) {
      errorMessage = error.message
    }
    
    alert('Ошибка при создании продукта: ' + errorMessage)
  } finally {
    creating.value = false
  }
}

const editProduct = (product) => {
  // Копируем данные продукта для редактирования
  editingProduct.value = {
    id: product.id,
    name: product.name || '',
    description: product.description || '',
    price: product.price || null,
    collection: product.collection || '',
    category: product.category || null,
    productStatus: product.productStatus || 'IN_STOCK',
    variants: product.variants || [],
    gender: product.gender || 'MALE'
  }
  
  showEditModal.value = true
}

const closeEditModal = () => {
  showEditModal.value = false
  editingProduct.value = {
    id: null,
    name: '',
    description: '',
    price: null,
    collection: '',
    category: null,
    productStatus: 'IN_STOCK',
    variants: [],
    gender: 'MALE'
  }
}

const updateProduct = async () => {
  if (!editingProduct.value.id || !editingProduct.value.name.trim()) {
    alert('Заполните обязательные поля')
    return
  }

  updating.value = true
  
  try {
    console.log(`Обновляем продукт с ID: ${editingProduct.value.id}`)
    
    // Подготавливаем данные для отправки
    const productDTO = {
      id: editingProduct.value.id,
      name: editingProduct.value.name.trim(),
      description: editingProduct.value.description?.trim() || null,
      price: editingProduct.value.price ? parseFloat(editingProduct.value.price) : null,
      collection: editingProduct.value.collection?.trim() || null,
      category: editingProduct.value.category,
      productStatus: editingProduct.value.productStatus,
      variants: editingProduct.value.variants || [],
      gender: editingProduct.value.gender
    }
    
    console.log('Отправляем данные:', productDTO)
    
    // Отправляем PUT запрос
    const response = await api.put(`/api/dev/product/update/${editingProduct.value.id}`, productDTO)
    
    console.log('Ответ сервера:', response)
    
    // Обновляем продукт в локальном массиве
    const index = products.value.findIndex(p => p.id === editingProduct.value.id)
    if (index !== -1) {
      // Если сервер возвращает обновленный продукт, используем его
      if (response.data) {
        products.value[index] = response.data
      } else {
        // Иначе обновляем локально
        products.value[index] = { ...products.value[index], ...productDTO }
      }
    }
    
    alert(`Продукт "${editingProduct.value.name}" успешно обновлен`)
    closeEditModal()
    
  } catch (error) {
    console.error('Ошибка при обновлении продукта:', error)
    
    let errorMessage = 'Неизвестная ошибка'
    if (error.response?.data?.message) {
      errorMessage = error.response.data.message
    } else if (error.message) {
      errorMessage = error.message
    }
    
    alert('Ошибка при обновлении продукта: ' + errorMessage)
  } finally {
    updating.value = false
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

// Загрузка категорий для селекта
const fetchCategories = async () => {
  try {
    console.log('Загружаем категории...')
    const response = await api.get('/api/dev/category/all') // Предполагаем такой endpoint
    
    if (response && response.data) {
      if (Array.isArray(response.data)) {
        categories.value = response.data
      } else if (response.data.data && Array.isArray(response.data.data)) {
        categories.value = response.data.data
      }
    }
    
    console.log('Категории загружены:', categories.value.length)
  } catch (error) {
    console.error('Ошибка при загрузке категорий:', error)
    // Не показываем alert для категорий, так как продукт может работать и без них
    categories.value = []
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

onMounted(async () => {
  await fetchCategories() // Сначала загружаем категории
  await fetchProducts()   // Затем продукты
})
</script>

<style scoped>
.product-table {
  min-height: 400px;
  padding: 20px;
}

/* Заголовок таблицы */
.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 0 4px;
}

.table-header h2 {
  margin: 0;
  color: #303133;
  font-size: 24px;
  font-weight: 600;
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
  margin: 0 0 20px 0;
}

/* Стили для кнопок */
.create-btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  background-color: #409eff;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.create-btn:hover:not(:disabled) {
  background-color: #337ecc;
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(64, 158, 255, 0.3);
}

.create-btn:active:not(:disabled) {
  transform: translateY(0);
}

.create-btn:disabled {
  background-color: #c0c4cc;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

.create-btn.secondary {
  background-color: #909399;
}

.create-btn.secondary:hover:not(:disabled) {
  background-color: #73767a;
  box-shadow: 0 4px 8px rgba(144, 147, 153, 0.3);
}

.create-icon {
  width: 16px;
  height: 16px;
  fill: currentColor;
}

/* Стили для действий в таблице */
.actions {
  text-align: center;
  padding: 8px 12px;
  white-space: nowrap;
}

.edit-btn {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 6px 12px;
  background-color: #e6a23c;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
  margin-right: 8px;
}

.edit-btn:hover:not(:disabled) {
  background-color: #cf7d2e;
  transform: translateY(-1px);
  box-shadow: 0 2px 4px rgba(230, 162, 60, 0.3);
}

.edit-btn:active:not(:disabled) {
  transform: translateY(0);
}

.edit-btn:disabled {
  background-color: #c0c4cc;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
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
}

.delete-btn:hover:not(:disabled) {
  background-color: #f04b4b;
  transform: translateY(-1px);
  box-shadow: 0 2px 4px rgba(245, 108, 108, 0.3);
}

.delete-btn:active:not(:disabled) {
  transform: translateY(0);
}

.delete-btn:disabled {
  background-color: #c0c4cc;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

.edit-icon,
.delete-icon {
  width: 14px;
  height: 14px;
  fill: currentColor;
}

/* Стили модального окна */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  padding: 20px;
}

.modal-content {
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  max-width: 600px;
  width: 100%;
  max-height: 90vh;
  overflow-y: auto;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid #ebeef5;
}

.modal-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.close-btn {
  background: none;
  border: none;
  padding: 4px;
  cursor: pointer;
  color: #909399;
  transition: color 0.2s;
}

.close-btn:hover {
  color: #606266;
}

.close-btn svg {
  width: 20px;
  height: 20px;
  fill: currentColor;
}

/* Стили формы */
.edit-form {
  padding: 24px;
}

.form-group {
  margin-bottom: 20px;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.form-group label {
  display: block;
  margin-bottom: 6px;
  font-weight: 500;
  color: #303133;
  font-size: 14px;
}

.form-input,
.form-textarea,
.form-select {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  font-size: 14px;
  transition: border-color 0.2s;
  box-sizing: border-box;
}

.form-input:focus,
.form-textarea:focus,
.form-select:focus {
  outline: none;
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.1);
}

.form-textarea {
  resize: vertical;
  min-height: 80px;
}

/* Действия модального окна */
.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding-top: 20px;
  border-top: 1px solid #ebeef5;
  margin-top: 20px;
}

.cancel-btn {
  padding: 10px 20px;
  background: #f4f4f5;
  color: #606266;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.cancel-btn:hover {
  background: #e9e9eb;
}

.save-btn {
  padding: 10px 20px;
  background-color: #409eff;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.save-btn:hover:not(:disabled) {
  background-color: #337ecc;
}

.save-btn:disabled {
  background-color: #c0c4cc;
  cursor: not-allowed;
}

/* Адаптивность */
@media (max-width: 768px) {
  .table-header {
    flex-direction: column;
    gap: 16px;
    align-items: stretch;
  }
  
  .table-header h2 {
    text-align: center;
    font-size: 20px;
  }
  
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
  
  .actions {
    padding: 8px 4px;
  }
  
  .edit-btn,
  .delete-btn {
    padding: 4px 8px;
    font-size: 11px;
    margin-right: 4px;
  }
  
  .edit-btn span,
  .delete-btn span {
    display: none;
  }
  
  .form-row {
    grid-template-columns: 1fr;
    gap: 0;
  }
  
  .modal-content {
    margin: 10px;
    max-height: calc(100vh - 20px);
  }
}</style>