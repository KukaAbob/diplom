<template>
  <div class="upload-container">
    <h2>Загрузка изображений</h2>

    <div class="form-group">
      <label for="productId">ID продукта:</label>
      <input
        type="text"
        id="productId"
        v-model="productId"
        placeholder="Введите ID продукта"
        :class="{ error: validationErrors.productId }"
      />
      <p v-if="validationErrors.productId" class="error-message">
        {{ validationErrors.productId }}
      </p>
    </div>

    <div class="form-group">
      <label for="imageUpload">Изображение:</label>
      <div
        class="upload-area"
        @click="triggerFileInput"
        @drop.prevent="handleFileDrop"
        @dragover.prevent
      >
        <input
          type="file"
          id="imageUpload"
          ref="fileInput"
          @change="handleFileSelect"
          accept="image/*"
          class="file-input"
        />
        <div v-if="!selectedFile" class="placeholder">
          <span>Перетащите изображение сюда или кликните для выбора файла</span>
        </div>
        <div v-else class="preview">
          <img :src="previewUrl" alt="Предпросмотр" class="preview-image" />
          <span class="file-name">{{ selectedFile.name }}</span>
          <button @click.stop="removeFile" class="remove-btn">×</button>
        </div>
      </div>
      <p v-if="validationErrors.file" class="error-message">{{ validationErrors.file }}</p>
    </div>

    <div class="form-controls">
      <button @click="uploadImage" :disabled="isUploading" class="upload-btn">
        <span v-if="isUploading">Загрузка...</span>
        <span v-else>Загрузить</span>
      </button>
    </div>

    <div v-if="uploadStatus" :class="['status-message', uploadStatus.type]">
      {{ uploadStatus.message }}
    </div>
  </div>
</template>

<script>
import api from '@/api/api'

export default {
  name: 'ImageUploader',
  data() {
    return {
      productId: '',
      selectedFile: null,
      previewUrl: null,
      isUploading: false,
      validationErrors: {
        productId: '',
        file: '',
      },
      uploadStatus: null,
    }
  },
  methods: {
    triggerFileInput() {
      this.$refs.fileInput.click()
    },

    handleFileSelect(event) {
      const file = event.target.files[0]
      if (file) {
        this.processFile(file)
      }
    },

    handleFileDrop(event) {
      const file = event.dataTransfer.files[0]
      if (file && file.type.startsWith('image/')) {
        this.processFile(file)
      } else {
        this.validationErrors.file = 'Пожалуйста, выберите изображение'
      }
    },

    processFile(file) {
      this.selectedFile = file
      this.validationErrors.file = ''
      this.previewUrl = URL.createObjectURL(file)
    },

    removeFile(event) {
      event.stopPropagation()
      this.selectedFile = null
      if (this.previewUrl) {
        URL.revokeObjectURL(this.previewUrl)
        this.previewUrl = null
      }
    },

    validate() {
      let isValid = true
      this.validationErrors = {
        productId: '',
        file: '',
      }

      if (!this.productId.trim()) {
        this.validationErrors.productId = 'ID продукта обязателен'
        isValid = false
      }

      if (!this.selectedFile) {
        this.validationErrors.file = 'Пожалуйста, выберите изображение'
        isValid = false
      }

      return isValid
    },

    async uploadImage() {
      if (!this.validate()) {
        return
      }

      this.isUploading = true
      this.uploadStatus = null

      try {
        const formData = new FormData()
        formData.append('file', this.selectedFile)
        formData.append('productId', this.productId)

        // Получаем JWT токен из localStorage
        const token = localStorage.getItem('jwtToken') // или 'authToken', 'jwt' - в зависимости от ключа

        // Настраиваем заголовки
        const headers = {
          'Content-Type': 'multipart/form-data',
        }

        // Добавляем токен в заголовки, если он есть
        if (token) {
          headers['Authorization'] = `Bearer ${token}`
        }

        const response = await api.post('/api/images/upload', formData, {
          headers,
        })

        this.uploadStatus = {
          type: 'success',
          message: 'Изображение успешно загружено!',
        }

        // Очистка формы после успешной загрузки
        this.productId = ''
        this.removeFile({ stopPropagation: () => {} })
      } catch (error) {
        console.error('Ошибка загрузки:', error)

        // Обработка ошибки авторизации
        if (error.response?.status === 401) {
          this.uploadStatus = {
            type: 'error',
            message: 'Ошибка авторизации. Пожалуйста, войдите в систему заново.',
          }
        } else {
          this.uploadStatus = {
            type: 'error',
            message: `Ошибка: ${error.response?.data?.message || error.message || 'Не удалось загрузить изображение'}`,
          }
        }
      } finally {
        this.isUploading = false
      }
    },
  },
}
</script>

<style scoped>
.upload-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

h2 {
  color: #333;
  margin-bottom: 20px;
  text-align: center;
}

.form-group {
  margin-bottom: 20px;
}

label {
  display: block;
  margin-bottom: 6px;
  font-weight: 600;
  color: #444;
}

input[type='text'] {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

input[type='text'].error {
  border-color: #dc3545;
}

.upload-area {
  position: relative;
  border: 2px dashed #aaa;
  border-radius: 6px;
  padding: 30px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
  background-color: #fff;
  min-height: 150px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.upload-area:hover {
  border-color: #666;
  background-color: #f9f9f9;
}

.file-input {
  position: absolute;
  opacity: 0;
  width: 0;
  height: 0;
}

.placeholder {
  color: #777;
  font-size: 14px;
}

.preview {
  width: 100%;
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.preview-image {
  max-width: 100%;
  max-height: 200px;
  border-radius: 4px;
  margin-bottom: 10px;
}

.file-name {
  font-size: 14px;
  color: #333;
  word-break: break-all;
}

.remove-btn {
  position: absolute;
  top: -10px;
  right: -10px;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background-color: #ff4d4f;
  color: white;
  border: none;
  font-size: 16px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}

.remove-btn:hover {
  background-color: #ff7875;
}

.form-controls {
  text-align: center;
  margin-top: 10px;
}

.upload-btn {
  background-color: #1890ff;
  color: #fff;
  border: none;
  padding: 10px 24px;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
  transition: background 0.3s ease;
}

.upload-btn:hover {
  background-color: #40a9ff;
}

.upload-btn:disabled {
  background-color: #91caff;
  cursor: not-allowed;
}

.error-message {
  color: #dc3545;
  font-size: 12px;
  margin-top: 4px;
}

.status-message {
  margin-top: 20px;
  padding: 10px;
  border-radius: 4px;
  text-align: center;
}

.status-message.success {
  background-color: #d4edda;
  color: #155724;
}

.status-message.error {
  background-color: #f8d7da;
  color: #721c24;
}
</style>
