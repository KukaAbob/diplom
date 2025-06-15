<template>
  <div>
    <div class="back-button" @click="goBack">
      <i class="back-arrow">←</i>
      <span>Назад</span>
    </div>

    <div class="manager-container">
      <!-- Left content area - changes based on selected menu item -->
      <div class="content-area">
        <router-view></router-view>
      </div>

      <!-- Right sidebar menu -->
      <div class="sidebar-menu">
        <div class="menu-item" @click="navigateTo('products')">
          <i class="el-icon-goods"></i>
          <span>Управление продуктами</span>
          <el-icon><ArrowRight /></el-icon>
        </div>

        <div class="menu-item" @click="navigateTo('users')">
          <i class="el-icon-user"></i>
          <span>Управление пользователями</span>
          <el-icon><ArrowRight /></el-icon>
        </div>

        <div class="menu-item" @click="navigateTo('orders')">
          <i class="el-icon-shopping-cart-full"></i>
          <span>Управление заказами</span>
          <el-icon><ArrowRight /></el-icon>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { ArrowRight } from '@element-plus/icons-vue'

const router = useRouter()

const navigateTo = (section) => {
  router.push(`/dev-manager/${section}`)
}

const goBack = () => {
  router.push('/')
}
</script>

<style scoped>
.back-button {
  display: flex;
  align-items: center;
  cursor: pointer;
  margin-bottom: 20px;
  max-width: 1200px;
  margin-left: auto;
  margin-right: auto;
  padding: 0 20px;
  top: 30px;
  position: relative;
}

.back-arrow {
  font-size: 24px;
  margin-right: 5px;
}

.manager-container {
  display: flex;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px 20px;
}

.content-area {
  flex: 1;
  padding-right: 20px;
}

.sidebar-menu {
  width: 300px;
  border-left: 1px solid #eee;
  padding-left: 20px;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 15px 0;
  cursor: pointer;
  border-bottom: 1px solid #eee;
}

.menu-item i {
  margin-right: 10px;
  font-size: 20px;
}

.menu-item span {
  flex: 1;
}

.menu-item:hover {
  background-color: #f5f7fa;
}

:deep(.el-icon) {
  margin-left: auto;
}
</style>
