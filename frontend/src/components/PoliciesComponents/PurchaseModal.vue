<template>
  <div v-if="policy" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-white p-6 rounded-lg w-[32rem] space-y-4">
      <h3 class="font-bold text-lg">Confirm Purchase</h3>
      <p>Review the policy details before confirming your purchase.</p>
      <div class="space-y-2">
        <p><strong>Policy:</strong> {{ policy.name }}</p>
        <p><strong>Coverage:</strong> Rs.{{ policy.coverageAmount.toLocaleString() }}</p>
        <p><strong>Annual Premium:</strong> Rs.{{ policy.premium }}</p>
        <p><strong>Duration:</strong> {{ policy.duration }}</p>
        <p><strong>Start Date:</strong> {{ formatDate(new Date()) }}</p>
        <p><strong>End Date:</strong> {{ formatDate(new Date(Date.now() + 365 * 24 * 60 * 60 * 1000)) }}</p>
      </div>
      <div class="flex justify-end gap-2 mt-4">
        <button class="border px-4 py-2 rounded" @click="$emit('close')">Cancel</button>
        <button class="bg-brand-backgroundTheme text-white px-4 py-2 rounded" @click="confirmPurchase">
          Confirm Purchase
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from "vue";

const props = defineProps({
  policy: Object
});

const emits = defineEmits(["close", "purchase"]);

function formatDate(date) {
  return new Date(date).toLocaleDateString();
}

function confirmPurchase() {
  emits("purchase", props.policy);
}
</script>
