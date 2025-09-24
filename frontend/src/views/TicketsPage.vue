<template>
  <div class="min-h-screen">
    <header class="bg-white shadow-sm border-b border-gray-200">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-end items-center h-16">
          <div class="flex items-center gap-3 justify-end text-right">
            <div>
              <div class="font-semibold text-gray-900">{{ user.name }}</div>
              <div class="text-sm text-gray-500">{{ user.email }}</div>
            </div>
            <div
              class="w-10 h-10 bg-gray-300 rounded-full flex items-center justify-center"
            >
              <span class="text-gray-600 font-medium">{{ userInitials }}</span>
            </div>
          </div>
        </div>
      </div>
    </header>

    <main class="max-w-7xl px-4 sm:px-6 lg:px-8 py-8">
      <div class="flex justify-between items-center mb-8">
        <div>
          <p class="text-lg text-gray-900">Support Tickets</p>
        </div>
        <div class="flex gap-3">
          <BaseButton
            @click="setActiveView('tickets')"
            :variant="activeView === 'tickets' ? 'theme' : 'outline'"
          >
            View Tickets
          </BaseButton>

          <BaseButton
            :variant="activeView === 'create' ? 'theme' : 'secondary'"
            @click="setActiveView('create')"
          >
            Raise Ticket
          </BaseButton>
        </div>
      </div>

      <!-- Dynamic Component Rendering -->
      <TicketsList
        v-if="activeView === 'tickets'"
        :tickets="tickets"
        @create-first-ticket="setActiveView('create')"
        @view-ticket="handleViewTicket"
      />

      <CreateTicketForm
        v-if="activeView === 'create'"
        @ticket-created="handleTicketCreated"
        @cancel="setActiveView('tickets')"
      />
    </main>
  </div>
</template>

<script setup>
import { ref, computed } from "vue";
import BaseButton from "@/components/BaseButton.vue";
import TicketsList from "@/components/TicketComponents/TicketList.vue";
import CreateTicketForm from "@/components/TicketComponents/SupportForm.vue";

// Reactive data
const user = ref({
  name: "John Doe",
  email: "somethin@mail.com",
});

const activeView = ref("tickets"); // 'tickets' | 'create'

const tickets = ref([
  {
    id: 1,
    title: "Login Issues",
    description: "Unable to login to my account",
    status: "OPEN",
    createdAt: new Date("2024-01-15"),
  },
  {
    id: 2,
    title: "Payment Problem",
    description: "Payment was charged twice",
    status: "RESOLVED",
    createdAt: new Date("2024-01-20"),
  },
]);

// Computed properties
const userInitials = computed(() => {
  return user.value.name
    .split(" ")
    .map((word) => word.charAt(0))
    .join("")
    .toUpperCase()
    .substring(0, 2);
});

// Methods
const setActiveView = (view) => {
  activeView.value = view;
};

const handleViewTicket = (ticketId) => {
  console.log("View ticket:", ticketId);
  // Handle view individual ticket
};

const handleTicketCreated = (newTicket) => {
  // Add the new ticket to the list
  tickets.value.unshift({
    id: tickets.value.length + 1,
    ...newTicket,
    createdAt: new Date(),
  });

  // Switch back to tickets view
  setActiveView("tickets");

  console.log("New ticket created:", newTicket);
};
</script>
