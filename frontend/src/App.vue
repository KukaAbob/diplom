<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import Header from './components/Header.vue'
import Footer from './components/Footer.vue'

const route = useRoute()

const showHeaderFooter = computed(() => !route.meta.hideHeaderFooter)
</script>

<template>
  <Header v-if="showHeaderFooter" />
  <RouterView />
  <Footer v-if="showHeaderFooter" />
</template>

<style scoped></style>
