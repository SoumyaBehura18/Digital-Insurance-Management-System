import { createApp } from 'vue'
import App from './App.vue'
import router from './router';
import './assets/main.css'
import claimsStore from './store/ClaimsManagementStore/index.js' 

const app = createApp(App);
app.use(router);
app.use(claimsStore);
app.mount("#app");
