<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import TablaSolicitudes from '@/components/TablaSolicitudes.vue'
import { solicitudService } from '@/services/solicitudService'

const router = useRouter()
const solicitudes = ref([])
const cargando = ref(true)

const cargar = async () => {
  cargando.value = true
  try {
    const resp = await solicitudService.listarMisSolicitudes()
    solicitudes.value = Array.isArray(resp.data) ? resp.data : []
  } catch (e) {
    console.error(e)
  } finally {
    cargando.value = false
  }
}

onMounted(cargar)

const cancelar = async (idSolicitud) => {
  if (!confirm('¿Seguro que quieres cancelar esta solicitud?')) return
  try {
    await solicitudService.cancelar(idSolicitud)
    await cargar()
  } catch (e) {
    alert('No se pudo cancelar la solicitud.')
  }
}

const verHistorial = (idSolicitud) => {
  router.push(`/solicitudes/historial/${idSolicitud}`)
}
</script>

<template>
  <div class="container my-4">
    <h1 class="text-success mb-4">
      <i class="bi bi-clipboard-heart"></i> Mis solicitudes de adopción
    </h1>

    <div v-if="cargando" class="text-center my-4">
      <div class="spinner-border text-success"></div>
    </div>

    <TablaSolicitudes v-else :solicitudes="solicitudes" :modo-admin="false" @cancelar="cancelar"
      @ver-historial="verHistorial" />
  </div>
</template>