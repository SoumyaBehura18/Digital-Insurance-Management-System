<template>
  <div v-if="policy" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-white p-6 rounded-lg w-[32rem] space-y-4">
      <h3 class="font-bold text-lg">
        {{ policy.isRenewal ? "Confirm Renewal" : "Confirm Purchase" }}
      </h3>
      <p>
        Review the policy details before confirming your
        {{ policy.isRenewal ? "renewal" : "purchase" }}.
      </p>

      <div class="space-y-2">
        <p><strong>Policy:</strong> {{ policy.policyName ?? policy.name ?? "N/A" }}</p>
        <p><strong>Coverage:</strong> Rs.{{ formatCurrency(policy.coverage) }}</p>

        <p>
          <strong>{{ policy.isRenewal ? "Renewal Premium" : "Annual Premium" }}:</strong>
          Rs.{{ formatCurrency(premiumToPay) }}
        </p>

        <p><strong>Duration:</strong> {{ policy.duration ?? "N/A" }} months</p>
        <p><strong>Start Date:</strong> {{ formatDate(startDate) }}</p>
        <p><strong>End Date:</strong> {{ formatDate(endDate) }}</p>
      </div>

      <div class="flex justify-end gap-2 mt-4">
        <button class="border px-4 py-2 rounded" @click="$emit('close')">
          Cancel
        </button>
        <button class="bg-brand-backgroundTheme text-white px-4 py-2 rounded" :disabled="loading"
          @click="confirmPurchase">
          {{ loading ? "Processing..." : (policy.isRenewal ? "Confirm Renewal" : "Confirm Purchase") }}
        </button>
      </div>

      <div v-if="error" class="text-red-500 mt-2">{{ error }}</div>
      <div v-if="success" class="text-green-500 mt-2">
        {{ policy.isRenewal ? "Renewal successful!" : "Purchase successful!" }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, computed } from "vue";
import { defineProps, defineEmits } from "vue";
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

// Start date is always today
const startDate = new Date();

// Compute end date dynamically based on policy.duration
const endDate = computed(() => {
  const durationMonths = props.policy?.duration ?? 12; // default 12 months
  const date = new Date(startDate);
  date.setMonth(date.getMonth() + durationMonths);
  return date;
});

// Decide whether to use premium or renewalRate
const premiumToPay = computed(() => {
  if (!props.policy) return null;
  if (props.policy.isRenewal && props.policy.renewalRate) {
    return props.policy.renewalRate;
  }
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
      // Call Vuex action for renewal
      updatedPolicy = await store.dispatch('userPolicies/renewPolicy', props.policy);
    } else {
      // Normal purchase flow
      const storedUser = localStorage.getItem('currentUser');
      if (!storedUser) throw new Error('User not logged in');

      const userId = JSON.parse(storedUser).userId;
      const payload = {
        userId,
        policyId: props.policy.policyId ?? props.policy.id,
        startDate: startDate.toISOString().split('T')[0],
        endDate: endDate.value.toISOString().split('T')[0],
        status: 'ACTIVE',
        premiumPaid: premiumToPay.value
      };

      const response = await makeRequestWithToken(
        'POST',
        '/user/policy/purchase',
        payload
      );

      updatedPolicy = response.data;

      // Update store
      store.commit('userPolicies/SET_POLICIES', [...store.state.userPolicies.policies, updatedPolicy]);
    }

    // Notify parent and close modal
    emits('purchased', updatedPolicy);
    emits('close'); // ✅ close modal

  } catch (err) {
    error.value = err.response?.data || err.message || 'Action failed';
  } finally {
    loading.value = false;
  }
}

</script>
