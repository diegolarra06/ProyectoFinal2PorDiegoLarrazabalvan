
<script setup>
import { RouterLink, useRouter } from 'vue-router'
import { authStore } from '@/stores/auth'
import { authService } from '@/services/authService'

const router = useRouter()

const cerrarSesion = async () => {
  try {
    await authService.logout()
  } catch (e) {

  } finally {
    authStore.logout()
    router.push('/')
  }
}
</script>

<template>
  <header class="cabecera">
    <div class="logo">
      <RouterLink to="/" class="logo-link">
        <i class="bi bi-heart-fill"></i>
        AdoptaUnCompañero
      </RouterLink>
    </div>

    <nav class="nav-principal">
      <RouterLink to="/">Inicio</RouterLink>
      <RouterLink to="/catalogo">Catálogo</RouterLink>
      <RouterLink to="/noticias">Noticias</RouterLink>

      <template v-if="authStore.state.autenticado && authStore.esCliente()">
        <RouterLink to="/area-personal">Mi Área</RouterLink>
        <RouterLink to="/favoritos">Favoritos</RouterLink>
        <RouterLink to="/mis-solicitudes">Mis Solicitudes</RouterLink>
      </template>

      <template v-if="authStore.esAdmin()">
        <RouterLink to="/admin">Panel Admin</RouterLink>
      </template>
    </nav>

    <div class="acciones">
      <template v-if="!authStore.state.autenticado">
        <RouterLink to="/login" class="btn-login">Iniciar sesión</RouterLink>
        <RouterLink to="/registro" class="btn-registro">Registrarse</RouterLink>
      </template>
      <template v-else>
        <span class="usuario-actual">
          <i class="bi bi-person-circle"></i>
          {{ authStore.state.email }}
        </span>
        <button @click="cerrarSesion" class="btn-logout">
          <i class="bi bi-box-arrow-right"></i> Salir
        </button>
      </template>
    </div>
  </header>
</template>

<style scoped>
.cabecera {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: var(--color-primario);
  color: white;
  padding: 12px 24px;
  position: sticky;
  top: 0;
  z-index: 1000;
  box-shadow: var(--sombra-suave);
  flex-wrap: wrap;
  gap: 10px;
}
.logo-link {
  color: white;
  font-size: 1.4rem;
  font-weight: bold;
  text-decoration: none;
  display: flex;
  align-items: center;
  gap: 8px;
}
.nav-principal {
  display: flex;
  gap: 20px;
}
.nav-principal a {
  color: white;
  font-weight: 500;
  padding: 6px 10px;
  border-radius: var(--radius);
  transition: var(--transition);
}
.nav-principal a:hover,
.nav-principal a.router-link-exact-active {
  background-color: var(--color-primario-oscuro);
  text-decoration: none;
}
.acciones {
  display: flex;
  align-items: center;
  gap: 10px;
}
.btn-login, .btn-registro {
  padding: 6px 14px;
  border-radius: var(--radius);
  background-color: white;
  color: var(--color-primario);
  font-weight: 600;
}
.btn-registro {
  background-color: var(--color-secundario);
  color: white;
}
.btn-logout {
  background-color: transparent;
  color: white;
  border: 1px solid white;
  padding: 6px 12px;
  border-radius: var(--radius);
  cursor: pointer;
}
.usuario-actual {
  font-size: 0.9rem;
}
</style>