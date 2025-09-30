<template>
  <div v-if="policy" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-white p-6 rounded-lg w-[32rem] space-y-4 shadow-lg">
      <!-- Title -->
      <h3 class="font-bold text-lg text-gray-800">
        {{ policy.isRenewal ? "Confirm Renewal" : "Confirm Purchase" }}
      </h3>

      <p class="text-gray-600">
        Review the policy details before confirming your
        {{ policy.isRenewal ? "renewal" : "purchase" }}.
      </p>

      <!-- Policy details -->
      <div class="space-y-2 text-gray-700">
        <p><strong>Policy:</strong> {{ policy.policyName ?? policy.name ?? "N/A" }}</p>
        <p><strong>Coverage:</strong> Rs.{{ formatCurrency(policy.coverage) }}</p>

        <!-- PREMIUM DISPLAY SECTION -->
        <div>
          <p class="flex items-center gap-2">
            <strong>{{ policy.isRenewal ? "Renewal Premium" : "Annual Premium" }}:</strong>

            <!-- If eligible for NCB discount -->
            <template v-if="policy.isRenewal && policy.noClaimBonus === false">
              <span class="line-through text-gray-400">
                Rs.{{ formatCurrency(policy.renewalRate) }}
              </span>
              <span class="text-green-600 font-semibold">
                Rs.{{ formatCurrency(premiumToPay) }}
              </span>
            </template>

            <!-- Otherwise normal premium -->
            <template v-else>
              <span>Rs.{{ formatCurrency(premiumToPay) }}</span>
            </template>
          </p>

          <!-- Discount message -->
          <div
            v-if="policy.isRenewal && policy.noClaimBonus === false"
            class="mt-1 text-green-700 bg-green-50 border border-green-200 p-2 rounded-md text-sm"
          >
            🎉 You are eligible for a <strong>10% No Claim Bonus discount!</strong>
          </div>
        </div>

        <p><strong>Duration:</strong> {{ policy.duration ?? "N/A" }} months</p>
        <p><strong>Start Date:</strong> {{ formatDate(startDate) }}</p>
        <p><strong>End Date:</strong> {{ formatDate(endDate) }}</p>
      </div>

      <!-- Buttons -->
      <div class="flex justify-end gap-2 mt-4">
        <button class="border px-4 py-2 rounded hover:bg-gray-100" @click="$emit('close')">
          Cancel
        </button>
        <button
          class="bg-brand-backgroundTheme text-white px-4 py-2 rounded hover:opacity-90"
          :disabled="loading"
          @click="confirmPurchase"
        >
          {{ loading ? "Processing..." : (policy.isRenewal ? "Confirm Renewal" : "Confirm Purchase") }}
        </button>
      </div>

      <!-- Feedback messages -->
      <div v-if="error" class="text-red-500 mt-2">{{ error }}</div>
      <div v-if="success" class="text-green-500 mt-2">
        {{ policy.isRenewal ? "Renewal successful!" : "Purchase successful!" }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from "vue";
import { useStore } from "vuex";
import { makeRequestWithToken } from "@/utils/requests";

const props = defineProps({
  policy: Object
});
const emits = defineEmits(["close", "purchased"]);

const store = useStore();

const loading = ref(false);
const error = ref(null);
const success = ref(false);

// Start date is today
const startDate = new Date();

// End date computed from duration
const endDate = computed(() => {
  const durationMonths = props.policy?.duration ?? 12;
  const date = new Date(startDate);
  date.setMonth(date.getMonth() + durationMonths);
  return date;
});

// Premium calculation with NCB discount
const premiumToPay = computed(() => {
  if (!props.policy) return null;

  if (props.policy.isRenewal && props.policy.renewalRate) {
    // ✅ Apply 10% discount if eligible for No Claim Bonus
    if (props.policy.noClaimBonus === false) {
      return (props.policy.renewalRate * 0.9).toFixed(2);
    }
    return props.policy.renewalRate;
  }

  // For new purchases
  return props.policy.premium ?? props.policy.premiumRate;
});

function formatDate(date) {
  if (!date) return "N/A";
  return new Date(date).toLocaleDateString();
}

function formatCurrency(value) {
  if (value == null || isNaN(value)) return "N/A";
  return Number(value).toLocaleString();
}

async function confirmPurchase() {
  error.value = null;
  success.value = false;
  loading.value = true;

  try {
    let updatedPolicy;

    if (props.policy?.isRenewal) {
      // Renewal via Vuex
      updatedPolicy = await store.dispatch("userPolicies/renewPolicy", {
        ...props.policy,
        premiumPaid: premiumToPay.value, 
        startDate: startDate.toISOString().split("T")[0],
        endDate: endDate.value.toISOString().split("T")[0]
      });
    } else {
      // Purchase new
      const storedUser = localStorage.getItem("currentUser");
      if (!storedUser) throw new Error("User not logged in");

      const userId = JSON.parse(storedUser).userId;
      const payload = {
        userId,
        policyId: props.policy.policyId ?? props.policy.id,
        startDate: startDate.toISOString().split("T")[0],
        endDate: endDate.value.toISOString().split("T")[0],
        status: "ACTIVE",
        premiumPaid: premiumToPay.value
      };

      const response = await makeRequestWithToken("POST", "/user/policy/purchase", payload);
      updatedPolicy = response.data;

      // Update local store
      store.commit("userPolicies/SET_POLICIES", [...store.state.userPolicies.policies, updatedPolicy]);
    }

    // Notify and close modal
    emits("purchased", updatedPolicy);
    emits("close");
    success.value = true;
  } catch (err) {
    error.value = err.response?.data || err.message || "Action failed";
  } finally {
    loading.value = false;
  }
}
</script>
