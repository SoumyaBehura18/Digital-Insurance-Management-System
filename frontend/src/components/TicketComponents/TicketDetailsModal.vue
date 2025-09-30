<template>
  <BaseModal
    :is-open="isOpen"
    :title="ticket?.subject || '-'"
    :subtitle="`Ticket #${ticket?.id || ''}`"
    size="xl"
    @close="$emit('close')"
  >
    <!-- Modal Content -->
    <div v-if="ticket" class="space-y-6">
      <!-- Status and Customer Info Row -->
      <div class="flex items-center justify-between">
        <!-- Left: Status and Date Info -->
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

        <!-- Right: Customer Info -->

        <div
          v-if="props.userDetails"
          class="bg-gray-50 rounded-lg p-3 min-w-0 flex-shrink-0"
        >
          <div class="flex items-center gap-3">
            <!-- Customer Avatar -->
            <div
              class="w-10 h-10 rounded-full bg-blue-500 flex items-center justify-center text-white font-medium flex-shrink-0"
            >
              {{ getCustomerInitials(props.userDetails.name) }}
            </div>

            <!-- Customer Details -->
            <div class="min-w-0 flex-1">
              <button
                @click="handleViewCustomer"
                class="text-sm font-medium text-gray-900 hover:text-blue-600 transition-colors cursor-pointer truncate block"
                :title="ticket.customer?.name"
              >
                {{ props.userDetails.name || "Unknown Customer" }}
              </button>
              <p
                class="text-xs text-gray-500 truncate"
                :title="ticket.customer?.email"
              >
                {{ props.userDetails.email || "No email provided" }}
              </p>
              <div class="flex items-center gap-2 mt-1">
                <span class="text-xs text-gray-400">
                  ID: {{ props.userDetails.id || ticket.userId }}
                </span>
              </div>
            </div>

            <!-- View Customer Button -->
            <button
              @click="handleViewCustomer"
              class="p-1 text-gray-400 hover:text-gray-600 transition-colors"
              title="View customer details"
            >
              <svg
                class="w-4 h-4"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"
                />
              </svg>
            </button>
          </div>
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

      <!-- Messages Section -->
      <div>
        <h4
          class="text-sm font-medium text-gray-900 mb-3 flex items-center gap-2"
        >
          <svg
            class="w-4 h-4"
            fill="none"
            stroke="currentColor"
            viewBox="0 0 24 24"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"
            />
          </svg>
          Messages
        </h4>

        <!-- Messages Container -->
        <div
          class="bg-gray-50 rounded-lg p-4 max-h-80 overflow-y-auto space-y-3"
        >
          <!-- No Messages State -->
          <div
            v-if="!ticket.messages || ticket.messages.length === 0"
            class="text-center py-8"
          >
            <div class="text-gray-400 mb-2">
              <svg
                class="w-12 h-12 mx-auto"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="1"
                  d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"
                />
              </svg>
            </div>
            <p class="text-sm text-gray-500">No messages yet</p>
            <p class="text-xs text-gray-400">
              Start the conversation by adding a message below
            </p>
          </div>

          <!-- Message List -->
          <div v-else class="space-y-3">
            <div
              v-for="message in sortedMessages"
              :key="message.id"
              class="flex gap-3"
              :class="
                isCurrentUser(message.authorId)
                  ? 'justify-end'
                  : 'justify-start'
              "
            >
              <!-- Avatar -->
              <div
                v-if="!isCurrentUser(message.authorId)"
                class="w-8 h-8 rounded-full flex items-center justify-center text-xs font-medium flex-shrink-0"
                :class="
                  isCurrentUserAdmin()
                    ? 'bg-blue-100 text-blue-600'
                    : 'bg-gray-200 text-gray-600'
                "
              >
                {{ isCurrentUserAdmin() ? "U" : "A" }}
              </div>

              <!-- Message Bubble -->
              <div
                class="max-w-xs lg:max-w-md px-4 py-2 rounded-lg"
                :class="
                  isCurrentUser(message.authorId)
                    ? 'bg-blue-500 text-white rounded-br-sm'
                    : isCurrentUserAdmin()
                    ? 'bg-white text-gray-800 border border-blue-200 rounded-bl-sm'
                    : 'bg-white text-gray-800 border border-gray-200 rounded-bl-sm'
                "
              >
                <!-- Message Header -->
                <div class="flex items-center justify-between mb-1 gap-1">
                  <span
                    class="text-xs font-medium"
                    :class="
                      isCurrentUser(message.authorId)
                        ? 'text-blue-100'
                        : 'text-gray-500'
                    "
                  >
                    {{ getAuthorLabel(message.authorId) }}
                  </span>
                  <span
                    class="text-xs"
                    :class="
                      isCurrentUser(message.authorId)
                        ? 'text-blue-200'
                        : 'text-gray-400'
                    "
                  >
                    {{ formatDateTime(message.timestamp) }}
                  </span>
                </div>
                <!-- Message Content -->
                <p class="text-sm">{{ message.content }}</p>
              </div>

              <!-- Current User Avatar -->
              <div
                v-if="isCurrentUser(message.authorId)"
                class="w-8 h-8 rounded-full bg-blue-500 flex items-center justify-center text-xs font-medium text-white flex-shrink-0"
              >
                {{ currentUserRole === "admin" ? "A" : "U" }}
              </div>
            </div>
          </div>
        </div>

        <!-- Add Message Form -->
        <div v-if="ticket.status !== 'CLOSED'" class="mt-4">
          <div class="flex gap-3">
            <div class="flex-1">
              <textarea
                v-model="newMessage"
                placeholder="Type your message..."
                rows="3"
                class="w-full px-3 py-2 border border-gray-300 rounded-lg resize-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                :disabled="isSubmitting"
              ></textarea>
            </div>
          </div>
          <div class="flex justify-between items-center mt-2">
            <div class="flex items-center gap-2 text-xs text-gray-500">
              <span
                class="inline-flex items-center px-2 py-1 rounded-full text-xs"
                :class="
                  currentUserRole === 'admin'
                    ? 'bg-blue-100 text-blue-600'
                    : 'bg-gray-100 text-gray-600'
                "
              >
                Sending as {{ currentUserRole === "admin" ? "Admin" : "User" }}
              </span>
            </div>
            <BaseButton
              :disabled="!newMessage.trim() || isSubmitting"
              :loading="isSubmitting"
              size="sm"
              @click="handleAddMessage"
              class="flex"
            >
              <svg
                class="w-4 h-4 mr-1"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M12 19l9 2-9-18-9 18 9-2zm0 0v-8"
                />
              </svg>
              Send
            </BaseButton>
          </div>
        </div>

        <!-- Closed Ticket Notice -->
        <div v-else class="bg-gray-100 rounded-lg p-3 text-center">
          <p class="text-sm text-gray-600">
            <svg
              class="w-4 h-4 inline mr-1"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M12 15v2m0 0v2m0-2h2m-2 0h-2m9-6V9a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2m10 0h2a2 2 0 002-2z"
              />
            </svg>
            This ticket is closed. No new messages can be added.
          </p>
        </div>
      </div>

      <!-- Timeline/Activity (updated) -->
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
          <div
            v-if="ticket.messages && ticket.messages.length > 0"
            class="flex gap-3"
          >
            <div
              class="w-2 h-2 bg-purple-500 rounded-full mt-2 flex-shrink-0"
            ></div>
            <div>
              <p class="text-sm text-gray-900">
                {{ ticket.messages.length }} message{{
                  ticket.messages.length !== 1 ? "s" : ""
                }}
                exchanged
              </p>
              <p class="text-xs text-gray-500">
                Last message:
                {{
                  formatDateTime(
                    sortedMessages[sortedMessages.length - 1]?.timestamp
                  )
                }}
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
                {{
                  formatDateTime(
                    ticket.resolvedAt || ticket.updatedAt || ticket.createdAt
                  )
                }}
              </p>
            </div>
          </div>
          <div v-if="ticket.status === 'CLOSED'" class="flex gap-3">
            <div
              class="w-2 h-2 bg-gray-500 rounded-full mt-2 flex-shrink-0"
            ></div>
            <div>
              <p class="text-sm text-gray-900">Ticket closed</p>
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
          📧 support@insurance.com • 📞 1800-629-599-70
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
          v-if="ticket?.status !== 'RESOLVED' && ticket?.status !== 'CLOSED'"
          customClass="bg-green-600 hover:bg-green-700"
          @click="handleResolveOrCloseTicket('RESOLVED')"
        >
          Mark as Resolved
        </BaseButton>
        <BaseButton
          v-if="ticket?.status === 'RESOLVED'"
          customClass="bg-green-600 hover:bg-green-700"
          @click="handleResolveOrCloseTicket('CLOSED')"
        >
          Close the Ticket
        </BaseButton>
      </div>
    </template>
  </BaseModal>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import BaseModal from "@/components/BaseModal.vue";
import BaseButton from "@/components/BaseButton.vue";
import {
  getStatusClassIcon,
  getStatusClasses,
  formatDate,
  formatDateTime,
} from "@/utils/helperFunctions";
import { useStore } from "vuex";

const store = useStore();

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
  userDetails: {
    type: Object,
    default: null,
  },
});

// Emits
const emit = defineEmits([
  "close",
  "update-ticket",
  "resolve-ticket",
  "close-ticket",
  "message-added",
  "view-customer",
]);

// Reactive data
const newMessage = ref("");
const isSubmitting = ref(false);

let currentUser = JSON.parse(localStorage.getItem("currentUser"));
const currentUserRole = currentUser ? currentUser.role : "user";

// Computed properties
const sortedMessages = computed(() => {
  if (!props.ticket?.messages || props.ticket.messages.length === 0) {
    return [];
  }

  // Sort messages by timestamp in ascending order (oldest first)
  return [...props.ticket.messages].sort((a, b) => {
    const dateA = new Date(a.timestamp);
    const dateB = new Date(b.timestamp);
    return dateA - dateB;
  });
});

// Helper functions
const isCurrentUser = (authorId) => {
  return authorId === currentUser.id;
};

const isCurrentUserAdmin = () => {
  return currentUserRole === "admin";
};

const getAuthorLabel = (authorId) => {
  if (isCurrentUser(authorId)) {
    return currentUserRole === "admin" ? "You (Admin)" : "You";
  }
  return isCurrentUserAdmin() ? "User" : "Support Admin";
};

const getCustomerInitials = (customer) => {
  if (!customer?.name) return "??";
  const names = customer.name.split(" ");
  if (names.length === 1) return names[0].charAt(0).toUpperCase();
  return (names[0].charAt(0) + names[names.length - 1].charAt(0)).toUpperCase();
};

// Event handlers
const handleUpdateTicket = () => {
  emit("update-ticket", props.ticket);
};

const handleViewCustomer = () => {
  emit("view-customer", props.ticket.customer || { id: props.ticket.userId });
};

const handleResolveOrCloseTicket = async (status) => {
  try {
    if (status !== "RESOLVED" && status !== "CLOSED") return;
    const newTicket = { ...props.ticket, status: status };
    await store.dispatch("tickets/updateTicket", {
      ticketData: newTicket,
      ticketId: newTicket.id,
    });
    if (status === "RESOLVED") {
      emit("resolve-ticket", props.ticket);
    } else if (status === "CLOSED") {
      emit("close-ticket", props.ticket);
    }
    emit("close");
  } catch (error) {
    console.error("Error resolving ticket:", error);
  }
};

const handleAddMessage = async () => {
  if (!newMessage.value.trim() || isSubmitting.value) return;

  try {
    isSubmitting.value = true;

    const messageData = {
      ticketId: props.ticket.id,
      content: newMessage.value.trim(),
      authorId: currentUser.id,
    };

    // Dispatch action to add message
    await store.dispatch("tickets/addMessageToTicket", {
      ticketId: props.ticket.id,
      message: messageData,
    });

    sortedMessages.value.push({
      ...messageData,
      id: Date.now(), // Temporary ID; in real app, backend would provide this
      timestamp: new Date().toISOString(),
    });

    // Clear the message input
    newMessage.value = "";

    // Emit event to parent component
    emit("message-added", messageData);
  } catch (error) {
    console.error("Error adding message:", error);
  } finally {
    isSubmitting.value = false;
  }
};
</script>
