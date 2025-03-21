// views/profile/ReturnsView.vue
<template>
  <div class="returns-container">
    <div class="returns-header">
      <h2>Returns</h2>
    </div>

    <div class="empty-state">
      <div class="empty-icon">
        <img src="@/assets/img/icons/profile.svg" alt="Returns" />
      </div>
      <h3>No returns found</h3>
      <p>You haven't made any return requests yet.</p>
      <p>If you need to return an item, please visit our returns policy.</p>

      <div class="action-button">
        <button>View returns policy</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {}
  },
}
</script>

<style scoped>
.returns-container {
  padding: 20px 0;
}

.returns-header {
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
