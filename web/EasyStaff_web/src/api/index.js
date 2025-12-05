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
  }
};

export default api;
