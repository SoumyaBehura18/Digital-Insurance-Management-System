<template>
  <div class="min-h-screen bg-gray-50">
    <header class="bg-white shadow-sm border-b border-gray-200">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-end items-center h-16">
          <div class="flex items-center gap-3 justify-end text-right">
            <div>
              <div class="font-semibold text-gray-900">
                {{ adminUser.name }}
              </div>
              <div class="text-sm text-gray-500">{{ adminUser.email }}</div>
            </div>
            <div
              class="w-10 h-10 bg-gray-300 rounded-full flex items-center justify-center"
            >
              <span class="text-gray-600 font-medium">{{
                adminUserInitials
              }}</span>
            </div>
          </div>
        </div>
      </div>
    </header>

    <main class="container mx-auto py-8 px-4 space-y-8">
      <div
        class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4"
      >
        <div class="space-y-1">
          <h1 class="text-3xl font-bold tracking-tight text-gray-900">
            Manage Support Tickets
          </h1>
          <p class="text-gray-600">
            Handle customer support requests and inquiries
          </p>
        </div>
      </div>

      <div class="grid grid-cols-1 sm:grid-cols-4 gap-6">
        <div class="bg-white rounded-lg border p-6 shadow-sm">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-sm font-medium text-gray-600">Total Tickets</p>
              <p class="text-2xl font-bold text-indigo-600">
                {{ tickets.length }}
              </p>
            </div>
            <div class="p-2 bg-indigo-100 rounded-lg">
              <MessageSquare class="h-5 w-5 text-indigo-500" />
            </div>
          </div>
        </div>

        <div class="bg-white rounded-lg border p-6 shadow-sm">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-sm font-medium text-gray-600">Open</p>
              <p class="text-2xl font-bold text-red-600">
                {{ tickets.filter((t) => t.status === "OPEN").length }}
              </p>
            </div>
            <div class="p-2 bg-red-100 rounded-lg">
              <MessageSquare class="h-5 w-5 text-red-600" />
            </div>
          </div>
        </div>

        <div class="bg-white rounded-lg border p-6 shadow-sm">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-sm font-medium text-gray-600">Resolved</p>
              <p class="text-2xl font-bold text-green-600">
                {{ tickets.filter((t) => t.status === "RESOLVED").length }}
              </p>
            </div>
            <div class="p-2 bg-green-100 rounded-lg">
              <CheckCircle class="h-5 w-5 text-green-600" />
            </div>
          </div>
        </div>

        <div class="bg-white rounded-lg border p-6 shadow-sm">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-sm font-medium text-gray-600">Closed</p>
              <p class="text-2xl font-bold text-gray-600">
                {{ tickets.filter((t) => t.status === "CLOSED").length }}
              </p>
            </div>
            <div class="p-2 bg-gray-100 rounded-lg">
              <XCircle class="h-5 w-5 text-gray-600" />
            </div>
          </div>
        </div>
      </div>

      <div class="bg-white rounded-lg border shadow-sm">
        <div class="p-6 border-b border-gray-200 space-y-4">
          <div
            class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4"
          >
            <h2 class="text-lg font-semibold text-gray-900">
              Support Tickets ({{ filteredTickets.length }})
            </h2>

            <div class="flex flex-wrap gap-2">
              <BaseButton
                variant="outline"
                size="sm"
                @click="statusFilter = null"
                :customClass="[
                  'transition-colors',
                  statusFilter === null
                    ? 'bg-indigo-50 text-indigo-700 border-indigo-200 hover:bg-indigo-100'
                    : 'hover:bg-gray-50',
                ]"
              >
                All Tickets ({{ tickets.length }})
              </BaseButton>
              <BaseButton
                variant="outline"
                size="sm"
                @click="statusFilter = 'OPEN'"
                :customClass="[
                  'transition-colors',
                  statusFilter === 'OPEN'
                    ? 'bg-red-50 text-red-700 border-red-200 hover:bg-red-100'
                    : 'hover:bg-gray-50',
                ]"
              >
                Open ({{ tickets.filter((t) => t.status === "OPEN").length }})
              </BaseButton>
              <BaseButton
                variant="outline"
                size="sm"
                @click="statusFilter = 'RESOLVED'"
                :customClass="[
                  'transition-colors',
                  statusFilter === 'RESOLVED'
                    ? 'bg-green-50 text-green-700 border-green-200 hover:bg-green-100'
                    : 'hover:bg-gray-50',
                ]"
              >
                Resolved ({{
                  tickets.filter((t) => t.status === "RESOLVED").length
                }})
              </BaseButton>
              <BaseButton
                variant="outline"
                size="sm"
                @click="statusFilter = 'CLOSED'"
                :customClass="[
                  'transition-colors',
                  statusFilter === 'CLOSED'
                    ? 'bg-gray-50 text-gray-700 border-gray-200 hover:bg-gray-100'
                    : 'hover:bg-gray-50',
                ]"
              >
                Closed ({{
                  tickets.filter((t) => t.status === "CLOSED").length
                }})
              </BaseButton>
            </div>
          </div>

          <div class="relative max-w-md">
            <div
              class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none"
            >
              <svg
                class="h-5 w-5 text-gray-400"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
                />
              </svg>
            </div>
            <input
              type="text"
              v-model="searchQuery"
              placeholder="Search by ticket ID, customer, or subject..."
              class="block w-full pl-10 pr-10 py-2 border border-gray-300 rounded-lg leading-5 bg-gray-100 placeholder-gray-500 focus:outline-none focus:placeholder-gray-400 focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
            />
            <div
              v-if="searchQuery"
              class="absolute inset-y-0 right-0 pr-3 flex items-center"
            >
              <button
                @click="searchQuery = ''"
                class="text-gray-400 hover:text-gray-600"
              >
                <svg
                  class="h-4 w-4"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M6 18L18 6M6 6l12 12"
                  />
                </svg>
              </button>
            </div>
          </div>
        </div>

        <div class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th
                  class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider font-semibold"
                >
                  Ticket ID
                </th>
                <th
                  class="px-6 py-3 text-left text-sm font-medium text-gray-500 tracking-wider font-semibold"
                >
                  Customer
                </th>
                <th
                  class="px-6 py-3 text-left text-sm font-medium text-gray-500 tracking-wider font-semibold"
                >
                  Subject
                </th>
                <th
                  class="px-6 py-3 text-left text-sm font-medium text-gray-500 tracking-wider font-semibold"
                >
                  Description
                </th>
                <th
                  class="px-6 py-3 text-left text-sm font-medium text-gray-500 tracking-wider font-semibold"
                >
                  Created Date
                </th>
                <th
                  class="px-6 py-3 text-left text-sm font-medium text-gray-500 tracking-wider font-semibold"
                >
                  Status
                </th>
                <th
                  class="px-6 py-3 text-left text-sm font-medium text-gray-500 tracking-wider font-semibold"
                >
                  Actions
                </th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr
                v-for="ticket in paginatedTickets"
                :key="ticket.id"
                class="hover:bg-gray-50"
              >
                <td
                  class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900"
                >
                  #{{ ticket.id }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                  {{ ticket.customerName }}
                </td>
                <td class="px-6 py-4 text-sm text-gray-900">
                  <div class="max-w-xs truncate">{{ ticket.subject }}</div>
                </td>
                <td class="px-6 py-4 text-sm text-gray-500">
                  <div class="max-w-xs truncate">{{ ticket.description }}</div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  {{ formatDate(ticket.createdAt) }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span
                    class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium"
                    :class="getStatusClasses(ticket.status)"
                  >
                    <component
                      :is="getStatusClassIcon(ticket.status)"
                      class="w-3 h-3 mr-1"
                    />
                    {{ ticket.status }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                  <div class="flex items-center gap-2">
                    <BaseButton
                      variant="outline"
                      size="sm"
                      @click="viewTicket(ticket)"
                      customClass="text-indigo-600 hover:text-indigo-900 flex items-center"
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
                          d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"
                        />
                        <path
                          stroke-linecap="round"
                          stroke-linejoin="round"
                          stroke-width="2"
                          d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"
                        />
                      </svg>
                      View
                    </BaseButton>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <div v-if="filteredTickets.length === 0" class="p-12 text-center">
          <svg
            class="mx-auto h-12 w-12 text-gray-400"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"
            />
          </svg>
          <h3 class="mt-2 text-sm font-medium text-gray-900">
            {{ searchQuery ? "No tickets found" : "No support tickets" }}
          </h3>
          <p class="mt-1 text-sm text-gray-500">
            {{
              searchQuery
                ? `No tickets match "${searchQuery}". Try adjusting your search.`
                : statusFilter
                ? `No tickets with ${statusFilter.toLowerCase()} status.`
                : "No support tickets have been created yet."
            }}
          </p>
          <div class="mt-6" v-if="searchQuery">
            <BaseButton variant="outline" @click="searchQuery = ''">
              Clear Search
            </BaseButton>
          </div>
        </div>

        <div
          v-if="totalPages > 1"
          class="bg-white px-4 py-3 border-t border-gray-200 sm:px-6"
        >
          <div class="flex items-center justify-between">
            <div class="flex items-center">
              <p class="text-sm text-gray-700">
                Showing {{ (currentPage - 1) * itemsPerPage + 1 }} to
                {{
                  Math.min(currentPage * itemsPerPage, filteredTickets.length)
                }}
                of {{ filteredTickets.length }} results
              </p>
            </div>
            <div class="flex gap-2">
              <BaseButton
                variant="outline"
                size="sm"
                :disabled="currentPage === 1"
                @click="currentPage--"
              >
                Previous
              </BaseButton>
              <BaseButton
                variant="outline"
                size="sm"
                :disabled="currentPage === totalPages"
                @click="currentPage++"
              >
                Next
              </BaseButton>
            </div>
          </div>
        </div>
      </div>
    </main>

    <TicketDetailsModal
      :is-open="isModalOpen"
      :ticket="selectedTicket"
      @close="handleCloseModal"
      @update-ticket="handleUpdateTicket"
      @resolve-ticket="handleResolveTicket"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from "vue";
import BaseButton from "@/components/BaseButton.vue";
import TicketDetailsModal from "@/components/TicketComponents/TicketDetailsModal.vue";
import { useStore } from "vuex";
import { MessageSquare, CheckCircle, XCircle } from "lucide-vue-next";

const store = useStore();

// Admin info (can be dynamic)
const adminUser = ref({ name: "Admin User", email: "admin@example.com" });
const adminUserInitials = computed(() =>
  adminUser.value.name
    .split(" ")
    .map((n) => n[0])
    .join("")
);

const tickets = ref([]);
const statusFilter = ref(null);
const searchQuery = ref("");
const currentPage = ref(1);
const itemsPerPage = ref(10);

const isModalOpen = ref(false);
const selectedTicket = ref(null);

// Fetch tickets
const fetchAllTickets = async () => {
  try {
    const response = await store.dispatch("tickets/fetchAllTickets");
    tickets.value = Array.isArray(response) ? response : response.tickets;
  } catch (err) {
    console.error("Error fetching tickets:", err);
  }
};

onMounted(fetchAllTickets);

// Computed: filtered tickets
const filteredTickets = computed(() => {
  return tickets.value.filter((ticket) => {
    const matchesStatus =
      !statusFilter.value || ticket.status === statusFilter.value;
    const matchesQuery =
      !searchQuery.value ||
      ticket.id.toString().includes(searchQuery.value) ||
      ticket.subject?.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      ticket.description
        ?.toLowerCase()
        .includes(searchQuery.value.toLowerCase()) ||
      ticket.userId.toString().includes(searchQuery.value);
    return matchesStatus && matchesQuery;
  });
});

// Pagination
const totalPages = computed(() =>
  Math.ceil(filteredTickets.value.length / itemsPerPage.value)
);
const paginatedTickets = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage.value;
  const end = start + itemsPerPage.value;
  return filteredTickets.value.slice(start, end);
});

// Watch filters to reset page
watch([statusFilter, searchQuery], () => (currentPage.value = 1));

// Methods
const formatDate = (date) => (date ? new Date(date).toLocaleString() : "-");

const getStatusClasses = (status) => {
  switch (status) {
    case "OPEN":
      return "bg-red-100 text-red-800 border border-red-200";
    case "RESOLVED":
      return "bg-green-100 text-green-800 border border-green-200";
    case "CLOSED":
      return "bg-gray-100 text-gray-800 border border-gray-200";
    default:
      return "bg-gray-100 text-gray-800 border border-gray-200";
  }
};

const viewTicket = (ticket) => {
  selectedTicket.value = ticket;
  isModalOpen.value = true;
};
const handleCloseModal = () => {
  selectedTicket.value = null;
  isModalOpen.value = false;
};
const handleUpdateTicket = async () => {
  await fetchAllTickets(); // refetch to update table after editing
};
const handleResolveTicket = async () => {
  await fetchAllTickets(); // refetch after resolving
};
</script>
