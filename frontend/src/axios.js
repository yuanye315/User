import axios from 'axios';

// 创建一个 Axios 实例
const instance = axios.create({
    baseURL: 'http://localhost:8080',
    timeout: 10000, // 请求超时时间
});

// 可以配置请求拦截器、响应拦截器等
instance.interceptors.request.use(
    (config) => {
        // 在发送请求之前做些什么
        // 例如，添加认证 token
        return config;
    },
    (error) => {
        // 对请求错误做些什么
        return Promise.reject(error);
    }
);

instance.interceptors.response.use(
    (response) => {
        // 对响应数据做些什么
        return response.data;
    },
    (error) => {
        // 对响应错误做些什么
        return Promise.reject(error);
    }
);

export default instance;
