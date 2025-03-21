// Continuing views/profile/PrivacyView.vue
<template>
  <div class="privacy-container">
    <div class="privacy-header">
      <h2>Privacy policy</h2>
    </div>

    <div class="privacy-content">
      <div class="privacy-section">
        <h3>Introduction</h3>
        <p>
          We respect your privacy and are committed to protecting your personal data. This privacy
          policy will inform you about how we look after your personal data when you visit our
          website and tell you about your privacy rights and how the law protects you.
        </p>
      </div>

      <div class="privacy-section">
        <h3>Data we collect</h3>
        <p>
          We may collect, use, store and transfer different kinds of personal data about you which
          we have grouped together as follows:
        </p>
        <ul>
          <li>Identity Data includes first name, last name, username or similar identifier.</li>
          <li>
            Contact Data includes billing address, delivery address, email address and telephone
            numbers.
          </li>
          <li>Financial Data includes payment card details.</li>
          <li>
            Transaction Data includes details about payments to and from you and other details of
            products you have purchased.
          </li>
          <li>
            Technical Data includes internet protocol (IP) address, browser type and version, time
            zone setting and location, browser plug-in types and versions, operating system and
            platform.
          </li>
          <li>
            Profile Data includes your username and password, purchases made by you, your
            preferences, feedback and survey responses.
          </li>
        </ul>
      </div>

      <div class="privacy-section">
        <h3>How we use your data</h3>
        <p>
          We will only use your personal data when the law allows us to. Most commonly, we will use
          your personal data in the following circumstances:
        </p>
        <ul>
          <li>
            Where we need to perform the contract we are about to enter into or have entered into
            with you.
          </li>
          <li>
            Where it is necessary for our legitimate interests and your interests and fundamental
            rights do not override those interests.
          </li>
          <li>Where we need to comply with a legal or regulatory obligation.</li>
        </ul>
      </div>

      <div class="privacy-section">
        <h3>Data security</h3>
        <p>
          We have put in place appropriate security measures to prevent your personal data from
          being accidentally lost, used or accessed in an unauthorized way, altered or disclosed. In
          addition, we limit access to your personal data to those employees, agents, contractors
          and other third parties who have a business need to know.
        </p>
      </div>

      <div class="privacy-section">
        <h3>Data retention</h3>
        <p>
          We will only retain your personal data for as long as necessary to fulfill the purposes we
          collected it for, including for the purposes of satisfying any legal, accounting, or
          reporting requirements.
        </p>
      </div>

      <div class="privacy-section">
        <h3>Your legal rights</h3>
        <p>
          Under certain circumstances, you have rights under data protection laws in relation to
          your personal data, including:
        </p>
        <ul>
          <li>Request access to your personal data.</li>
          <li>Request correction of your personal data.</li>
          <li>Request erasure of your personal data.</li>
          <li>Object to processing of your personal data.</li>
          <li>Request restriction of processing your personal data.</li>
          <li>Request transfer of your personal data.</li>
          <li>Right to withdraw consent.</li>
        </ul>
      </div>

      <div class="privacy-section">
        <h3>Contact</h3>
        <p>
          If you have any questions about this privacy policy or our privacy practices, please
          contact our data privacy manager:
        </p>
        <div class="contact-info">
          <p>Email: privacy@example.com</p>
          <p>
            Postal address: Privacy Manager, Example Company, 123 Example Street, Example City, EX1
            2YZ
          </p>
        </div>
      </div>

      <div class="privacy-controls">
        <h3>Your privacy controls</h3>
        <div class="control-option">
          <input type="checkbox" id="marketing" v-model="privacySettings.marketing" />
          <label for="marketing">Receive marketing communications</label>
        </div>

        <div class="control-option">
          <input type="checkbox" id="thirdParty" v-model="privacySettings.thirdParty" />
          <label for="thirdParty">Share data with third parties</label>
        </div>

        <div class="control-option">
          <input type="checkbox" id="analytics" v-model="privacySettings.analytics" />
          <label for="analytics">Allow analytics cookies</label>
        </div>

        <div class="action-button">
          <button>Save privacy settings</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      privacySettings: {
        marketing: false,
        thirdParty: false,
        analytics: false,
      },
    }
  },
}
</script>

<style scoped>
.privacy-container {
  padding: 20px 0;
}

.privacy-header {
  margin-bottom: 30px;
}

.privacy-section {
  margin-bottom: 30px;
}

.privacy-section h3 {
  margin-bottom: 15px;
}

.privacy-section p {
  margin-bottom: 15px;
  line-height: 1.5;
}

.privacy-section ul {
  padding-left: 20px;
  margin-bottom: 15px;
}

.privacy-section li {
  margin-bottom: 8px;
}

.contact-info {
  margin-top: 10px;
  padding: 15px;
  background-color: #f8f8f8;
  border-radius: 4px;
}

.privacy-controls {
  margin-top: 40px;
  padding: 20px;
  background-color: #f8f8f8;
  border-radius: 4px;
}

.privacy-controls h3 {
  margin-bottom: 20px;
}

.control-option {
  margin-bottom: 15px;
  display: flex;
  align-items: center;
}

.control-option input {
  margin-right: 10px;
}

.action-button {
  margin-top: 20px;
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
