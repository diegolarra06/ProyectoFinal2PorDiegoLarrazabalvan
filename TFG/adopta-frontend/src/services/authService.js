// =============================================================
// SERVICIO DE AUTENTICACIÓN
// El backend devuelve siempre JSON (no HTML), así que ya no
// hace falta manejar redirects manualmente.
// =============================================================
import api from './api'

export const authService = {

  // LOGIN: form-urlencoded a /login (Spring Security)
  // Devuelve 200 OK con {ok:true} si va bien, o 401 si falla
  async login(email, password) {
    const params = new URLSearchParams()
    params.append('email', email)
    params.append('password', password)
    return api.post('/login', params, {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
    })
  },

  // LOGOUT
  async logout() {
    return api.post('/logout')
  },

  // REGISTRO vía REST JSON
  async registrar(nombre, email, password, telefono, direccion) {
    return api.post('/api/registro', {
      nombre, email, password, telefono, direccion
    })
  },

  // Comprobar la sesión actual
  async miSesion() {
    return api.get('/api/me')
  }
}