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
      <!-- Replace placeholder with dashboard view -->
      <DashboardView />
    </main>
  </div>
</template>

<script setup>
import HeaderLayout from "@/components/layout/HeaderLayout.vue";
import DashboardView from "@/components/admindashboard/DashboardView.vue";
import { ref } from "vue";

const isCollapsed = ref(true);

const setIsCollapsed = (val) => {
  isCollapsed.value = val;
};

const user = ref({
  name: "John Doe",
  email: "john.doe@example.com",
});
</script>
