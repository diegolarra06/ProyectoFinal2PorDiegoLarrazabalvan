<!--
  ===========================================================
  ÁREA PERSONAL (descripción 2.3.3)
  Carga los datos del usuario logueado desde /api/me
  y permite editar nombre, teléfono y dirección.
  ===========================================================
-->
<script setup>
import { ref, onMounted } from 'vue'
import { authStore } from '@/stores/auth'
import { usuarioService } from '@/services/usuarioService'
import api from '@/services/api'

const usuario = ref({
  idUsuario: null,
  nombre: '',
  email: '',
  telefono: '',
  direccion: ''
})
const editando = ref(false)
const cargando = ref(true)
const mensaje = ref(null)

// Carga los datos REALES del usuario logueado desde /api/me
const cargarPerfil = async () => {
  cargando.value = true
  try {
    const resp = await api.get('/api/me')
    if (resp.data.autenticado) {
      usuario.value = {
        idUsuario: resp.data.idUsuario,
        nombre: resp.data.nombre || '',
        email: resp.data.email || '',
        telefono: resp.data.telefono || '',
        direccion: resp.data.direccion || ''
      }
    } else {
      mensaje.value = { tipo: 'error', texto: 'No estás autenticado.' }
    }
  } catch (e) {
    console.error(e)
    mensaje.value = { tipo: 'error', texto: 'Error cargando tus datos.' }
  } finally {
    cargando.value = false
  }
}

onMounted(cargarPerfil)

const guardar = async () => {
  // Validación: debe haber un idUsuario válido
  if (!usuario.value.idUsuario) {
    mensaje.value = { tipo: 'error', texto: 'No se puede guardar: usuario no identificado.' }
    return
  }

  try {
    await usuarioService.actualizarDatos(
      usuario.value.idUsuario,
      usuario.value.nombre,
      usuario.value.telefono,
      usuario.value.direccion
    )
    mensaje.value = { tipo: 'ok', texto: 'Datos actualizados correctamente.' }
    editando.value = false
    // Recargar para confirmar
    await cargarPerfil()
  } catch (e) {
    console.error(e)
    mensaje.value = {
      tipo: 'error',
      texto: 'No se pudieron guardar los cambios: ' +
             (e.response?.data?.error || e.message)
    }
  }
}

const cancelar = async () => {
  editando.value = false
  await cargarPerfil()
  mensaje.value = null
}
</script>

<template>
  <div class="container my-4">
    <h1 class="text-success">
      <i class="bi bi-person-circle"></i> Mi área personal
    </h1>

    <div v-if="mensaje"
         :class="mensaje.tipo === 'ok' ? 'mensaje-ok' : 'mensaje-error'">
      {{ mensaje.texto }}
    </div>

    <div v-if="cargando" class="text-center my-4">
      <div class="spinner-border text-success"></div>
    </div>

    <div v-else class="card mt-3">
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
                   :disabled="!editando" required />
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

          <div class="d-flex gap-2">
            <button v-if="!editando" type="button" class="btn-primario"
                    @click="editando = true">
              <i class="bi bi-pencil"></i> Editar
            </button>
            <button v-else type="submit" class="btn-primario">
              <i class="bi bi-check"></i> Guardar
            </button>
            <button v-if="editando" type="button" class="btn btn-secondary"
                    @click="cancelar">
              Cancelar
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- Accesos rápidos -->
    <div class="row mt-4">
      <div class="col-md-6 mb-3">
        <RouterLink to="/favoritos" class="card text-decoration-none h-100">
          <div class="card-body text-center">
            <i class="bi bi-heart-fill text-danger" style="font-size: 2rem;"></i>
            <h4>Mis favoritos</h4>
            <p class="text-muted">Ver los animales que has marcado</p>
          </div>
        </RouterLink>
      </div>
      <div class="col-md-6 mb-3">
        <RouterLink to="/mis-solicitudes" class="card text-decoration-none h-100">
          <div class="card-body text-center">
            <i class="bi bi-clipboard-heart text-success" style="font-size: 2rem;"></i>
            <h4>Mis solicitudes</h4>
            <p class="text-muted">Consulta el estado de tus adopciones</p>
          </div>
        </RouterLink>
      </div>
    </div>
  </div>
</template>