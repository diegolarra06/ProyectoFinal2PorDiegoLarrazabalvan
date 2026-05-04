<!--
  ===========================================================
  ÁREA PERSONAL (descripción 2.3.3)
  ===========================================================
-->
<script setup>
import { ref, onMounted } from 'vue'
import { authStore } from '@/stores/auth'
import { usuarioService } from '@/services/usuarioService'

const usuario = ref({
  idUsuario: null,
  nombre: '',
  email: authStore.state.email,
  telefono: '',
  direccion: ''
})
const editando = ref(false)
const mensaje = ref(null)

onMounted(async () => {
  // Idealmente cargarías los datos desde el backend
  // Como tu controlador devuelve la vista entera, simulo los datos:
  usuario.value.email = authStore.state.email
})

const guardar = async () => {
  try {
    await usuarioService.actualizarDatos(
      usuario.value.idUsuario,
      usuario.value.nombre,
      usuario.value.telefono,
      usuario.value.direccion
    )
    mensaje.value = { tipo: 'ok', texto: 'Datos actualizados correctamente.' }
    editando.value = false
  } catch (e) {
    mensaje.value = { tipo: 'error', texto: 'No se pudieron guardar los cambios.' }
  }
}
</script>

<template>
  <div class="container my-4">
    <h1 class="text-success">
      <i class="bi bi-person-circle"></i> Mi área personal
    </h1>

    <div v-if="mensaje" :class="mensaje.tipo === 'ok' ? 'mensaje-ok' : 'mensaje-error'">
      {{ mensaje.texto }}
    </div>

    <div class="card mt-3">
      <div class="card-header bg-success text-white">
        <i class="bi bi-info-circle"></i> Datos personales
      </div>
      <div class="card-body">
        <form @submit.prevent="guardar">
          <div class="mb-3">
            <label class="form-label">Email (no editable)</label>
            <input :value="usuario.email" type="email" class="form-control" disabled />
          </div>
          <div class="mb-3">
            <label class="form-label">Nombre</label>
            <input v-model="usuario.nombre" type="text" class="form-control"
                   :disabled="!editando" />
          </div>
          <div class="mb-3">
            <label class="form-label">Teléfono</label>
            <input v-model="usuario.telefono" type="tel" class="form-control"
                   :disabled="!editando" />
          </div>
          <div class="mb-3">
            <label class="form-label">Dirección</label>
            <input v-model="usuario.direccion" type="text" class="form-control"
                   :disabled="!editando" />
          </div>

          <button v-if="!editando" type="button" class="btn-primario"
                  @click="editando = true">
            <i class="bi bi-pencil"></i> Editar
          </button>
          <button v-else type="submit" class="btn-primario">
            <i class="bi bi-check"></i> Guardar
          </button>
        </form>
      </div>
    </div>

    <div class="row mt-4">
      <div class="col-md-6">
        <RouterLink to="/favoritos" class="card text-decoration-none">
          <div class="card-body text-center">
            <i class="bi bi-heart-fill text-danger" style="font-size: 2rem;"></i>
            <h4>Mis favoritos</h4>
            <p>Ver los animales que has marcado</p>
          </div>
        </RouterLink>
      </div>
      <div class="col-md-6">
        <RouterLink to="/mis-solicitudes" class="card text-decoration-none">
          <div class="card-body text-center">
            <i class="bi bi-clipboard-heart text-success" style="font-size: 2rem;"></i>
            <h4>Mis solicitudes</h4>
            <p>Consulta el estado de tus adopciones</p>
          </div>
        </RouterLink>
      </div>
    </div>
  </div>
</template>