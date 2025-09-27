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
import { ref, computed } from "vue";
import BaseButton from "@/components/BaseButton.vue";
import TicketDetailsModal from "@/components/TicketComponents/TicketDetailsModal.vue";
import { getStatusClassIcon, getStatusClasses } from "@/utils/helperFunctions";
import { MessageSquare, CheckCircle, XCircle } from "lucide-vue-next";

// Admin user data
const adminUser = ref({
  name: "Admin User",
  email: "admin@insurance.com",
});

// State
const statusFilter = ref(null);
const searchQuery = ref("");
const currentPage = ref(1);
const itemsPerPage = ref(10);
const isModalOpen = ref(false);
const selectedTicket = ref(null);

// Sample tickets data (replace with API call)
const tickets = ref([
  {
    id: 1,
    customerName: "John Doe",
    subject: "Policy renewal question",
    description:
      "I need help understanding my policy renewal options and pricing changes.",
    status: "OPEN",
    createdAt: new Date("2024-09-20"),
    relatedPolicy: "Home Insurance Policy - HI001",
    relatedClaim: null,
  },
  {
    id: 2,
    customerName: "John Doe",
    subject: "Claim status inquiry",
    description: "When will my claim be processed?",
    status: "RESOLVED",
    createdAt: new Date("2024-09-18"),
    relatedPolicy: null,
    relatedClaim: "Water Damage Claim - WD001",
  },
  {
    id: 3,
    customerName: "Jane Smith",
    subject: "Update contact information",
    description: "I need to update my phone number and address in my account.",
    status: "CLOSED",
    createdAt: new Date("2024-09-15"),
    relatedPolicy: null,
    relatedClaim: null,
  },
]);

// Computed properties
const adminUserInitials = computed(() => {
  return adminUser.value.name
    .split(" ")
    .map((word) => word.charAt(0))
    .join("")
    .toUpperCase()
    .substring(0, 2);
});

const filteredTickets = computed(() => {
  let result = tickets.value;

  // Filter by status
  if (statusFilter.value) {
    result = result.filter((ticket) => ticket.status === statusFilter.value);
  }

  // Filter by search query
  if (searchQuery.value.trim()) {
    const query = searchQuery.value.toLowerCase().trim();
    result = result.filter((ticket) => {
      return (
        ticket.id.toString().includes(query) ||
        ticket.customerName.toLowerCase().includes(query) ||
        ticket.subject.toLowerCase().includes(query) ||
        ticket.description.toLowerCase().includes(query) ||
        ticket.status.toLowerCase().includes(query)
      );
    });
  }

  return result;
});

const totalPages = computed(() => {
  return Math.ceil(filteredTickets.value.length / itemsPerPage.value);
});

const paginatedTickets = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage.value;
  const end = start + itemsPerPage.value;
  return filteredTickets.value.slice(start, end);
});

// Methods
const formatDate = (date) => {
  return new Intl.DateTimeFormat("en-US", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
  }).format(new Date(date));
};

const getStatusBadgeClasses = (status) => {
  const baseClasses =
    "inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium gap-1";

  switch (status) {
    case "OPEN":
      return `${baseClasses} bg-red-100 text-red-800 border border-red-200`;
    case "RESOLVED":
      return `${baseClasses} bg-green-100 text-green-800 border border-green-200`;
    case "CLOSED":
      return `${baseClasses} bg-gray-100 text-gray-800 border border-gray-200`;
    default:
      return `${baseClasses} bg-gray-100 text-gray-800 border border-gray-200`;
  }
};

const getStatusIcon = (status) => {
  switch (status) {
    case "OPEN":
      return "⚠️";
    case "RESOLVED":
      return "✅";
    case "CLOSED":
      return "❌";
    default:
      return "📋";
  }
};

const viewTicket = (ticket) => {
  selectedTicket.value = ticket;
  isModalOpen.value = true;
};

const handleCloseModal = () => {
  isModalOpen.value = false;
  selectedTicket.value = null;
};

const handleUpdateTicket = (ticket) => {
  console.log("Update ticket:", ticket);
  // Handle ticket update logic
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

// Reset pagination when filters change
import { watch } from "vue";
watch([statusFilter, searchQuery], () => {
  currentPage.value = 1;
});
</script>
