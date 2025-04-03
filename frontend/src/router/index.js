import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import AuthorizationView from '@/views/AuthorizationView.vue'
import RegistrationView from '@/views/RegistrationView.vue'
import ProfileView from '@/views/ProfileView.vue'
import ProductView from '@/views/ProductView.vue'
import CartView from '@/views/CartView.vue'
import PurchasesView from '@/views/profile/PurchasesView.vue'
import ReturnsView from '@/views/profile/ReturnsView.vue'
import DetailsView from '@/views/profile/DetailsView.vue'
import AddressesView from '@/views/profile/AddressesView.vue'
import PaymentView from '@/views/profile/PaymentView.vue'
import NewsletterView from '@/views/profile/NewsletterView.vue'
import ServiceView from '@/views/profile/ServiceView.vue'
import PrivacyView from '@/views/profile/PrivacyView.vue'
import Wishlist from '@/views/Wishlist.vue'
import ManCatalogView from '@/views/ManCatalogView.vue'
import WomanCatalogView from '@/views/WomanCatalogView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/auth',
      name: 'auth',
      component: AuthorizationView,
      meta: { hideHeaderFooter: true },
    },
    {
      path: '/registration',
      name: 'registration',
      component: RegistrationView,
      meta: { hideHeaderFooter: true },
    },
    {
      path: '/profile',
      name: 'profile',
      component: ProfileView,
      meta: { hideHeaderFooter: true },
      children: [
        { path: '', redirect: 'purchases' },
        { path: 'purchases', component: PurchasesView },
        { path: 'returns', component: ReturnsView },
        { path: 'details', component: DetailsView },
        { path: 'addresses', component: AddressesView },
        { path: 'payment', component: PaymentView },
        { path: 'newsletter', component: NewsletterView },
        { path: 'service', component: ServiceView },
        { path: 'privacy', component: PrivacyView },
      ],
    },
    {
      path: '/product/:id',
      name: 'product',
      component: ProductView,
    },
    {
      path: '/cart',
      name: 'cart',
      component: CartView,
      meta: { hideHeaderFooter: true },
    },
    {
      path: '/wishlist',
      name: 'wishlist',
      component: Wishlist,
    },
    {
      path: '/man',
      name: 'man',
      component: ManCatalogView,
    },
    {
      path: '/woman',
      name: 'woman',
      component: WomanCatalogView,
    },
  ],
})

export default router
