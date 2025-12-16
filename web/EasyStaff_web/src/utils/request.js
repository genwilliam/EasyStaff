import axios from 'axios';
import { ElMessage } from 'element-plus';

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8';

const request = axios.create({
  baseURL: import.meta.env.VITE_APP_BASE_API,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json;charset=utf-8'
  },
  withCredentials: true // 带上 Cookie，后端才能用 Session 记住登录状态
});

// request 拦截器
request.interceptors.request.use(
  config => {
    return config;
  },
  error => Promise.reject(error)
);

request.interceptors.response.use(
  response => {
    const res = response?.data;

    if (res && typeof res.code !== 'undefined') {
      if (res.code === 0) {
        return res;
      } else {
        ElMessage.error(res.msg || '请求失败');
        return Promise.reject(new Error(res.msg || '请求失败'));
      }
    }

    return response;
  },
  error => {
    if (error?.response?.status === 401) {
      localStorage.removeItem('userInfo');
      ElMessage.error('登录已过期，请重新登录');
      window.location.href = '/login';
      return Promise.reject(error);
    }
    ElMessage.error(error.message || '请求失败');
    return Promise.reject(error);
  }
);

// 封装请求方法
const http = {
  get(url, params = {}, config = {}) {
    return request.get(url, { params, ...config });
  },
  // 下载文件（返回 blob）
  download(url, params = {}) {
    return request.get(url, { params, responseType: 'blob' }).then(res => res.data);
  },
  post(url, data = {}, config = {}) {
    return request.post(url, data, config);
  },
  put(url, data = {}, config = {}) {
    return request.put(url, data, config);
  },
  delete(url, data = {}, config = {}) {
    return request.delete(url, { data, ...config });
  }
};

export default http;
