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
import DashboardView from "@/components/AdminDashboard/DashboardView.vue";
import { ref, onMounted } from "vue";
import { useStore } from "vuex";

const store = useStore();
const isCollapsed = ref(true);

const setIsCollapsed = (val) => {
  isCollapsed.value = val;
};

const user = ref({
  name: "John Doe",
  email: "john.doe@example.com",
});

// Load data for the admin dashboard
onMounted(async () => {
  console.log('Admin Dashboard mounted, loading data...');
  
  try {
    // Load claims data (which contains policy information for breakdown)
    const claimsActionName = store._actions['claims/fetchAllClaims'] ? 'claims/fetchAllClaims' : 'fetchAllClaims';
    await store.dispatch(claimsActionName);
    console.log('Claims data loaded for dashboard');
    
    // Load tickets data for dashboard
    const ticketsActionName = store._actions['adminTickets/fetchAllTickets'] ? 'adminTickets/fetchAllTickets' : 'fetchAllTickets';
    await store.dispatch(ticketsActionName);
    console.log('Tickets data loaded for dashboard');
    
    // Note: PolicyStore is designed for user-specific policy recommendations
    // For admin dashboard, we derive policy statistics from claims data
    // If we need actual policy catalog data, we'd need a different admin endpoint
    
  } catch (error) {
    console.error('Error loading admin dashboard data:', error);
  }
});
</script>
