<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { authService } from '@/services/authService'

const router = useRouter()

const form = ref({
  nombre: '',
  email: '',
  password: '',
  passwordConfirm: '',
  telefono: '',
  direccion: ''
})
const cargando = ref(false)
const error = ref(null)
const exito = ref(false)

const registrar = async () => {
  error.value = null
  if (form.value.password !== form.value.passwordConfirm) {
    error.value = 'Las contraseñas no coinciden.'
    return
  }
  cargando.value = true
  try {
    await authService.registrar(
      form.value.nombre, form.value.email, form.value.password,
      form.value.telefono, form.value.direccion
    )
    exito.value = true
    setTimeout(() => router.push('/login'), 2000)
  } catch (e) {
    error.value = 'Error en el registro. ¿El email ya existe?'
  } finally {
    cargando.value = false
  }
}
</script>

<template>
  <div class="registro-wrapper">
    <div class="registro-card">
      <h2><i class="bi bi-person-plus"></i> Crear cuenta</h2>

      <div v-if="error" class="mensaje-error">{{ error }}</div>
      <div v-if="exito" class="mensaje-ok">
        ¡Registro completado! Redirigiendo al login…
      </div>

      <form v-if="!exito" @submit.prevent="registrar">
        <div class="mb-3">
          <label class="form-label">Nombre completo *</label>
          <input v-model="form.nombre" type="text" class="form-control" required />
        </div>
        <div class="mb-3">
          <label class="form-label">Email *</label>
          <input v-model="form.email" type="email" class="form-control" required />
        </div>
        <div class="row">
          <div class="col-md-6 mb-3">
            <label class="form-label">Contraseña *</label>
            <input v-model="form.password" type="password" class="form-control" required minlength="4" />
          </div>
          <div class="col-md-6 mb-3">
            <label class="form-label">Repite contraseña *</label>
            <input v-model="form.passwordConfirm" type="password" class="form-control" required />
          </div>
        </div>
        <div class="mb-3">
          <label class="form-label">Teléfono</label>
          <input v-model="form.telefono" type="tel" class="form-control" />
        </div>
        <div class="mb-3">
          <label class="form-label">Dirección</label>
          <input v-model="form.direccion" type="text" class="form-control" />
        </div>

        <button type="submit" class="btn-primario w-100" :disabled="cargando">
          <span v-if="cargando">Registrando…</span>
          <span v-else>Crear cuenta</span>
        </button>
      </form>
    </div>
  </div>
</template>

<style scoped>
.registro-wrapper {
  display: flex;
  justify-content: center;
  padding: 30px;
}

.registro-card {
  background: white;
  padding: 40px;
  border-radius: var(--radius);
  box-shadow: var(--sombra-media);
  width: 100%;
  max-width: 600px;
}

.registro-card h2 {
  color: var(--color-primario);
  margin-bottom: 20px;
  text-align: center;
}
</style>