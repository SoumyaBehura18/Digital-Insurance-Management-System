import { createRouter, createWebHistory } from "vue-router";

import DashboardPage from "@/views/User/DashboardPage.vue";
import PoliciesPage from "@/views/User/PoliciesPage.vue";
import ClaimsPage from "@/views/User/ClaimsPage.vue";
import ChatbotPage from "@/views/User/ChatbotPage.vue";
import TicketsPage from "@/views/User/TicketsPage.vue";
import LandingPage from "@/views/LandingPage.vue";
import SupportTickets from "@/views/Admin/SupportTickets.vue";

const routes = [
  { path: "/", component: LandingPage },
  { path: "/dashboard", component: DashboardPage },
  { path: "/policies", component: PoliciesPage },
  { path: "/claims", component: ClaimsPage },
  { path: "/submit-claim", component: ClaimsPage },
  { path: "/chatbot", component: ChatbotPage },
  { path: "/tickets", component: TicketsPage },
  { path: "/admin/tickets", component: SupportTickets },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
