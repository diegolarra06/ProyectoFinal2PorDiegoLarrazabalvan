
import api from './api'

export const noticiaService = {

  async listarPublicadas() {
    return api.get('/api/noticias')
  },

  async listarDestacadas(cantidad = 5) {
    return api.get('/api/noticias/destacadas', { params: { cantidad } })
  },

  async listarTodasAdmin() {
    return api.get('/api/noticias/admin')
  },

  async obtenerFicha(id) {
    return api.get(`/api/noticias/${id}`)
  },

  async crear(noticia) {
    return api.post('/api/noticias', noticia)
  },

  async actualizar(id, noticia) {
    return api.put(`/api/noticias/${id}`, noticia)
  },

  async borrar(id) {
    return api.delete(`/api/noticias/${id}`)
  },

  async subirImagen(id, archivo) {
    const formData = new FormData()
    formData.append('archivo', archivo)
    return api.post(`/api/noticias/${id}/imagen`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  }
}