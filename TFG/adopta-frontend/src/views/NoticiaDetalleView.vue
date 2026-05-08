<script setup>
import { ref, watch, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { noticiaService } from '@/services/noticiaService'

const route = useRoute()
const router = useRouter()

const noticia = ref(null)
const cargando = ref(true)
const error = ref(null)

const cargar = async (id) => {
  cargando.value = true
  error.value = null
  try {
    const resp = await noticiaService.obtenerFicha(id)
    noticia.value = resp.data
  } catch (e) {
    error.value = 'No se pudo cargar la noticia.'
  } finally {
    cargando.value = false
  }
}

onMounted(() => cargar(route.params.id))

watch(() => route.params.id, (nuevoId) => {
  if (nuevoId) cargar(nuevoId)
})

const urlImagen = (imagen) => {
  if (imagen) return `http://localhost:8080/uploads/${imagen}`
  return '/img/Portada.jpeg'
}

const formatoFecha = (fecha) => {
  if (!fecha) return ''
  return new Date(fecha).toLocaleDateString('es-ES', {
    day: '2-digit', month: 'long', year: 'numeric',
    hour: '2-digit', minute: '2-digit'
  })
}
</script>

<template>
  <div v-if="cargando" class="text-center my-5">
    <div class="spinner-border text-success"></div>
  </div>

  <div v-else-if="error" class="container mt-4">
    <div class="mensaje-error">{{ error }}</div>
    <RouterLink to="/noticias" class="btn-primario">← Volver a noticias</RouterLink>
  </div>

  <article v-else-if="noticia" class="noticia-detalle">

    <header class="noticia-hero" :style="{ backgroundImage: `url(${urlImagen(noticia.imagen)})` }">
      <div class="noticia-hero-overlay">
        <div class="container">
          <RouterLink to="/noticias" class="btn-volver">
            <i class="bi bi-arrow-left"></i> Volver a noticias
          </RouterLink>
          <h1>{{ noticia.titulo }}</h1>
          <p v-if="noticia.subtitulo" class="subtitulo">{{ noticia.subtitulo }}</p>
          <div class="meta">
            <span v-if="noticia.autor">
              <i class="bi bi-person"></i> {{ noticia.autor }}
            </span>
            <span>
              <i class="bi bi-calendar"></i> {{ formatoFecha(noticia.fechaPublicacion) }}
            </span>
          </div>
        </div>
      </div>
    </header>

    <div class="container my-5">
      <div class="contenido-noticia">
        <p v-for="(parrafo, i) in noticia.contenido.split('\n')" :key="i">
          {{ parrafo }}
        </p>
      </div>

      <hr class="my-5" />

      <div class="text-center">
        <RouterLink to="/noticias" class="btn btn-outline-success me-2">
          ← Más noticias
        </RouterLink>
        <RouterLink to="/catalogo" class="btn-primario">
          <i class="bi bi-house-heart"></i> Ver catálogo
        </RouterLink>
      </div>
    </div>
  </article>
</template>

<style scoped>
.noticia-hero {
  height: 60vh;
  min-height: 400px;
  background-size: cover;
  background-position: center;
  position: relative;
}

.noticia-hero-overlay {
  background: linear-gradient(to bottom, rgba(0, 0, 0, 0.3) 0%, rgba(0, 0, 0, 0.85) 100%);
  height: 100%;
  display: flex;
  align-items: flex-end;
  padding-bottom: 40px;
  color: white;
}

.btn-volver {
  display: inline-block;
  color: white;
  margin-bottom: 20px;
  padding: 6px 15px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 20px;
}

.btn-volver:hover {
  background: rgba(255, 255, 255, 0.35);
  color: white;
  text-decoration: none;
}

.noticia-hero h1 {
  font-size: 3rem;
  margin-bottom: 10px;
}

.subtitulo {
  font-size: 1.3rem;
  font-style: italic;
  opacity: 0.95;
  margin-bottom: 15px;
}

.meta {
  display: flex;
  gap: 25px;
  font-size: 0.95rem;
  opacity: 0.9;
}

.meta i {
  margin-right: 5px;
}

.contenido-noticia {
  max-width: 800px;
  margin: 0 auto;
  font-size: 1.1rem;
  line-height: 1.8;
}

.contenido-noticia p {
  margin-bottom: 1.2rem;
}

@media (max-width: 768px) {
  .noticia-hero h1 {
    font-size: 2rem;
  }

  .noticia-hero {
    height: 50vh;
  }
}
</style>