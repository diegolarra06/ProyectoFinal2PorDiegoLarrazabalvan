<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { noticiaService } from '@/services/noticiaService'

const router = useRouter()
const noticias = ref([])
const cargando = ref(true)

onMounted(async () => {
  try {
    const resp = await noticiaService.listarPublicadas()
    noticias.value = resp.data || []
  } finally {
    cargando.value = false
  }
})

const verNoticia = (id) => router.push(`/noticias/${id}`)

const urlImagen = (imagen) => {
  if (imagen) return `http://localhost:8080/uploads/${imagen}`
  return '/img/Portada.jpeg'
}

const formatoFecha = (fecha) => {
  if (!fecha) return ''
  return new Date(fecha).toLocaleDateString('es-ES', {
    day: '2-digit', month: 'long', year: 'numeric'
  })
}
</script>

<template>
  <div class="container my-4">
    <h1 class="text-success mb-4">
      <i class="bi bi-newspaper"></i> Noticias
    </h1>

    <div v-if="cargando" class="text-center my-4">
      <div class="spinner-border text-success"></div>
    </div>

    <div v-else-if="noticias.length === 0" class="alert alert-info">
      No hay noticias publicadas en este momento.
    </div>

    <div v-else class="row">
      <div v-for="n in noticias" :key="n.idNoticia" class="col-md-6 col-lg-4 mb-4">
        <div class="card h-100 noticia-card" @click="verNoticia(n.idNoticia)">
          <img :src="urlImagen(n.imagen)" class="card-img-top" :alt="n.titulo" />
          <div class="card-body d-flex flex-column">
            <h5 class="card-title text-success">{{ n.titulo }}</h5>
            <p v-if="n.subtitulo" class="text-muted small">{{ n.subtitulo }}</p>
            <p class="card-text">{{ n.contenido.length > 120
              ? n.contenido.slice(0, 120) + '…'
              : n.contenido }}</p>
            <div class="mt-auto d-flex justify-content-between align-items-center">
              <small class="text-muted">{{ formatoFecha(n.fechaPublicacion) }}</small>
              <span class="text-success">
                Leer más <i class="bi bi-arrow-right"></i>
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.noticia-card {
  cursor: pointer;
  transition: all 0.3s ease;
  border: none;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.noticia-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.15);
}

.noticia-card img {
  height: 200px;
  object-fit: cover;
}
</style>