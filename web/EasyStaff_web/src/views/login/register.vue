<template>
  <div class="login-wrapper">
    <div class="login-left">
      <h1>员工管理系统</h1>
      <p>注册一个新账号</p>
    </div>
    <div class="divider"></div>
    <div class="login-right">
      <div id="login-section">
        <h2>注册</h2>
        <div class="input-group">
          <label>用户名：</label>
          <input type="text" v-model="username" />
        </div>
        <div class="input-group">
          <label>密码：</label>
          <input type="password" v-model="password" />
        </div>
        <div class="input-group">
          <label>昵称：</label>
          <input type="text" v-model="nickname" />
        </div>
        <button class="login-btn" @click="handleRegister">注册</button>
        <button class="back-btn" @click="goLogin">返回登录</button>
        <div v-if="msg" :class="msgType === 'error' ? 'error-msg' : 'success-msg'">{{ msg }}</div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import api from '@/api';

const router = useRouter();
const username = ref('');
const password = ref('');
const nickname = ref('');
const msg = ref('');
const msgType = ref<'error' | 'success'>('success');

const handleRegister = async () => {
  msg.value = '';
  try {
    await api.register({
      username: username.value,
      password: password.value,
      nickname: nickname.value
    });
    msg.value = '注册成功，请登录';
    msgType.value = 'success';
    setTimeout(() => router.push('/login'), 800);
  } catch (err: any) {
    msg.value = err?.message || '注册失败';
    msgType.value = 'error';
  }
};

const goLogin = () => {
  router.push('/login');
};
</script>

<style scoped>
.login-wrapper {
  display: flex;
  width: 100%;
  height: 100vh;
  background: #eaf7fb;
}
.login-left {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #c3ecf7, #b1e0ef);
  padding: 40px;
  color: #054b68;
}
.divider {
  width: 2px;
  background-color: rgba(0, 0, 0, 0.1);
}
.login-right {
  width: 420px;
  display: flex;
  justify-content: center;
  align-items: center;
}
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
  background-color: #ffffff;
  color: #333333;
  font-size: 14px;
  box-sizing: border-box;
}
input:focus {
  border-color: #4caf50;
  background-color: #ffffff;
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
.back-btn {
  width: 100%;
  padding: 8px 0;
  background: #999;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 16px;
  cursor: pointer;
  margin-top: 10px;
}
.error-msg {
  margin-top: 12px;
  color: red;
  text-align: center;
}
.success-msg {
  margin-top: 12px;
  color: green;
  text-align: center;
}
</style>
