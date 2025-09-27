<template>
  <div class="min-h-screen">
    <HeaderLayout
      :user="user"
      :isCollapsed="isCollapsed"
      :setIsCollapsed="setIsCollapsed"
      class="w-full"
    />

    <main class="max-w-7xl px-4 sm:px-6 lg:px-8 py-8">
      <div class="flex justify-between items-center mb-8">
        <div class="flex items-center gap-3">
          <ArrowLeft
            v-if="activeView === 'create'"
            @click="activeView = 'tickets'"
            class="cursor-pointer h-5 text-gray-600 hover:bg-gray-200 rounded-full h-6 w-6 p-1"
          />
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

      <TicketsList
        v-if="activeView === 'tickets'"
        :tickets="tickets"
        @create-first-ticket="setActiveView('create')"
        @update-ticket="handleUpdateTicket"
        @resolve-ticket="handleResolveTicket"
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
import { ArrowLeft } from "lucide-vue-next";
import HeaderLayout from "@/components/layout/HeaderLayout.vue";

const isCollapsed = ref(true);
const setIsCollapsed = (val) => (isCollapsed.value = val);

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

const setActiveView = (view) => {
  activeView.value = view;
};

const handleUpdateTicket = (ticket) => {
  console.log("Update ticket:", ticket);
  setActiveView("create");
};

const handleResolveTicket = (ticket) => {
  // Find and update the ticket in the array
  const ticketIndex = tickets.value.findIndex((t) => t.id === ticket.id);
  if (ticketIndex !== -1) {
    tickets.value[ticketIndex].status = "RESOLVED";
    tickets.value[ticketIndex].updatedAt = new Date();
  }
  console.log("Ticket resolved:", ticket.id);
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
