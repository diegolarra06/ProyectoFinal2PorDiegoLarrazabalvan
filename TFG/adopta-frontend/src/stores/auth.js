
import { reactive, readonly } from 'vue'

const STORAGE_KEY = 'adopta-auth'

const guardado = localStorage.getItem(STORAGE_KEY)
const estadoInicial = guardado
  ? JSON.parse(guardado)
  : { autenticado: false, email: null, rol: null }

const state = reactive(estadoInicial)

const persistir = () => {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(state))
}

export const authStore = {

  state: readonly(state),

  login(email, rol) {
    state.autenticado = true
    state.email = email
    state.rol = rol
    persistir()
  },


  logout() {
    state.autenticado = false
    state.email = null
    state.rol = null
    persistir()
  },

  esAdmin() {
    return state.rol === 'ADMIN'
  },

  esCliente() {
    return state.rol === 'CLIENTE'
  }
}