<!--
  ===========================================================
  GESTIÓN DE ANIMALES (descripción 2.2.4.1)
  CRUD completo por parte del administrador
  ===========================================================
-->
<script setup>
import { ref, onMounted } from 'vue'
import { animalService } from '@/services/animalService'

const animales = ref([])
const cargando = ref(false)
const mensaje = ref(null)

// Form de creación/edición
const editando = ref(false)
const form = ref(formVacio())

function formVacio() {
  return {
    id: null, nombre: '', especie: '', edad: null, tamano: '',
    personalidad: '', necesidadesEspeciales: '', estadoSanitario: '',
    estado: 'DISPONIBLE'
  }
}

const cargar = async () => {
  cargando.value = true
  try {
    const resp = await animalService.listarPaginado(0, 100)
    animales.value = resp.data.content || []
  } finally { cargando.value = false }
}

onMounted(cargar)

const editar = (a) => {
  editando.value = true
  form.value = {
    id: a.idAnimal,
    nombre: a.nombre, especie: a.especie, edad: a.edad,
    tamano: a.tamano, personalidad: a.personalidad,
    necesidadesEspeciales: a.necesidadesEspeciales,
    estadoSanitario: a.estadoSanitario, estado: a.estado
  }
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

const limpiar = () => {
  editando.value = false
  form.value = formVacio()
}

const guardar = async () => {
  try {
    if (editando.value) {
      await animalService.actualizar(form.value)
      mensaje.value = { tipo: 'ok', texto: 'Animal actualizado.' }
    } else {
      await animalService.insertar(form.value)
      mensaje.value = { tipo: 'ok', texto: 'Animal creado.' }
    }
    limpiar()
    await cargar()
  } catch (e) {
    mensaje.value = { tipo: 'error', texto: 'Error al guardar.' }
  }
}

const borrar = async (id) => {
  if (!confirm('¿Borrar este animal?')) return
  await animalService.borrar(id)
  await cargar()
}

const subirImagen = async (idAnimal, event) => {
  const archivo = event.target.files[0]
  if (!archivo) return
  try {
    await animalService.subirImagen(idAnimal, archivo)
    mensaje.value = { tipo: 'ok', texto: 'Imagen subida correctamente.' }
    await cargar()
  } catch (e) {
    mensaje.value = { tipo: 'error', texto: 'Error subiendo la imagen.' }
  }
}
</script>

<template>
  <div class="container my-4">
    <h1 class="text-success"><i class="bi bi-house-heart"></i> Gestión de animales</h1>

    <div v-if="mensaje" :class="mensaje.tipo === 'ok' ? 'mensaje-ok' : 'mensaje-error'">
      {{ mensaje.texto }}
    </div>

    <!-- Formulario alta/edición -->
    <div class="card my-4">
      <div class="card-header bg-success text-white">
        <i class="bi bi-plus-circle"></i>
        {{ editando ? 'Editar animal' : 'Nuevo animal' }}
      </div>
      <div class="card-body">
        <form @submit.prevent="guardar" class="row g-3">
          <div class="col-md-4">
            <label class="form-label">Nombre *</label>
            <input v-model="form.nombre" class="form-control" required />
          </div>
          <div class="col-md-4">
            <label class="form-label">Especie *</label>
            <input v-model="form.especie" class="form-control" required />
          </div>
          <div class="col-md-2">
            <label class="form-label">Edad</label>
            <input v-model.number="form.edad" type="number" class="form-control" min="0" />
          </div>
          <div class="col-md-2">
            <label class="form-label">Tamaño</label>
            <select v-model="form.tamano" class="form-select">
              <option value="">—</option>
              <option>Pequeño</option><option>Mediano</option><option>Grande</option>
            </select>
          </div>
          <div class="col-md-12">
            <label class="form-label">Personalidad</label>
            <textarea v-model="form.personalidad" class="form-control" rows="2"></textarea>
          </div>
          <div class="col-md-6">
            <label class="form-label">Necesidades especiales</label>
            <textarea v-model="form.necesidadesEspeciales" class="form-control" rows="2"></textarea>
          </div>
          <div class="col-md-6">
            <label class="form-label">Estado sanitario</label>
            <textarea v-model="form.estadoSanitario" class="form-control" rows="2"></textarea>
          </div>
          <div class="col-md-4">
            <label class="form-label">Estado</label>
            <select v-model="form.estado" class="form-select">
              <option value="DISPONIBLE">DISPONIBLE</option>
              <option value="RESERVADO">RESERVADO</option>
              <option value="ADOPTADO">ADOPTADO</option>
            </select>
          </div>
          <div class="col-12 d-flex gap-2">
            <button type="submit" class="btn-primario">
              <i class="bi bi-check"></i> {{ editando ? 'Actualizar' : 'Crear' }}
            </button>
            <button v-if="editando" type="button" class="btn btn-secondary" @click="limpiar">
              Cancelar
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- Listado -->
    <h3>Animales registrados</h3>
    <table class="table table-hover">
      <thead class="table-success">
        <tr>
          <th>ID</th><th>Nombre</th><th>Especie</th><th>Edad</th>
          <th>Estado</th><th>Imagen</th><th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="a in animales" :key="a.idAnimal">
          <td>{{ a.idAnimal }}</td>
          <td>{{ a.nombre }}</td>
          <td>{{ a.especie }}</td>
          <td>{{ a.edad }}</td>
          <td>
            <span class="badge"
                  :class="{
                    'bg-success': a.estado === 'DISPONIBLE',
                    'bg-warning text-dark': a.estado === 'RESERVADO',
                    'bg-secondary': a.estado === 'ADOPTADO'
                  }">
              {{ a.estado }}
            </span>
          </td>
          <td>
            <input type="file" accept="image/*"
                   @change="subirImagen(a.idAnimal, $event)"
                   class="form-control form-control-sm" />
          </td>
          <td>
            <button class="btn btn-sm btn-outline-primary me-1" @click="editar(a)">
              <i class="bi bi-pencil"></i>
            </button>
            <button class="btn btn-sm btn-outline-danger" @click="borrar(a.idAnimal)">
              <i class="bi bi-trash"></i>
            </button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>