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
import WishlistView from '@/views/WishlistView.vue'
import GeneralCatalogView from '@/views/GeneralCatalogView.vue'
import SearchResultsPageView from '@/views/SearchResultsPageView.vue'
import CheckoutView from '@/views/CheckoutView.vue'
import OrderConfirmation from '@/views/OrderConfirmation.vue'
import ImageUploaderViewDev from '@/views/ImageUploaderViewDev.vue'
import ManagerView from '@/views/admin/ManagerView.vue'
import UserManagementView from '@/views/admin/UserManagementView.vue'
import ProductManagementView from '@/views/admin/ProductManagementView.vue'
import OrderManagementView from '@/views/admin/OrderManagementView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/authentication',
      name: 'authentication',
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
      component: WishlistView,
      meta: { hideHeaderFooter: true },
    },
    {
      path: '/general',
      name: 'general',
      component: GeneralCatalogView,
    },
    {
      path: '/search',
      name: 'search',
      component: SearchResultsPageView,
    },
    {
      path: '/checkout',
      name: 'Checkout',
      component: CheckoutView,
      meta: { hideHeaderFooter: true },
    },
    {
      path: '/order-success',
      name: 'confirmation',
      component: OrderConfirmation,
      meta: { hideHeaderFooter: true },
    },
    {
      path: '/dev-image',
      name: 'dev-image',
      component: ImageUploaderViewDev,
    },
    {
      path: '/dev-manager',
      name: 'dev-manager',
      component: ManagerView,
      meta: { hideHeaderFooter: true },
      children: [
        { path: '', redirect: '/dev-manager/users' },
        { path: 'users', component: UserManagementView },
        { path: 'products', component: ProductManagementView },
        { path: 'orders', component: OrderManagementView },
      ],
    },
  ],
})

export default router
