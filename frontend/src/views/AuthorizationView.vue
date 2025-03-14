<template>
  <div class="header">
    <button class="close-btn">
      <img src="@/assets/img/icons/logo2.svg" alt="Закрыть" />
    </button>
    <img src="@/assets/img/logo.png" alt="Логотип" class="logo" />
  </div>
  <div class="content">
    <div class="main-content">
      <h1 class="title">ВОЙДИТЕ ИЛИ СОЗДАЙТЕ АККАУНТ</h1>
      <div class="auth">
        <div class="authorization">
          <p class="subtitle">
            У ВАС УЖЕ ЕСТЬ УЧЕТНАЯ ЗАПИСЬ? ВВЕДИТЕ СВОИ ДАННЫЕ ДЛЯ ВХОДА В СИСТЕМУ
          </p>
          <input type="email" placeholder="Укажите свой адрес электронной почты" class="input" />
          <input type="password" placeholder="Пароль" class="input" />
          <button class="btn black-btn">НАЧАТЬ СЕАНС</button>
        </div>
        <div class="registration">
          <p class="subtitle">
            У ВАС НЕТ ЛИЧНОГО КАБИНЕТА? <br />
            ЗАРЕГИСТРИРУЙТЕСЬ
          </p>
          <ul class="benefits">
            <li><img src="@/assets/img/icons/logo4.svg" alt="" /> Отследите свои заказы</li>
            <li>
              <img src="@/assets/img/icons/logo1.svg" alt="" /> Сохраните свои данные по доставке и
              оплате, чтобы сэкономить время при следующих покупках
            </li>
          </ul>
          <RouterLink to="/registration" class="reg-button">
            <button class="btn white-btn">СОЗДАТЬ АККАУНТ</button>
          </RouterLink>
        </div>
      </div>
    </div>
    <div class="footer">
      <p class="footer-text">ВОЙДИТЕ ИЛИ ЗАРЕГИСТРИРУЙТЕСЬ С</p>
      <img src="@/assets/img/icons/logo3.svg" alt="Google" class="google-logo" />
      <p class="policy">
        Осуществляя вход или регистрацию с использованием моего профиля в социальной сети, я даю
        согласие на связывание моего аккаунта в соответствии с Политикой конфиденциальности.
      </p>
    </div>
  </div>
</template>

<script setup></script>

<style scoped>
.content {
  max-width: 900px;
  margin: auto;
  text-align: center;
}
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
}
.close-btn img {
  width: 20px;
}
.logo {
  width: 150px;
}
.title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 50px;
}
.auth {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}
.authorization,
.registration {
  width: 45%;
  text-align: left;
}
.subtitle {
  font-size: 14px;
  font-weight: bold;
  margin-bottom: 30px;
}
.input {
  width: 385px;
  padding: 10px;
  margin: 10px 0;
  border: none;
  outline: none;
  background: transparent;
  border-bottom: 1px solid #ccc;
}
.options {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  margin-top: 10px;
}
.btn {
  width: 100%;
  padding: 10px;
  margin-top: 20px;
  border: none;
  font-weight: bold;
  cursor: pointer;
}
.black-btn {
  background: black;
  color: white;
  margin-top: 65px;
}
.white-btn {
  background: white;
  border: 1px solid black;
}
.benefits {
  list-style: none;
  padding: 10px;
}
.benefits li {
  display: flex;
  align-items: center;
  margin-top: 20px;
  margin-bottom: 20px;
}
.benefits img {
  width: 20px;
  margin-right: 10px;
}

.main-content {
  margin-bottom: 150px;
}

.footer {
  margin-top: 30px;
  text-align: center;
}
.footer-text {
  font-size: 14px;
  font-weight: bold;
}
.google-logo {
  width: 30px;
  margin: 10px 0;
}
.policy {
  font-size: 12px;
  color: gray;
}
</style>
