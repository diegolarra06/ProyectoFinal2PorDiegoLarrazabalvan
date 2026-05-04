// =============================================================
// CONFIGURACIÓN BASE DE AXIOS (Tema 10 PDF - Axios)
// Aquí centralizamos la configuración para todas las peticiones
// HTTP al backend Spring Boot en http://localhost:8080
// =============================================================
import axios from 'axios'

// Instancia de axios con configuración común
const api = axios.create({
  baseURL: 'http://localhost:8080',
  withCredentials: true, // Envía la cookie de sesión de Spring Security
  headers: {
    'Content-Type': 'application/json'
  }
})

// Interceptor de respuesta: si recibimos 401 (no autenticado),
// redirigimos al login automáticamente
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      console.warn('Sesión expirada o no autenticado')
    }
    return Promise.reject(error)
  }
)

export default api