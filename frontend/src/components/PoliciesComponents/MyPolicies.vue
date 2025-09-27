<template>
  <div class="p-4 overflow-x-auto">
    <!-- Loading -->
    <div v-if="loading" class="flex items-center justify-center py-8">
      <svg class="animate-spin h-6 w-6 text-blue-500 mr-2" xmlns="http://www.w3.org/2000/svg" fill="none"
        viewBox="0 0 24 24">
        <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
        <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v4a4 4 0 00-4 4H4z"></path>
      </svg>
      <span class="text-gray-600">Loading your policies...</span>
    </div>

    <!-- Error -->
    <div v-else-if="error" class="text-red-600 py-2 text-center font-semibold bg-red-50 rounded-lg">
      {{ error }}
    </div>

    <!-- Policies Table -->
    <div v-else class="bg-white shadow-lg rounded-lg overflow-hidden">
      <table class="w-full table-auto border-collapse">
        <thead>
          <tr class="bg-[#e0e0ff] text-[#6c63ff]">
            <th class="px-4 py-3 text-left font-semibold">Policy Name</th>
            <th class="px-4 py-3 text-left font-semibold">Type</th>
            <th class="px-4 py-3 text-left font-semibold">Start Date</th>
            <th class="px-4 py-3 text-left font-semibold">End Date</th>
            <th class="px-4 py-3 text-left font-semibold">Status</th>
            <th class="px-4 py-3 text-left font-semibold">Premium Paid</th>
            <th class="px-4 py-3 text-left font-semibold">NCB Eligibility</th>
            <th class="px-4 py-3 text-left font-semibold">Actions</th>
            <th class="px-4 py-3 text-left font-semibold">Cancel</th> <!-- New column -->
          </tr>
        </thead>
        <tbody>
          <tr v-for="(policy, index) in policies" :key="policy.id"
            :class="index % 2 === 0 ? 'bg-white' : 'bg-purple-50'" class="hover:bg-purple-100 transition-colors">
            <td class="px-4 py-3 font-medium text-gray-800">
              {{ policy.policyName }}
            </td>
            <td class="px-4 py-3 text-gray-700">{{ policy.policyType }}</td>
            <td class="px-4 py-3 text-gray-700">
              {{ formatDate(policy.startDate) }}
            </td>
            <td class="px-4 py-3 text-gray-700">
              {{ formatDate(policy.endDate) }}
            </td>
            <td class="px-4 py-3">
              <span :class="{
                'bg-green-100 text-green-800': policy.status === 'ACTIVE',
                'bg-gray-200 text-gray-700': policy.status === 'RENEWED',
                'bg-red-100 text-red-800': policy.status === 'EXPIRED',
                'bg-gray-300 text-gray-600': policy.status === 'CANCELLED'
              }" class="px-3 py-1 rounded-full text-xs font-semibold">
                {{ policy.status }}
              </span>
            </td>
            <td class="px-4 py-3 text-gray-700">
              Rs.{{ formatCurrency(policy.premiumPaid) }}
            </td>
            <td class="px-4 py-3">
              <span v-if="!policy.noClaimBonus"
                class="bg-green-100 text-green-800 px-3 py-1 rounded-full text-xs font-semibold">
                Eligible
              </span>
              <span v-else class="bg-red-100 text-red-800 px-3 py-1 rounded-full text-xs font-semibold">
                Not Eligible
              </span>
            </td>
            <td class="px-4 py-3">
              <button v-if="canRenew(policy)"
                class="bg-purple-500 hover:bg-purple-600 text-white px-3 py-1 rounded-full text-xs font-medium transition"
                @click="openModal(policy)">
                Renew
                <span v-if="policy.status === 'EXPIRED'">
                  ({{ 15 - daysSinceExpiry(policy) }} days left)
                </span>
              </button>
            </td>
            <td class="px-4 py-3 text-center">
              <button v-show="policy.status !== 'CANCELLED'" @click="cancelPolicy(policy)" class="text-red-500 hover:text-red-700 transition">
                <Trash class="w-5 h-5" />
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Purchase / Renewal Modal -->
    <PurchaseModal v-if="selectedPolicy" :policy="selectedPolicy" @close="selectedPolicy = null"
      @purchased="openModal" />
  </div>
</template>

<script setup>
import { ref, onMounted, computed, nextTick } from "vue";
import { useStore } from "vuex";
import PurchaseModal from "@/components/PoliciesComponents/PurchaseModal.vue"; // ✅ import your modal
import { Trash } from "lucide-vue-next";


const store = useStore();

const selectedPolicy = ref(null);

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

function openModal(userPolicy) {
  console.log("Opening modal for userPolicy:", userPolicy);

  const allPolicies = store.getters["policies/getAllPolicies"] || [];
  console.log("All base policies:", allPolicies);

  const basePolicy = allPolicies.find(
    (p) => p.policyId === (userPolicy.policyId || userPolicy.id)
  );
  if (!basePolicy) {
    console.error(
      "❌ Base policy not found for:",
      userPolicy.policyId || userPolicy.id
    );
    return;
  }

  console.log("✅ Base policy found:", basePolicy);

  selectedPolicy.value = null; // reset first
  nextTick(() => {
    selectedPolicy.value = {
      ...basePolicy,
      ...userPolicy,
      isRenewal: true
    };
  });
}

async function cancelPolicy(policy) {
  if (!confirm(`Are you sure you want to cancel ${policy.policyName}?`)) return;

  try {
    await store.dispatch("userPolicies/cancelPolicy", policy);
     const storedUser = localStorage.getItem("currentUser");
    const userId = storedUser ? JSON.parse(storedUser).userId : null;
    if (userId) {
      await store.dispatch("userPolicies/fetchUserPolicies", userId);
    }
  } catch (err) {
    console.error("Cancel failed:", err);
  }
}


function checkAndExpirePolicies(policies) {
  const now = new Date();

  policies.forEach(policy => {
    if (!policy.endDate) return;

    const endDate = new Date(policy.endDate);
    const diffDays = (now - endDate) / (1000 * 60 * 60 * 24);

    // Expire if more than 15 days past end date
    if (diffDays > 15 && policy.status !== 'EXPIRED') {
      store.dispatch('userPolicies/expirePolicy', policy);
    }
  });
}

// Example: call after fetching user policies
onMounted(async () => {
  const storedUser = localStorage.getItem('currentUser');
  const userId = storedUser ? JSON.parse(storedUser).userId : null;

  if (userId) {
    await store.dispatch('userPolicies/fetchUserPolicies', userId);
    checkAndExpirePolicies(store.state.userPolicies.policies);
  }
});

</script>
