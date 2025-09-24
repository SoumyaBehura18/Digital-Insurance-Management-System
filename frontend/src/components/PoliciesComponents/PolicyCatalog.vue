<template>
  <!-- Catalog View -->
  <div>
    <!-- Filter -->
    <div class="flex items-center gap-4">
      <div class="flex items-center gap-2">
        <Funnel class="w-6 h-6 text-brand-textTheme" />
        <select v-model="filter" class="border border-gray-300 rounded-lg bg-gray-200 px-2 py-1">
          <option value="All">All Typess</option>
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
        <button class="w-full bg-brand-backgroundTheme text-white py-2 rounded-xl" @click="$emit('purchase', policy)">
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from "vue";
import { Funnel } from "lucide-vue-next";

// Mock Data (later replace with state management)
const availablePolicies = ref([
  { name: "Life Protect", type: "Life", coverageAmount: 1000000, premium: 1200, duration: "1 year" },
  { name: "Health Secure", type: "Health", coverageAmount: 500000, premium: 800, duration: "1 year" },
  { name: "Vehicle Shield", type: "Vehicle", coverageAmount: 300000, premium: 600, duration: "1 year" },
]);

// Local state for filtering
const filter = ref("All");

const filteredPolicies = computed(() =>
  availablePolicies.value.filter(p => filter.value === "All" || p.type === filter.value)
);

</script>
