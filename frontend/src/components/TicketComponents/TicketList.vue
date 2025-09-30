<template>
  <div class="bg-gray-50 rounded-lg shadow">
    <div class="px-4 py-3 sm:px-6 border-b border-gray-200">
      <h3 class="text-lg text-gray-900 font-semibold">Your Support Tickets</h3>
    </div>

    <div class="p-4 sm:p-6">
      <!-- No tickets -->
      <div v-if="tickets.length === 0" class="text-center py-12">
        <div class="mb-4">
          <svg
            class="mx-auto h-12 w-12 sm:h-16 sm:w-16 text-gray-400"
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
        <h3 class="text-md sm:text-lg font-medium text-gray-900 mb-2">
          No support tickets yet
        </h3>
        <BaseButton variant="theme" @click="$emit('create-first-ticket')">
          Create Your First Ticket
        </BaseButton>
      </div>

      <!-- Tickets list -->
      <div v-else class="space-y-3 sm:space-y-4">
        <div
          v-for="ticket in tickets"
          :key="ticket.id"
          class="border border-gray-200 rounded-lg p-3 sm:p-4 hover:shadow-sm transition-shadow bg-white flex flex-col sm:flex-row sm:justify-between sm:items-start gap-2 sm:gap-4"
        >
          <div class="flex-1 min-w-0">
            <h3 class="font-medium text-gray-900 text-sm sm:text-base truncate">
              {{ ticket.subject }}
            </h3>
            <p class="text-xs sm:text-sm text-gray-500 mt-1 truncate">
              {{ ticket.description }}
            </p>
            <div class="flex flex-wrap items-center gap-2 mt-2 text-xs sm:text-sm">
              <span
                class="inline-flex items-center px-2 py-0.5 rounded-full font-medium"
                :class="getStatusClasses(ticket.status ?? 'OPEN')"
              >
                <component
                  :is="getStatusClassIcon(ticket.status ?? 'OPEN')"
                  class="w-3 h-3 mr-1"
                />
                {{ ticket.status ?? "OPEN" }}
              </span>
              <span class="text-gray-500">
                {{ formatDate(ticket.createdAt) }}
              </span>
            </div>
          </div>

          <div class="flex gap-2 mt-2 sm:mt-0 flex-wrap sm:flex-nowrap">
            <BaseButton
              variant="theme"
              size="sm"
              @click="handleViewTicket(ticket)"
              class="flex items-center whitespace-nowrap"
            >
              <Eye class="h-4 sm:h-5" />
              <span class="ml-1">View</span>
            </BaseButton>
            <BaseButton
              variant="outline"
              size="sm"
              @click="$emit('update-ticket', ticket)"
              class="flex items-center whitespace-nowrap"
            >
              <Pencil class="h-3.5 sm:h-4" />
              <span class="ml-1">Edit</span>
            </BaseButton>
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

const props = defineProps({
  tickets: {
    type: Array,
    required: true,
    default: () => [],
  },
});

const emit = defineEmits([
  "create-first-ticket",
  "view-ticket",
  "update-ticket",
  "resolve-ticket",
  "close-ticket",
]);

const isModalOpen = ref(false);
const selectedTicket = ref(null);

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
