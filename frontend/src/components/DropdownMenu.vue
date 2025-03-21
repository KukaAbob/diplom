<template>
  <div class="menu-container" :class="{ 'menu-open': isDropdownMenuOpen }">
    <div class="left-content">
      <div class="top">
        <div class="close-button">
          <button class="close-button-1">X</button>
        </div>
        <div class="logo">
          <img src="@/assets/img/logo.png" alt="" />
        </div>
        <div class="buttons">
          <div class="wishlist-button">
            <button>
              <img src="@/assets/img/icons/logo1.svg" alt="" />
            </button>
          </div>
          <div class="cart-button">
            <button>
              <img src="@/assets/img/icons/logo2.svg" alt="" />
            </button>
          </div>
        </div>
        <div class="search">
          <input type="text" />
        </div>
        <div class="new-button">
          <h2>НОВИНКИ</h2>
        </div>
        <div class="category-buttons">
          <ul>
            <li>ОБУВЬ</li>
            <li>ОДЕЖДА</li>
            <li>АКСЕССУАРЫ</li>
            <li>СУМКИ</li>
            <li>ИСКУССТВЕННЫЕ</li>
          </ul>
        </div>
      </div>
    </div>
    <div class="right-content">
      <div class="banner">
        <img src="@/assets/img/banner.jpg" alt="" />
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'DropdownMenu',
  props: {
    isOpen: {
      type: Boolean,
      default: false,
    },
  },
}
</script>

<style scoped></style>
