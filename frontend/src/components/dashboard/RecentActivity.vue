<template>
  <div class="bg-white rounded-xl shadow-md p-6">
    <h2 class="text-lg font-semibold text-gray-800 mb-4">Recent Activity</h2>
    <div class="space-y-4">
      <div
        v-for="(policy, index) in recentPolicies"
        :key="policy.id"
        class="flex items-center gap-4 p-4 rounded-lg border transition"
        :class="rowBgClass(index)"
      >
        <!-- Icon with colored circle -->
        <div
          class="flex items-center justify-center w-10 h-10 rounded-full"
          :class="iconBgClass(policy.policyType)"
        >
          <component
            :is="getIcon(policy.policyType)"
            class="w-5 h-5"
            :class="iconColorClass(policy.policyType)"
          />
        </div>

        <!-- Policy Info -->
        <div class="flex-1">
          <p class="font-medium text-gray-900">{{ policy.policyName }}</p>
          <p class="text-sm text-gray-500">
            Started on {{ formatDate(policy.startDate) }}
          </p>
        </div>

        <!-- Status Badge -->
        <span
          :class="statusClass(policy.status)"
          class="px-3 py-1 rounded-full text-xs font-semibold"
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
  recentPolicies: Array,
});

function formatDate(date) {
  return date ? new Date(date).toLocaleDateString() : "N/A";
}

function statusClass(status) {
  switch (status) {
    case "ACTIVE":
      return "bg-green-100 text-green-800";
    case "RENEWED":
      return "bg-blue-100 text-blue-800";
    case "EXPIRED":
      return "bg-red-100 text-red-800";
    case "CANCELLED":
      return "bg-gray-200 text-gray-700";
    default:
      return "bg-gray-100 text-gray-700";
  }
}

// Icon mapping
function getIcon(type) {
  switch (type?.toUpperCase()) {
    case "HEALTH":
      return Heart;
    case "LIFE":
      return Shield;
    case "VEHICLE":
      return Car;
    default:
      return Shield;
  }
}

// Icon background colors
function iconBgClass(type) {
  switch (type?.toUpperCase()) {
    case "HEALTH":
      return "bg-green-100";
    case "LIFE":
      return "bg-pink-100";
    case "VEHICLE":
      return "bg-blue-100";
    default:
      return "bg-gray-100";
  }
}

// Icon colors
function iconColorClass(type) {
  switch (type?.toUpperCase()) {
    case "HEALTH":
      return "text-green-600";
    case "LIFE":
      return "text-pink-600";
    case "VEHICLE":
      return "text-blue-600";
    default:
      return "text-gray-600";
  }
}

// Alternate row gradient backgrounds
function rowBgClass(index) {
  return index % 2 === 0
    ? "bg-gradient-to-r from-purple-50 to-indigo-50 hover:from-purple-100 hover:to-indigo-100"
    : "bg-gradient-to-r from-pink-50 to-rose-50 hover:from-pink-100 hover:to-rose-100";
}
</script>
