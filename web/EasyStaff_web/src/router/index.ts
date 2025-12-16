import { createRouter, createWebHistory } from 'vue-router';
import api from '@/api';
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
            title: '用户管理',
            requiresAuth: true
          }
        },
        {
          path: '/admin/users',
          name: 'adminUsers',
          component: () => import('@/views/admin/users.vue'),
          meta: {
            title: '账户管理',
            requiresAuth: true,
            roles: ['ADMIN']
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

router.beforeEach(async to => {
  const requiresAuth = !!to.meta?.requiresAuth;
  const allowRoles = (to.meta?.roles as string[] | undefined) || [];

  let currentUser = null;
  const cached = localStorage.getItem('userInfo');
  if (cached) {
    currentUser = JSON.parse(cached);
  }

  // 如果需要鉴权但没有缓存，则尝试获取当前用户
  if (requiresAuth && !currentUser) {
    try {
      const res = await api.currentUser();
      currentUser = res.data;
      localStorage.setItem('userInfo', JSON.stringify(res.data));
    } catch (e) {
      currentUser = null;
    }
  }

  if (requiresAuth && !currentUser) {
    return { path: '/login', query: { redirect: to.fullPath } };
  }

  if (allowRoles.length > 0 && currentUser && !allowRoles.includes(currentUser.role)) {
    return { path: '/employees' };
  }

  return true;
});

export default router;
