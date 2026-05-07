<!--
  ===========================================================
  GESTIÓN DE NOTICIAS (admin)
  CRUD completo + subida de imagen
  ===========================================================
-->
<script setup>
import { ref, onMounted } from 'vue'
import { noticiaService } from '@/services/noticiaService'

const noticias = ref([])
const cargando = ref(false)
const mensaje = ref(null)

const editando = ref(false)
const form = ref(formVacio())
const archivoNuevo = ref(null)

function formVacio() {
  return {
    id: null, titulo: '', subtitulo: '', contenido: '',
    autor: '', publicada: true
  }
}

const cargar = async () => {
  cargando.value = true
  try {
    const resp = await noticiaService.listarTodasAdmin()
    noticias.value = resp.data || []
  } finally { cargando.value = false }
}

onMounted(cargar)

const editar = (n) => {
  editando.value = true
  form.value = {
    id: n.idNoticia,
    titulo: n.titulo, subtitulo: n.subtitulo, contenido: n.contenido,
    autor: n.autor, publicada: n.publicada
  }
  archivoNuevo.value = null
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

const limpiar = () => {
  editando.value = false
  form.value = formVacio()
  archivoNuevo.value = null
  const input = document.getElementById('archivo-noticia')
  if (input) input.value = ''
}

const onArchivoSeleccionado = (event) => {
  archivoNuevo.value = event.target.files[0] || null
}

const guardar = async () => {
  try {
    let idNoticia = form.value.id

    if (editando.value) {
      await noticiaService.actualizar(idNoticia, form.value)
    } else {
      const resp = await noticiaService.crear(form.value)
      idNoticia = resp.data
    }

    if (archivoNuevo.value && idNoticia) {
      await noticiaService.subirImagen(idNoticia, archivoNuevo.value)
    }

    mensaje.value = {
      tipo: 'ok',
      texto: editando.value ? 'Noticia actualizada.' : 'Noticia creada.'
    }
    limpiar()
    await cargar()
  } catch (e) {
    mensaje.value = {
      tipo: 'error',
      texto: 'Error al guardar: ' + (e.response?.data?.error || e.message)
    }
  }
}

const borrar = async (id) => {
  if (!confirm('¿Borrar esta noticia?')) return
  await noticiaService.borrar(id)
  await cargar()
}

const formatoFecha = (fecha) => {
  if (!fecha) return ''
  return new Date(fecha).toLocaleDateString('es-ES')
}
</script>

<template>
  <div class="container my-4">
    <h1 class="text-success">
      <i class="bi bi-newspaper"></i> Gestión de noticias
    </h1>

    <div v-if="mensaje"
         :class="mensaje.tipo === 'ok' ? 'mensaje-ok' : 'mensaje-error'">
      {{ mensaje.texto }}
    </div>

    <!-- Formulario -->
    <div class="card my-4">
      <div class="card-header bg-success text-white">
        <i class="bi bi-plus-circle"></i>
        {{ editando ? `Editar noticia #${form.id}` : 'Nueva noticia' }}
      </div>
      <div class="card-body">
        <form @submit.prevent="guardar" class="row g-3">
          <div class="col-md-12">
            <label class="form-label">Título *</label>
            <input v-model="form.titulo" class="form-control" required maxlength="200" />
          </div>
          <div class="col-md-12">
            <label class="form-label">Subtítulo</label>
            <input v-model="form.subtitulo" class="form-control" maxlength="300" />
          </div>
          <div class="col-md-12">
            <label class="form-label">Contenido *</label>
            <textarea v-model="form.contenido" class="form-control" rows="8" required
                      placeholder="Escribe el contenido de la noticia. Usa Enter para separar párrafos."></textarea>
          </div>
          <div class="col-md-6">
            <label class="form-label">Autor</label>
            <input v-model="form.autor" class="form-control" />
          </div>
          <div class="col-md-3 d-flex align-items-end">
            <div class="form-check">
              <input v-model="form.publicada" type="checkbox"
                     class="form-check-input" id="publicada" />
              <label for="publicada" class="form-check-label">Publicada</label>
            </div>
          </div>

          <div class="col-md-12">
            <label class="form-label">
              <i class="bi bi-image"></i> Imagen de la noticia
            </label>
            <input id="archivo-noticia" type="file" accept="image/*"
                   @change="onArchivoSeleccionado"
                   class="form-control" />
            <small class="text-muted">JPG, PNG o WEBP. Máx. 5 MB.</small>
          </div>

          <div class="col-12 d-flex gap-2">
            <button type="submit" class="btn-primario">
              <i class="bi bi-check"></i> {{ editando ? 'Actualizar' : 'Crear' }}
            </button>
            <button v-if="editando" type="button"
                    class="btn btn-secondary" @click="limpiar">
              Cancelar
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- Listado -->
    <h3>Noticias registradas</h3>
    <div v-if="cargando" class="text-center my-4">
      <div class="spinner-border text-success"></div>
    </div>
    <table v-else class="table table-hover">
      <thead class="table-success">
        <tr>
          <th>ID</th><th>Imagen</th><th>Título</th>
          <th>Autor</th><th>Fecha</th><th>Estado</th><th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="noticias.length === 0">
          <td colspan="7" class="text-center text-muted">
            Aún no hay noticias. Crea la primera arriba.
          </td>
        </tr>
        <tr v-for="n in noticias" :key="n.idNoticia">
          <td>{{ n.idNoticia }}</td>
          <td>
            <img v-if="n.imagen"
                 :src="`http://localhost:8080/uploads/${n.imagen}`"
                 class="thumb" :alt="n.titulo" />
            <span v-else class="text-muted">—</span>
          </td>
          <td>
            <strong>{{ n.titulo }}</strong>
            <br><small class="text-muted">{{ n.subtitulo }}</small>
          </td>
          <td>{{ n.autor || '—' }}</td>
          <td>{{ formatoFecha(n.fechaPublicacion) }}</td>
          <td>
            <span :class="n.publicada
                          ? 'badge bg-success'
                          : 'badge bg-secondary'">
              {{ n.publicada ? 'Publicada' : 'Borrador' }}
            </span>
          </td>
          <td>
            <button class="btn btn-sm btn-outline-primary me-1" @click="editar(n)">
              <i class="bi bi-pencil"></i>
            </button>
            <button class="btn btn-sm btn-outline-danger" @click="borrar(n.idNoticia)">
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
  width: 60px; height: 45px; object-fit: cover; border-radius: 4px;
}
</style>