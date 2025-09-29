<template>
  <div class="bg-gray-50 rounded-lg shadow">
    <div class="px-6 py-4 border-b border-gray-200">
      <h3 class="text-lg text-gray-900">Your Support Tickets</h3>
    </div>

    <div class="p-6">
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

      <div v-else class="space-y-4">
        <div
          v-for="ticket in tickets"
          :key="ticket.id"
          class="border border-gray-200 rounded-lg p-4 hover:shadow-sm transition-shadow bg-white"
        >
          <div class="flex justify-between items-start">
            <div>
              <h3 class="font-medium text-gray-900">{{ ticket.subject }}</h3>
              <p class="text-sm text-gray-500 mt-1 w-64 truncate">
                {{ ticket.description }}
              </p>
              <div class="flex items-center gap-4 mt-2">
                <span
                  class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium"
                  :class="getStatusClasses(ticket.status ?? 'OPEN')"
                >
                  <component
                    :is="getStatusClassIcon(ticket.status ?? 'OPEN')"
                    class="w-3 h-3 mr-1"
                  />
                  {{ ticket.status ?? "OPEN" }}
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
                @click="handleViewTicket(ticket)"
                class="flex items-center"
              >
                <Eye class="h-5" />
                View
              </BaseButton>
              <BaseButton
                variant="outline"
                size="sm"
                @click="$emit('update-ticket', ticket)"
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

    <TicketDetailsModal
      :is-open="isModalOpen"
      :ticket="selectedTicket"
      @close="handleCloseModal"
      @update-ticket="handleUpdateTicket"
      @resolve-ticket="handleResolveTicket"
      @close-ticket="handleCloseTicket"
    />
  </div>
</template>

<script setup>
import { ref } from "vue";
import BaseButton from "@/components/BaseButton.vue";
import TicketDetailsModal from "@/components/TicketComponents/TicketDetailsModal.vue";
import { Pencil, Eye } from "lucide-vue-next";
import {
  getStatusClassIcon,
  getStatusClasses,
  formatDate,
} from "@/utils/helperFunctions";

// Props
const props = defineProps({
  tickets: {
    type: Array,
    required: true,
    default: () => [],
  },
  ticketSelected: {
    type: Object,
    default: null,
  },
});

// Emits
const emit = defineEmits([
  "create-first-ticket",
  "view-ticket",
  "update-ticket",
  "resolve-ticket",
]);

// Modal state
const isModalOpen = ref(false);
const selectedTicket = ref(null);

// Methods
const handleViewTicket = (ticket) => {
  selectedTicket.value = ticket;
  isModalOpen.value = true;
  emit("view-ticket", ticket.id);
};

const handleCloseModal = () => {
  isModalOpen.value = false;
  selectedTicket.value = null;
};

const handleUpdateTicket = (ticket) => {
  emit("update-ticket", ticket);
};

const handleResolveTicket = (ticket) => {
  emit("resolve-ticket", ticket);
};

const handleCloseTicket = (ticket) => {
  emit("close-ticket", ticket);
};
</script>
