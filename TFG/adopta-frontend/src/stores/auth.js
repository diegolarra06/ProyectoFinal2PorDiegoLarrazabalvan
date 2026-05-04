// =============================================================
// STORE DE AUTENTICACIÓN (sin Pinia, simple con reactive)
// Tema 8 PDF - reactive()
// Mantiene en memoria el usuario logueado y su rol.
// Se persiste en localStorage para no perderlo al refrescar.
// =============================================================
import { reactive, readonly } from 'vue'

const STORAGE_KEY = 'adopta-auth'

// Estado inicial: leemos de localStorage si existe
const guardado = localStorage.getItem(STORAGE_KEY)
const estadoInicial = guardado
  ? JSON.parse(guardado)
  : { autenticado: false, email: null, rol: null }

const state = reactive(estadoInicial)

const persistir = () => {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(state))
}

export const authStore = {
  // Estado de solo lectura desde fuera
  state: readonly(state),

  // Marca como logueado
  login(email, rol) {
    state.autenticado = true
    state.email = email
    state.rol = rol
    persistir()
  },

  // Cierra la sesión
  logout() {
    state.autenticado = false
    state.email = null
    state.rol = null
    persistir()
  },

  // Helpers para comprobar el rol
  esAdmin() {
    return state.rol === 'ADMIN'
  },

  esCliente() {
    return state.rol === 'CLIENTE'
  }
}