<template>
  <div class="p-4 overflow-x-auto">
    <!-- Loading -->
    <div v-if="loading" class="flex items-center justify-center py-8">
      <svg class="animate-spin h-6 w-6 text-blue-500 mr-2" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
        <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
        <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v4a4 4 0 00-4 4H4z"></path>
      </svg>
      <span class="text-gray-600">Loading your policies...</span>
    </div>

    <!-- Error -->
    <div v-else-if="error" class="text-red-600 py-2 text-center">
      {{ error }}
    </div>

    <!-- Policies Table -->
    <table v-else class="w-full table-auto">
      <thead>
        <tr class="bg-gray-100">
          <th class="px-4 py-2 text-left">Policy Name</th>
          <th class="px-4 py-2 text-left">Type</th>
          <th class="px-4 py-2 text-left">Start Date</th>
          <th class="px-4 py-2 text-left">End Date</th>
          <th class="px-4 py-2 text-left">Status</th>
          <th class="px-4 py-2 text-left">Premium Paid</th>
          <th class="px-4 py-2 text-left">Claim Status</th>
          <th class="px-4 py-2 text-left">Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="policy in policies" :key="policy.id" class="border-b">
          <td class="px-4 py-2 font-medium">{{ policy.policyName }}</td>
          <td class="px-4 py-2">{{ policy.policyType }}</td>
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
          <td class="px-4 py-2">Rs.{{ formatCurrency(policy.premiumPaid) }}</td>
          <td class="px-4 py-2">
            <span v-if="!policy.noClaimBonus" class="bg-green-100 text-green-800 px-2 py-1 rounded text-sm">
              Eligible
            </span>
            <span v-else class="bg-red-100 text-red-800 px-2 py-1 rounded text-sm">
              Not Eligible
            </span>
          </td>
          <td class="px-4 py-2">
            <button
              v-if="canRenew(policy)"
              class="border px-2 py-1 rounded text-sm"
              @click="$emit('renew', policy)"
            >
              Renew
              <span v-if="policy.status === 'EXPIRED'">
                ({{ 15 - daysSinceExpiry(policy) }} days left)
              </span>
            </button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { onMounted, computed } from "vue";
import { useStore } from "vuex";

const store = useStore();

// Getters
const policies = computed(() => store.getters["userPolicies/getPolicies"]);
const loading = computed(() => store.getters["userPolicies/isLoading"]);
const error = computed(() => store.getters["userPolicies/getError"]);

function formatDate(date) {
  if (!date) return "N/A";
  return new Date(date).toLocaleDateString();
}

function formatCurrency(value) {
  if (!value || isNaN(value)) return "N/A";
  return Number(value).toLocaleString();
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

// Fetch policies on mount
onMounted(() => {
  const storedUser = localStorage.getItem("currentUser")
  console.log("Stored user raw:", storedUser)
  
  const userId= storedUser ? JSON.parse(storedUser).userId : null;
  console.log("Resolved userId:", userId)

  if (userId) {
    store.dispatch("userPolicies/fetchUserPolicies", userId)
  }
})
</script>
