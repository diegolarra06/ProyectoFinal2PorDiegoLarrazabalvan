<!--
  ===========================================================
  CATÁLOGO DE ANIMALES
  - Listado con paginación
  - Filtros por nombre, especie, tamaño y estado
  - Acceso a ficha individual
  ===========================================================
-->
<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import TarjetaAnimal from '@/components/TarjetaAnimal.vue'
import { animalService } from '@/services/animalService'
import { favoritoService } from '@/services/favoritoService'
import { authStore } from '@/stores/auth'

const router = useRouter()

// Estado del listado
const animales = ref([])
const paginaActual = ref(0)
const totalPaginas = ref(0)
const totalElementos = ref(0)
const tamanoPagina = ref(8)
const cargando = ref(false)
const error = ref(null)

// Especies para el desplegable
const especiesDisponibles = ref([])

// Favoritos
const idsFavoritos = ref(new Set())

// Filtros
const filtros = ref({
  nombre: '',
  especie: '',
  edadMin: null,
  edadMax: null,
  tamano: '',
  estado: ''
})

const hayFiltrosActivos = () => {
  return Boolean(
    filtros.value.nombre ||
    filtros.value.especie ||
    filtros.value.edadMin ||
    filtros.value.edadMax ||
    filtros.value.tamano ||
    filtros.value.estado
  )
}

// Cargar especies desde /api/animales/especies
const cargarEspecies = async () => {
  try {
    const resp = await animalService.listarEspecies()
    especiesDisponibles.value = Array.isArray(resp.data) ? resp.data : []
  } catch (e) {
    console.error('Error cargando especies', e)
    especiesDisponibles.value = []
  }
}

// Carga listado paginado normal desde /api/animales
const cargar = async () => {
  cargando.value = true
  error.value = null

  try {
    const resp = await animalService.listarPaginado(
      paginaActual.value,
      tamanoPagina.value,
      'idAnimal'
    )

    animales.value = resp.data.content || []
    totalPaginas.value = resp.data.totalPages || 0
    totalElementos.value = resp.data.totalElements || 0
  } catch (e) {
    console.error('Error cargando animales', e)
    error.value = 'No se pudieron cargar los animales.'
    animales.value = []
    totalPaginas.value = 0
    totalElementos.value = 0
  } finally {
    cargando.value = false
  }
}

// Cargar favoritos del usuario
const cargarFavoritos = async () => {
  if (!authStore.state.autenticado) return

  try {
    const resp = await favoritoService.listar()

    if (Array.isArray(resp.data)) {
      idsFavoritos.value = new Set(
        resp.data.map(f => f.idAnimal || f.animal?.idAnimal)
      )
    }
  } catch (e) {
    console.warn('No se pudieron cargar favoritos', e)
  }
}

// Aplicar filtros usando /api/animales/buscar
const aplicarFiltros = async () => {
  cargando.value = true
  error.value = null
  paginaActual.value = 0

  try {
    const params = {}

    if (filtros.value.nombre) params.nombre = filtros.value.nombre
    if (filtros.value.especie) params.especie = filtros.value.especie
    if (filtros.value.edadMin) params.edadMin = filtros.value.edadMin
    if (filtros.value.edadMax) params.edadMax = filtros.value.edadMax
    if (filtros.value.tamano) params.tamano = filtros.value.tamano
    if (filtros.value.estado) params.estado = filtros.value.estado

    const resp = await animalService.buscarConFiltros(params)

    animales.value = Array.isArray(resp.data) ? resp.data : []
    totalElementos.value = animales.value.length
    totalPaginas.value = 1
  } catch (e) {
    console.error('Error aplicando filtros', e)
    error.value = 'No se pudieron aplicar los filtros.'
    animales.value = []
    totalElementos.value = 0
    totalPaginas.value = 0
  } finally {
    cargando.value = false
  }
}

// Limpiar filtros y volver al listado paginado
const limpiarFiltros = async () => {
  filtros.value = {
    nombre: '',
    especie: '',
    edadMin: null,
    edadMax: null,
    tamano: '',
    estado: ''
  }

  paginaActual.value = 0
  await cargar()
}

// Cambiar página
const cambiarPagina = (n) => {
  paginaActual.value = n
}

// Si no hay filtros activos, al cambiar página recarga paginado
watch(paginaActual, async () => {
  if (!hayFiltrosActivos()) {
    await cargar()
  }
})

// Ir a ficha
const verFicha = (id) => {
  router.push(`/animal/${id}`)
}

// Toggle favorito
const toggleFavorito = async (idAnimal) => {
  if (!authStore.state.autenticado) {
    router.push({ name: 'Login', query: { redirect: '/catalogo' } })
    return
  }

  try {
    if (idsFavoritos.value.has(idAnimal)) {
      await favoritoService.quitar(idAnimal)
      idsFavoritos.value.delete(idAnimal)
    } else {
      await favoritoService.agregar(idAnimal)
      idsFavoritos.value.add(idAnimal)
    }

    idsFavoritos.value = new Set(idsFavoritos.value)
  } catch (e) {
    console.error('Error cambiando favorito', e)
  }
}

onMounted(async () => {
  await cargarEspecies()
  await cargar()
  await cargarFavoritos()
})
</script>

<template>
  <div class="catalogo">
    <h1 class="titulo-pagina">
      <i class="bi bi-house-heart"></i> Catálogo de animales
    </h1>

    <!-- Filtros -->
    <section class="filtros">
      <h3>Filtrar</h3>

      <form @submit.prevent="aplicarFiltros" class="row g-2">
        <div class="col-md-3">
          <input
            v-model="filtros.nombre"
            type="text"
            class="form-control"
            placeholder="Nombre…"
          />
        </div>

        <div class="col-md-2">
          <select v-model="filtros.especie" class="form-select">
            <option value="">Todas las especies</option>
            <option
              v-for="esp in especiesDisponibles"
              :key="esp"
              :value="esp"
            >
              {{ esp }}
            </option>
          </select>
        </div>

        <div class="col-md-2">
          <select v-model="filtros.tamano" class="form-select">
            <option value="">Todos los tamaños</option>
            <option value="Pequeño">Pequeño</option>
            <option value="Mediano">Mediano</option>
            <option value="Grande">Grande</option>
          </select>
        </div>

        <div class="col-md-2">
          <select v-model="filtros.estado" class="form-select">
            <option value="">Todos los estados</option>
            <option value="DISPONIBLE">Disponible</option>
            <option value="RESERVADO">Reservado</option>
            <option value="ADOPTADO">Adoptado</option>
          </select>
        </div>

        <div class="col-md-3 d-flex gap-2">
          <button type="submit" class="btn-primario flex-fill">
            <i class="bi bi-search"></i> Buscar
          </button>

          <button
            type="button"
            class="btn btn-secondary"
            @click="limpiarFiltros"
          >
            Limpiar
          </button>
        </div>
      </form>
    </section>

    <!-- Resumen -->
    <p class="resumen">
      Mostrando {{ animales.length }} de {{ totalElementos }} animales
    </p>

    <div v-if="error" class="alert alert-danger">
      {{ error }}
    </div>

    <div v-if="cargando" class="text-center my-4">
      <div class="spinner-border text-success"></div>
    </div>

    <div v-else-if="animales.length === 0" class="alert alert-warning">
      No hay animales que coincidan con los filtros.
    </div>

    <div v-else class="grid-animales">
      <TarjetaAnimal
        v-for="a in animales"
        :key="a.idAnimal"
        :animal="a"
        :es-favorito="idsFavoritos.has(a.idAnimal)"
        :mostrar-boton-favorito="authStore.state.autenticado"
        @ver-ficha="verFicha"
        @toggle-favorito="toggleFavorito"
      />
    </div>

    <!-- Paginación -->
    <nav
      v-if="totalPaginas > 1 && !hayFiltrosActivos()"
      class="paginacion"
    >
      <button
        class="btn-pag"
        :disabled="paginaActual === 0"
        @click="cambiarPagina(paginaActual - 1)"
      >
        <i class="bi bi-chevron-left"></i> Anterior
      </button>

      <span class="info-pag">
        Página {{ paginaActual + 1 }} de {{ totalPaginas }}
      </span>

      <button
        class="btn-pag"
        :disabled="paginaActual + 1 >= totalPaginas"
        @click="cambiarPagina(paginaActual + 1)"
      >
        Siguiente <i class="bi bi-chevron-right"></i>
      </button>
    </nav>
  </div>
</template>

<style scoped>
.catalogo {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.titulo-pagina {
  color: var(--color-primario);
  margin-bottom: 20px;
}

.filtros {
  background: white;
  padding: 20px;
  border-radius: var(--radius);
  box-shadow: var(--sombra-suave);
  margin-bottom: 20px;
}

.filtros h3 {
  color: var(--color-primario);
  margin-bottom: 15px;
}

.resumen {
  color: #666;
}

.grid-animales {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.paginacion {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  margin: 30px 0;
}

.btn-pag {
  padding: 10px 20px;
  background: var(--color-primario);
  color: white;
  border: none;
  border-radius: var(--radius);
  cursor: pointer;
}

.btn-pag:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style>