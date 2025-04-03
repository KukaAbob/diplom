<template>
  <div>
    <!-- Back button -->
    <div class="back-button" @click="goBack">
      <i class="back-arrow">←</i>
      <span>Back</span>
    </div>

    <div class="profile-container">
      <!-- Left content area - changes based on selected menu item -->
      <div class="content-area">
        <router-view></router-view>
      </div>

      <!-- Right sidebar menu -->
      <div class="sidebar-menu">
        <div class="menu-item" @click="navigateTo('purchases')">
          <i class="icon-purchases"></i>
          <span>My purchases</span>
          <i class="icon-arrow-right"></i>
        </div>

        <div class="menu-item" @click="navigateTo('returns')">
          <i class="icon-returns"></i>
          <span>Returns</span>
          <i class="icon-arrow-right"></i>
        </div>

        <div class="menu-item" @click="navigateTo('details')">
          <i class="icon-details"></i>
          <span>Personal details</span>
          <i class="icon-arrow-right"></i>
        </div>

        <div class="menu-item" @click="navigateTo('addresses')">
          <i class="icon-addresses"></i>
          <span>Addresses</span>
          <i class="icon-arrow-right"></i>
        </div>

        <div class="menu-item" @click="navigateTo('payment')">
          <i class="icon-payment"></i>
          <span>Payment methods</span>
          <i class="icon-arrow-right"></i>
        </div>

        <div class="menu-item" @click="navigateTo('newsletter')">
          <i class="icon-newsletter"></i>
          <span>Newsletter</span>
          <i class="icon-arrow-right"></i>
        </div>

        <div class="menu-item" @click="navigateTo('service')">
          <i class="icon-service"></i>
          <span>Customer service</span>
          <i class="icon-arrow-right"></i>
        </div>

        <div class="menu-item" @click="navigateTo('privacy')">
          <i class="icon-privacy"></i>
          <span>Privacy policy</span>
          <i class="icon-arrow-right"></i>
        </div>

        <div class="logout-button" @click="logout">Log out</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  methods: {
    navigateTo(section) {
      this.$router.push(`/profile/${section}`)
    },
    goBack() {
      // Navigate back to previous page
      // You can adjust this to go to a specific page if needed
      this.$router.push('/')
    },
    logout() {
      localStorage.removeItem('jwtToken')
      localStorage.removeItem('userEmail')
      isAuthenticated.value = false
      isProfileMenuOpen.value = false
      router.push('/')
    },
  },
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

.profile-container {
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
}

.menu-item .icon-arrow-right {
  margin-left: auto;
}

.logout-button {
  margin-top: 30px;
  background-color: #000;
  color: white;
  padding: 15px;
  text-align: center;
  border-radius: 30px;
  cursor: pointer;
}
</style>
