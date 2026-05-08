<script setup>
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { solicitudService } from '@/services/solicitudService'

const route = useRoute()
const router = useRouter()

const idAnimal = route.params.idAnimal
const comentarios = ref('')
const cargando = ref(false)
const exito = ref(false)
const error = ref(null)

const enviar = async () => {
  cargando.value = true
  error.value = null
  try {
    await solicitudService.iniciar(idAnimal, comentarios.value)
    exito.value = true
    setTimeout(() => router.push('/mis-solicitudes'), 2000)
  } catch (e) {
    error.value = 'No se pudo crear la solicitud. Es posible que ya tengas una solicitud para este animal.'
  } finally {
    cargando.value = false
  }
}
</script>

<template>
  <div class="solicitud-wrapper">
    <div class="solicitud-card">
      <h2><i class="bi bi-heart-fill"></i> Solicitud de adopción</h2>

      <div v-if="exito" class="mensaje-ok">
        ¡Solicitud creada! Te llevamos a tus solicitudes…
      </div>
      <div v-if="error" class="mensaje-error">{{ error }}</div>

      <form v-if="!exito" @submit.prevent="enviar">
        <p>Estás a punto de iniciar una solicitud de adopción.
          Por favor, cuéntanos algo sobre tu situación, tu vivienda
          y por qué quieres adoptar:</p>

        <textarea v-model="comentarios" class="form-control my-3" rows="6"
          placeholder="Vivo en un piso con jardín, tengo experiencia con animales..." required></textarea>

        <div class="d-flex gap-2">
          <RouterLink :to="`/animal/${idAnimal}`" class="btn btn-secondary">
            Cancelar
          </RouterLink>
          <button type="submit" class="btn-primario flex-fill" :disabled="cargando">
            <span v-if="cargando">Enviando…</span>
            <span v-else><i class="bi bi-send"></i> Enviar solicitud</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<style scoped>
.solicitud-wrapper {
  padding: 30px;
  display: flex;
  justify-content: center;
}

.solicitud-card {
  background: white;
  padding: 40px;
  border-radius: var(--radius);
  box-shadow: var(--sombra-media);
  max-width: 600px;
  width: 100%;
}

.solicitud-card h2 {
  color: var(--color-primario);
  margin-bottom: 20px;
}
</style>