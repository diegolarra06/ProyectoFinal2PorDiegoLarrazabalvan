
<script setup>
const anuncios = [
  { titulo: '¡Pelota!',           img: 'pelota.jpeg',  url: 'https://www.miscota.es/' },
  { titulo: '¡Chuches!',          img: 'chuche.jpeg',  url: 'https://www.amazon.es/' },
  { titulo: '¡Cuenco!',           img: 'cuenco.webp',  url: 'https://collarofsweden.com/' },
  { titulo: '¡Juguetes!',         img: 'pienso.jpeg',  url: 'https://www.amazon.es/' }
]
</script>

<template>
  <aside class="barra-publi">
    <h3>Publicidad</h3>
    <div v-for="(a, i) in anuncios" :key="i" class="anuncio">
      <a :href="a.url" target="_blank" rel="noopener">
        <img :src="`/img/${a.img}`" :alt="a.titulo" />
        <h4>{{ a.titulo }}</h4>
      </a>
    </div>
  </aside>
</template>

<style scoped>
.barra-publi { padding: 15px; background: white; border-radius: var(--radius); box-shadow: var(--sombra-suave); }
.barra-publi h3 { color: var(--color-primario); margin-bottom: 10px; }
.anuncio { margin-bottom: 15px; text-align: center; }
.anuncio img { width: 100%; max-height: 130px; object-fit: cover; border-radius: var(--radius); }
.anuncio h4 { font-size: 0.9rem; margin-top: 6px; }
</style>