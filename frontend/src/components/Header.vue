<template>
  <header class="header">
    <div class="menu">
      <button @click="toggleMenu">☰ Меню</button>
    </div>

    <div class="logo-container">
      <img src="@/assets/img/logo.png" alt="logo" class="logo" />
    </div>

    <div class="actions">
      <input type="text" placeholder="Search" class="search" />
      <RouterLink to="/profile" class="cart-button">
        <div class="icons">
          <img src="@/assets/img/icons/profile.svg" alt="Профиль" class="icon" />
        </div>
      </RouterLink>
      <RouterLink to="/cart" class="cart-button">
        <div class="icons">
          <img src="@/assets/img/icons/cart.svg" alt="Корзина" class="icon" />
        </div>
      </RouterLink>
    </div>
  </header>
</template>

<script setup></script>

<style scoped>
/* Основной контейнер */
.header {
  display: flex;
  align-items: center;
  justify-content: space-between; /* Равномерное распределение */
  padding: 10px 50px;
  height: 80px;
  background: transparent;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 1000;
  box-sizing: border-box;
}

/* Меню слева */
button {
  flex: 1; /* Занимает пространство слева */
  border: 1px solid #6b6161;
  background: transparent;
  height: 40px;
  width: 100px;
  border-radius: 15px;
  font-family: 'Roboto Flex', sans-serif;
  font-size: 16px; /* Размер шрифта */
  font-weight: 400; /* Толщина */
}

/* Логотип по центру */
.logo-container {
  flex: 2; /* Центрирует логотип */
  display: flex;
  justify-content: center;
}

/* Правая часть */
.actions {
  flex: 1; /* Занимает пространство справа */
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 15px; /* Расстояние между элементами */
}

/* Размер иконок */
.icon {
  width: 25px;
  height: 25px;
}

/* Логотип */
.logo {
  max-width: 250px;
  height: auto;
}

/* Поле поиска */
.search {
  padding: 5px;
  border: 1px solid #6b6161;
  background: transparent;
  height: 30px;
  width: 250px;
  border-radius: 15px;
  font-family: 'Roboto Flex', sans-serif;
  font-size: 16px; /* Размер шрифта */
  font-weight: 400; /* Толщина */
  padding-left: 15px;
}
</style>
