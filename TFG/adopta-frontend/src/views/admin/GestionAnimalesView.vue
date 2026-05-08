
<script setup>
import { ref, onMounted } from 'vue'
import { animalService } from '@/services/animalService'

const animales = ref([])
const cargando = ref(false)
const mensaje = ref(null)


const especiesDisponibles = ref([])


const editando = ref(false)
const form = ref(formVacio())


const imagenes = ref([])
const archivoNuevo = ref(null)

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

const cargarEspecies = async () => {
  try {
    const resp = await animalService.listarEspecies()
    especiesDisponibles.value = resp.data || []
  } catch (e) {
    console.error('Error cargando especies', e)
  }
}

onMounted(async () => {
  await cargar()
  await cargarEspecies()
})

const cargarImagenes = async (idAnimal) => {
  try {
    const resp = await animalService.listarImagenes(idAnimal)
    imagenes.value = resp.data || []
  } catch (e) {
    imagenes.value = []
  }
}

const editar = async (a) => {
  editando.value = true
  form.value = {
    id: a.idAnimal,
    nombre: a.nombre, especie: a.especie, edad: a.edad,
    tamano: a.tamano, personalidad: a.personalidad,
    necesidadesEspeciales: a.necesidadesEspeciales,
    estadoSanitario: a.estadoSanitario, estado: a.estado
  }
  await cargarImagenes(a.idAnimal)
  archivoNuevo.value = null
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

const limpiar = () => {
  editando.value = false
  form.value = formVacio()
  imagenes.value = []
  archivoNuevo.value = null
}

const onArchivoSeleccionado = (event) => {
  archivoNuevo.value = event.target.files[0] || null
}

const guardar = async () => {
  try {
    let idAnimal = form.value.id

    if (editando.value) {
      await animalService.actualizar(form.value)
    } else {
      const resp = await animalService.insertar(form.value)
      idAnimal = resp.data
    }

    if (archivoNuevo.value && idAnimal) {
      try {
        await animalService.subirImagen(idAnimal, archivoNuevo.value)
        mensaje.value = { tipo: 'ok', texto: 'Animal e imagen guardados correctamente.' }
      } catch (errImg) {

        mensaje.value = {
          tipo: 'error',
          texto: 'Animal guardado, pero falló la imagen: ' +
                 (errImg.response?.data?.error || errImg.message)
        }
      }
    } else {
      mensaje.value = {
        tipo: 'ok',
        texto: editando.value ? 'Animal actualizado.' : 'Animal creado.'
      }
    }

    limpiar()
    await cargar()
    await cargarEspecies() 
  } catch (e) {
    mensaje.value = {
      tipo: 'error',
      texto: 'Error al guardar: ' + (e.response?.data?.error || e.message)
    }
  }
}

const borrar = async (id) => {
  if (!confirm('¿Borrar este animal? Se borrarán también sus imágenes.')) return
  await animalService.borrar(id)
  await cargar()
  await cargarEspecies() 
}


const subirImagenExtra = async () => {
  if (!archivoNuevo.value || !form.value.id) {
    mensaje.value = { tipo: 'error', texto: 'Selecciona un archivo primero.' }
    return
  }
  try {
    await animalService.subirImagen(form.value.id, archivoNuevo.value)
    mensaje.value = { tipo: 'ok', texto: 'Imagen subida correctamente.' }
    archivoNuevo.value = null
    document.getElementById('archivo-input').value = ''
    await cargarImagenes(form.value.id)
    await cargar()
  } catch (e) {
    mensaje.value = {
      tipo: 'error',
      texto: 'Error al subir imagen: ' + (e.response?.data?.error || e.message)
    }
  }
}

const borrarImagen = async (idImagen) => {
  if (!confirm('¿Borrar esta imagen?')) return
  try {
    await animalService.borrarImagen(idImagen)
    await cargarImagenes(form.value.id)
    await cargar()
    mensaje.value = { tipo: 'ok', texto: 'Imagen eliminada.' }
  } catch (e) {
    mensaje.value = { tipo: 'error', texto: 'Error al borrar imagen.' }
  }
}
</script>

<template>
  <div class="container my-4">
    <h1 class="text-success"><i class="bi bi-house-heart"></i> Gestión de animales</h1>

    <div v-if="mensaje"
         :class="mensaje.tipo === 'ok' ? 'mensaje-ok' : 'mensaje-error'">
      {{ mensaje.texto }}
    </div>


    <div class="card my-4">
      <div class="card-header bg-success text-white">
        <i class="bi bi-plus-circle"></i>
        {{ editando ? `Editar animal #${form.id}` : 'Nuevo animal' }}
      </div>
      <div class="card-body">
        <form @submit.prevent="guardar" class="row g-3">
          <div class="col-md-4">
            <label class="form-label">Nombre *</label>
            <input v-model="form.nombre" class="form-control" required />
          </div>


          <div class="col-md-4">
            <label class="form-label">Especie *</label>
            <input v-model="form.especie" class="form-control" required
                   list="especies-existentes"
                   placeholder="Escribe o elige (Perro, Gato, Loro...)" />
            <datalist id="especies-existentes">
              <option v-for="esp in especiesDisponibles" :key="esp" :value="esp" />
            </datalist>
            <small class="text-muted">
              Puedes escribir una especie nueva o elegir una existente
            </small>
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

          <div class="col-md-8">
            <label class="form-label">
              <i class="bi bi-image"></i>
              {{ editando ? 'Añadir nueva imagen' : 'Imagen del animal (opcional)' }}
            </label>
            <input id="archivo-input" type="file" accept="image/*"
                   @change="onArchivoSeleccionado"
                   class="form-control" />
            <small class="text-muted">JPG, PNG o WEBP. Máx. 5 MB.</small>
          </div>

          <div class="col-12 d-flex gap-2">
            <button type="submit" class="btn-primario">
              <i class="bi bi-check"></i> {{ editando ? 'Actualizar' : 'Crear' }}
            </button>
            <button v-if="editando && archivoNuevo"
                    type="button" class="btn btn-info" @click="subirImagenExtra">
              <i class="bi bi-upload"></i> Subir solo la imagen
            </button>
            <button v-if="editando" type="button" class="btn btn-secondary" @click="limpiar">
              Cancelar
            </button>
          </div>
        </form>

        <div v-if="editando && imagenes.length > 0" class="mt-4">
          <h5><i class="bi bi-images"></i> Imágenes actuales</h5>
          <div class="galeria-imagenes">
            <div v-for="img in imagenes" :key="img.idImagen" class="img-item">
              <img :src="img.urlCompleta" :alt="`Imagen ${img.idImagen}`" />
              <button type="button"
                      class="btn btn-sm btn-danger btn-borrar-img"
                      @click="borrarImagen(img.idImagen)">
                <i class="bi bi-trash"></i>
              </button>
            </div>
          </div>
        </div>
        <div v-else-if="editando" class="mt-3 text-muted">
          <i class="bi bi-info-circle"></i> Este animal no tiene imágenes todavía.
        </div>
      </div>
    </div>

    <h3>Animales registrados</h3>
    <div v-if="cargando" class="text-center my-4">
      <div class="spinner-border text-success"></div>
    </div>
    <table v-else class="table table-hover">
      <thead class="table-success">
        <tr>
          <th>ID</th><th>Foto</th><th>Nombre</th><th>Especie</th>
          <th>Edad</th><th>Estado</th><th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="a in animales" :key="a.idAnimal">
          <td>{{ a.idAnimal }}</td>
          <td>
            <img v-if="a.imagenPrincipal"
                 :src="`http://localhost:8080/uploads/${a.imagenPrincipal}`"
                 :alt="a.nombre"
                 class="thumb" />
            <span v-else class="text-muted">—</span>
          </td>
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

<style scoped>
.thumb {
  width: 50px; height: 50px; object-fit: cover; border-radius: 6px;
}
.galeria-imagenes {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  gap: 12px;
  margin-top: 10px;
}
.img-item {
  position: relative;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 5px rgba(0,0,0,0.1);
}
.img-item img {
  width: 100%;
  height: 130px;
  object-fit: cover;
  display: block;
}
.btn-borrar-img {
  position: absolute;
  top: 5px;
  right: 5px;
  padding: 2px 8px;
  font-size: 0.8rem;
}
</style>