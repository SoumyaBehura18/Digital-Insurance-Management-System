import { createApp } from 'vue'
import { createPinia } from 'pinia'
import './index.css'


createApp(App).mount('#app')


import App from './App.vue'
import router from './router'

const app = createApp(App)

app.use(createPinia())
app.use(router)

app.mount('#app')
