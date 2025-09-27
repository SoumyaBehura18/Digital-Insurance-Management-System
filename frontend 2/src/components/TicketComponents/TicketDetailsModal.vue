<template>
  <BaseModal
    :is-open="isOpen"
    :title="ticket?.title || '-'"
    :subtitle="`Ticket #${ticket?.id || ''}`"
    size="lg"
    @close="$emit('close')"
  >
    <!-- Modal Content -->
    <div v-if="ticket" class="space-y-6">
      <!-- Status and Date Info -->
      <div class="flex items-center justify-between">
        <div class="flex items-center gap-3">
          <span
            class="inline-flex items-center px-3 py-1 rounded-full text-sm font-medium"
            :class="getStatusClasses(ticket.status)"
          >
            <component
              :is="getStatusClassIcon(ticket.status)"
              class="w-3 h-3 mr-1"
            />
            {{ ticket.status }}
          </span>
          <span class="text-sm text-gray-500">
            Created {{ formatDate(ticket.createdAt) }}
          </span>
        </div>
      </div>

      <!-- Description -->
      <div>
        <h4 class="text-sm font-medium text-gray-900 mb-2">Description</h4>
        <p class="text-gray-700 leading-relaxed">
          {{ ticket.description }}
        </p>
      </div>

      <!-- Additional Details -->
      <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
        <!-- Related Policy -->
        <div v-if="ticket.relatedPolicy">
          <h4 class="text-sm font-medium text-gray-900 mb-2">Related Policy</h4>
          <div class="bg-gray-50 rounded-lg p-3">
            <p class="text-sm text-gray-700">{{ ticket.relatedPolicy }}</p>
          </div>
        </div>

        <!-- Related Claim -->
        <div v-if="ticket.relatedClaim">
          <h4 class="text-sm font-medium text-gray-900 mb-2">Related Claim</h4>
          <div class="bg-gray-50 rounded-lg p-3">
            <p class="text-sm text-gray-700">{{ ticket.relatedClaim }}</p>
          </div>
        </div>
      </div>

      <!-- Timeline/Activity (placeholder) -->
      <div>
        <h4 class="text-sm font-medium text-gray-900 mb-3">
          Activity Timeline
        </h4>
        <div class="space-y-3">
          <div class="flex gap-3">
            <div
              class="w-2 h-2 bg-green-500 rounded-full mt-2 flex-shrink-0"
            ></div>
            <div>
              <p class="text-sm text-gray-900">Ticket created</p>
              <p class="text-xs text-gray-500">
                {{ formatDateTime(ticket.createdAt) }}
              </p>
            </div>
          </div>
          <div v-if="ticket.status === 'RESOLVED'" class="flex gap-3">
            <div
              class="w-2 h-2 bg-blue-500 rounded-full mt-2 flex-shrink-0"
            ></div>
            <div>
              <p class="text-sm text-gray-900">Ticket resolved</p>
              <p class="text-xs text-gray-500">
                {{ formatDateTime(ticket.updatedAt || ticket.createdAt) }}
              </p>
            </div>
          </div>
        </div>
      </div>

      <!-- Contact Information -->
      <div class="bg-blue-50 rounded-lg p-4">
        <h4 class="text-sm font-medium text-blue-900 mb-2">Need Help?</h4>
        <p class="text-sm text-blue-700 mb-2">
          Our support team is here to help you resolve this issue.
        </p>
        <p class="text-sm text-blue-600">
          📧 support@company.com • 📞 1-800-SUPPORT
        </p>
      </div>
    </div>

    <!-- Modal Footer -->
    <template #footer>
      <div class="flex gap-3 justify-end">
        <BaseButton variant="outline" @click="$emit('close')">
          Close
        </BaseButton>
        <BaseButton
          v-if="ticket?.status === 'OPEN'"
          variant="outline"
          @click="handleUpdateTicket"
        >
          Update Ticket
        </BaseButton>
        <BaseButton
          v-if="ticket?.status !== 'RESOLVED'"
          customClass="bg-green-600 hover:bg-green-700"
          @click="handleResolveTicket"
        >
          Mark as Resolved
        </BaseButton>
      </div>
    </template>
  </BaseModal>
</template>

<script setup>
import BaseModal from "@/components/BaseModal.vue";
import BaseButton from "@/components/BaseButton.vue";
import {
  getStatusClassIcon,
  getStatusClasses,
  formatDate,
} from "@/utils/helperFunctions";
import { X, BookMarked } from "lucide-vue-next";

// Props
const props = defineProps({
  isOpen: {
    type: Boolean,
    required: true,
  },
  ticket: {
    type: Object,
    default: null,
  },
});

// Emits
const emit = defineEmits(["close", "update-ticket", "resolve-ticket"]);

const formatDateTime = (date) => {
  return new Intl.DateTimeFormat("en-US", {
    year: "numeric",
    month: "short",
    day: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  }).format(new Date(date));
};

const handleUpdateTicket = () => {
  emit("update-ticket", props.ticket);
};

const handleResolveTicket = () => {
  emit("resolve-ticket", props.ticket);
  emit("close");
};
</script>
