<template>
  <div class="mt-4">
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
                    : 'bg-gray-100 text-gray-600 px-2 py-1 rounded text-sm'"
                >
                  {{ policy.status }}
                </span>
              </td>
              <td class="px-4 py-2">
                <button v-if="canRenew(policy)" class="border px-2 py-1 rounded text-sm"
                  @click="$emit('renew', policy)">
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
</template>

<script setup>
import { ref } from "vue";

// Mock Data (will move to Pinia/Vuex later)
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
