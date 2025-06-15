<template>
  <div class="product-management">
    <el-row justify="end" class="mb-4">
      <el-button type="primary" @click="dialogVisible = true">
        <el-icon><Plus /></el-icon>
        Добавить продукт
      </el-button>
    </el-row>

    <el-card v-loading="loading">
      <el-table :data="products" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="Название" />
        <el-table-column prop="price" label="Цена">
          <template #default="{ row }">
            {{ row.price }} ₽
          </template>
        </el-table-column>
        <el-table-column prop="description" label="Описание" show-overflow-tooltip />
        <el-table-column label="Действия" width="200" fixed="right">
          <template #default="{ row }">
            <el-button-group>
              <el-button type="primary" size="small" @click="handleEdit(row)">
                <el-icon><Edit /></el-icon>
              </el-button>
              <el-button type="danger" size="small" @click="handleDelete(row.id)">
                <el-icon><Delete /></el-icon>
              </el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog
      v-model="dialogVisible"
      :title="editingProduct ? 'Редактировать продукт' : 'Новый продукт'"
      width="500px"
    >
      <el-form :model="form" label-position="top">
        <el-form-item label="Название" required>
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="Цена" required>
          <el-input-number v-model="form.price" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="Описание">
          <el-input v-model="form.description" type="textarea" rows="4" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">Отмена</el-button>
        <el-button type="primary" @click="handleSubmit">
          {{ editingProduct ? 'Сохранить' : 'Создать' }}
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete } from '@element-plus/icons-vue'
import api from '@/api/api'

const products = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const editingProduct = ref(null)
const form = ref({
  name: '',
  price: 0,
  description: ''
})

const fetchProducts = async () => {
  loading.value = true
  try {
    const response = await api.get('/api/dev/product/all')
    products.value = response.data
  } catch (error) {
    ElMessage.error('Ошибка при загрузке продуктов')
  } finally {
    loading.value = false
  }
}

const handleEdit = (product) => {
  editingProduct.value = product
  form.value = { ...product }
  dialogVisible.value = true
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('Вы уверены, что хотите удалить этот продукт?', 'Внимание')
    await api.delete(`/api/dev/product/${id}`)
    ElMessage.success('Продукт успешно удален')
    fetchProducts()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('Ошибка при удалении продукта')
    }
  }
}

const handleSubmit = async () => {
  try {
    if (editingProduct.value) {
      await api.put(`/api/dev/product/${editingProduct.value.id}`, form.value)
      ElMessage.success('Продукт успешно обновлен')
    } else {
      await api.post('/api/dev/product/create', form.value)
      ElMessage.success('Продукт успешно создан')
    }
    dialogVisible.value = false
    editingProduct.value = null
    form.value = { name: '', price: 0, description: '' }
    fetchProducts()
  } catch (error) {
    ElMessage.error('Ошибка при сохранении продукта')
  }
}

onMounted(fetchProducts)
</script>

<style scoped>
.product-management {
  min-height: 400px;
}

.mb-4 {
  margin-bottom: 1rem;
}
</style>