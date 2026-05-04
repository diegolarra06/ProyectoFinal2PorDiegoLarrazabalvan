<!--
  ===========================================================
  CATÁLOGO DE ANIMALES (descripción 2.3.1)
  - Listado con paginación (PDF 6.3 backend)
  - Filtros por especie, edad, tamaño, estado, nombre (2.3.1.5-2.3.1.9)
  - Acceso a la ficha individual (2.3.1.10)
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

// Favoritos (sólo si está logueado)
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

// Carga el listado paginado
const cargar = async () => {
  cargando.value = true
  try {
    const resp = await animalService.listarPaginado(paginaActual.value, tamanoPagina.value)
    animales.value = resp.data.content || []
    totalPaginas.value = resp.data.totalPages
    totalElementos.value = resp.data.totalElements
  } catch (e) {
    console.error('Error cargando animales', e)
  } finally {
    cargando.value = false
  }
}

// Carga los favoritos del usuario para marcar las tarjetas
const cargarFavoritos = async () => {
  if (!authStore.state.autenticado) return
  try {
    const resp = await favoritoService.listar()
    // El backend devuelve HTML o JSON según endpoint;
    // aquí asumimos JSON con lista. Si te devuelve HTML, ignora.
    if (Array.isArray(resp.data)) {
      idsFavoritos.value = new Set(resp.data.map(f => f.idAnimal))
    }
  } catch (e) {
    // si el endpoint devuelve la vista entera (HTML), no pasa nada
  }
}

onMounted(async () => {
  await cargar()
  await cargarFavoritos()
})

// Cambia de página
const cambiarPagina = (n) => {
  paginaActual.value = n
}

// Tema 10 PDF - watcher: al cambiar la página, recargamos
watch(paginaActual, cargar)

// Aplicar filtros (busca con POST como hace el controlador del back)
const aplicarFiltros = async () => {
  cargando.value = true
  try {
    // Para la búsqueda con filtros usamos el endpoint /animales/catalogo
    // que devuelve HTML, así que mejor usamos el endpoint REST paginado
    // y filtramos en cliente para el ejemplo.
    paginaActual.value = 0
    await cargar()
    // Filtrado simple en cliente:
    if (filtros.value.nombre) {
      animales.value = animales.value.filter(a =>
        a.nombre.toLowerCase().includes(filtros.value.nombre.toLowerCase())
      )
    }
    if (filtros.value.especie) {
      animales.value = animales.value.filter(a => a.especie === filtros.value.especie)
    }
    if (filtros.value.estado) {
      animales.value = animales.value.filter(a => a.estado === filtros.value.estado)
    }
    if (filtros.value.tamano) {
      animales.value = animales.value.filter(a => a.tamano === filtros.value.tamano)
    }
  } finally {
    cargando.value = false
  }
}

// Ir a la ficha
const verFicha = (id) => router.push(`/animal/${id}`)

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
  } catch (e) {
    console.error(e)
  }
}
</script>

<template>
  <div class="catalogo">
    <h1 class="titulo-pagina">
      <i class="bi bi-house-heart"></i> Catálogo de animales
    </h1>

    <!-- Filtros (2.3.1.5 a 2.3.1.9) -->
    <section class="filtros">
      <h3>Filtrar</h3>
      <form @submit.prevent="aplicarFiltros" class="row g-2">
        <div class="col-md-3">
          <input v-model="filtros.nombre" type="text" class="form-control"
                 placeholder="Nombre…" />
        </div>
        <div class="col-md-2">
          <select v-model="filtros.especie" class="form-select">
            <option value="">Todas las especies</option>
            <option value="Perro">Perro</option>
            <option value="Gato">Gato</option>
            <option value="Conejo">Conejo</option>
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
        </div>
      </form>
    </section>

    <!-- Resultados -->
    <p class="resumen">
      Mostrando {{ animales.length }} de {{ totalElementos }} animales
    </p>

    <div v-if="cargando" class="text-center my-4">
      <div class="spinner-border text-success"></div>
    </div>

    <div v-else-if="animales.length === 0" class="alert alert-warning">
      No hay animales que coincidan con los filtros.
    </div>

    <div v-else class="grid-animales">
      <TarjetaAnimal v-for="a in animales"
                     :key="a.idAnimal"
                     :animal="a"
                     :es-favorito="idsFavoritos.has(a.idAnimal)"
                     :mostrar-boton-favorito="authStore.state.autenticado"
                     @ver-ficha="verFicha"
                     @toggle-favorito="toggleFavorito" />
    </div>

    <!-- Paginación -->
    <nav v-if="totalPaginas > 1" class="paginacion">
      <button class="btn-pag" :disabled="paginaActual === 0"
              @click="cambiarPagina(paginaActual - 1)">
        <i class="bi bi-chevron-left"></i> Anterior
      </button>
      <span class="info-pag">Página {{ paginaActual + 1 }} de {{ totalPaginas }}</span>
      <button class="btn-pag" :disabled="paginaActual + 1 >= totalPaginas"
              @click="cambiarPagina(paginaActual + 1)">
        Siguiente <i class="bi bi-chevron-right"></i>
      </button>
    </nav>
  </div>
</template>

<style scoped>
.catalogo { padding: 20px; max-width: 1200px; margin: 0 auto; }
.titulo-pagina { color: var(--color-primario); margin-bottom: 20px; }
.filtros { background: white; padding: 20px; border-radius: var(--radius);
           box-shadow: var(--sombra-suave); margin-bottom: 20px; }
.filtros h3 { color: var(--color-primario); margin-bottom: 15px; }
.resumen { color: #666; }
.grid-animales {
  display: grid; grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 20px; margin-bottom: 30px;
}
.paginacion {
  display: flex; justify-content: center; align-items: center;
  gap: 20px; margin: 30px 0;
}
.btn-pag {
  padding: 10px 20px; background: var(--color-primario); color: white;
  border: none; border-radius: var(--radius); cursor: pointer;
}
.btn-pag:disabled { opacity: 0.5; cursor: not-allowed; }
</style>