<script setup>
import { ref, onMounted } from 'vue'
import { citaService } from '@/services/citaService'

const citas = ref([])
const form = ref({ idSolicitud: null, fechaCita: '', observaciones: '' })

const cargar = async () => {
  const resp = await citaService.listar()
  citas.value = Array.isArray(resp.data) ? resp.data : []
}

onMounted(cargar)

const programar = async () => {
  await citaService.programar(form.value.idSolicitud, form.value.fechaCita, form.value.observaciones)
  form.value = { idSolicitud: null, fechaCita: '', observaciones: '' }
  await cargar()
}

const borrar = async (id) => {
  if (!confirm('¿Eliminar la cita?')) return
  await citaService.borrar(id)
  await cargar()
}
</script>

<template>
  <div class="container my-4">
    <h1 class="text-success"><i class="bi bi-calendar-event"></i> Gestión de citas</h1>

    <div class="card my-3">
      <div class="card-header bg-success text-white">Programar nueva cita</div>
      <div class="card-body">
        <form @submit.prevent="programar" class="row g-2">
          <div class="col-md-3">
            <input v-model.number="form.idSolicitud" type="number"
                   placeholder="ID solicitud" class="form-control" required />
          </div>
          <div class="col-md-4">
            <input v-model="form.fechaCita" type="datetime-local"
                   class="form-control" required />
          </div>
          <div class="col-md-3">
            <input v-model="form.observaciones" placeholder="Observaciones" class="form-control" />
          </div>
          <div class="col-md-2">
            <button class="btn-primario w-100">Programar</button>
          </div>
        </form>
      </div>
    </div>

    <table class="table table-hover">
      <thead class="table-success">
        <tr><th>ID</th><th>Fecha</th><th>Solicitud</th><th>Adoptante</th><th>Animal</th><th>Acciones</th></tr>
      </thead>
      <tbody>
        <tr v-for="c in citas" :key="c.idCita">
          <td>{{ c.idCita }}</td>
          <td>{{ new Date(c.fechaCita).toLocaleString('es-ES') }}</td>
          <td>#{{ c.idSolicitud }}</td>
          <td>{{ c.nombreUsuario }}</td>
          <td>{{ c.nombreAnimal }}</td>
          <td>
            <button class="btn btn-sm btn-outline-danger" @click="borrar(c.idCita)">
              <i class="bi bi-trash"></i>
            </button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>