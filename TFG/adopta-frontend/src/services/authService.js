
import api from './api'

export const authService = {

  async login(email, password) {
    const params = new URLSearchParams()
    params.append('email', email)
    params.append('password', password)
    return api.post('/login', params, {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
    })
  },


  async logout() {
    return api.post('/logout')
  },

  async registrar(nombre, email, password, telefono, direccion) {
    return api.post('/api/registro', {
      nombre, email, password, telefono, direccion
    })
  },

  async miSesion() {
    return api.get('/api/me')
  }
}