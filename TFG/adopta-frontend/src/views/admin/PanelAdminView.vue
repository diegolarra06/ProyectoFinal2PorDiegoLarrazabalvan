<script setup></script>

<template>
  <div class="container my-4">
    <h1 class="text-success"><i class="bi bi-gear-fill"></i> Panel de administración</h1>
    <p>Bienvenido al panel de gestión del centro de adopción.</p>

    <div class="row mt-4">
      <div class="col-md-4 mb-3">
        <RouterLink to="/admin/animales" class="card text-decoration-none h-100">
          <div class="card-body text-center">
            <i class="bi bi-house-heart text-success" style="font-size: 2.5rem;"></i>
            <h4>Animales</h4>
            <p>Gestionar el catálogo (alta, baja, modificación, imágenes)</p>
          </div>
        </RouterLink>
      </div>
      <div class="col-md-4 mb-3">
        <RouterLink to="/admin/solicitudes" class="card text-decoration-none h-100">
          <div class="card-body text-center">
            <i class="bi bi-clipboard-heart text-success" style="font-size: 2.5rem;"></i>
            <h4>Solicitudes</h4>
            <p>Revisar y cambiar el estado de las solicitudes</p>
          </div>
        </RouterLink>
      </div>
      <div class="col-md-4 mb-3">
        <RouterLink to="/admin/usuarios" class="card text-decoration-none h-100">
          <div class="card-body text-center">
            <i class="bi bi-people text-success" style="font-size: 2.5rem;"></i>
            <h4>Usuarios</h4>
            <p>Gestionar usuarios registrados</p>
          </div>
        </RouterLink>
      </div>
      <div class="col-md-4 mb-3">
        <RouterLink to="/admin/citas" class="card text-decoration-none h-100">
          <div class="card-body text-center">
            <i class="bi bi-calendar-event text-success" style="font-size: 2.5rem;"></i>
            <h4>Citas</h4>
            <p>Programar citas con adoptantes</p>
          </div>
        </RouterLink>
      </div>
      <div class="col-md-4 mb-3">
        <RouterLink to="/admin/estadisticas" class="card text-decoration-none h-100">
          <div class="card-body text-center">
            <i class="bi bi-bar-chart text-success" style="font-size: 2.5rem;"></i>
            <h4>Estadísticas</h4>
            <p>Datos de actividad del centro</p>
          </div>
        </RouterLink>
      </div>
      <div class="col-md-4 mb-3">
      <RouterLink to="/admin/noticias" class="card text-decoration-none h-100">
      <div class="card-body text-center">
      <i class="bi bi-newspaper text-success" style="font-size: 2.5rem;"></i>
      <h4>Noticias</h4>
       <p>Crear, editar y publicar noticias del centro</p>
    </div>
  </RouterLink>
</div>
    </div>
  </div>
</template>