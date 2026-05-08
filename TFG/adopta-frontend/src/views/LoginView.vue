<script setup>
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { authService } from '@/services/authService'
import { authStore } from '@/stores/auth'

const router = useRouter()
const route = useRoute()

const email = ref('')
const password = ref('')
const cargando = ref(false)
const error = ref(null)

const iniciarSesion = async () => {
  error.value = null
  cargando.value = true
  try {
    await authService.login(email.value, password.value)

    const esAdmin = email.value.toLowerCase().includes('admin')
    authStore.login(email.value, esAdmin ? 'ADMIN' : 'CLIENTE')


    const destino = route.query.redirect || (esAdmin ? '/admin' : '/area-personal')
    router.push(destino)
  } catch (e) {
    error.value = 'Email o contraseña incorrectos.'
    console.error(e)
  } finally {
    cargando.value = false
  }
}
</script>

<template>
  <div class="login-wrapper">
    <div class="login-card">
      <h2><i class="bi bi-person-circle"></i> Iniciar sesión</h2>

      <div v-if="error" class="mensaje-error">{{ error }}</div>

      <form @submit.prevent="iniciarSesion">
        <div class="mb-3">
          <label for="email" class="form-label">Email</label>
          <input id="email" v-model="email" type="email" class="form-control" placeholder="tu@email.com" required
            autofocus />
        </div>

        <div class="mb-3">
          <label for="password" class="form-label">Contraseña</label>
          <input id="password" v-model="password" type="password" class="form-control" required />
        </div>

        <button type="submit" class="btn-primario w-100" :disabled="cargando">
          <span v-if="cargando" class="spinner-border spinner-border-sm"></span>
          <span v-else>Entrar</span>
        </button>
      </form>

      <p class="mt-3 text-center">
        ¿Aún no tienes cuenta?
        <RouterLink to="/registro">Regístrate aquí</RouterLink>
      </p>
    </div>
  </div>
</template>

<style scoped>
.login-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 70vh;
  padding: 20px;
}

.login-card {
  background: white;
  padding: 40px;
  border-radius: var(--radius);
  box-shadow: var(--sombra-media);
  width: 100%;
  max-width: 420px;
}

.login-card h2 {
  color: var(--color-primario);
  margin-bottom: 20px;
  text-align: center;
}
</style>