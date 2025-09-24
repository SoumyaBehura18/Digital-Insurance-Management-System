<template>
  <div class="flex flex-col h-screen">
    <!-- Header -->
    <HeaderLayout
      :user="user"
      :isCollapsed="isCollapsed"
      :setIsCollapsed="setIsCollapsed"
      class="w-full"
    />

    <!-- Page Content -->
    <main class="flex-1 p-6 overflow-auto">
      <h1 class="text-2xl font-bold">Users Page</h1>
      <p>This is Users Page</p>
    </main>
  </div>
</template>

<script setup>
import HeaderLayout from "@/components/layout/HeaderLayout.vue";
import { ref } from "vue";

// Component name is automatically inferred as "UsersPage" from filename when using <script setup>

const isCollapsed = ref(true);

const setIsCollapsed = (val) => {
  isCollapsed.value = val;
};

const user = ref({
  name: "Admin User",
  email: "admin@example.com",
});
</script>
