
<script setup></script>

<template>
  <footer class="pie">
    <p>
      <i class="bi bi-envelope"></i>
      <a href="mailto:contacto@adoptauncompanero.com">contacto@adoptauncompanero.com</a>
      <span class="sep">|</span>
      <i class="bi bi-telephone"></i> 923 33 69 12
      <span class="sep">|</span>
      © {{ new Date().getFullYear() }} AdoptaUnCompañero — TFG DAW Calasanz Salamanca
    </p>
  </footer>
</template>

<style scoped>
.pie {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  background-color: var(--color-primario);
  color: white;
  text-align: center;
  padding: 8px 0;
  font-size: 0.9rem;
  z-index: 999;
}
.pie a { color: white; }
.sep { margin: 0 10px; opacity: 0.6; }
</style>