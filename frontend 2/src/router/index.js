import { createRouter, createWebHistory } from "vue-router";
import DashboardPage from "@/views/User/DashboardPage.vue";
import PoliciesPage from "@/views/User/PoliciesPage.vue";
import ClaimsPage from "@/views/User/ClaimsPage.vue";
import ChatbotPage from "@/views/User/ChatbotPage.vue";
import TicketsPage from "@/views/User/TicketsPage.vue";
import LandingPage from "@/views/LandingPage.vue";
import LoginPage from "../views/LoginPage.vue";
import RegisterPage from "../views/RegisterPage.vue";
 
// Admin Components
import AdminDashboard from "@/views/Admin/AdminDashboard.vue";
import ManagePolicies from "@/views/Admin/ManagePolicies.vue";
import ManageClaims from "@/views/Admin/ManageClaims.vue";
import SupportTickets from "@/views/Admin/SupportTickets.vue";
import UsersPage from "@/views/Admin/UsersPage.vue";
 
// import RegisterPage from "../views/RegisterPage.vue";
 
 
const routes = [
  // Public routes
  { path: "/", component: LandingPage },
  { path: "/login", component: LoginPage },
  {path: "/register", component: RegisterPage },
  
  // User Routes - direct paths (no prefix)
  {
    path: "/dashboard",
    component: DashboardPage,
    meta: { requiresAuth: true, role: 'user' }
  },
  {
    path: "/policies",
    component: PoliciesPage,
    meta: { requiresAuth: true, role: 'user' }
  },
  {
    path: "/claims",
    component: ClaimsPage,
    meta: { requiresAuth: true, role: 'user' }
  },
  {
    path: "/submit-claim",
    component: ClaimsPage,
    meta: { requiresAuth: true, role: 'user' }
  },
  {
    path: "/chatbot",
    component: ChatbotPage,
    meta: { requiresAuth: true, role: 'user' }
  },
  {
    path: "/tickets",
    component: TicketsPage,
    meta: { requiresAuth: true, role: 'user' }
  },
  
  // Admin Routes - all under /admin prefix
  {
    path: "/admin/dashboard",
    component: AdminDashboard,
    meta: { requiresAuth: true, role: 'admin' }
  },
  {
    path: "/admin/policies",
    component: ManagePolicies,
    meta: { requiresAuth: true, role: 'admin' }
  },
  {
    path: "/admin/claims",
    component: ManageClaims,
    meta: { requiresAuth: true, role: 'admin' }
  },
  {
    path: "/admin/tickets",
    component: SupportTickets,
    meta: { requiresAuth: true, role: 'admin' }
  },
  {
    path: "/admin/users",
    component: UsersPage,
    meta: { requiresAuth: true, role: 'admin' }
  },
  
 
  
  // Catch-all route for 404
  {
    path: "/:pathMatch(.*)*",
    name: "NotFound",
    component: () => import("@/views/NotFound.vue")
  }
];
 
const router = createRouter({
  history: createWebHistory(),
  routes,
});
 
// Global Navigation Guard
// Global Navigation Guard
router.beforeEach((to, from, next) => {
  // Get full user object from localStorage
  const storedUser = localStorage.getItem('currentUser');
  const user = storedUser ? JSON.parse(storedUser) : null;
  const token = user?.token;
  const userRole = user?.role;

  // Check if route requires authentication
  if (to.meta.requiresAuth) {
    if (!token) {
      // No token, redirect to login
      next('/login');
      return;
    }

    // Check role-based access
    if (to.meta.role && to.meta.role !== userRole) {
      // Wrong role, redirect based on user's actual role
      if (userRole === 'admin') {
        next('/admin/dashboard');
      } else if (userRole === 'user') {
        next('/dashboard');
      } else {
        next('/login');
      }
      return;
    }
  }

  // If user is already logged in and tries to access login page
  if (to.path === '/login' && token) {
    if (userRole === 'admin') {
      next('/admin/dashboard');
    } else if (userRole === 'user') {
      next('/dashboard');
    }
    return;
  }

  // Allow navigation
  next();
});

export default router;
 
 