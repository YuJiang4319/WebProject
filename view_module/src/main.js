import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from "./router"
import request from './utils/request'

const app = createApp(App)
app.config.globalProperties.$request = request
app.use(router)
app.mount('#app')
