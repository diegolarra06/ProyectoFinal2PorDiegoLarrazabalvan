import api from './api'

export const citaService = {

  async listar() {
    return api.get('/api/citas')
  },

  async programar(idSolicitud, fechaCita, observaciones) {
    return api.post('/api/citas', { idSolicitud, fechaCita, observaciones })
  },

  async borrar(idCita) {
    return api.delete(`/api/citas/${idCita}`)
  }
}