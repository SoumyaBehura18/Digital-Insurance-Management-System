<template>
  <div class="flex h-screen">
    <!-- Show Admin Sidebar for admin users -->
    <AdminSidebarLayout
      v-if="currentRoute !== '/' && currentRoute !== '/login' && currentUserRole === 'admin'"
      :currentPage="currentRoute"
      :setCurrentPage="setCurrentPage"
      :isCollapsed="isCollapsed"
      :setIsCollapsed="setIsCollapsed"
      :isDarkMode="isDarkMode"
      :setIsDarkMode="setIsDarkMode"
      :onLogout="onLogout"
    />
    
    <!-- Show User Sidebar for regular users -->
    <SidebarLayout
      v-else-if="currentRoute !== '/' && currentRoute !== '/login' && currentUserRole === 'user'"
      :currentPage="currentRoute"
      :setCurrentPage="setCurrentPage"
      :isCollapsed="isCollapsed"
      :setIsCollapsed="setIsCollapsed"
      :isDarkMode="isDarkMode"
      :setIsDarkMode="setIsDarkMode"
      :onLogout="onLogout"
    />

    <main class="flex-1 overflow-auto">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { ref, computed } from "vue";
import SidebarLayout from "./components/layout/SidebarLayout.vue";
import AdminSidebarLayout from "./components/layout/AdminSidebarLayout.vue";
import { useRouter, useRoute } from "vue-router";

const router = useRouter();
const route = useRoute();

const isCollapsed = ref(true);
const isDarkMode = ref(false);

const setIsCollapsed = (val) => (isCollapsed.value = val);
const setIsDarkMode = (val) => (isDarkMode.value = val);

const currentRoute = computed(() => route.path);
const currentUserRole = computed(() => localStorage.getItem('role'));

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
  
  // Redirect to login
  router.push('/login');
  
  console.log("User logged out");
};
</script>
