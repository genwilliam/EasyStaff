<template>
  <header class="layout-header">
    <div class="brand-logo" @click="goEmployees">
      <span class="icon">📡</span>
      <span class="text">员工管理</span>
    </div>
    <nav class="nav">
      <button v-if="isAdmin" @click="goAdmin">账户管理</button>
      <button @click="goEmployees">员工列表</button>
    </nav>
  </header>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const cached = localStorage.getItem('userInfo');
const userInfo = cached ? JSON.parse(cached) : null;
const isAdmin = computed(() => userInfo && userInfo.role === 'ADMIN');

const goAdmin = () => {
  router.push('/admin/users');
};

const goEmployees = () => {
  router.push('/employees');
};
</script>

<style scoped>
.layout-header {
  height: 56px;
  background-color: #001529;
  color: #fff;
  display: flex;
  align-items: center; /* 垂直居中 (Y轴) */
  justify-content: center; /* <-- 修改这里：水平居中 (X轴) */
  padding: 0 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  flex-shrink: 0;
  z-index: 20;
}

.brand-logo {
  font-size: 18px;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 10px;
  letter-spacing: 1px;
  user-select: none;
  cursor: default;
}

.nav {
  position: absolute;
  right: 20px;
  display: flex;
  gap: 12px;
}

.nav button {
  background: #1677ff;
  color: #fff;
  border: none;
  padding: 6px 12px;
  border-radius: 6px;
  cursor: pointer;
}
</style>
