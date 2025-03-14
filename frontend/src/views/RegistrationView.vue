<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import api from '@/api/api' // Подключаем API-инстанс

const router = useRouter()

const email = ref('')
const password = ref('')
const phone = ref('')
const errorMessage = ref('')

// Функция регистрации
const register = async () => {
  try {
    const userData = {
      username: email.value.split('@')[0], // Генерируем имя пользователя из email
      password: password.value,
      email: email.value,
      phone: phone.value,
      roles: ['USER'], // Фиксированная роль
    }

    // Отправляем данные на сервер
    const response = await api.post('/auth/sign-up', userData)

    // Сохраняем токен
    localStorage.setItem('jwtToken', response.data.token)
    console.log(response.data)
    console.log(localStorage.getItem('jwtToken'))

    // Перенаправляем пользователя на главную страницу
    router.push('/')
  } catch (error) {
    errorMessage.value = 'Ошибка регистрации. Проверьте данные.'
    console.error('Ошибка регистрации', error)
  }
}
</script>

<template>
  <div class="container">
    <div class="image-section">
      <div class="header">
        <RouterLink to="/">
          <button class="back-button">
            <img src="@/assets/img/logo.png" alt="Логотип" class="logo" />
          </button>
        </RouterLink>
      </div>
    </div>
    <div class="form-section">
      <h1>Создай аккаунт</h1>
      <form @submit.prevent="register">
        <input v-model="email" type="email" placeholder="E-mail*" required />
        <input v-model="password" type="password" placeholder="Пароль*" required />
        <div class="phone-input">
          <select>
            <option>+7 - Казахстан</option>
          </select>
          <input v-model="phone" type="tel" placeholder="Номер телефона*" required />
        </div>

        <p class="terms">
          Нажимая на кнопку «Создать аккаунт», я подтверждаю, что прочитал (-а) и принимаю
          <a href="#">условия использования и покупки</a> и понимаю
          <a href="#">информацию об использовании моих персональных данных</a>, изложенную в
          <a href="#">политике конфиденциальности</a>.
        </p>

        <p v-if="errorMessage" class="error">{{ errorMessage }}</p>

        <button type="submit" class="register-button">Создать аккаунт</button>
      </form>
    </div>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  height: 100vh;
}

.image-section {
  flex: 1;
  background: url('@/assets/img/banner-reg.jpg') center/cover no-repeat;
  position: relative;
}

/* Стили для кнопки "назад" и логотипа */
.header {
  position: absolute;
  top: 20px;
  left: 20px;
  display: flex;
  align-items: center;
}

.back-button {
  background: transparent;
  border: none;
  cursor: pointer;
  padding: 5px;
  display: flex;
  align-items: center;
}

.back-button img {
  width: 240px;
  height: auto;
  opacity: 0.6;
  transition: opacity 0.2s;
}

.back-button img:hover {
  opacity: 1;
}

.form-section {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 40px;
  background: white;
}

form {
  width: 100%;
  max-width: 600px;
  display: flex;
  flex-direction: column;
}

input,
select,
button {
  width: 100%;
  padding: 10px;
  margin: 10px 0;
  border: none;
  outline: none;
  background: transparent;
  border-bottom: 1px solid #ccc;
}

.phone-input {
  display: flex;
  gap: 10px;
}

.checkbox {
  display: flex;
  align-items: center;
  font-size: 14px;
}

.terms {
  font-size: 12px;
  color: gray;
}

.terms a {
  color: black;
  font-weight: bold;
}

.register-button {
  background: black;
  color: white;
  border: none;
  cursor: pointer;
  height: 40px;
  transition: opacity 0.2s;
}

.register-button:hover {
  opacity: 0.8;
}
</style>
