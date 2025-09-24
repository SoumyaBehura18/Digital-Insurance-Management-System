<template>
  <div class="flex h-screen">
    <SidebarLayout
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
import { useRouter, useRoute } from "vue-router";

const router = useRouter();
const route = useRoute();

const isCollapsed = ref(true);
const isDarkMode = ref(false);

const setIsCollapsed = (val) => (isCollapsed.value = val);
const setIsDarkMode = (val) => (isDarkMode.value = val);

const currentRoute = computed(() => route.path); // ✅ now works
const setCurrentPage = (page) => {
  router.push(`/${page}`);
};

const onLogout = () => {
  console.log("Logout clicked!");
};
</script>
