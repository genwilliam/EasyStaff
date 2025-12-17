<template>
  <div class="user-admin">
    <h2>账户管理</h2>

    <div class="create-box">
      <h3>创建用户</h3>
      <div class="create-grid">
        <label>用户名：<input v-model="createForm.username" placeholder="必填" /></label>
        <label>密码：<input type="password" v-model="createForm.password" placeholder="必填" /></label>
        <label>昵称：<input v-model="createForm.nickname" placeholder="可选" /></label>
        <label>
          角色：
          <select v-model="createForm.role">
            <option value="USER">普通用户</option>
            <option value="ADMIN">管理员</option>
          </select>
        </label>
      </div>
      <button class="create-btn" @click="createUser">创建</button>
      <p v-if="createMsg" :class="createMsgType">{{ createMsg }}</p>
    </div>

    <div class="search-bar">
      <label>
        用户名：
        <input v-model="query.username" placeholder="模糊查询" @keyup.enter="loadUsers(1)" />
      </label>
      <label>
        角色：
        <select v-model="query.role" @change="loadUsers(1)">
          <option value="">全部</option>
          <option value="ADMIN">管理员</option>
          <option value="USER">普通用户</option>
        </select>
      </label>
      <button @click="loadUsers(1)">查询</button>
      <button @click="resetQuery">重置</button>
    </div>

    <div class="table-container">
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>用户名</th>
            <th>昵称</th>
            <th>角色</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in users" :key="user.id">
            <td>{{ user.id }}</td>
            <td>{{ user.username }}</td>
            <td>{{ user.nickname || '-' }}</td>
            <td>
              <select v-model="user.role" @change="changeRole(user)">
                <option value="ADMIN">ADMIN</option>
                <option value="USER">USER</option>
              </select>
            </td>
            <td>
              <button @click="openReset(user)">重置密码</button>
              <button v-if="isSuperAdmin" @click="removeUser(user)" class="danger">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="pagination">
      <button @click="prevPage" :disabled="page <= 1">上一页</button>
      <span>第 {{ page }} 页 / 共 {{ total }} 条</span>
      <button @click="nextPage" :disabled="page * query.pageSize >= total">下一页</button>
    </div>

    <div v-if="showResetDialog" class="dialog">
      <div class="dialog-content">
        <h3>重置密码 - {{ currentUser?.username }}</h3>
        <input type="password" v-model="resetPassword" placeholder="新密码" />
        <div class="dialog-actions">
          <button @click="submitReset">确认</button>
          <button class="cancel" @click="closeReset">取消</button>
        </div>
        <p v-if="dialogMsg" class="dialog-msg">{{ dialogMsg }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, computed } from 'vue';
import api from '@/api';

const query = reactive({
  username: '',
  role: '',
  pageSize: 10
});

const users = ref([]);
const page = ref(1);
const total = ref(0);

const isSuperAdmin = computed(() => {
  const cached = localStorage.getItem('userInfo');
  const info = cached ? JSON.parse(cached) : null;
  return info && info.username === 'admin';
});

const showResetDialog = ref(false);
const resetPassword = ref('');
const currentUser = ref(null);
const dialogMsg = ref('');

const createForm = reactive({
  username: '',
  password: '',
  nickname: '',
  role: 'USER'
});
const createMsg = ref('');
const createMsgType = ref('success');

const loadUsers = async (p = 1) => {
  page.value = p;
  try {
    const params = {
      page: page.value,
      pageSize: query.pageSize,
      username: query.username || undefined,
      role: query.role || undefined
    };
    const res = await api.getUsers(params);
    users.value = res.data.list;
    total.value = res.data.total;
  } catch (err) {
    alert(err.message || '加载用户失败');
  }
};

const resetQuery = () => {
  query.username = '';
  query.role = '';
  loadUsers(1);
};

const prevPage = () => {
  if (page.value > 1) loadUsers(page.value - 1);
};

const nextPage = () => {
  if (page.value * query.pageSize < total.value) loadUsers(page.value + 1);
};

const openReset = user => {
  currentUser.value = user;
  resetPassword.value = '';
  dialogMsg.value = '';
  showResetDialog.value = true;
};

const closeReset = () => {
  showResetDialog.value = false;
};

const submitReset = async () => {
  if (!resetPassword.value) {
    dialogMsg.value = '新密码不能为空';
    return;
  }
  try {
    await api.resetUserPassword(currentUser.value.id, { newPassword: resetPassword.value });
    dialogMsg.value = '重置成功';
    setTimeout(closeReset, 600);
  } catch (err) {
    dialogMsg.value = err.message || '重置失败';
  }
};

const changeRole = async user => {
  try {
    await api.updateUserRole(user.id, { role: user.role });
  } catch (err) {
    alert(err.message || '更新角色失败');
    loadUsers(page.value);
  }
};

const removeUser = async user => {
  if (!isSuperAdmin.value) {
    alert('只有 admin 可以删除用户');
    return;
  }
  if (user.username === 'admin') {
    alert('不能删除超级管理员');
    return;
  }
  if (!confirm(`确定删除用户 ${user.username} 吗？`)) return;
  try {
    await api.deleteUser(user.id);
    alert('删除成功');
    loadUsers(page.value);
  } catch (err) {
    alert(err.message || '删除失败');
  }
};

const createUser = async () => {
  if (!createForm.username || !createForm.password) {
    createMsg.value = '用户名和密码必填';
    createMsgType.value = 'error';
    return;
  }
  try {
    await api.createUser({
      username: createForm.username,
      password: createForm.password,
      nickname: createForm.nickname,
      role: createForm.role
    });
    createMsg.value = '创建成功';
    createMsgType.value = 'success';
    createForm.username = '';
    createForm.password = '';
    createForm.nickname = '';
    createForm.role = 'USER';
    loadUsers(1);
  } catch (err) {
    createMsg.value = err.message || '创建失败';
    createMsgType.value = 'error';
  }
};

onMounted(() => {
  loadUsers(1);
});
</script>

<style scoped>
.user-admin {
  padding: 20px;
  background: #f7f9fb;
  min-height: calc(100vh - 56px);
}

.create-box {
  background: #fff;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  padding: 12px;
  margin-bottom: 12px;
}

.create-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 10px;
  margin: 8px 0;
}

.create-grid input,
.create-grid select {
  width: 100%;
  padding: 6px 8px;
  border: 1px solid #cbd5e1;
  border-radius: 6px;
}

.create-btn {
  padding: 8px 14px;
  border: none;
  background: #16a34a;
  color: white;
  border-radius: 6px;
  cursor: pointer;
}

.create-box p.success {
  color: green;
}

.create-box p.error {
  color: red;
}

.search-bar {
  display: flex;
  gap: 12px;
  align-items: center;
  margin-bottom: 16px;
  background: #fff;
  padding: 12px;
  border-radius: 8px;
  border: 1px solid #e2e8f0;
}

.search-bar input,
.search-bar select {
  padding: 6px 8px;
  border: 1px solid #cbd5e1;
  border-radius: 6px;
}

.search-bar button {
  padding: 8px 14px;
  border: none;
  background: #2563eb;
  color: white;
  border-radius: 6px;
  cursor: pointer;
}

.table-container {
  background: #fff;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  overflow: hidden;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th,
td {
  padding: 10px;
  border-bottom: 1px solid #e2e8f0;
  text-align: center;
}

th {
  background: #f1f5f9;
  font-weight: 600;
}

.pagination {
  margin-top: 12px;
  display: flex;
  gap: 12px;
  align-items: center;
}

.pagination button {
  padding: 6px 12px;
  border: none;
  background: #2563eb;
  color: white;
  border-radius: 6px;
  cursor: pointer;
}

.dialog {
  position: fixed;
  left: 0;
  top: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.35);
  display: flex;
  align-items: center;
  justify-content: center;
}

.dialog-content {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  width: 320px;
}

.dialog-content input {
  width: 100%;
  padding: 8px;
  border: 1px solid #cbd5e1;
  border-radius: 6px;
  margin-bottom: 10px;
}

.dialog-actions {
  display: flex;
  gap: 10px;
}

.dialog-actions .cancel {
  background: #9ca3af;
}

.dialog button {
  padding: 8px 12px;
  border: none;
  background: #2563eb;
  color: white;
  border-radius: 6px;
  cursor: pointer;
}

.danger {
  background: #dc2626 !important;
}

.dialog-msg {
  margin-top: 8px;
  color: green;
}
</style>

