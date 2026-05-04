import api from './api'

export const solicitudService = {

  async iniciar(idAnimal, comentarios) {
    return api.post('/api/solicitudes', { idAnimal, comentarios })
  },

  async listarMisSolicitudes() {
    return api.get('/api/solicitudes/mias')
  },

  async historial(idSolicitud) {
    return api.get(`/api/solicitudes/${idSolicitud}/historial`)
  },

  async cancelar(idSolicitud) {
    return api.delete(`/api/solicitudes/${idSolicitud}`)
  },

  async listarAdmin() {
    return api.get('/api/solicitudes')
  },

  async cambiarEstado(idSolicitud, idEstadoNuevo, comentarioAdmin) {
    return api.put(`/api/solicitudes/${idSolicitud}/estado`,
                   { idEstadoNuevo, comentarioAdmin })
  }
}