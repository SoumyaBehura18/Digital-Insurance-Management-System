<template>
  <div class="bg-gray-50 rounded-lg shadow">
    <!-- Section Header -->
    <div class="px-6 py-4 border-b border-gray-200">
      <h3 class="text-lg text-gray-900">Your Support Tickets</h3>
    </div>

    <div class="p-6">
      <!-- Empty State -->
      <div v-if="tickets.length === 0" class="text-center py-12">
        <div class="mb-4">
          <svg
            class="mx-auto h-16 w-16 text-gray-400"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="1.5"
              d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"
            />
          </svg>
        </div>
        <h3 class="text-lg font-medium text-gray-900 mb-2">
          No support tickets yet
        </h3>
        <BaseButton variant="theme" @click="$emit('create-first-ticket')">
          Create Your First Ticket
        </BaseButton>
      </div>

      <!-- Tickets List -->
      <div v-else class="space-y-4">
        <div
          v-for="ticket in tickets"
          :key="ticket.id"
          class="border border-gray-200 rounded-lg p-4 hover:shadow-sm transition-shadow bg-white"
        >
          <div class="flex justify-between items-start">
            <div>
              <h3 class="font-medium text-gray-900">{{ ticket.title }}</h3>
              <p class="text-sm text-gray-500 mt-1">
                {{ ticket.description }}
              </p>
              <div class="flex items-center gap-4 mt-2">
                <span
                  class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium"
                  :class="getStatusClasses(ticket.status)"
                >
                  {{ ticket.status }}
                </span>
                <span class="text-xs text-gray-500">{{
                  formatDate(ticket.createdAt)
                }}</span>
              </div>
            </div>
            <div class="flex gap-2 mt-5">
              <BaseButton
                variant="theme"
                size="sm"
                @click="$emit('view-ticket', ticket.id)"
                class="flex items-center"
              >
                <Eye class="h-5" />
                View
              </BaseButton>
              <BaseButton
                variant="outline"
                size="sm"
                @click="$emit('create-first-ticket', ticket.id)"
                class="flex items-center"
              >
                <Pencil class="h-4" />
                Edit
              </BaseButton>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import BaseButton from "@/components/BaseButton.vue";
import { Pencil, Eye } from "lucide-vue-next";

// Props
const props = defineProps({
  tickets: {
    type: Array,
    required: true,
  },
});

// Emits
const emit = defineEmits(["create-first-ticket", "view-ticket"]);

// Methods
const getStatusClasses = (status) => {
  const statusClasses = {
    OPEN: "bg-green-100 text-green-800",
    "in-progress": "bg-yellow-100 text-yellow-800",
    CLOSED: "bg-gray-100 text-gray-800",
    RESOLVED: "bg-blue-100 text-blue-800",
  };
  return statusClasses[status] || "bg-gray-100 text-gray-800";
};

const formatDate = (date) => {
  return new Intl.DateTimeFormat("en-US", {
    year: "numeric",
    month: "short",
    day: "numeric",
  }).format(new Date(date));
};
</script>
