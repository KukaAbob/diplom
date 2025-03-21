// views/profile/DetailsView.vue
<template>
  <div class="details-container">
    <div class="details-header">
      <h2>Personal details</h2>
    </div>

    <div class="details-form">
      <div class="form-group">
        <label>Email</label>
        <input type="email" placeholder="Enter your email address" />
      </div>

      <div class="form-group">
        <label>Phone number</label>
        <input type="tel" placeholder="Enter your phone number" />
      </div>

      <div class="action-button">
        <button>Save changes</button>
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
.details-container {
  padding: 20px 0;
}

.details-header {
  margin-bottom: 30px;
}

.details-form {
  max-width: 500px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: 500;
}

.form-group input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.action-button {
  margin-top: 30px;
}

.action-button button {
  padding: 12px 30px;
  background: #000;
  color: white;
  border: none;
  border-radius: 30px;
  cursor: pointer;
}
</style>
