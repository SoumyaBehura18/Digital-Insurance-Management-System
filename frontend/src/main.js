import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store"; // import the root store
import "./assets/main.css";

import VueToast from "vue-toast-notification";
import "vue-toast-notification/dist/theme-sugar.css";

const app = createApp(App);
app.use(VueToast, {
  position: "top-right",
  duration: 5000,
  dismissible: true,
});
app.use(router);
app.use(store);
app.mount("#InsureCore");
