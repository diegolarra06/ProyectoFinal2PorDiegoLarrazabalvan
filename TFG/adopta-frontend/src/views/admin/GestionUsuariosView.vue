<script setup>
import { ref, onMounted } from 'vue'
import { usuarioService } from '@/services/usuarioService'

const usuarios = ref([])
const filtros = ref({ id: null, nombre: '', email: '' })

const cargar = async () => {
  const resp = await usuarioService.listarTodos(filtros.value)
  usuarios.value = Array.isArray(resp.data) ? resp.data : []
}

onMounted(cargar)

const borrar = async (id) => {
  if (!confirm('¿Eliminar este usuario?')) return
  await usuarioService.borrar(id)
  await cargar()
}
</script>

<template>
  <div class="container my-4">
    <h1 class="text-success"><i class="bi bi-people"></i> Gestión de usuarios</h1>

    <form @submit.prevent="cargar" class="row g-2 my-3">
      <div class="col-md-3">
        <input v-model="filtros.nombre" placeholder="Nombre" class="form-control" />
      </div>
      <div class="col-md-3">
        <input v-model="filtros.email" placeholder="Email" class="form-control" />
      </div>
      <div class="col-md-2">
        <button class="btn-primario w-100">Buscar</button>
      </div>
    </form>

    <table class="table table-hover">
      <thead class="table-success">
        <tr>
          <th>ID</th><th>Nombre</th><th>Email</th>
          <th>Teléfono</th><th>Rol</th><th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="u in usuarios" :key="u.idUsuario">
          <td>{{ u.idUsuario }}</td>
          <td>{{ u.nombre }}</td>
          <td>{{ u.email }}</td>
          <td>{{ u.telefono }}</td>
          <td>
            <span class="badge bg-info text-dark">{{ u.rol }}</span>
          </td>
          <td>
            <button class="btn btn-sm btn-outline-danger" @click="borrar(u.idUsuario)">
              <i class="bi bi-trash"></i>
            </button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>