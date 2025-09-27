<template>
  <div class="bg-white rounded-xl shadow-md p-6">
    <h2 class="text-lg font-semibold text-gray-800 mb-4">Recent Activity</h2>

    <div class="space-y-4">
      <div
        v-for="(activity, index) in combinedActivities"
        :key="activity.id + activity.type"
        class="flex items-center gap-4 p-4 rounded-lg border transition"
        :class="rowBgClass(index)"
      >
        <!-- Icon with type color -->
        <div
          class="flex items-center justify-center w-10 h-10 rounded-full"
          :class="iconBgClass(activity.type)"
        >
          <component
            :is="getIcon(activity.type)"
            class="w-5 h-5"
            :class="iconColorClass(activity.type)"
          />
        </div>

        <!-- Activity Info -->
        <div class="flex-1">
          <p class="font-medium text-gray-900">{{ activity.title }}</p>
          <p class="text-sm text-gray-500">
            {{ activity.subtitle }}
          </p>
        </div>

        <!-- Status -->
        <span
          v-if="activity.status"
          :class="statusClass(activity.status)"
          class="px-3 py-1 rounded-full text-xs font-semibold"
        >
          {{ activity.status }}
        </span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from "vue";
import { Shield, Heart, Car, FileText, MessageSquare } from "lucide-vue-next";

const props = defineProps({
  recentPolicies: Array,
  recentClaims: Array,
  recentTickets: Array,
});

function formatDate(date) {
  return date ? new Date(date).toLocaleDateString() : "N/A";
}

// ✅ Combine all into a single unified list
const combinedActivities = computed(() => {
  const policyActivities = (props.recentPolicies || []).map((p) => ({
    id: p.id,
    title: p.policyName,
    subtitle: `Policy started on ${formatDate(p.startDate)}`,
    status: p.status,
    type: "POLICY",
  }));

  const claimActivities = (props.recentClaims || []).map((c) => ({
    id: c.id,
    title: `Claim #${c.id}`,
    subtitle: `Filed for ${c.policyName || "a policy"} on ${formatDate(c.date)}`,
    status: c.status,
    type: "CLAIM",
  }));

  const ticketActivities = (props.recentTickets || []).map((t) => ({
    id: t.id,
    title: t.title,
    subtitle: "Support Ticket Created",
    status: t.status,
    type: "TICKET",
  }));

  // ✅ Merge and sort newest first
  return [...policyActivities, ...claimActivities, ...ticketActivities].slice(-6).reverse();
});

// Style helpers
function getIcon(type) {
  switch (type) {
    case "POLICY":
      return Shield;
    case "CLAIM":
      return FileText;
    case "TICKET":
      return MessageSquare;
    default:
      return Shield;
  }
}

function iconBgClass(type) {
  switch (type) {
    case "POLICY":
      return "bg-blue-100";
    case "CLAIM":
      return "bg-yellow-100";
    case "TICKET":
      return "bg-purple-100";
    default:
      return "bg-gray-100";
  }
}

function iconColorClass(type) {
  switch (type) {
    case "POLICY":
      return "text-blue-600";
    case "CLAIM":
      return "text-yellow-600";
    case "TICKET":
      return "text-purple-600";
    default:
      return "text-gray-600";
  }
}

function statusClass(status) {
  switch (status) {
    case "ACTIVE":
    case "APPROVED":
      return "bg-green-100 text-green-800";
    case "PENDING":
      return "bg-yellow-100 text-yellow-800";
    case "REJECTED":
    case "EXPIRED":
      return "bg-red-100 text-red-800";
    default:
      return "bg-gray-100 text-gray-700";
  }
}

function rowBgClass(index) {
  return index % 2 === 0
    ? "bg-gradient-to-r from-indigo-50 to-blue-50 hover:from-indigo-100 hover:to-blue-100"
    : "bg-gradient-to-r from-purple-50 to-pink-50 hover:from-purple-100 hover:to-pink-100";
}
</script>
