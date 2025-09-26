<template>
  <div class="flex flex-col h-screen">
    <HeaderLayout
      :user="user"
      :isCollapsed="isCollapsed"
      :setIsCollapsed="setIsCollapsed"
      class="w-full"
    />
    <div class="space-y-6 p-6">
      <!-- Header with View Buttons -->
      <div class="flex items-center justify-between">
        <h1>{{ view === "catalog" ? "Policy Catalog" : "My Policies" }}</h1>
        <div class="flex gap-2">
          <button
            :class="
              view === 'catalog'
                ? 'bg-brand-backgroundTheme text-white px-4 py-2 rounded-xl'
                : 'border border-gray-300 px-4 py-2 rounded-xl'
            "
            @click="view = 'catalog'"
          >
            Browse Policies
          </button>
          <button
            :class="
              view === 'my-policies'
                ? 'bg-brand-backgroundTheme text-white px-4 py-2 rounded-xl'
                : 'border border-gray-300 px-4 py-2 rounded-xl'
            "
            @click="view = 'my-policies'"
          >
            My Policies
          </button>
        </div>
      </div>

      <!-- Policy Catalog -->
      <PolicyCatalog
        v-if="view === 'catalog'"
        :availablePolicies="availablePolicies"
        @purchase="showPurchaseModal = $event"
      />

      <!-- My Policies -->
      <MyPolicies
        v-if="view === 'my-policies'"
        :userPolicies="userPolicies"
        @renew="showPurchaseModal = $event"
      />

      <!-- Purchase Modal -->
      <div class="space-y-6">
        <PurchaseModal
          :policy="showPurchaseModal"
          @close="showPurchaseModal = null"
          @purchase="purchasePolicy"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from "vue";
import PurchaseModal from "@/components/PoliciesComponents/PurchaseModal.vue";
import HeaderLayout from "@/components/layout/HeaderLayout.vue";
import PolicyCatalog from "@/components/PoliciesComponents/PolicyCatalog.vue";
import MyPolicies from "@/components/PoliciesComponents/MyPolicies.vue";

// Mock user
const user = ref({ name: "John Doe", email: "john.doe@example.com" });

// State for header collapse
const isCollapsed = ref(false);
function setIsCollapsed(value) {
  isCollapsed.value = value;
}

// Mock available policies
const availablePolicies = ref([
  { name: "Life Protect", type: "Life", coverageAmount: 1000000, premium: 1200, duration: "1 year" },
  { name: "Health Secure", type: "Health", coverageAmount: 500000, premium: 800, duration: "1 year" },
  { name: "Vehicle Shield", type: "Vehicle", coverageAmount: 300000, premium: 600, duration: "1 year" },
]);

// Mock user policies
const userPolicies = ref([
  { id: "1", name: "Life Protect", type: "Life", coverageAmount: 1000000, premium: 1200, duration: "1 year", startDate: "2025-09-01", endDate: "2026-09-01", status: "ACTIVE" },
  { id: "2", name: "Health Secure", type: "Health", coverageAmount: 500000, premium: 800, duration: "1 year", startDate: "2025-06-15", endDate: "2026-06-15", status: "ACTIVE" },
  { id: "3", name: "Vehicle Shield", type: "Vehicle", coverageAmount: 300000, premium: 600, duration: "1 year", startDate: "2024-09-10", endDate: "2025-09-11", status: "EXPIRED" },
]);

// Local states
const view = ref("catalog");
const showPurchaseModal = ref(null);

// Purchase handler
function purchasePolicy(policy) {
  const newPolicy = {
    ...policy,
    id: Date.now().toString(),
    startDate: new Date().toISOString().split("T")[0],
    endDate: new Date(Date.now() + 365 * 24 * 60 * 60 * 1000).toISOString().split("T")[0],
    status: "ACTIVE",
  };
  userPolicies.value.push(newPolicy);
  showPurchaseModal.value = null;
  alert(`Policy "${policy.name}" purchased successfully!`);
}
</script>
