import axios from 'axios';

const instance = axios.create({
    baseURL: 'https://vagentbetabackend.onrender.com',
    withCredentials: true, // if you use cookies
});

// Add a request interceptor
instance.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

export default instance;