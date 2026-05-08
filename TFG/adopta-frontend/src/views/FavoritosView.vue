<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import TarjetaAnimal from '@/components/TarjetaAnimal.vue'
import { favoritoService } from '@/services/favoritoService'

const router = useRouter()
const favoritos = ref([])
const cargando = ref(true)

const cargar = async () => {
  cargando.value = true
  try {
    const resp = await favoritoService.listar()
    favoritos.value = Array.isArray(resp.data)
      ? resp.data.map(f => ({
        idAnimal: f.idAnimal,
        nombre: f.nombreAnimal,
        especie: f.especie,
        estado: f.estado,
        imagenPrincipal: f.imagenPrincipal
      }))
      : []
  } catch (e) {
    console.error(e)
  } finally {
    cargando.value = false
  }
}

onMounted(cargar)

const verFicha = (id) => router.push(`/animal/${id}`)

const quitar = async (idAnimal) => {
  await favoritoService.quitar(idAnimal)
  await cargar()
}
</script>

<template>
  <div class="container my-4">
    <h1 class="text-success">
      <i class="bi bi-heart-fill"></i> Mis favoritos
    </h1>

    <div v-if="cargando" class="text-center my-4">
      <div class="spinner-border text-success"></div>
    </div>

    <div v-else-if="favoritos.length === 0" class="alert alert-info mt-3">
      Aún no has marcado ningún animal como favorito.
      <RouterLink to="/catalogo">Ir al catálogo →</RouterLink>
    </div>

    <div v-else class="grid-animales mt-3">
      <TarjetaAnimal v-for="a in favoritos" :key="a.idAnimal" :animal="a" :es-favorito="true"
        :mostrar-boton-favorito="true" @ver-ficha="verFicha" @toggle-favorito="quitar" />
    </div>
  </div>
</template>

<style scoped>
.grid-animales {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 20px;
}
</style>