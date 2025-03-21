// views/profile/NewsletterView.vue
<template>
  <div class="newsletter-container">
    <div class="newsletter-header">
      <h2>Newsletter</h2>
    </div>

    <div class="newsletter-content">
      <p>
        Subscribe to our newsletter to stay updated with our latest collections, promotions, and
        exclusive offers.
      </p>

      <div class="subscription-options">
        <div class="option">
          <input type="checkbox" id="news" v-model="preferences.news" />
          <label for="news">General news and updates</label>
        </div>

        <div class="option">
          <input type="checkbox" id="promotions" v-model="preferences.promotions" />
          <label for="promotions">Promotions and discounts</label>
        </div>

        <div class="option">
          <input type="checkbox" id="newArrivals" v-model="preferences.newArrivals" />
          <label for="newArrivals">New arrivals</label>
        </div>

        <div class="option">
          <input type="checkbox" id="events" v-model="preferences.events" />
          <label for="events">Events and exclusive invitations</label>
        </div>
      </div>

      <div class="action-button">
        <button>Save preferences</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      preferences: {
        news: false,
        promotions: false,
        newArrivals: false,
        events: false,
      },
    }
  },
}
</script>

<style scoped>
.newsletter-container {
  padding: 20px 0;
}

.newsletter-header {
  margin-bottom: 30px;
}

.newsletter-content p {
  margin-bottom: 20px;
  max-width: 600px;
}

.subscription-options {
  margin-bottom: 30px;
}

.option {
  margin-bottom: 15px;
  display: flex;
  align-items: center;
}

.option input {
  margin-right: 10px;
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
