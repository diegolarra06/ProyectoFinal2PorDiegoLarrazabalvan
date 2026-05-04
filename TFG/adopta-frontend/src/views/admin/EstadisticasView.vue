<script setup>
import { ref, onMounted } from 'vue'
import api from '@/services/api'

const estadisticas = ref(null)
const cargando = ref(true)

onMounted(async () => {
  try {
    const resp = await api.get('/api/estadisticas')
    estadisticas.value = resp.data
  } catch (e) {
    console.error(e)
  } finally {
    cargando.value = false
  }
})
</script>

<template>
  <div class="container my-4">
    <h1 class="text-success"><i class="bi bi-bar-chart"></i> Estadísticas</h1>

    <div v-if="cargando" class="text-center my-4">
      <div class="spinner-border text-success"></div>
    </div>

    <div v-else-if="estadisticas" class="row mt-3">
      <div class="col-md-3 mb-3"><div class="card text-center">
        <div class="card-body">
          <h3 class="text-success">{{ estadisticas.totalAnimales }}</h3>
          <p>Total animales</p>
        </div></div></div>
      <div class="col-md-3 mb-3"><div class="card text-center">
        <div class="card-body">
          <h3 class="text-info">{{ estadisticas.animalesDisponibles }}</h3>
          <p>Disponibles</p>
        </div></div></div>
      <div class="col-md-3 mb-3"><div class="card text-center">
        <div class="card-body">
          <h3 class="text-secondary">{{ estadisticas.animalesAdoptados }}</h3>
          <p>Adoptados</p>
        </div></div></div>
      <div class="col-md-3 mb-3"><div class="card text-center">
        <div class="card-body">
          <h3 class="text-warning">{{ estadisticas.porcentajeAdoptados.toFixed(1) }}%</h3>
          <p>% adoptados</p>
        </div></div></div>

      <div class="col-md-4 mb-3"><div class="card text-center">
        <div class="card-body">
          <h3 class="text-success">{{ estadisticas.solicitudesAprobadas }}</h3>
          <p>Solicitudes aprobadas</p>
        </div></div></div>
      <div class="col-md-4 mb-3"><div class="card text-center">
        <div class="card-body">
          <h3 class="text-danger">{{ estadisticas.solicitudesRechazadas }}</h3>
          <p>Solicitudes rechazadas</p>
        </div></div></div>
      <div class="col-md-4 mb-3"><div class="card text-center">
        <div class="card-body">
          <h3 class="text-primary">{{ estadisticas.especieMasAdoptada }}</h3>
          <p>Especie más adoptada</p>
        </div></div></div>
    </div>
  </div>
</template>