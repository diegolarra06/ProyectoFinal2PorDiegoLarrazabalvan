<script setup>
const noticias = [
  { titulo: '¡10 nuevos animales esperan ser adoptados!',
    fecha: '01/05/2026', img: '/img/lluvia1.jpg',
    texto: 'Esta semana han llegado al centro 10 nuevos compañeros buscando hogar.' },
  { titulo: 'Día solidario de adopciones',
    fecha: '20/04/2026', img: '/img/lluvia2.jpg',
    texto: 'El próximo sábado celebramos una jornada de puertas abiertas en el centro.' },
  { titulo: 'Consejos para nuevos adoptantes',
    fecha: '10/04/2026', img: '/img/lluvia3.jpg',
    texto: 'Lo que debes saber antes de adoptar a tu primera mascota.' }
]
</script>

<template>
  <div class="container my-4">
    <h1 class="text-success"><i class="bi bi-newspaper"></i> Noticias</h1>
    <div class="row mt-3">
      <div v-for="(n, i) in noticias" :key="i" class="col-md-4 mb-3">
        <div class="card h-100">
          <img :src="n.img" class="card-img-top" :alt="n.titulo" style="height:200px;object-fit:cover;" />
          <div class="card-body">
            <h5 class="card-title">{{ n.titulo }}</h5>
            <p class="text-muted small">{{ n.fecha }}</p>
            <p>{{ n.texto }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>