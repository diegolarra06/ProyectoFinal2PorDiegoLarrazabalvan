
<script setup>
import HeaderMenu from './components/HeaderMenu.vue'
import PieContacto from './components/PieContacto.vue'
</script>

<template>
  <div id="app-wrapper">
    <HeaderMenu />

    <main class="main-content">
      <RouterView />
    </main>

    <PieContacto />
  </div>
</template>

<style scoped>
#app-wrapper {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}
.main-content {
  flex: 1;
  padding-bottom: 60px;
}
</style>