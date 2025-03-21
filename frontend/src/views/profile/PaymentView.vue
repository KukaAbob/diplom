// views/profile/PaymentView.vue
<template>
  <div class="payment-container">
    <div class="payment-header">
      <h2>Payment methods</h2>
    </div>

    <div class="empty-state" v-if="paymentMethods.length === 0">
      <div class="empty-icon">
        <img src="@/assets/img/icons/profile.svg" alt="Payment" />
      </div>
      <h3>No payment methods saved</h3>
      <p>You haven't saved any payment methods yet.</p>

      <div class="action-button">
        <button>Add payment method</button>
      </div>
    </div>

    <div class="payment-list" v-else>
      <div class="payment-card" v-for="(method, index) in paymentMethods" :key="index">
        <div class="card-info">
          <div class="card-icon">
            <img :src="method.icon" :alt="method.type" />
          </div>
          <div class="card-details">
            <h3>{{ method.type }}</h3>
            <p>•••• •••• •••• {{ method.lastFour }}</p>
            <p>Expires: {{ method.expiry }}</p>
          </div>
        </div>
        <div class="card-actions">
          <button class="edit-btn">Edit</button>
          <button class="delete-btn">Delete</button>
        </div>
      </div>

      <div class="action-button">
        <button>Add payment method</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      paymentMethods: [],
    }
  },
}
</script>

<style scoped>
.payment-container {
  padding: 20px 0;
}

.payment-header {
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

.payment-card {
  border: 1px solid #eee;
  padding: 15px;
  margin-bottom: 15px;
  border-radius: 4px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-info {
  display: flex;
  align-items: center;
}

.card-icon {
  margin-right: 15px;
}

.card-icon img {
  width: 40px;
}

.card-actions button {
  margin-left: 10px;
  padding: 5px 10px;
  background: none;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
}

.card-actions .delete-btn {
  color: #ff3b30;
  border-color: #ff3b30;
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
