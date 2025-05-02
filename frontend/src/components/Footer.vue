<template>
  <footer class="footer">
    <div class="content">
      <div class="navbar">
        <div class="navbar-1">
          <nav>
            <h4>Помощь</h4>
            <ul>
              <li>Часто задаваемые вопросы</li>
              <li>Доставка</li>
              <li>Как избежать мошенничества</li>
              <li>Как сделать онлайн заказ</li>
            </ul>
          </nav>
        </div>
        <div class="navbar-2">
          <h4>Компания</h4>
          <nav>
            <ul>
              <li>О нас</li>
              <li>Работайте с нами</li>
            </ul>
          </nav>
        </div>
      </div>
      <div class="div-logo">
        <img src="@/assets/img/logo.png" alt="logo" />
      </div>
      <div class="under-logo-text">
        <h4>The content of this site is copyright-protected and is the property of Gay Dickson</h4>
      </div>
      <div class="social-media">
        <ul>
          <li><img src="@/assets/img/icons/logo1.svg" alt="" /></li>
          <li><img src="@/assets/img/icons/logo2.svg" alt="" /></li>
          <li><img src="@/assets/img/icons/logo3.svg" alt="" /></li>
          <li><img src="@/assets/img/icons/logo4.svg" alt="" /></li>
        </ul>
      </div>
    </div>
  </footer>
</template>

<script setup></script>

<style scoped>
.footer {
  background-color: #f5f5f5;
  padding: 50px 80px;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  font-family: 'Tinos', serif;
  font-size: 14px;
  color: #333;
}
.navbar {
  display: flex;
  justify-content: center;
  text-align: left;
  gap: 60px;
  width: 100%;
  max-width: 1200px;
  margin-bottom: 20px;
}
.navbar h4 nav {
  font-weight: bold;
  margin-bottom: 10px;
}
.navbar ul {
  list-style: none;
  padding: 0;
  margin: 0;
}
.navbar li {
  margin-bottom: 8px;
  cursor: pointer;
  transition: color 0.3s;
}
.navbar li:hover {
  color: red;
}
.div-logo img {
  max-width: 120px;
  margin-bottom: 15px;
}

/* Текст под логотипом */
.under-logo-text h4 {
  font-size: 12px;
  font-weight: normal;
  color: #666;
  margin-bottom: 20px;
}

/* Соцсети */
.social-media ul {
  display: flex;
  gap: 20px;
  padding: 0;
  margin: 0;
  list-style: none;
  justify-content: center;
}

.social-media img {
  width: 24px;
  height: 24px;
  transition: opacity 0.3s;
}

.social-media img:hover {
  opacity: 0.7;
}
</style>
