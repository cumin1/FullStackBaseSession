
import { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import router from './router'
import axios from "axios";

const app = createApp(App)

axios.defaults.baseURL = 'http://localhost:8080'

app.use(router)
app.use(ElementPlus)

app.mount('#app')
