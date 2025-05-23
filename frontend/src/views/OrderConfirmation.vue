<template>
  <div class="main-screen">
    <div class="logo">
      <img src="@/assets/img/logo.png" alt="" />
    </div>
    <div class="center-text">
      <div class="text-header">
        <h1>Спасибо за покупку!</h1>
      </div>
      <div class="text-underheader">
        <p>Вы будете перенаправлены на заглавную страницу</p>
      </div>
    </div>
  </div>
</template>

<script>
import { useRouter } from 'vue-router'

const router = useRouter
// Страница с минимальным таймером
export default {
  mounted() {
    setTimeout(() => {
      this.$router.push('/')
      // Или без Vue Router:
      // window.location.href = '/';
    }, 3000) // 3000 миллисекунд = 3 секунды
  },
}
</script>

<style scoped>
.main-screen {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  margin-top: 20%;
}
.logo img {
  width: 400px;
  height: auto;
}
.center-text {
  text-align: center;
}
.text-underheader {
  color: darkgray;
}
</style>
