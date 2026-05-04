// =============================================================
// SERVICIO DE AUTENTICACIÓN
// Login y registro contra los endpoints del backend Spring Security.
// El login se hace contra /login (form-urlencoded, no JSON)
// porque Spring Security espera ese formato.
// =============================================================
import api from './api'

export const authService = {

  // Login: envía email/password y Spring Security crea la sesión
  async login(email, password) {
    const params = new URLSearchParams()
    params.append('email', email)
    params.append('password', password)

    return api.post('/login', params, {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
    })
  },

  // Cierra la sesión en el servidor
  async logout() {
    return api.post('/logout')
  },

  // Registro de un cliente nuevo
  async registrar(nombre, email, password, telefono, direccion) {
    const params = new URLSearchParams()
    params.append('nombre', nombre)
    params.append('email', email)
    params.append('password', password)
    if (telefono) params.append('telefono', telefono)
    if (direccion) params.append('direccion', direccion)

    return api.post('/registro', params, {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
    })
  }
}