
import api from './api'

export const animalService = {

  async listarPaginado(page = 0, size = 8, sort = 'idAnimal') {
    return api.get('/api/animales', { params: { page, size, sort } })
  },

  async listarEspecies() {
    return api.get('/api/animales/especies')
  },

  async buscarConFiltros(filtros) {
    return api.get('/api/animales/buscar', { params: filtros })
  },

  async obtenerFicha(idAnimal) {
    return api.get(`/api/animales/${idAnimal}`)
  },

  async insertar(animal) {
    return api.post('/api/animales', animal)
  },

  async actualizar(animal) {
    return api.put(`/api/animales/${animal.id}`, animal)
  },

  async borrar(id) {
    return api.delete(`/api/animales/${id}`)
  },


  async listarImagenes(idAnimal) {
    return api.get(`/api/animales/${idAnimal}/imagenes`)
  },

  async subirImagen(idAnimal, archivo) {
    const formData = new FormData()
    formData.append('archivo', archivo)
    return api.post(`/api/animales/${idAnimal}/imagenes`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  },
  
  async borrarImagen(idImagen) {
    return api.delete(`/api/animales/imagenes/${idImagen}`)
  }
  
}