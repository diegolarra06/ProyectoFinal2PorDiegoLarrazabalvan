<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import TarjetaAnimal from '@/components/TarjetaAnimal.vue'
import { animalService } from '@/services/animalService'
import { noticiaService } from '@/services/noticiaService'

const router = useRouter()

const animalesDestacados = ref([])
const noticiasDestacadas = ref([])
const cargando = ref(true)
const error = ref(null)

onMounted(async () => {
  try {
    const [respAnimales, respNoticias] = await Promise.all([
      animalService.listarPaginado(0, 4),
      noticiaService.listarDestacadas(5)
    ])
    animalesDestacados.value = respAnimales.data.content || []
    noticiasDestacadas.value = respNoticias.data || []
  } catch (e) {
    error.value = 'No se pudieron cargar los datos.'
    console.error(e)
  } finally {
    cargando.value = false
  }
})

const verFicha = (id) => router.push(`/animal/${id}`)
const verNoticia = (id) => router.push(`/noticias/${id}`)

const urlImagenNoticia = (imagen) => {
  if (imagen) return `http://localhost:8080/uploads/${imagen}`
  return '/img/Portada.jpeg'
}


const recortar = (texto, max = 120) => {
  if (!texto) return ''
  return texto.length > max ? texto.slice(0, max) + '…' : texto
}
</script>

<template>

  <section class="hero">
    <div class="hero-content">
      <h1>Adopta Un Compañero</h1>
      <p>¡Haz una diferencia en la vida de un animal hoy mismo!</p>
      <RouterLink to="/catalogo" class="btn btn-light btn-lg mt-3">
        <i class="bi bi-search"></i> Ver catálogo
      </RouterLink>
    </div>
  </section>

  <div class="container my-5">

    <section v-if="noticiasDestacadas.length > 0" class="seccion-noticias">
      <h2 class="text-center text-success mb-4">
        <i class="bi bi-newspaper"></i> Últimas noticias
      </h2>

      <div id="carruselNoticias" class="carousel slide" data-bs-ride="carousel">

        <div class="carousel-indicators">
          <button v-for="(n, i) in noticiasDestacadas" :key="`ind-${n.idNoticia}`" type="button"
            data-bs-target="#carruselNoticias" :data-bs-slide-to="i" :class="{ active: i === 0 }"
            :aria-label="`Slide ${i + 1}`"></button>
        </div>

        <div class="carousel-inner rounded-4 shadow">
          <div v-for="(n, i) in noticiasDestacadas" :key="n.idNoticia" class="carousel-item"
            :class="{ active: i === 0 }">
            <div class="slide-noticia" :style="{ backgroundImage: `url(${urlImagenNoticia(n.imagen)})` }">
              <div class="slide-overlay">
                <h3>{{ n.titulo }}</h3>
                <p v-if="n.subtitulo" class="subtitulo">{{ n.subtitulo }}</p>
                <p>{{ recortar(n.contenido, 200) }}</p>
                <button class="btn btn-light btn-lg" @click="verNoticia(n.idNoticia)">
                  <i class="bi bi-arrow-right"></i> Leer noticia completa
                </button>
              </div>
            </div>
          </div>
        </div>

        <button class="carousel-control-prev" type="button" data-bs-target="#carruselNoticias" data-bs-slide="prev">
          <span class="carousel-control-prev-icon"></span>
          <span class="visually-hidden">Anterior</span>
        </button>
        <button class="carousel-control-next" type="button" data-bs-target="#carruselNoticias" data-bs-slide="next">
          <span class="carousel-control-next-icon"></span>
          <span class="visually-hidden">Siguiente</span>
        </button>
      </div>

      <div class="text-center mt-3">
        <RouterLink to="/noticias" class="btn btn-outline-success">
          Ver todas las noticias →
        </RouterLink>
      </div>
    </section>

    <section class="my-5">
      <h2 class="text-center text-success">¡Adopta y cambia una vida!</h2>
      <p class="lead text-center">
        Adoptar un animal no solo cambia su vida, sino también la tuya.
      </p>
      <div class="row mt-4">
        <div class="col-md-6">
          <h4 class="text-success">Beneficios de adoptar</h4>
          <ul>
            <li>Reduces el número de animales en refugios.</li>
            <li>Recibes un compañero leal y agradecido.</li>
            <li>Contribuyes a disminuir la cría irresponsable.</li>
            <li>Las mascotas adoptadas suelen estar vacunadas y esterilizadas.</li>
          </ul>
        </div>
        <div class="col-md-6">
          <h4 class="text-success">¿Cómo adoptar?</h4>
          <ol>
            <li>Visita el catálogo y elige tu compañero.</li>
            <li>Inicia una solicitud desde su ficha.</li>
            <li>Espera la revisión del centro.</li>
            <li>Acude a la cita y llévate a tu nuevo amigo.</li>
          </ol>
        </div>
      </div>
    </section>

    <section class="my-5">
      <h2 class="text-center text-success mb-4">Animales destacados</h2>

      <div v-if="cargando" class="text-center">
        <div class="spinner-border text-success"></div>
      </div>

      <div v-else-if="error" class="mensaje-error">{{ error }}</div>

      <div v-else class="grid-animales">
        <TarjetaAnimal v-for="a in animalesDestacados" :key="a.idAnimal" :animal="a" @ver-ficha="verFicha" />
      </div>

      <div class="text-center mt-4">
        <RouterLink to="/catalogo" class="btn-primario">
          Ver catálogo completo →
        </RouterLink>
      </div>
    </section>

  </div>
</template>

<style scoped>
.seccion-noticias {
  margin: 30px 0;
}

.carousel-inner {
  overflow: hidden;
}

.slide-noticia {
  height: 450px;
  background-size: cover;
  background-position: center;
  position: relative;
  display: flex;
  align-items: flex-end;
}

.slide-overlay {
  background: linear-gradient(to top, rgba(0, 0, 0, 0.85), rgba(0, 0, 0, 0.2));
  color: white;
  padding: 40px;
  width: 100%;
}

.slide-overlay h3 {
  font-size: 2.2rem;
  margin-bottom: 10px;
}

.slide-overlay .subtitulo {
  font-size: 1.2rem;
  opacity: 0.9;
  margin-bottom: 8px;
  font-style: italic;
}

.slide-overlay p {
  margin-bottom: 15px;
  max-width: 800px;
}

.grid-animales {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 20px;
  margin: 20px 0;
}

@media (max-width: 768px) {
  .slide-noticia {
    height: 350px;
  }

  .slide-overlay h3 {
    font-size: 1.5rem;
  }

  .slide-overlay {
    padding: 20px;
  }
}
</style>