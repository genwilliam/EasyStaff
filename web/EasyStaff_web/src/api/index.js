import http from '@/utils/request.js';

const api = {
  // 登录接口
  login(data) {
    return http.post('/api/login', data); // 返回的是 { code, data, msg }
  },

  // 退出接口
  logout() {
    return http.post('/api/logout');
  },

  // 注册
  register(data) {
    return http.post('/api/register', data);
  },

  // 当前用户
  currentUser() {
    return http.get('/api/current-user');
  },

  // 用户列表（管理员）
  getUsers(params) {
    return http.get('/api/users', params);
  },

  // 重置密码（管理员）
  resetUserPassword(id, data) {
    return http.post(`/api/users/${id}/reset-password`, data);
  },

  // 更新角色（管理员）
  updateUserRole(id, data) {
    return http.post(`/api/users/${id}/role`, data);
  },

  // 创建用户（管理员）
  createUser(data) {
    return http.post('/api/users', data);
  },

  // 获取员工列表（带分页 & 查询）
  getEmployees(params) {
    return http.get('/api/employees', params);
  },

  // 新增员工
  addEmployee(data) {
    return http.post('/api/employees', data);
  },

  // 删除员工
  deleteEmployee(id) {
    return http.delete(`/api/employees/${id}`);
  },

  // 详情
  getEmployeeDetail(id) {
    return http.get(`/api/employees/${id}`);
  },

  // 更新员工
  updateEmployee(id, data) {
    return http.put(`/api/employees/${id}`, data);
  },

  // 批量删除员工
  batchDeleteEmployees(ids) {
    return http.delete('/api/employees/batch', ids);
  },

  // 批量更新员工
  batchUpdateEmployees(data) {
    return http.put('/api/employees/batch', data);
  },

  // 导入员工
  importEmployees(formData) {
    return http.post('/api/employees/import', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    });
  },

  // 获取员工统计信息
  getEmployeeStatistics() {
    return http.get('/api/employees/statistics');
  },

  // 导出员工列表为 CSV（按查询条件）
  exportEmployees(params) {
    return http.download('/api/employees/export', params);
  }
};

export default api;
