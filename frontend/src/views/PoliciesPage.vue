<template>
  <div class="space-y-6">
    <HeaderLayout
      :user="user"
      :isCollapsed="isCollapsed"
      :setIsCollapsed="setIsCollapsed"
      class="w-full"
    />
    <!-- Header with View Buttons -->
    <div class="flex items-center justify-between">
      <h1>{{ view === "catalog" ? "Policy Catalog" : "My Policies" }}</h1>
      <div class="flex gap-2">
        <button
          :class="view === 'catalog' ? 'bg-brand-backgroundTheme text-white px-4 py-2 rounded-xl' : 'border border-gray-300 px-4 py-2 rounded-xl'"
          @click="view = 'catalog'">
          Browse Policies
        </button>
        <button
          :class="view === 'my-policies' ? 'bg-brand-backgroundTheme text-white px-4 py-2 rounded-xl' : 'border border-gray-300 px-4 py-2 rounded-xl'"
          @click="view = 'my-policies'">
          My Policies
        </button>
      </div>
    </div>

    <!-- Catalog View -->
    <div v-if="view === 'catalog'">
      <!-- Filter -->
      <div class="flex items-center gap-4">
        <div class="flex items-center gap-2">
          <Funnel class="w-6 h-6 text-brand-textTheme" />
          <select v-model="filter" class="border border-gray-300 rounded-lg bg-gray-200 px-2 py-1">
            <option value="All">All Types</option>
            <option value="Life">Life</option>
            <option value="Health">Health</option>
            <option value="Vehicle">Vehicle</option>
          </select>
        </div>
      </div>

      <!-- Policy Cards -->
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mt-4">
        <div v-for="(policy, index) in filteredPolicies" :key="index" class="border rounded-xl shadow p-4 space-y-4">
          <div class="flex justify-between items-center">
            <h2 class="font-bold">{{ policy.name }}</h2>
            <span class="border border-gray-300 px-2 py-1 rounded-lg text-sm">{{ policy.type }}</span>
          </div>
          <div class="space-y-2">
            <div class="flex justify-between">
              <span class="text-sm text-gray-500">Coverage</span>
              <span class="font-medium">Rs.{{ policy.coverageAmount.toLocaleString() }}</span>
            </div>
            <div class="flex justify-between">
              <span class="text-sm text-gray-500">Premium</span>
              <span class="font-medium">Rs.{{ policy.premium }}/year</span>
            </div>
            <div class="flex justify-between">
              <span class="text-sm text-gray-500">Duration</span>
              <span class="font-medium">{{ policy.duration }}</span>
            </div>
          </div>
          <button class="w-full bg-brand-backgroundTheme text-white py-2 rounded-xl" @click="showPurchaseModal = policy">
            Purchase Policy
          </button>
        </div>
      </div>
    </div>

    <!-- My Policies View -->
    <div v-if="view === 'my-policies'" class="mt-4">
      <div class="border rounded-lg shadow">
        <div class="p-4 border-b">
          <h2 class="font-bold">Your Active Policies</h2>
        </div>
        <div class="p-4 overflow-x-auto">
          <table class="w-full table-auto">
            <thead>
              <tr class="bg-gray-100">
                <th class="px-4 py-2 text-left">Policy Name</th>
                <th class="px-4 py-2 text-left">Type</th>
                <th class="px-4 py-2 text-left">Start Date</th>
                <th class="px-4 py-2 text-left">End Date</th>
                <th class="px-4 py-2 text-left">Status</th>
                <th class="px-4 py-2 text-left">Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="policy in userPolicies" :key="policy.id" class="border-b">
                <td class="px-4 py-2 font-medium">{{ policy.name }}</td>
                <td class="px-4 py-2">{{ policy.type }}</td>
                <td class="px-4 py-2">{{ formatDate(policy.startDate) }}</td>
                <td class="px-4 py-2">{{ formatDate(policy.endDate) }}</td>
                <td class="px-4 py-2">
                  <span
                    :class="policy.status === 'ACTIVE' 
                    ? 'bg-green-100 text-green-800 px-2 py-1 rounded text-sm' 
                    : 'bg-gray-100 text-gray-600 px-2 py-1 rounded text-sm'">
                    {{ policy.status }}
                  </span>
                </td>
                <td class="px-4 py-2">
                  <button v-if="canRenew(policy)" class="border px-2 py-1 rounded text-sm"
                    @click="showPurchaseModal = policy">
                    Renew
                    <span v-if="policy.status === 'EXPIRED'">
                      ({{ 15 - daysSinceExpiry(policy) }} days left to renew)
                    </span>
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
      <div class="space-y-6">
    <!-- Header and Views ... same as before -->

    <!-- Use the new PurchaseModal component -->
    <PurchaseModal
      :policy="showPurchaseModal"
      @close="showPurchaseModal = null"
      @purchase="purchasePolicy"
    />
  </div>
  </div>
</template>

<script setup>
import { ref, computed } from "vue";
import { Funnel } from "lucide-vue-next";
import PurchaseModal from "@/components/PurchaseModal.vue";
import HeaderLayout from "../components/layout/HeaderLayout.vue";
const user = ref({
  name: "John Doe",
  email: "john.doe@example.com",
});

// Mock Data
const availablePolicies = ref([
  { name: "Life Protect", type: "Life", coverageAmount: 1000000, premium: 1200, duration: "1 year" },
  { name: "Health Secure", type: "Health", coverageAmount: 500000, premium: 800, duration: "1 year" },
  { name: "Vehicle Shield", type: "Vehicle", coverageAmount: 300000, premium: 600, duration: "1 year" },
]);

const userPolicies = ref([
  {
    id: "1",
    name: "Life Protect",
    type: "Life",
    coverageAmount: 1000000,
    premium: 1200,
    duration: "1 year",
    startDate: "2025-09-01",
    endDate: "2026-09-01",
    status: "ACTIVE"
  },
  {
    id: "2",
    name: "Health Secure",
    type: "Health",
    coverageAmount: 500000,
    premium: 800,
    duration: "1 year",
    startDate: "2025-06-15",
    endDate: "2026-06-15",
    status: "ACTIVE"
  },
  {
    id: "3",
    name: "Vehicle Shield",
    type: "Vehicle",
    coverageAmount: 300000,
    premium: 600,
    duration: "1 year",
    startDate: "2024-09-10",
    endDate: "2025-09-11",
    status: "EXPIRED"
  }
]);

// Local States
const view = ref("catalog");
const filter = ref("All");
const showPurchaseModal = ref(null);

const filteredPolicies = computed(() =>
  availablePolicies.value.filter(p => filter.value === "All" || p.type === filter.value)
);

function purchasePolicy(policy) {
  const newPolicy = {
    ...policy,
    id: Date.now().toString(),
    startDate: new Date().toISOString().split("T")[0],
    endDate: new Date(Date.now() + 365*24*60*60*1000).toISOString().split("T")[0],
    status: "ACTIVE",
  };
  userPolicies.value.push(newPolicy);
  showPurchaseModal.value = null;
  alert("Policy purchased successfully!");
}

function formatDate(date) {
  return new Date(date).toLocaleDateString();
}

function canRenew(policy) {
  if (policy.status === "ACTIVE") return true;
  if (policy.status === "EXPIRED" && policy.endDate) {
    const endDate = new Date(policy.endDate);
    const now = new Date();
    const diffDays = (now - endDate) / (1000 * 60 * 60 * 24);
    return diffDays <= 15 && diffDays >= 0;
  }
  return false;
}

function daysSinceExpiry(policy) {
  if (!policy.endDate) return null;
  const endDate = new Date(policy.endDate);
  const now = new Date();
  return Math.ceil((now - endDate) / (1000 * 60 * 60 * 24));
}
</script>


