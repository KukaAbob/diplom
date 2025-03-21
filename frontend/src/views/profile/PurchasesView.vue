// views/profile/PurchasesView.vue
<template>
  <div class="purchases-container">
    <div class="purchases-header">
      <h2>My purchases</h2>
    </div>

    <div class="empty-state" v-if="!hasPurchases">
      <div class="empty-icon">
        <img src="@/assets/img/icons/profile.svg" alt="Buy online" />
      </div>
      <h3>There are no orders</h3>
      <p>You have yet to make an online purchase.</p>
      <p>Take a look at our latest arrivals!</p>

      <div class="action-button">
        <button>See new arrivals</button>
      </div>
    </div>

    <div class="purchases-list" v-else>
      <!-- Your purchases list here -->
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      hasPurchases: false,
    }
  },
}
</script>

<style scoped>
.purchases-container {
  padding: 20px 0;
}

.purchases-header {
  margin-bottom: 30px;
}

.empty-state {
  text-align: center;
  padding: 40px 0;
}

.empty-icon {
  margin-bottom: 20px;
}

.empty-icon img {
  width: 100px;
}

.action-button {
  margin-top: 30px;
}

.action-button button {
  padding: 12px 30px;
  border: 1px solid #000;
  background: white;
  border-radius: 30px;
  cursor: pointer;
}
</style>
