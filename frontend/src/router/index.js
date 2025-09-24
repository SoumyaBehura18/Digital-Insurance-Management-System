import { createRouter, createWebHistory } from "vue-router";
import DashboardPage from "../views/DashboardPage.vue";
import PoliciesPage from "../views/PoliciesPage.vue";
import ClaimsPage from "../views/ClaimsPage.vue";
import ChatbotPage from "../views/ChatbotPage.vue";
import TicketsPage from "../views/TicketsPage.vue";

const routes = [
  { path: "/", redirect: "/dashboard" },
  { path: "/dashboard", component: DashboardPage },
  { path: "/policies", component: PoliciesPage },
  { path: "/claims", component: ClaimsPage },
  { path: "/submit-claim", component: ClaimsPage },
  { path: "/chatbot", component: ChatbotPage },
  { path: "/tickets", component: TicketsPage },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
