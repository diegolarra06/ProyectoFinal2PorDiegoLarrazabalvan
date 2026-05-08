import { createRouter, createWebHistory } from 'vue-router'
import {authStore} from '../stores/auth.js'

const routes = [

  { path: '/',         name: 'Home',        component: () => import('@/views/HomeView.vue') },
  { path: '/login',    name: 'Login',       component: () => import('@/views/LoginView.vue') },
  { path: '/registro', name: 'Registro',    component: () => import('@/views/RegistroView.vue') },
  { path: '/catalogo', name: 'Catalogo',    component: () => import('@/views/CatalogoView.vue') },
  { path: '/animal/:id', name: 'FichaAnimal', component: () => import('@/views/FichaAnimalView.vue'), props: true },
  { path: '/noticias', name: 'Noticias',    component: () => import('@/views/NoticiasView.vue') },


   {
  path: '/noticias/:id',
  name: 'NoticiaDetalle',
  component: () => import('@/views/NoticiaDetalleView.vue'),
  props: true
},
  {
    path: '/area-personal',
    name: 'AreaPersonal',
    component: () => import('@/views/AreaPersonalView.vue'),
    meta: { requiereAuth: true }
  },
  {
    path: '/favoritos',
    name: 'Favoritos',
    component: () => import('@/views/FavoritosView.vue'),
    meta: { requiereAuth: true }
  },
  {
    path: '/mis-solicitudes',
    name: 'MisSolicitudes',
    component: () => import('@/views/MisSolicitudesView.vue'),
    meta: { requiereAuth: true }
  },
  {
    path: '/iniciar-solicitud/:idAnimal',
    name: 'IniciarSolicitud',
    component: () => import('@/views/IniciarSolicitudView.vue'),
    props: true,
    meta: { requiereAuth: true }
  },


  {
    path: '/admin',
    name: 'PanelAdmin',
    component: () => import('@/views/admin/PanelAdminView.vue'),
    meta: { requiereAuth: true, requiereAdmin: true }
  },
  {
    path: '/admin/animales',
    name: 'GestionAnimales',
    component: () => import('@/views/admin/GestionAnimalesView.vue'),
    meta: { requiereAuth: true, requiereAdmin: true }
  },
  {
    path: '/admin/solicitudes',
    name: 'GestionSolicitudes',
    component: () => import('@/views/admin/GestionSolicitudesView.vue'),
    meta: { requiereAuth: true, requiereAdmin: true }
  },
  {
    path: '/admin/usuarios',
    name: 'GestionUsuarios',
    component: () => import('@/views/admin/GestionUsuariosView.vue'),
    meta: { requiereAuth: true, requiereAdmin: true }
  },
  {
    path: '/admin/citas',
    name: 'GestionCitas',
    component: () => import('@/views/admin/GestionCitasView.vue'),
    meta: { requiereAuth: true, requiereAdmin: true }
  },
  {
  path: '/admin/noticias',
  name: 'GestionNoticias',
  component: () => import('@/views/admin/GestionNoticiasView.vue'),
  meta: { requiereAuth: true, requiereAdmin: true }
  },
  {
    path: '/admin/estadisticas',
    name: 'Estadisticas',
    component: () => import('@/views/admin/EstadisticasView.vue'),
    meta: { requiereAuth: true, requiereAdmin: true }
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

router.beforeEach((to, from) => {
  if (to.meta.requiereAuth && !authStore.state.autenticado) {
    return { name: 'Login', query: { redirect: to.fullPath } }
  }
  if (to.meta.requiereAdmin && !authStore.esAdmin()) {
    return { name: 'Home' }
  }
})

export default router
