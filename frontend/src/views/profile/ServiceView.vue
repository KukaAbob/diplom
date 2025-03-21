// views/profile/ServiceView.vue
<template>
  <div class="service-container">
    <div class="service-header">
      <h2>Customer service</h2>
    </div>

    <div class="service-content">
      <div class="service-options">
        <div class="service-option">
          <h3>Contact us</h3>
          <p>Have questions or need assistance? Our customer service team is here to help.</p>
          <button>Get in touch</button>
        </div>

        <div class="service-option">
          <h3>FAQ</h3>
          <p>Find answers to frequently asked questions about orders, returns, and more.</p>
          <button>View FAQs</button>
        </div>

        <div class="service-option">
          <h3>Live chat</h3>
          <p>Chat with our customer service representatives in real-time.</p>
          <button>Start chat</button>
        </div>

        <div class="service-option">
          <h3>Return policy</h3>
          <p>Learn about our return policy and how to return items.</p>
          <button>View policy</button>
        </div>
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
.service-container {
  padding: 20px 0;
}

.service-header {
  margin-bottom: 30px;
}

.service-options {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.service-option {
  border: 1px solid #eee;
  padding: 20px;
  border-radius: 4px;
}

.service-option h3 {
  margin-bottom: 10px;
}

.service-option p {
  margin-bottom: 15px;
  color: #666;
}

.service-option button {
  padding: 10px 20px;
  background: none;
  border: 1px solid #000;
  border-radius: 30px;
  cursor: pointer;
}
</style>
