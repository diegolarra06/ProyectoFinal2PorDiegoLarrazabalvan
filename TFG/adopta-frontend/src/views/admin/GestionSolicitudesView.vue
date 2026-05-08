
<script setup>
import { ref, onMounted } from 'vue'
import TablaSolicitudes from '@/components/TablaSolicitudes.vue'
import { solicitudService } from '@/services/solicitudService'

const solicitudes = ref([])
const cargando = ref(true)
const mostrarModal = ref(false)
const solicitudSeleccionada = ref(null)
const nuevoEstado = ref(1)
const comentario = ref('')

const ESTADOS = [
  { id: 1, nombre: 'EN_REVISION' },
  { id: 2, nombre: 'APROBADA' },
  { id: 3, nombre: 'RECHAZADA' },
  { id: 4, nombre: 'EN_PROCESO' },
  { id: 5, nombre: 'FINALIZADA' }
]

const cargar = async () => {
  cargando.value = true
  try {
    const resp = await solicitudService.listarAdmin()
    solicitudes.value = Array.isArray(resp.data) ? resp.data : []
  } finally { cargando.value = false }
}

onMounted(cargar)

const abrirModalCambioEstado = (s) => {
  solicitudSeleccionada.value = s
  nuevoEstado.value = s.idEstado
  comentario.value = ''
  mostrarModal.value = true
}

const confirmarCambio = async () => {
  try {
    await solicitudService.cambiarEstado(
      solicitudSeleccionada.value.idSolicitud,
      nuevoEstado.value,
      comentario.value
    )
    mostrarModal.value = false
    await cargar()
  } catch (e) {
    alert('Error al cambiar el estado.')
  }
}

const verHistorial = (id) => {

  alert('Historial de la solicitud ' + id)
}
</script>

<template>
  <div class="container my-4">
    <h1 class="text-success">
      <i class="bi bi-clipboard-heart"></i> Gestión de solicitudes
    </h1>

    <div v-if="cargando" class="text-center my-4">
      <div class="spinner-border text-success"></div>
    </div>

    <TablaSolicitudes v-else
                      :solicitudes="solicitudes"
                      :modo-admin="true"
                      @cambiar-estado="abrirModalCambioEstado"
                      @ver-historial="verHistorial" />


    <div v-if="mostrarModal" class="modal-overlay" @click.self="mostrarModal = false">
      <div class="modal-card">
        <h3>Cambiar estado de la solicitud #{{ solicitudSeleccionada.idSolicitud }}</h3>
        <p>{{ solicitudSeleccionada.nombreUsuario }} → {{ solicitudSeleccionada.nombreAnimal }}</p>

        <label class="form-label">Nuevo estado</label>
        <select v-model="nuevoEstado" class="form-select mb-3">
          <option v-for="e in ESTADOS" :key="e.id" :value="e.id">{{ e.nombre }}</option>
        </select>

        <label class="form-label">Comentario para el adoptante</label>
        <textarea v-model="comentario" class="form-control" rows="3"
                  placeholder="Este comentario llegará por email"></textarea>

        <div class="d-flex gap-2 mt-3">
          <button class="btn btn-secondary flex-fill" @click="mostrarModal = false">
            Cancelar
          </button>
          <button class="btn-primario flex-fill" @click="confirmarCambio">
            Confirmar cambio
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.modal-overlay {
  position: fixed; inset: 0; background: rgba(0,0,0,0.5);
  display: flex; justify-content: center; align-items: center; z-index: 2000;
}
.modal-card {
  background: white; padding: 30px; border-radius: var(--radius);
  max-width: 500px; width: 90%;
}
</style>