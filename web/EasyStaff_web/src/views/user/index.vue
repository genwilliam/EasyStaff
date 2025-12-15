<template>
  <div class="employee-page">
    <h2>员工管理</h2>

    <!-- 统计信息区域 -->
    <div class="statistics-section">
      <h3>统计信息</h3>
      <div class="stats-grid">
        <div class="stat-card">
          <div class="stat-label">总员工数</div>
          <div class="stat-value">{{ statistics.total || 0 }}</div>
        </div>
        <div class="stat-card" v-for="stat in statistics.positionStats" :key="stat.position">
          <div class="stat-label">{{ stat.position }}</div>
          <div class="stat-value">{{ stat.count }}</div>
        </div>
      </div>
      <button @click="loadStatistics" class="refresh-btn">刷新统计</button>
    </div>

    <!-- 搜索区域 -->
    <div class="search-area">
      <label>
        姓名：
        <input v-model="query.name" type="text" placeholder="请输入姓名" />
      </label>

      <label>
        职位：
        <input v-model="query.position" type="text" placeholder="请输入职位" />
      </label>

      <label>
        状态：
        <select v-model="query.employmentStatus">
          <option value="">全部</option>
          <option value="ACTIVE">在职</option>
          <option value="INACTIVE">离职</option>
        </select>
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
      <button @click="resetQuery">重置</button>
    </div>

    <!-- 操作按钮区域 -->
    <div class="action-buttons">
      <button @click="toggleAddForm">{{ showAddForm ? '关闭表单' : '添加员工' }}</button>
      <button @click="batchDelete" :disabled="selectedIds.length === 0" class="batch-delete-btn">
        批量删除 ({{ selectedIds.length }})
      </button>
      <button @click="exportCsv" class="export-btn">导出当前列表</button>
    </div>

    <!-- 添加/编辑员工表单 -->
    <div v-if="showAddForm || showEditForm" class="form-section">
      <h3>{{ showEditForm ? '编辑员工' : '新增员工' }}</h3>

      <div class="form-grid">
        <label>
          姓名：<span class="required">*</span>
          <input v-model="formEmployee.name" type="text" required />
        </label>

        <label>
          年龄：
          <input v-model.number="formEmployee.age" type="number" min="1" max="100" />
        </label>

        <label>
          职位：
          <input v-model="formEmployee.position" type="text" />
        </label>

        <label>
          状态：
          <select v-model="formEmployee.employmentStatus">
            <option value="ACTIVE">在职</option>
            <option value="INACTIVE">离职</option>
          </select>
        </label>

        <label>
          部门：
          <input v-model="formEmployee.department" type="text" />
        </label>

        <label>
          邮箱：
          <input v-model="formEmployee.email" type="email" />
        </label>

        <label>
          电话：
          <input v-model="formEmployee.phone" type="tel" />
        </label>

        <label>
          入职日期：
          <input v-model="formEmployee.entryDate" type="date" />
        </label>
      </div>

      <div class="form-actions">
        <button @click="submitForm">{{ showEditForm ? '更新' : '提交' }}</button>
        <button @click="cancelForm" class="cancel-btn">取消</button>
      </div>

      <p v-if="formMsg" :class="formMsgType">{{ formMsg }}</p>
    </div>

    <!-- 员工表格 -->
    <div class="table-container">
      <table class="employee-table">
        <thead>
          <tr>
            <th style="width: 40px">
              <input type="checkbox" @change="toggleSelectAll" :checked="isAllSelected" />
            </th>
            <th style="width: 60px">ID</th>
            <th>姓名</th>
            <th>年龄</th>
            <th>职位</th>
            <th>状态</th>
            <th>部门</th>
            <th>邮箱</th>
            <th>电话</th>
            <th>入职日期</th>
            <th style="width: 200px">操作</th>
          </tr>
        </thead>

        <tbody>
          <tr v-for="emp in employees" :key="emp.id">
            <td>
              <input type="checkbox" :value="emp.id" v-model="selectedIds" />
            </td>
            <td>{{ emp.id }}</td>
            <td>{{ emp.name }}</td>
            <td>{{ emp.age }}</td>
            <td>{{ emp.position }}</td>
            <td>{{ statusLabel(emp.employmentStatus) }}</td>
            <td>{{ emp.department || '-' }}</td>
            <td>{{ emp.email || '-' }}</td>
            <td>{{ emp.phone || '-' }}</td>
            <td>{{ emp.entryDate }}</td>
            <td>
              <button @click="editEmployee(emp)" class="edit-btn">编辑</button>
              <button @click="showDetail(emp.id)" class="detail-btn">详情</button>
              <button @click="deleteEmployee(emp.id)" class="delete-btn">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 分页 -->
    <div class="pagination">
      <button @click="prevPage" :disabled="page <= 1">上一页</button>
      <span>当前第 {{ page }} 页 / 共 {{ total }} 条</span>
      <button @click="nextPage" :disabled="page * 4 >= total">下一页</button>
    </div>

    <!-- 详情弹窗 -->
    <div v-if="showDetailBox" class="detail-box">
      <h3>员工详情</h3>
      <div class="detail-content">
        <div class="detail-item"><strong>ID:</strong> {{ detailData.id }}</div>
        <div class="detail-item"><strong>姓名:</strong> {{ detailData.name }}</div>
        <div class="detail-item"><strong>年龄:</strong> {{ detailData.age }}</div>
        <div class="detail-item"><strong>职位:</strong> {{ detailData.position }}</div>
        <div class="detail-item"><strong>状态:</strong> {{ statusLabel(detailData.employmentStatus) }}</div>
        <div class="detail-item"><strong>部门:</strong> {{ detailData.department || '-' }}</div>
        <div class="detail-item"><strong>邮箱:</strong> {{ detailData.email || '-' }}</div>
        <div class="detail-item"><strong>电话:</strong> {{ detailData.phone || '-' }}</div>
        <div class="detail-item"><strong>入职日期:</strong> {{ detailData.entryDate }}</div>
        <div class="detail-item"><strong>创建时间:</strong> {{ detailData.createTime }}</div>
        <div class="detail-item"><strong>更新时间:</strong> {{ detailData.updateTime }}</div>
      </div>
      <button @click="showDetailBox = false" class="close-btn">关闭</button>
    </div>

    <!-- 退出登录 -->
    <div class="logout-section">
      <button @click="logout" class="logout-btn">退出登录</button>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import api from '@/api';

const router = useRouter();

// 分页
const page = ref(1);
const total = ref(0);
const employees = ref([]);

// 统计信息
const statistics = ref({
  total: 0,
  positionStats: []
});

// 查询条件
const query = reactive({
  name: '',
  position: '',
  employmentStatus: '',
  startDate: '',
  endDate: ''
});

// 表单相关
const showAddForm = ref(false);
const showEditForm = ref(false);
const formMsg = ref('');
const formMsgType = ref('success');
const editingId = ref(null);

const formEmployee = reactive({
  name: '',
  age: null,
  position: '',
  employmentStatus: 'ACTIVE',
  department: '',
  email: '',
  phone: '',
  entryDate: ''
});

// 批量选择
const selectedIds = ref([]);

const isAllSelected = computed(() => {
  return employees.value.length > 0 && selectedIds.value.length === employees.value.length;
});

// 详情
const showDetailBox = ref(false);
const detailData = ref({});

// ----------- API 调用 -------------

const loadEmployees = async (p = 1) => {
  page.value = p;

  try {
    const params = {
      page: page.value,
      name: query.name || undefined,
      position: query.position || undefined,
      employmentStatus: query.employmentStatus || undefined,
      startDate: query.startDate || undefined,
      endDate: query.endDate || undefined
    };

    const res = await api.getEmployees(params);
    employees.value = res.data.list;
    total.value = res.data.total;
    selectedIds.value = []; // 清空选择
  } catch (err) {
    console.error('加载员工列表失败:', err);
    alert(err.message || '加载失败');
  }
};

const loadStatistics = async () => {
  try {
    const res = await api.getEmployeeStatistics();
    statistics.value = res.data;
  } catch (err) {
    console.error('加载统计信息失败:', err);
  }
};

const addEmployee = async () => {
  if (!formEmployee.name) {
    formMsg.value = '姓名不能为空';
    formMsgType.value = 'error';
    return;
  }

  try {
    await api.addEmployee(formEmployee);
    formMsg.value = '添加成功';
    formMsgType.value = 'success';
    resetForm();
    showAddForm.value = false;
    loadEmployees(1);
    loadStatistics();
  } catch (err) {
    formMsg.value = err.message || '添加失败';
    formMsgType.value = 'error';
  }
};

const updateEmployee = async () => {
  if (!formEmployee.name) {
    formMsg.value = '姓名不能为空';
    formMsgType.value = 'error';
    return;
  }

  try {
    await api.updateEmployee(editingId.value, formEmployee);
    formMsg.value = '更新成功';
    formMsgType.value = 'success';
    resetForm();
    showEditForm.value = false;
    editingId.value = null;
    loadEmployees(page.value);
    loadStatistics();
  } catch (err) {
    formMsg.value = err.message || '更新失败';
    formMsgType.value = 'error';
  }
};

const deleteEmployee = async id => {
  if (!confirm('确定删除该员工？')) return;

  try {
    await api.deleteEmployee(id);
    alert('删除成功');
    loadEmployees(page.value);
    loadStatistics();
  } catch (err) {
    alert(err.message || '删除失败');
  }
};

const batchDelete = async () => {
  if (selectedIds.value.length === 0) {
    alert('请选择要删除的员工');
    return;
  }

  if (!confirm(`确定删除选中的 ${selectedIds.value.length} 个员工？`)) return;

  try {
    await api.batchDeleteEmployees(selectedIds.value);
    alert('批量删除成功');
    selectedIds.value = [];
    loadEmployees(page.value);
    loadStatistics();
  } catch (err) {
    alert(err.message || '批量删除失败');
  }
};

const showDetail = async id => {
  try {
    const res = await api.getEmployeeDetail(id);
    detailData.value = res.data;
    showDetailBox.value = true;
  } catch (err) {
    alert(err.message || '获取详情失败');
  }
};

const editEmployee = emp => {
  editingId.value = emp.id;
  formEmployee.name = emp.name || '';
  formEmployee.age = emp.age || null;
  formEmployee.position = emp.position || '';
  formEmployee.employmentStatus = emp.employmentStatus || 'ACTIVE';
  formEmployee.department = emp.department || '';
  formEmployee.email = emp.email || '';
  formEmployee.phone = emp.phone || '';
  formEmployee.entryDate = emp.entryDate || '';
  showEditForm.value = true;
  showAddForm.value = false;
  formMsg.value = '';
};

const submitForm = () => {
  if (showEditForm.value) {
    updateEmployee();
  } else {
    addEmployee();
  }
};

const cancelForm = () => {
  resetForm();
  showAddForm.value = false;
  showEditForm.value = false;
  editingId.value = null;
};

const resetForm = () => {
  formEmployee.name = '';
  formEmployee.age = null;
  formEmployee.position = '';
  formEmployee.employmentStatus = 'ACTIVE';
  formEmployee.department = '';
  formEmployee.email = '';
  formEmployee.phone = '';
  formEmployee.entryDate = '';
  formMsg.value = '';
};

const toggleAddForm = () => {
  showAddForm.value = !showAddForm.value;
  showEditForm.value = false;
  editingId.value = null;
  resetForm();
};

const toggleSelectAll = event => {
  if (event.target.checked) {
    selectedIds.value = employees.value.map(emp => emp.id);
  } else {
    selectedIds.value = [];
  }
};

const resetQuery = () => {
  query.name = '';
  query.position = '';
  query.employmentStatus = '';
  query.startDate = '';
  query.endDate = '';
  loadEmployees(1);
};

// 分页按钮
const prevPage = () => {
  if (page.value > 1) loadEmployees(page.value - 1);
};

const nextPage = () => {
  if (page.value * 4 < total.value) loadEmployees(page.value + 1);
};

// 导出 CSV
const exportCsv = async () => {
  try {
    const params = {
      name: query.name || undefined,
      position: query.position || undefined,
      employmentStatus: query.employmentStatus || undefined,
      startDate: query.startDate || undefined,
      endDate: query.endDate || undefined
    };
    const res = await api.exportEmployees(params);
    const blob = new Blob([res], { type: 'text/csv;charset=utf-8;' });
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', 'employees.csv');
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    window.URL.revokeObjectURL(url);
  } catch (err) {
    alert(err.message || '导出失败');
  }
};

const statusLabel = status => {
  if (status === 'INACTIVE') return '离职';
  return '在职';
};

// 退出
const logout = async () => {
  try {
    await api.logout();
    alert('已退出登录');
    router.push('/login');
  } catch (e) {
    console.error(e);
  }
};

onMounted(() => {
  loadEmployees(1);
  loadStatistics();
});
</script>

<style scoped>
/* 整体页面 */
.employee-page {
  padding: 20px;
  font-family: 'Arial', sans-serif;
  color: #222;
  max-width: 1400px;
  margin: 0 auto;
}

.employee-page h2 {
  font-size: 24px;
  margin-bottom: 20px;
  color: #2a7f2b;
}

/* 统计信息区域 */
.statistics-section {
  background: #f0f9f0;
  border: 1px solid #b6e3b8;
  border-radius: 10px;
  padding: 16px;
  margin-bottom: 20px;
}

.statistics-section h3 {
  margin: 0 0 12px 0;
  color: #2a7f2b;
  font-size: 18px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  gap: 12px;
  margin-bottom: 12px;
}

.stat-card {
  background: white;
  border: 1px solid #d5e8d6;
  border-radius: 8px;
  padding: 12px;
  text-align: center;
}

.stat-label {
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #2a7f2b;
}

.refresh-btn {
  background: #2a7f2b;
  color: white;
  border: none;
  padding: 6px 16px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
}

.refresh-btn:hover {
  background: #256a26;
}

/* 搜索区域 */
.search-area {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  margin-bottom: 20px;
  align-items: center;
  padding: 16px;
  background: #f8fff8;
  border-radius: 8px;
  border: 1px solid #d5e8d6;
}

.search-area label {
  color: #2a7f2b;
  font-weight: bold;
  display: flex;
  align-items: center;
  gap: 8px;
}

.search-area input {
  padding: 6px 10px;
  border: 1px solid #2a7f2b;
  border-radius: 6px;
  outline: none;
  color: #222;
  background: white;
}
.search-area select {
  padding: 6px 10px;
  border: 1px solid #2a7f2b;
  border-radius: 6px;
  outline: none;
  color: #222;
  background: white;
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

/* 操作按钮区域 */
.action-buttons {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
}

.action-buttons button {
  background: #2a7f2b;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 6px;
  cursor: pointer;
  font-weight: bold;
}

.action-buttons button:hover:not(:disabled) {
  background: #256a26;
}

.action-buttons button:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.batch-delete-btn {
  background: #f44336 !important;
}

.batch-delete-btn:hover:not(:disabled) {
  background: #d32f2f !important;
}

.export-btn {
  background: #2196f3 !important;
}

.export-btn:hover {
  background: #1976d2 !important;
}

/* 表单区域 */
.form-section {
  margin-bottom: 20px;
  background: #f8fff8;
  border: 1px solid #b6e3b8;
  padding: 20px;
  border-radius: 10px;
}

.form-section h3 {
  margin: 0 0 16px 0;
  color: #2a7f2b;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 16px;
  margin-bottom: 16px;
}

.form-grid label {
  display: flex;
  flex-direction: column;
  gap: 6px;
  font-weight: bold;
  color: #2a7f2b;
}

.required {
  color: red;
}

.form-grid input {
  padding: 8px 10px;
  border: 1px solid #2a7f2b;
  border-radius: 6px;
  color: #222;
  background: white;
}

.form-actions {
  display: flex;
  gap: 12px;
}

.form-actions button {
  padding: 8px 20px;
  border-radius: 6px;
  border: none;
  cursor: pointer;
  font-weight: bold;
}

.form-actions button:first-child {
  background: #2a7f2b;
  color: white;
}

.form-actions button:first-child:hover {
  background: #256a26;
}

.cancel-btn {
  background: #999 !important;
  color: white !important;
}

.cancel-btn:hover {
  background: #777 !important;
}

.form-section p.success {
  color: green;
  margin-top: 12px;
}

.form-section p.error {
  color: red;
  margin-top: 12px;
}

/* 表格容器 */
.table-container {
  overflow-x: auto;
  margin-bottom: 16px;
}

.employee-table {
  width: 100%;
  border-collapse: collapse;
  background: white;
  min-width: 1200px;
}

.employee-table th,
.employee-table td {
  text-align: center;
  vertical-align: middle;
  padding: 10px;
  border-bottom: 1px solid #d5e8d6;
}

.employee-table th {
  background: #e7f7e8;
  font-weight: bold;
  color: #2a7f2b;
  border-bottom: 2px solid #2a7f2b;
}

.employee-table td {
  color: #333;
}

.employee-table tr:hover {
  background: #f4fbf5;
}

.employee-table input[type='checkbox'] {
  cursor: pointer;
}

.employee-table button {
  border: none;
  padding: 6px 12px;
  border-radius: 6px;
  color: white;
  cursor: pointer;
  margin: 0 4px;
  font-size: 13px;
}

.edit-btn {
  background: #2196f3;
}

.edit-btn:hover {
  background: #1976d2;
}

.detail-btn {
  background: #ff9800;
}

.detail-btn:hover {
  background: #f57c00;
}

.delete-btn {
  background: #f44336;
}

.delete-btn:hover {
  background: #d32f2f;
}

/* 分页 */
.pagination {
  margin-top: 16px;
  display: flex;
  align-items: center;
  gap: 15px;
  justify-content: center;
}

.pagination button {
  background: #2a7f2b;
  padding: 6px 16px;
  border-radius: 6px;
  border: none;
  color: white;
  cursor: pointer;
}

.pagination button:hover:not(:disabled) {
  background: #256a26;
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
  padding: 20px;
  background: #f7fff8;
  border-radius: 10px;
  position: relative;
}

.detail-box h3 {
  color: #2a7f2b;
  margin-top: 0;
}

.detail-content {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 12px;
  margin-bottom: 16px;
}

.detail-item {
  padding: 8px;
  background: white;
  border-radius: 6px;
  border: 1px solid #d5e8d6;
}

.detail-item strong {
  color: #2a7f2b;
  margin-right: 8px;
}

.close-btn {
  background: #999;
  color: white;
  border: none;
  padding: 8px 20px;
  border-radius: 6px;
  cursor: pointer;
}

.close-btn:hover {
  background: #777;
}

/* 退出登录 */
.logout-section {
  margin-top: 24px;
  text-align: center;
}

.logout-btn {
  background: #444;
  color: white;
  border: none;
  padding: 10px 24px;
  border-radius: 6px;
  cursor: pointer;
  font-weight: bold;
}

.logout-btn:hover {
  background: #222;
}
</style>
