<script setup>
import { computed } from 'vue'

const props = defineProps({
  animal: { type: Object, required: true },
  esFavorito: { type: Boolean, default: false },
  mostrarBotonFavorito: { type: Boolean, default: false }
})


const emit = defineEmits(['toggle-favorito', 'ver-ficha'])


const imagenUrl = computed(() => {
  if (props.animal.imagenPrincipal) {
    return `http://localhost:8080/uploads/${props.animal.imagenPrincipal}`
  }
  return '/img/perrera.jpg'
})

const claseBadge = computed(() => {
  switch (props.animal.estado) {
    case 'DISPONIBLE': return 'badge-disponible'
    case 'RESERVADO': return 'badge-reservado'
    case 'ADOPTADO': return 'badge-adoptado'
    default: return ''
  }
})
</script>

<template>
  <article class="tarjeta">
    <div class="tarjeta-img">
      <img :src="imagenUrl" :alt="animal.nombre" />
      <span class="badge" :class="claseBadge">{{ animal.estado }}</span>
    </div>

    <div class="tarjeta-body">
      <h3>{{ animal.nombre }}</h3>
      <p class="info">
        <i class="bi bi-tag"></i> {{ animal.especie }}
        <span v-if="animal.edad"> · {{ animal.edad }} años</span>
        <span v-if="animal.tamano"> · {{ animal.tamano }}</span>
      </p>
      <p class="personalidad" v-if="animal.personalidad">
        {{ animal.personalidad.length > 80
          ? animal.personalidad.slice(0, 80) + '…'
          : animal.personalidad }}
      </p>

      <div class="acciones">
        <button class="btn-primario" @click="emit('ver-ficha', animal.idAnimal)">
          <i class="bi bi-eye"></i> Ver ficha
        </button>
        <button v-if="mostrarBotonFavorito" class="btn-fav" :class="{ activo: esFavorito }"
          @click="emit('toggle-favorito', animal.idAnimal)">
          <i :class="esFavorito ? 'bi bi-heart-fill' : 'bi bi-heart'"></i>
        </button>
      </div>
    </div>
  </article>
</template>

<style scoped>
.tarjeta {
  background: white;
  border-radius: var(--radius);
  overflow: hidden;
  box-shadow: var(--sombra-suave);
  transition: var(--transition);
  display: flex;
  flex-direction: column;
}

.tarjeta:hover {
  transform: translateY(-4px);
  box-shadow: var(--sombra-media);
}

.tarjeta-img {
  position: relative;
  height: 200px;
  overflow: hidden;
}

.tarjeta-img img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.badge {
  position: absolute;
  top: 10px;
  right: 10px;
  padding: 4px 10px;
  border-radius: 20px;
  color: white;
  font-size: 0.75rem;
  font-weight: 600;
}

.badge-disponible {
  background-color: var(--color-success);
}

.badge-reservado {
  background-color: var(--color-warning);
}

.badge-adoptado {
  background-color: var(--color-secundario);
}

.tarjeta-body {
  padding: 15px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.tarjeta-body h3 {
  margin-bottom: 6px;
  color: var(--color-primario);
}

.info {
  font-size: 0.9rem;
  color: #666;
  margin-bottom: 8px;
}

.personalidad {
  font-size: 0.9rem;
  flex: 1;
}

.acciones {
  display: flex;
  gap: 8px;
  margin-top: 10px;
}

.btn-fav {
  background: white;
  border: 1px solid var(--color-gris-medio);
  border-radius: var(--radius);
  padding: 8px 12px;
  cursor: pointer;
  color: var(--color-error);
  transition: var(--transition);
}

.btn-fav.activo {
  background: var(--color-error);
  color: white;
}
</style>