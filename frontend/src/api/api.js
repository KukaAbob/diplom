import axios from "axios";
// Создаем экземпляр axios с базовой конфигурацией
const api = axios.create({
  baseURL: '', // относительные пути для работы через nginx
});

// Интерцептор для автоматического добавления токена ко всем запросам
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('jwtToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Интерцептор для обработки ошибок авторизации
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Токен истек или невалидный
      localStorage.removeItem('jwtToken');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// Используем api вместо axios везде
export default api;