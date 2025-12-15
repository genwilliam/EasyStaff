import { createRouter, createWebHistory } from 'vue-router';
import Layout from '../layouts/index.vue';
const router = createRouter({
  history: createWebHistory(import.meta.env.VITE_APP_BASE_URL),
  routes: [
    {
      path: '/',
      name: 'layout',
      component: Layout,
      redirect: '/login',
      children: [
        {
          path: '/employees',
          name: 'employees',
          component: () => import('@/views/user/index.vue'),
          meta: {
            title: '用户管理'
          }
        },
        {
          path: '/login',
          name: 'login',
          component: () => import('@/views/login/index.vue'),
          meta: {
            title: '登录'
          }
        },
        {
          path: '/register',
          name: 'register',
          component: () => import('@/views/login/register.vue'),
          meta: {
            title: '注册'
          }
        }
      ]
    }
  ]
});

export default router;
