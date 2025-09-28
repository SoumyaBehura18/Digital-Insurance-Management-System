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
        :ticket-selected="ticketSelected"
        @create-first-ticket="setActiveView('create')"
        @update-ticket="handleUpdateTicket"
        @resolve-ticket="handleResolveTicket"
      />

      <CreateTicketForm
        v-if="activeView === 'create'"
        :user-id="userId"
        :ticket="selectedTicket"
        :policies="policies"
        :claims="claims"
        @ticket-created="handleTicketCreated"
        @ticket-updated="handleTicketUpdated"
        @cancel="setActiveView('tickets')"
      />
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from "vue";
import BaseButton from "@/components/BaseButton.vue";
import TicketsList from "@/components/TicketComponents/TicketList.vue";
import CreateTicketForm from "@/components/TicketComponents/SupportForm.vue";
import { ArrowLeft } from "lucide-vue-next";
import HeaderLayout from "@/components/layout/HeaderLayout.vue";
import { useStore } from "vuex";

const isCollapsed = ref(true);
const setIsCollapsed = (val) => (isCollapsed.value = val);
const userId = ref(null);
const tickets = ref([]);
const policies = ref([]);
const claims = ref([]);
const store = useStore();

onMounted(async () => {
  const currentUser=JSON.parse(localStorage.getItem("currentUser"));
  userId.value = currentUser ? currentUser.id : null;
  if (userId.value) {
    await fetchUserTickets();
    await fetchPoliciesAndClaims();
  }
});

const fetchUserTickets = async () => {
  try {
    if (userId.value) {
      const response = await store.dispatch(
        "tickets/fetchUserTickets",
        userId.value
      );

      if (response) {
        tickets.value = response;
      }
    }
  } catch (error) {
    console.error("Error fetching tickets:", error);
  }
};

const fetchPoliciesAndClaims = async () => {
  try {
    if (store._modulesNamespaceMap["policies/"]) {
      policies.value = await store.dispatch(
        "userPolicies/fetchUserPolicies",
        userId.value
      );
      // policies.value = store.state.policies?.policies || [];
      console.log("The policies are: ", store.state);
    } else if (store._actions["fetchPolicies"]) {
      await store.dispatch("fetchPolicies");
      policies.value = store.state.policies || [];
    }

    if (store._actions["claims/fetchClaims"]) {
      await store.dispatch("claims/fetchClaims", userId.value);
      claims.value = store.state.claims?.claims || [];
    } else {
      await store.dispatch("fetchClaims", userId.value);
      claims.value = store.state.claims?.claims || [];
    }
  } catch (err) {
    console.error("Error fetching policies/claims:", err);
  }
};

const selectedTicket = ref(null);

const user = ref({
  name: "John Doe",
  email: "somethin@mail.com",
});

const activeView = ref("tickets"); // 'tickets' | 'create'

const setActiveView = (view) => {
  activeView.value = view;
};

const handleUpdateTicket = (ticket) => {
  selectedTicket.value = ticket;
  setActiveView("create");
};

const handleResolveTicket = async (ticket) => {
  // Find and update the ticket in the array
  console.log("Selected ticket to resolve:", ticket);
  const ticketIndex = tickets.value.findIndex((t) => t.id === ticket.id);
  if (ticketIndex !== -1) {
    await store.dispatch("tickets/resolveTicket", ticket.id);
    tickets.value[ticketIndex].status = "RESOLVED";
    tickets.value[ticketIndex].updatedAt = new Date();
  }
  console.log("Ticket resolved:", ticket.id);
};

const handleTicketUpdated = async () => {
  setActiveView("tickets");
  await fetchUserTickets();
};
const handleTicketCreated = (newTicket) => {
  // Add the new ticket to the list
  if (tickets.value.length === 0) {
    tickets.value = [];
  }
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
