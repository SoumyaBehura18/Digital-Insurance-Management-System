<template>
  <div class="flex h-screen">
    <!-- Conditionally show sidebar only if user is authenticated AND route is not "/", "/login", or "/register" -->
    <template v-if="!hideSidebar">
      <!-- Show Admin Sidebar for authenticated admin users -->
      <AdminSidebarLayout
        v-if="isAdmin"
        :currentPage="currentRoute"
        :setCurrentPage="setCurrentPage"
        :isCollapsed="isCollapsed"
        :setIsCollapsed="setIsCollapsed"
        :onLogout="onLogout"
      />

      <!-- Show User Sidebar for authenticated regular users -->
      <SidebarLayout
        v-else-if="!isAdmin"
        :currentPage="currentRoute"
        :setCurrentPage="setCurrentPage"
        :isCollapsed="isCollapsed"
        :setIsCollapsed="setIsCollapsed"
        :onLogout="onLogout"
      />
    </template>

    <!-- Main content -->
    <main
      :class="hideSidebar ? 'w-full overflow-auto' : 'flex-1 overflow-auto'"
    >
  
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from "vue";
import SidebarLayout from "./components/layout/SidebarLayout.vue";
import AdminSidebarLayout from "./components/layout/AdminSidebarLayout.vue";
import { useRouter, useRoute } from "vue-router";
const router = useRouter();
const route = useRoute();

const isCollapsed = ref(true);
const userRole = ref("");
const isAuthenticated = ref(false);

const setIsCollapsed = (val) => (isCollapsed.value = val);

const currentRoute = computed(() => route.path);
const isAdmin = computed(() => userRole.value === "admin");

//  Hide sidebar for these routes
const hideSidebar = computed(() => {
  const hiddenRoutes = ["/", "/login", "/register"];
  return hiddenRoutes.includes(route.path);
});

// Function to update authentication state
const updateAuthState = () => {
  const storedUser = localStorage.getItem("currentUser");
  const user = storedUser ? JSON.parse(storedUser) : null;
  const token = user?.token;
  const role = user?.role;

  isAuthenticated.value = !!token;
  userRole.value = role || "";
};

// Initialize authentication state on mount
onMounted(() => {
  updateAuthState();
});

// Watch for route changes to update auth state
watch(
  () => route.path,
  () => {
    updateAuthState();
  }
);

const setCurrentPage = (page) => {
  const userRole = localStorage.getItem("role");

  if (userRole === "admin") {
    router.push(`/admin/${page}`);
  } else {
    router.push(`/${page}`);
  }
};

const onLogout = () => {
  localStorage.removeItem("role");
  localStorage.removeItem("userId");

  updateAuthState();
  router.push("/login");

  console.log("User logged out");
};
</script>
