<template>
  <div class="flex h-screen">
    <!-- Show Admin Sidebar for authenticated admin users -->
    <AdminSidebarLayout
      v-if="isAuthenticated && isAdmin"
      :currentPage="currentRoute"
      :setCurrentPage="setCurrentPage"
      :isCollapsed="isCollapsed"
      :setIsCollapsed="setIsCollapsed"
      :isDarkMode="isDarkMode"
      :setIsDarkMode="setIsDarkMode"
      :onLogout="onLogout"
    />
    
    <!-- Show User Sidebar for authenticated regular users -->
    <SidebarLayout
      v-else-if="isAuthenticated && !isAdmin"
      :currentPage="currentRoute"
      :setCurrentPage="setCurrentPage"
      :isCollapsed="isCollapsed"
      :setIsCollapsed="setIsCollapsed"
      :isDarkMode="isDarkMode"
      :setIsDarkMode="setIsDarkMode"
      :onLogout="onLogout"
    />

    <!-- Main content - full width when not authenticated -->
    <main :class="isAuthenticated ? 'flex-1 overflow-auto' : 'w-full overflow-auto'">
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
const isDarkMode = ref(false);
const userRole = ref('');
const isAuthenticated = ref(false);

const setIsCollapsed = (val) => (isCollapsed.value = val);
const setIsDarkMode = (val) => (isDarkMode.value = val);

const currentRoute = computed(() => route.path);
const isAdmin = computed(() => userRole.value === 'admin');

// Function to update authentication state
const updateAuthState = () => {
  const token = localStorage.getItem('token');
  const role = localStorage.getItem('role');
  
  isAuthenticated.value = !!token;
  userRole.value = role || '';
};

// Initialize authentication state on mount
onMounted(() => {
  updateAuthState();
});

// Watch for route changes to update auth state
watch(() => route.path, () => {
  updateAuthState();
});

const setCurrentPage = (page) => {
  // Handle navigation based on current user role
  const userRole = localStorage.getItem('role');
  
  if (userRole === 'admin') {
    router.push(`/admin/${page}`);
  } else {
    router.push(`/${page}`);
  }
};

const onLogout = () => {
  // Clear all auth data
  localStorage.removeItem('token');
  localStorage.removeItem('role');
  localStorage.removeItem('userId');
  
  // Update reactive state
  updateAuthState();
  
  // Redirect to login
  router.push('/login');
  
  console.log("User logged out");
};
</script>