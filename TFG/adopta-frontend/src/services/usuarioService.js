import api from './api'

export const usuarioService = {

  async miPerfil() {
    return api.get('/api/me')
  },

  async actualizarDatos(id, nombre, telefono, direccion) {
    return api.put(`/api/usuarios/${id}`, { nombre, telefono, direccion })
  },

  async listarTodos() {
    return api.get('/api/usuarios')
  },

  async borrar(id) {
    return api.delete(`/api/usuarios/${id}`)
  }
}