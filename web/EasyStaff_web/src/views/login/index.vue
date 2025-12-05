<template>
  <div class="login-wrapper">
    <!-- 左侧区域 -->
    <div class="login-left">
      <h1>员工管理系统</h1>
      <p>欢迎使用 EasyStaff</p>
      <!-- <img src="" class="left-img" /> -->
    </div>

    <!-- 分割线 -->
    <div class="divider"></div>

    <!-- 右侧登录框 -->
    <div class="login-right">
      <div id="login-section">
        <h2>登录</h2>

        <div class="input-group">
          <label>用户名：</label>
          <input type="text" v-model="username" />
        </div>

        <div class="input-group">
          <label>密码：</label>
          <input type="password" v-model="password" />
        </div>

        <button class="login-btn" @click="handleLogin">登录</button>

        <div v-if="errorMsg" class="error-msg">{{ errorMsg }}</div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import api from '@/api';

const router = useRouter();

const username = ref('admin');
const password = ref('123456');
const errorMsg = ref('');

const handleLogin = async () => {
  errorMsg.value = '';

  try {
    const res = await api.login({
      username: username.value,
      password: password.value
    });

    console.log('登录成功：', res.data); // 这里是用户信息

    // TODO: 存用户信息
    // localStorage.setItem('userInfo', JSON.stringify(res.data));

    // 跳转到员工列表页
    router.push('/employees');
  } catch (err: any) {
    console.error(err);
    errorMsg.value = err.message || '登录失败，请稍后重试';
  }
};
</script>
<style scoped>
/* 整体容器：水平双栏布局 */
.login-wrapper {
  display: flex;
  width: 100%;
  height: 100vh;
  background: #eaf7fb;
}

/* 左侧区域 */
.login-left {
  flex: 1; /* 占左侧空间 */
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #c3ecf7, #b1e0ef);
  padding: 40px;
  color: #054b68;
}

.login-left h1 {
  font-size: 32px;
  margin-bottom: 15px;
}

.login-left p {
  font-size: 18px;
}

/* 分割线 */
.divider {
  width: 2px;
  background-color: rgba(0, 0, 0, 0.1);
}

/* 右侧登录区域 */
.login-right {
  width: 420px;
  display: flex;
  justify-content: center;
  align-items: center;
}

/* 登录框 */
#login-section {
  width: 85%;
  max-width: 350px;
  padding: 30px 25px;
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0px 4px 18px rgba(0, 0, 0, 0.15);
}

#login-section h2 {
  text-align: center;
  color: #2e7d32;
  margin-bottom: 22px;
}

.input-group {
  color: #2e7d32;
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

label {
  width: 70px;
  font-weight: 600;
}

input {
  flex: 1;
  padding: 8px 10px;
  border: 1px solid #a5d6a7;
  border-radius: 6px;
  background-color: #ffffff; /* ★ 永远白底，不透明 */
  color: #333333; /* ★ 深色字体 */
  font-size: 14px;
  box-sizing: border-box;
}

input:focus {
  border-color: #4caf50;
  background-color: #ffffff; /* ★ 确保 focus 时仍是白底 */
  outline: none;
  box-shadow: 0 0 4px rgba(76, 175, 80, 0.4);
}

.login-btn {
  width: 100%;
  padding: 8px 0;
  background: #43a047;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 16px;
  cursor: pointer;
}

.error-msg {
  margin-top: 12px;
  color: red;
  text-align: center;
}
</style>
