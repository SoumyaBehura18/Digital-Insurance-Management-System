<template>
  <div class="flex flex-col h-screen">
    <HeaderLayout :user="user" :isCollapsed="isCollapsed" :setIsCollapsed="setIsCollapsed" class="w-full" />

    <div class="space-y-6 p-6">
      <WelcomeSection :user="user" />

      <KpiCards
        :active-policies="activeUserPolicies"
        :claims="claims"
        :pending-tickets="pendingTickets"
        :total-coverage="totalCoverage"
      />

      <DashboardCharts
        :policies="activeUserPolicies"
        :claims="claims"
        :all-policies="allPolicies"
      />

      <QuickActions @navigate="goToPage" />

      <RecentActivity :recent-policies="recentPolicies" />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";

// Components
import HeaderLayout from "@/components/layout/HeaderLayout.vue";
import WelcomeSection from "@/components/dashboard/WelcomeSection.vue";
import KpiCards from "@/components/dashboard/KpiCards.vue";
import DashboardCharts from "@/components/dashboard/DashboardCharts.vue";
import QuickActions from "@/components/dashboard/QuickAction.vue";
import RecentActivity from "@/components/dashboard/RecentActivity.vue";

const store = useStore();
const router = useRouter();

const isCollapsed = ref(true);
const setIsCollapsed = (val) => (isCollapsed.value = val);
const user = ref(JSON.parse(localStorage.getItem("currentUser")) || { name: "John Doe" });

const policies = computed(() => store.getters["userPolicies/getPolicies"] || []);
const claims = computed(() => store.getters["claims/getClaims"] || []);
const allPolicies = computed(() => store.getters["policies/getAllPolicies"] || []);

const activeUserPolicies = computed(() => policies.value.filter(p => p.status === "ACTIVE" || p.status === "RENEWED"));

const totalCoverage = computed(() => {
  return activeUserPolicies.value.reduce((sum, userPolicy) => {
    const basePolicy = allPolicies.value.find(bp => bp.policyId === userPolicy.policyId);
    return sum + (basePolicy?.coverage || 0);
  }, 0);
});

const pendingTickets = ref([
  { id: 1, title: "Ticket 1" },
  { id: 2, title: "Ticket 2" },
]);

const recentPolicies = computed(() => policies.value.slice(-3).reverse());


const goToPage = (page) => router.push(`/${page}`);

// Fetch data
onMounted(async () => {
  const userId = user.value?.userId;

  const policyRequest = {
    smokingDrinking: user.value?.smokingDrinking || false,
    vehicleType: user.value?.vehicleType || "CAR",
    vehicleAge: user.value?.vehicleAge || 0,
    preexistingConditions: user.value?.preexistingConditions || [],
  };

  if (userId) {
    await store.dispatch("userPolicies/fetchUserPolicies", userId);
    await store.dispatch("claims/fetchClaims", userId);
    await store.dispatch("policies/fetchAllPolicies", policyRequest);
  }
});
</script>
