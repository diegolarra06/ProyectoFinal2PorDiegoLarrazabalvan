import api from './api'

export const favoritoService = {

  async listar() {
    return api.get('/api/favoritos')
  },

  async agregar(idAnimal) {
    return api.post(`/api/favoritos/${idAnimal}`)
  },

  async quitar(idAnimal) {
    return api.delete(`/api/favoritos/${idAnimal}`)
  }
}