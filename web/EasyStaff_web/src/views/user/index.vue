<template>
  <div class="employee-page">
    <h2 class="search-area">员工列表</h2>

    <!-- 查询条件 -->
    <div class="search-area">
      <label>
        姓名：
        <input v-model="query.name" type="text" />
      </label>

      <label>
        职位：
        <input v-model="query.position" type="text" />
      </label>

      <label>
        入职开始日期：
        <input v-model="query.startDate" type="date" />
      </label>

      <label>
        入职结束日期：
        <input v-model="query.endDate" type="date" />
      </label>

      <button @click="loadEmployees(1)">查询</button>
    </div>

    <!-- 添加员工 -->
    <div style="margin-top: 16px">
      <button @click="toggleAddForm">{{ showAddForm ? '关闭表单' : '添加员工' }}</button>
    </div>

    <div v-if="showAddForm" class="add-form">
      <h3>新增员工</h3>

      <label>
        姓名：
        <input v-model="newEmployee.name" type="text" />
      </label>

      <label>
        年龄：
        <input v-model.number="newEmployee.age" type="number" />
      </label>

      <label>
        职位：
        <input v-model="newEmployee.position" type="text" />
      </label>

      <label>
        入职日期：
        <input v-model="newEmployee.entryDate" type="date" />
      </label>

      <button @click="addEmployee">提交</button>

      <p v-if="addMsg" style="color: green">{{ addMsg }}</p>
    </div>

    <!-- 员工表格 -->
    <table class="employee-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>姓名</th>
          <th>年龄</th>
          <th>职位</th>
          <th>入职日期</th>
          <th>操作</th>
        </tr>
      </thead>

      <tbody>
        <tr v-for="emp in employees" :key="emp.id">
          <td>{{ emp.id }}</td>
          <td>{{ emp.name }}</td>
          <td>{{ emp.age }}</td>
          <td>{{ emp.position }}</td>
          <td>{{ emp.entryDate }}</td>
          <td>
            <button @click="showDetail(emp.id)">详情</button>
            <button @click="deleteEmployee(emp.id)">删除</button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- 分页 -->
    <div class="pagination">
      <button @click="prevPage" :disabled="page <= 1">上一页</button>
      <span>当前第 {{ page }} 页 / 共 {{ total }} 条</span>
      <button @click="nextPage">下一页</button>
    </div>

    <!-- 详情 -->
    <div v-if="showDetailBox" class="detail-box">
      <h3>员工详情</h3>
      <pre>{{ JSON.stringify(detailData, null, 2) }}</pre>
      <button @click="showDetailBox = false">关闭</button>
    </div>

    <!-- 退出 -->
    <div style="margin-top: 16px">
      <button @click="logout">退出登录</button>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { useRouter } from 'vue-router';
const router = useRouter();
const apiBase = 'http://localhost:8080';

// 分页
const page = ref(1);
const total = ref(0);
const employees = ref([]);

// 查询条件
const query = reactive({
  name: '',
  position: '',
  startDate: '',
  endDate: ''
});

// 添加员工区域
const showAddForm = ref(false);
const addMsg = ref('');

const newEmployee = reactive({
  name: '',
  age: null,
  position: '',
  entryDate: ''
});

// 详情
const showDetailBox = ref(false);
const detailData = ref({});

// ----------- API 调用 -------------

const loadEmployees = async (p = 1) => {
  page.value = p;

  const params = new URLSearchParams({
    page: page.value,
    name: query.name,
    position: query.position,
    startDate: query.startDate,
    endDate: query.endDate
  });

  const resp = await fetch(`${apiBase}/api/employees?${params}`, {
    credentials: 'include'
  });

  const data = await resp.json();
  if (data.code !== 0) {
    alert(data.msg);
    return;
  }

  employees.value = data.data.list;
  total.value = data.data.total;
};

const addEmployee = async () => {
  if (!newEmployee.name) {
    alert('姓名不能为空');
    return;
  }

  const resp = await fetch(`${apiBase}/api/employees`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
    body: JSON.stringify(newEmployee)
  });

  const data = await resp.json();
  if (data.code !== 0) {
    alert(data.msg);
    return;
  }

  addMsg.value = '添加成功';

  // 清空表单
  newEmployee.name = '';
  newEmployee.age = null;
  newEmployee.position = '';
  newEmployee.entryDate = '';

  loadEmployees(1);
};

const deleteEmployee = async id => {
  if (!confirm('确定删除？')) return;

  const resp = await fetch(`${apiBase}/api/employees/${id}`, {
    method: 'DELETE',
    credentials: 'include'
  });

  const data = await resp.json();
  if (data.code !== 0) {
    alert(data.msg);
    return;
  }

  loadEmployees(page.value);
};

const showDetail = async id => {
  const resp = await fetch(`${apiBase}/api/employees/${id}`, {
    credentials: 'include'
  });

  const data = await resp.json();
  if (data.code !== 0) {
    alert(data.msg);
    return;
  }

  detailData.value = data.data;
  showDetailBox.value = true;
};

const toggleAddForm = () => {
  showAddForm.value = !showAddForm.value;
};

// 分页按钮
const prevPage = () => {
  if (page.value > 1) loadEmployees(page.value - 1);
};

const nextPage = () => {
  loadEmployees(page.value + 1);
};

// 退出
const logout = async () => {
  try {
    await fetch(`${apiBase}/api/logout`, {
      method: 'POST',
      credentials: 'include'
    });

    // 清理本地缓存
    // localStorage.removeItem('user');

    alert('已退出登录');

    router.push('/login');
  } catch (e) {
    console.error(e);
  }
};

onMounted(() => {
  loadEmployees(1);
});
</script>

<style scoped>
/* 整体页面 */
.employee-page {
  padding: 20px;
  font-family: 'Arial', sans-serif;
  color: #222; /* 全局文字深色，保证可见 */
}

/* 标题 */
.employee-page h2 {
  font-size: 22px;
  margin-bottom: 15px;
  color: #2a7f2b;
}

/* 搜索区域 */
.search-area {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  margin-bottom: 20px;
  align-items: center;
}

.search-area label {
  color: #2a7f2b;
  font-weight: bold;
}

.search-area input {
  padding: 6px 10px;
  border: 1px solid #2a7f2b;
  border-radius: 6px;
  outline: none;
  color: #222;
  background: white; /* 👈 避免文字沉没 */
}

.search-area button {
  background: #2a7f2b;
  border: none;
  padding: 6px 16px;
  border-radius: 6px;
  cursor: pointer;
  color: white;
  font-weight: bold;
}

.search-area button:hover {
  background: #256a26;
}

/* 添加员工表单 */
.add-form {
  margin-top: 15px;
  background: #f8fff8;
  border: 1px solid #b6e3b8;
  padding: 16px;
  border-radius: 10px;
}

.add-form h3 {
  margin-bottom: 10px;
  color: #2a7f2b;
}

.add-form label {
  display: block;
  margin-bottom: 10px;
  font-weight: bold;
  color: #2a7f2b;
}

.add-form input {
  margin-left: 6px;
  padding: 6px 10px;
  border: 1px solid #2a7f2b;
  border-radius: 6px;
  color: #222;
  background: white;
}

.add-form button {
  background: #2a7f2b;
  color: white;
  padding: 8px 16px;
  border-radius: 6px;
  border: none;
  cursor: pointer;
  margin-top: 10px;
}

.add-form button:hover {
  background: #256a26;
}

/* 表格样式 */
.employee-table {
  margin-top: 20px;
  width: 100%;
  border-collapse: collapse;
  background: white;
}

.employee-table th {
  background: #e7f7e8; /* 表头浅绿色背景 */
  padding: 10px;
  font-weight: bold;
  color: #2a7f2b;
  border-bottom: 2px solid #2a7f2b;
}

.employee-table td {
  padding: 10px;
  color: #333; /* 深色字体 */
  border-bottom: 1px solid #d5e8d6;
}

/* 行 hover 提升可读性 */
.employee-table tr:hover {
  background: #f4fbf5;
}

/* 操作按钮 */
.employee-table button {
  background: #2a7f2b;
  border: none;
  padding: 6px 12px;
  border-radius: 6px;
  color: white;
  cursor: pointer;
  margin-right: 6px;
}

.employee-table button:hover {
  background: #256a26;
}

/* 分页 */
.pagination {
  margin-top: 16px;
  display: flex;
  align-items: center;
  gap: 15px;
}

.pagination button {
  background: #2a7f2b;
  padding: 6px 16px;
  border-radius: 6px;
  border: none;
  color: white;
  cursor: pointer;
}

.pagination button:disabled {
  background: #aaa;
  cursor: not-allowed;
}

.pagination span {
  color: #333;
}

/* 详情框 */
.detail-box {
  margin-top: 20px;
  border: 1px solid #2a7f2b;
  padding: 16px;
  background: #f7fff8;
  border-radius: 10px;
}

.detail-box h3 {
  color: #2a7f2b;
}

.detail-box pre {
  color: #333;
}

/* 退出按钮 */
button {
  transition: 0.2s;
}

.employee-page > div:last-child button {
  background: #444;
}

.employee-page > div:last-child button:hover {
  background: #222;
}
</style>
