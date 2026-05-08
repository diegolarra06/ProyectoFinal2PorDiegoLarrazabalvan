
<script setup>
defineProps({
  solicitudes: { type: Array, required: true },
  modoAdmin:   { type: Boolean, default: false }
})

const emit = defineEmits(['ver-historial', 'cancelar', 'cambiar-estado'])


const colorEstado = (nombre) => {
  const colores = {
    EN_REVISION: 'badge bg-warning text-dark',
    APROBADA:    'badge bg-success',
    RECHAZADA:   'badge bg-danger',
    EN_PROCESO:  'badge bg-info text-dark',
    FINALIZADA:  'badge bg-secondary'
  }
  return colores[nombre] || 'badge bg-secondary'
}
</script>

<template>
  <div class="table-responsive">
    <table class="table table-hover">
      <thead class="table-success">
        <tr>
          <th>#</th>
          <th>Fecha</th>
          <th>Animal</th>
          <th v-if="modoAdmin">Solicitante</th>
          <th>Estado</th>
          <th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="solicitudes.length === 0">
          <td :colspan="modoAdmin ? 6 : 5" class="text-center">
            No hay solicitudes para mostrar.
          </td>
        </tr>
        <tr v-for="s in solicitudes" :key="s.idSolicitud">
          <td>{{ s.idSolicitud }}</td>
          <td>{{ new Date(s.fechaSolicitud).toLocaleDateString('es-ES') }}</td>
          <td>{{ s.nombreAnimal }}</td>
          <td v-if="modoAdmin">
            {{ s.nombreUsuario }}<br>
            <small class="text-muted">{{ s.emailUsuario }}</small>
          </td>
          <td><span :class="colorEstado(s.nombreEstado)">{{ s.nombreEstado }}</span></td>
          <td>
            <button class="btn btn-sm btn-outline-primary me-1"
                    @click="emit('ver-historial', s.idSolicitud)">
              <i class="bi bi-clock-history"></i>
            </button>
            <button v-if="!modoAdmin && s.nombreEstado === 'EN_REVISION'"
                    class="btn btn-sm btn-outline-danger"
                    @click="emit('cancelar', s.idSolicitud)">
              Cancelar
            </button>
            <button v-if="modoAdmin"
                    class="btn btn-sm btn-success"
                    @click="emit('cambiar-estado', s)">
              Cambiar estado
            </button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>