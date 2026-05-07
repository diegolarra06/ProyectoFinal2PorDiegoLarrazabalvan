<!--
  ===========================================================
  PÁGINA DE INICIO (descripción 2.3)
  - Hero con imagen
  - Resumen de animales disponibles
  - Acceso a registro/login y al catálogo
  ===========================================================
-->
<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import TarjetaAnimal from '@/components/TarjetaAnimal.vue'
import { animalService } from '@/services/animalService'

const router = useRouter()
const animalesDestacados = ref([])
const cargando = ref(true)
const error = ref(null)

// 🆕 Noticias (mock)
const noticias = ref([
  {
    titulo: 'Nueva campaña de adopción',
    fecha: '01/05/2026',
    texto: 'Este mes lanzamos una campaña especial para fomentar la adopción responsable.',
    img: '/img/lluvia2.jpg'
  },
  {
    titulo: 'Jornada de puertas abiertas',
    fecha: '28/04/2026',
    texto: 'Ven a conocer a nuestros animales este fin de semana.',
    img: '/img/lluvia1.jpg'
  },
  {
    titulo: 'Consejos para nuevos adoptantes',
    fecha: '20/04/2026',
    texto: 'Te damos las claves para cuidar a tu nueva mascota.',
    img: '/img/lluvia3.jpg'
  }
])

// Tema 9 PDF - onMounted
onMounted(async () => {
  try {
    const resp = await animalService.listarPaginado(0, 4)
    animalesDestacados.value = resp.data.content || []
  } catch (e) {
    error.value = 'No se pudieron cargar los animales destacados.'
    console.error(e)
  } finally {
    cargando.value = false
  }
})

const verFicha = (id) => router.push(`/animal/${id}`)
</script>

<template>
  <!-- Hero -->
  <section class="hero">
    <div class="hero-content">
      <h1>Adopta Un Compañero</h1>
      <p>¡Haz una diferencia en la vida de un animal hoy mismo!</p>
      <RouterLink to="/catalogo" class="btn btn-light btn-lg mt-3">
        <i class="bi bi-search"></i> Ver catálogo
      </RouterLink>
    </div>
  </section>

  <div class="contenedor">
    <main class="contenedor-central">
      
      <h2>¡Adopta y cambia una vida!</h2>
      <p>
        Adoptar un animal no solo cambia la vida del animal, sino también la tuya.
        Miles de perros, gatos y otros animales esperan encontrar un hogar amoroso.
      </p>

      <h2>Beneficios de la adopción</h2>
      <ul>
        <li>Reduces el número de animales en refugios.</li>
        <li>Recibes un compañero leal y agradecido.</li>
        <li>Contribuyes a disminuir la cría irresponsable.</li>
        <li>Las mascotas adoptadas suelen estar vacunadas y esterilizadas.</li>
      </ul>

      <!-- Animales destacados -->
      <h2>Animales destacados</h2>

      <div v-if="cargando" class="text-center my-4">
        <div class="spinner-border text-success"></div>
        <p>Cargando animales…</p>
      </div>

      <div v-else-if="error" class="mensaje-error">{{ error }}</div>

      <div v-else class="grid-animales">
        <TarjetaAnimal
          v-for="a in animalesDestacados"
          :key="a.idAnimal"
          :animal="a"
          @ver-ficha="verFicha"
        />
      </div>

      <div class="text-center mt-4">
        <RouterLink to="/catalogo" class="btn-primario">
          Ver catálogo completo →
        </RouterLink>
      </div>

      <!-- 🆕 Noticias -->
      <h2 class="mt-5">Noticias destacadas</h2>

      <div class="container my-4">
        <div class="row mt-3">
          <div v-for="(n, i) in noticias" :key="i" class="col-md-4 mb-3">
            <div class="card h-100 shadow-sm">
              <img
                :src="n.img"
                class="card-img-top"
                :alt="n.titulo"
                style="height:200px; object-fit:cover;"
              />
              <div class="card-body">
                <h5 class="card-title">{{ n.titulo }}</h5>
                <p class="text-muted small">{{ n.fecha }}</p>
                <p>{{ n.texto }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>

    </main>
  </div>
</template>

<style scoped>

.grid-animales {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 20px;
  margin: 20px 0;
}
</style>