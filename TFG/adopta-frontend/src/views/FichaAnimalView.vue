<script setup>
import { ref, watch, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { animalService } from '@/services/animalService'
import { authStore } from '@/stores/auth'

const route = useRoute()
const router = useRouter()

const animal = ref(null)
const cargando = ref(true)
const error = ref(null)

const cargarAnimal = async (id) => {
  cargando.value = true
  error.value = null
  try {
    const resp = await animalService.obtenerFicha(id)

    animal.value = resp.data
  } catch (e) {
    error.value = 'No se pudo cargar el animal.'
  } finally {
    cargando.value = false
  }
}

onMounted(() => cargarAnimal(route.params.id))

watch(() => route.params.id, (nuevoId) => {
  if (nuevoId) cargarAnimal(nuevoId)
})

const solicitarAdopcion = () => {
  if (!authStore.state.autenticado) {
    router.push({ name: 'Login', query: { redirect: route.fullPath } })
    return
  }
  router.push(`/iniciar-solicitud/${route.params.id}`)
}
</script>

<template>
  <div class="ficha-wrapper" v-if="!cargando && animal">
    <RouterLink to="/catalogo" class="volver">
      <i class="bi bi-arrow-left"></i> Volver al catálogo
    </RouterLink>

    <div class="ficha">
      <div class="ficha-img">
        <img :src="animal.imagenes && animal.imagenes.length > 0
          ? `http://localhost:8080/uploads/${animal.imagenes[0].urlImagen}`
          : '/img/perrera.jpg'" :alt="animal.nombre" />
      </div>

      <div class="ficha-info">
        <h1>{{ animal.nombre }}</h1>
        <span class="badge-estado" :class="animal.estado">{{ animal.estado }}</span>

        <div class="datos">
          <p><strong>Especie:</strong> {{ animal.especie }}</p>
          <p v-if="animal.edad"><strong>Edad:</strong> {{ animal.edad }} años</p>
          <p v-if="animal.tamano"><strong>Tamaño:</strong> {{ animal.tamano }}</p>
        </div>

        <div class="seccion">
          <h3>Personalidad</h3>
          <p>{{ animal.personalidad || '—' }}</p>
        </div>

        <div class="seccion">
          <h3>Necesidades especiales</h3>
          <p>{{ animal.necesidadesEspeciales || 'Ninguna' }}</p>
        </div>

        <div class="seccion">
          <h3>Estado sanitario</h3>
          <p>{{ animal.estadoSanitario || '—' }}</p>
        </div>

        <button v-if="animal.estado === 'DISPONIBLE'" @click="solicitarAdopcion" class="btn-primario btn-grande">
          <i class="bi bi-heart-fill"></i> Iniciar proceso de adopción
        </button>
        <p v-else class="alert alert-info">
          Este animal no está disponible para adopción en este momento.
        </p>
      </div>
    </div>
  </div>

  <div v-else-if="cargando" class="text-center my-5">
    <div class="spinner-border text-success"></div>
  </div>

  <div v-else class="mensaje-error">{{ error }}</div>
</template>

<style scoped>
.ficha-wrapper {
  padding: 20px;
  max-width: 1100px;
  margin: 0 auto;
}

.volver {
  display: inline-block;
  margin-bottom: 20px;
}

.ficha {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 30px;
  background: white;
  padding: 30px;
  border-radius: var(--radius);
  box-shadow: var(--sombra-suave);
}

.ficha-img img {
  width: 100%;
  border-radius: var(--radius);
}

.ficha-info h1 {
  color: var(--color-primario);
}

.badge-estado {
  display: inline-block;
  padding: 6px 14px;
  border-radius: 20px;
  color: white;
  font-size: 0.85rem;
  margin: 10px 0;
  font-weight: 600;
}

.badge-estado.DISPONIBLE {
  background: var(--color-success);
}

.badge-estado.RESERVADO {
  background: var(--color-warning);
}

.badge-estado.ADOPTADO {
  background: var(--color-secundario);
}

.datos {
  margin: 15px 0;
}

.seccion {
  margin-top: 20px;
}

.seccion h3 {
  color: var(--color-primario);
  font-size: 1.1rem;
  margin-bottom: 5px;
}

.btn-grande {
  font-size: 1.1rem;
  padding: 14px 28px;
  margin-top: 25px;
  width: 100%;
}

@media (max-width: 800px) {
  .ficha {
    grid-template-columns: 1fr;
  }
}
</style>