import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

// Bootstrap (CSS + JS)
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.bundle.min.js'
import 'bootstrap-icons/font/bootstrap-icons.css'

// Estilos propios
import './assets/styles/variables.css'
import './assets/styles/primaria.css'

const app = createApp(App)

app.use(router)

app.mount('#app')
