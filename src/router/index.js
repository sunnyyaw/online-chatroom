import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginPage from '../components/LoginPage.vue'
import RegisterPage from '../components/RegisterPage.vue'
import UserPage from '@/components/UserPage.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/login',
      name: 'login',
      component: LoginPage
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterPage
    },
    {
      path: '/profile/:username',
      name: 'profile',
      component: UserPage
    }
  ]
})
router.beforeEach((to, from,next) => {
  if (to.path === '/login' || to.path === '/register') {
    return next();
  }
  const token = localStorage.getItem('token');
  if (token) {
    return next();
  }
  router.push({
    name: 'login'
  });
});

export default router
