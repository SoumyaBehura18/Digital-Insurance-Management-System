<template>
  <div class="bg-white rounded-lg shadow p-4">
    <h2 class="font-semibold mb-4">Recent Activity</h2>
    <div class="space-y-4">
      <div
        v-for="policy in recentPolicies"
        :key="policy.id"
        class="flex items-center gap-4 p-4 border rounded-lg"
      >
        <!-- Dynamic icon based on policy type -->
        <component
          :is="getIcon(policy.policyType)"
          class="w-6 h-6 text-brand-backgroundTheme"
        />

        <div class="flex-1">
          <p class="font-medium">{{ policy.policyName }}</p>
          <p class="text-sm text-brand-textTheme">
            Started on {{ formatDate(policy.startDate) }}
          </p>
        </div>
        <span
          :class="statusClass(policy.status)"
          class="px-2 py-1 rounded text-sm font-medium"
        >
          {{ policy.status }}
        </span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { Shield, Heart, Car } from "lucide-vue-next";

const props = defineProps({
  recentPolicies: Array
});

function formatDate(date) {
  return date ? new Date(date).toLocaleDateString() : "N/A";
}

function statusClass(status) {
  switch (status) {
    case "ACTIVE": return "bg-brand-backgroundTheme text-white";
    case "RENEWED": return "bg-gray-200 text-gray-700";
    case "EXPIRED": return "bg-red-100 text-red-800";
    case "CANCELLED": return "bg-gray-300 text-gray-600";
    default: return "bg-gray-100 text-gray-700";
  }
}

// Returns the appropriate icon component based on policy type
function getIcon(type) {
  switch (type) {
    case "HEALTH": return Heart;
    case "LIFE": return Shield;
    case "VEHICLE": return Car;
    default: return Shield;
  }
}
</script>
