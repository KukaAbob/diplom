// views/profile/AddressesView.vue
<template>
  <div class="addresses-container">
    <div class="addresses-header">
      <h2>Addresses</h2>
    </div>

    <div class="empty-state" v-if="addresses.length === 0">
      <div class="empty-icon">
        <img src="@/assets/img/icons/profile.svg" alt="Address" />
      </div>
      <h3>No addresses saved</h3>
      <p>You haven't saved any delivery addresses yet.</p>

      <div class="action-button">
        <button>Add new address</button>
      </div>
    </div>

    <div class="addresses-list" v-else>
      <div class="address-card" v-for="(address, index) in addresses" :key="index">
        <h3>{{ address.name }}</h3>
        <p>{{ address.street }}</p>
        <p>{{ address.city }}, {{ address.zipCode }}</p>
        <p>{{ address.country }}</p>
        <div class="address-actions">
          <button class="edit-btn">Edit</button>
          <button class="delete-btn">Delete</button>
        </div>
      </div>

      <div class="action-button">
        <button>Add new address</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      addresses: [],
    }
  },
}
</script>

<style scoped>
.addresses-container {
  padding: 20px 0;
}

.addresses-header {
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

.address-card {
  border: 1px solid #eee;
  padding: 15px;
  margin-bottom: 15px;
  border-radius: 4px;
}

.address-actions {
  margin-top: 10px;
}

.address-actions button {
  margin-right: 10px;
  padding: 5px 10px;
  background: none;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
}

.address-actions .delete-btn {
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
