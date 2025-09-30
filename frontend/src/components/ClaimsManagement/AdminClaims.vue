<template>
  <div class="w-full mx-auto p-6">
    <!-- Header -->
    <div class="mb-8">
      <h1 class="text-3xl font-bold text-gray-900 mb-2">Manage Claims</h1>
      <p class="text-gray-600">Review and process insurance claims</p>
    </div>

    <!-- Stats Cards -->
    <div class="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
      <!-- Total Claims -->
      <div class="bg-white rounded-lg border border-gray-200 p-6">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-brand-textTheme text-sm">Total Claims</p>
            <p class="text-3xl font-bold text-brand-backgroundTheme">
              {{ ($store.state.claims?.adminClaims || []).length }}
            </p>
          </div>
          <div
            class="w-12 h-12 bg-purple-100 rounded-lg flex items-center justify-center"
          >
            <svg
              class="w-6 h-6 text-brand-backgroundTheme"
              fill="currentColor"
              viewBox="0 0 20 20"
            >
              <path d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          </div>
        </div>
      </div>

      <!-- Pending Claims -->
      <div class="bg-white rounded-lg border border-gray-200 p-6">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-brand-textTheme text-sm">Pending</p>
            <p class="text-3xl font-bold text-yellow-600">
              {{ ($store.getters["claims/adminPendingClaims"] || []).length }}
            </p>
          </div>
          <div
            class="w-12 h-12 bg-yellow-100 rounded-lg flex items-center justify-center"
          >
            <svg
              class="w-6 h-6 text-yellow-600"
              fill="currentColor"
              viewBox="0 0 20 20"
            >
              <path
                fill-rule="evenodd"
                d="M10 18a8 8 0 100-16 8 8 0 000 16zm1-12a1 1 0 10-2 0v4a1 1 0 00.293.707l2.828 2.829a1 1 0 101.415-1.415L11 9.586V6z"
                clip-rule="evenodd"
              />
            </svg>
          </div>
        </div>
      </div>

      <!-- Approved Claims -->
      <div class="bg-white rounded-lg border border-gray-200 p-6">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-brand-textTheme text-sm">Approved</p>
            <p class="text-3xl font-bold text-green-600">
              {{ ($store.getters["claims/adminApprovedClaims"] || []).length }}
            </p>
          </div>
          <div
            class="w-12 h-12 bg-green-100 rounded-lg flex items-center justify-center"
          >
            <svg
              class="w-6 h-6 text-green-600"
              fill="currentColor"
              viewBox="0 0 20 20"
            >
              <path
                fill-rule="evenodd"
                d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z"
                clip-rule="evenodd"
              />
            </svg>
          </div>
        </div>
      </div>

      <!-- Rejected Claims -->
      <div class="bg-white rounded-lg border border-gray-200 p-6">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-brand-textTheme text-sm">Rejected</p>
            <p class="text-3xl font-bold text-red-600">
              {{ ($store.getters["claims/adminRejectedClaims"] || []).length }}
            </p>
          </div>
          <div
            class="w-12 h-12 bg-red-100 rounded-lg flex items-center justify-center"
          >
            <svg
              class="w-6 h-6 text-red-600"
              fill="currentColor"
              viewBox="0 0 20 20"
            >
              <path
                fill-rule="evenodd"
                d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
                clip-rule="evenodd"
              />
            </svg>
          </div>
        </div>
      </div>
    </div>

    <!-- Loading State -->
    <div v-if="$store.state.loading" class="text-center py-12">
      <div
        class="animate-spin rounded-full h-8 w-8 border-b-2 border-indigo-500 mx-auto"
      ></div>
      <p class="text-gray-500 mt-2">Loading claims...</p>
    </div>

    <!-- Error State -->
    <div v-else-if="$store.state.error" class="text-center py-12">
      <div class="text-red-600 mb-4">{{ $store.state.error }}</div>
      <button
        @click="loadAdminClaims"
        class="px-4 py-2 bg-indigo-600 text-white rounded hover:bg-indigo-700"
      >
        Try Again
      </button>
    </div>

    <!-- Claims Table + Filters (always visible) -->
    <div v-else class="bg-white rounded-lg border border-gray-200 shadow-sm">
      <!-- Table Header with Filters -->
      <div class="p-6 border-b border-gray-200">
        <div
          class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 mb-4"
        >
          <h2 class="text-lg font-semibold text-gray-900">
            Claims ({{ filteredClaims.length }})
          </h2>

          <!-- Filter Tabs -->
          <div class="flex items-center gap-2">
            <button
              @click="activeFilter = 'all'"
              :class="[
                'px-4 py-2 text-sm font-medium rounded-lg transition-colors',
                activeFilter === 'all'
                  ? 'bg-purple-100 text-brand-backgroundTheme border border-purple-200'
                  : 'text-brand-textTheme hover:text-gray-900 hover:bg-brand-hover hover:text-white',
              ]"
            >
              All Claims ({{ ($store.state.claims?.adminClaims || []).length }})
            </button>
            <button
              @click="activeFilter = 'pending'"
              :class="[
                'px-4 py-2 text-sm font-medium rounded-lg transition-colors',
                activeFilter === 'pending'
                  ? 'bg-purple-100 text-brand-backgroundTheme border border-purple-200'
                  : 'text-brand-textTheme hover:text-gray-900 hover:bg-brand-hover hover:text-white',
              ]"
            >
              Pending ({{
                ($store.getters["claims/adminPendingClaims"] || []).length
              }})
            </button>
            <button
              @click="activeFilter = 'approved'"
              :class="[
                'px-4 py-2 text-sm font-medium rounded-lg transition-colors',
                activeFilter === 'approved'
                  ? 'bg-purple-100 text-brand-backgroundTheme border border-purple-200'
                  : 'text-brand-textTheme hover:text-gray-900 hover:bg-brand-hover hover:text-white',
              ]"
            >
              Approved ({{
                ($store.getters["claims/adminApprovedClaims"] || []).length
              }})
            </button>
            <button
              @click="activeFilter = 'rejected'"
              :class="[
                'px-4 py-2 text-sm font-medium rounded-lg transition-colors',
                activeFilter === 'rejected'
                  ? 'bg-purple-100 text-brand-backgroundTheme border border-purple-200'
                  : 'text-brand-textTheme hover:text-gray-900 hover:bg-brand-hover hover:text-white',
              ]"
            >
              Rejected ({{
                ($store.getters["claims/adminRejectedClaims"] || []).length
              }})
            </button>
          </div>
        </div>

        <!-- Search Bar -->
        <div class="relative">
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
              ></path>
            </svg>
          </div>
          <input
            v-model="searchQuery"
            type="text"
            placeholder="Search by claim ID, email, or policy..."
            class="block w-full pl-10 pr-3 py-2 border border-gray-300 rounded-lg leading-5 bg-white placeholder-gray-500 focus:outline-none focus:placeholder-gray-400 focus:ring-1 focus:ring-brand-backgroundTheme focus:border-brand-backgroundTheme"
          />
        </div>
      </div>

      <div v-if="filteredClaims.length === 0" class="text-center py-12">
        <svg
          class="w-12 h-12 text-gray-400 mx-auto mb-4"
          fill="none"
          stroke="currentColor"
          viewBox="0 0 24 24"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"
          ></path>
        </svg>
        <p class="text-gray-500">
          No {{ activeFilter === "all" ? "" : activeFilter }} claims to review
          at the moment
        </p>
      </div>

      <div v-else class="overflow-x-auto">
        <table class="w-full text-sm">
          <thead class="bg-gray-50">
            <tr>
              <th
                class="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
              >
                Claim ID
              </th>
              <th
                class="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
              >
                Customer Email
              </th>
              <th
                class="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
              >
                Policy
              </th>
              <th
                class="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
              >
                Amount
              </th>
              <th
                class="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
              >
                Date
              </th>
              <th
                class="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
              >
                Status
              </th>
              <th
                class="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
              >
                Actions
              </th>
            </tr>
          </thead>
          <tbody class="bg-white divide-y divide-gray-200">
            <tr
              v-for="claim in filteredClaims"
              :key="claim.id"
              class="hover:bg-gray-50"
            >
              <td class="px-6 py-4 font-medium text-gray-900">
                #{{ claim.id }}
              </td>
              <td class="px-6 py-4 text-gray-900">
                {{ claim.userEmail || "No email" }}
              </td>
              <td class="px-6 py-4 text-gray-900">
                {{ claim.policyName || "-" }}
              </td>
              <td class="px-6 py-4 font-semibold text-gray-900">
                ${{ formatAmount(claim.claimAmount) }}
              </td>
              <td class="px-6 py-4 text-gray-500">
                {{ formatDate(claim.claimDate) }}
              </td>
              <td class="px-6 py-4">
                <span
                  class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium"
                  :class="{
                    'bg-yellow-100 text-yellow-800': claim.status === 'PENDING',
                    'bg-green-100 text-green-800': claim.status === 'APPROVED',
                    'bg-red-100 text-red-800': claim.status === 'REJECTED',
                  }"
                >
                  <svg
                    class="w-1.5 h-1.5 mr-1.5 rounded-full"
                    :class="{
                      'text-yellow-600': claim.status === 'PENDING',
                      'text-green-600': claim.status === 'APPROVED',
                      'text-red-600': claim.status === 'REJECTED',
                    }"
                    fill="currentColor"
                    viewBox="0 0 8 8"
                  >
                    <circle cx="4" cy="4" r="3" />
                  </svg>
                  {{
                    claim.status === "PENDING"
                      ? "Pending"
                      : claim.status === "APPROVED"
                      ? "Approved"
                      : "Rejected"
                  }}
                </span>
              </td>
              <td class="px-6 py-4">
                <div class="flex space-x-2">
                  <button
                    @click="openClaimModal(claim)"
                    class="inline-flex items-center px-3 py-1 text-xs font-medium text-indigo-600 bg-indigo-50 rounded-md hover:bg-indigo-100 transition-colors"
                  >
                    <svg
                      class="w-3 h-3 mr-1"
                      fill="currentColor"
                      viewBox="0 0 20 20"
                    >
                      <path d="M10 12a2 2 0 100-4 2 2 0 000 4z" />
                      <path
                        fill-rule="evenodd"
                        d="M.458 10C1.732 5.943 5.522 3 10 3s8.268 2.943 9.542 7c-1.274 4.057-5.064 7-9.542 7S1.732 14.057.458 10zM14 10a4 4 0 11-8 0 4 4 0 018 0z"
                        clip-rule="evenodd"
                      />
                    </svg>
                    View
                  </button>

                  <button
                    v-if="claim.documentLink"
                    @click="downloadDocument(claim.documentLink, claim.id)"
                    class="inline-flex items-center px-3 py-1 text-xs font-medium text-green-600 bg-green-50 rounded-md hover:bg-green-100 transition-colors"
                    title="Download supporting document"
                  >
                    <svg
                      class="w-3 h-3 mr-1"
                      fill="none"
                      stroke="currentColor"
                      viewBox="0 0 24 24"
                    >
                      <path
                        stroke-linecap="round"
                        stroke-linejoin="round"
                        stroke-width="2"
                        d="M12 10v6m0 0l-3-3m3 3l3-3m2 8H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"
                      />
                    </svg>
                    Document
                  </button>

                  <span
                    v-if="!claim.documentLink"
                    class="inline-flex items-center px-3 py-1 text-xs font-medium text-gray-400 bg-gray-50 rounded-md"
                    title="No document attached"
                  >
                    <svg
                      class="w-3 h-3 mr-1"
                      fill="none"
                      stroke="currentColor"
                      viewBox="0 0 24 24"
                    >
                      <path
                        stroke-linecap="round"
                        stroke-linejoin="round"
                        stroke-width="2"
                        d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"
                      />
                    </svg>
                    No doc
                  </span>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Claim Details Modal -->
    <div
      v-if="showModal"
      class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
      @click="closeModal"
    >
      <div class="bg-white rounded-lg p-6 max-w-2xl w-full mx-4" @click.stop>
        <div class="flex justify-between items-start mb-6">
          <h3 class="text-xl font-bold text-gray-900">
            Claim Details - #{{ selectedClaim?.id }}
          </h3>
          <button
            @click="closeModal"
            class="text-gray-400 hover:text-gray-600 transition-colors"
          >
            <svg
              class="w-6 h-6"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M6 18L18 6M6 6l12 12"
              ></path>
            </svg>
          </button>
        </div>

        <!-- Claim Info Grid -->
        <div class="grid grid-cols-2 gap-6 mb-6">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1"
              >Customer Email</label
            >
            <p class="text-gray-900">
              {{ selectedClaim?.userEmail || "No email" }}
            </p>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1"
              >Policy</label
            >
            <p class="text-gray-900">{{ selectedClaim?.policyName || "-" }}</p>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1"
              >Claim Amount</label
            >
            <p class="text-xl font-bold text-gray-900">
              ${{ formatAmount(selectedClaim?.claimAmount) }}
            </p>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1"
              >Date Submitted</label
            >
            <p class="text-gray-900">
              {{ formatDate(selectedClaim?.claimDate) }}
            </p>
          </div>
        </div>

        <!-- Reason -->
        <div class="mb-6">
          <label class="block text-sm font-medium text-gray-700 mb-2"
            >Reason for Claim</label
          >
          <div class="bg-gray-50 rounded-lg p-4">
            <p class="text-gray-900">
              {{ selectedClaim?.reason || "No reason provided" }}
            </p>
          </div>
        </div>

        <!-- Supporting Document -->
        <div class="mb-6">
          <label class="block text-sm font-medium text-gray-700 mb-2"
            >Supporting Document</label
          >
          <div
            v-if="selectedClaim?.documentLink"
            class="bg-blue-50 border border-blue-200 rounded-lg p-4"
          >
            <div class="flex items-center justify-between">
              <div class="flex items-center">
                <svg
                  class="w-5 h-5 text-blue-600 mr-2"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"
                  />
                </svg>
                <span class="text-blue-900 font-medium">Document attached</span>
              </div>
              <button
                @click="
                  downloadDocument(selectedClaim.documentLink, selectedClaim.id)
                "
                class="inline-flex items-center px-3 py-1 text-sm font-medium text-white bg-blue-600 rounded-md hover:bg-blue-700 transition-colors"
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
                    d="M12 10v6m0 0l-3-3m3 3l3-3m2 8H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"
                  />
                </svg>
                Download
              </button>
            </div>
          </div>
          <div v-else class="bg-gray-50 border border-gray-200 rounded-lg p-4">
            <div class="flex items-center text-gray-500">
              <svg
                class="w-5 h-5 mr-2"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"
                />
              </svg>
              <span>No supporting document provided</span>
            </div>
          </div>
        </div>

        <!-- Current Status -->
        <div class="mb-6">
          <label class="block text-sm font-medium text-gray-700 mb-2"
            >Current Status</label
          >
          <span
            class="inline-flex items-center px-2.5 py-0.5 rounded-full text-sm font-medium"
            :class="{
              'bg-yellow-100 text-yellow-800':
                selectedClaim?.status === 'PENDING',
              'bg-green-100 text-green-800':
                selectedClaim?.status === 'APPROVED',
              'bg-red-100 text-red-800': selectedClaim?.status === 'REJECTED',
            }"
          >
            <svg
              class="w-1.5 h-1.5 mr-1.5 rounded-full"
              :class="{
                'text-yellow-600': selectedClaim?.status === 'PENDING',
                'text-green-600': selectedClaim?.status === 'APPROVED',
                'text-red-600': selectedClaim?.status === 'REJECTED',
              }"
              fill="currentColor"
              viewBox="0 0 8 8"
            >
              <circle cx="4" cy="4" r="3" />
            </svg>
            {{
              selectedClaim?.status === "PENDING"
                ? "Pending"
                : selectedClaim?.status
            }}
          </span>
        </div>

        <!-- Admin Response Section (for pending claims) -->
        <div v-if="selectedClaim?.status === 'PENDING'" class="mb-6">
          <label class="block text-sm font-medium text-gray-700 mb-2"
            >Admin Response (Required)</label
          >
          <textarea
            v-model="selectedClaim.tempComment"
            placeholder="Add review comments before approving or rejecting..."
            rows="4"
            class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 resize-none"
          ></textarea>
        </div>

        <!-- Action Buttons (for pending claims) -->
        <div v-if="selectedClaim?.status === 'PENDING'" class="flex gap-3">
          <button
            @click="updateClaimStatus(selectedClaim, 'APPROVED')"
            class="flex-1 px-4 py-2 bg-brand-backgroundTheme text-white font-medium rounded-lg hover:bg-brand-hover transition-colors"
          >
            <svg
              class="w-4 h-4 inline mr-2"
              fill="currentColor"
              viewBox="0 0 20 20"
            >
              <path
                fill-rule="evenodd"
                d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z"
                clip-rule="evenodd"
              />
            </svg>
            Approve Claim
          </button>
          <button
            @click="updateClaimStatus(selectedClaim, 'REJECTED')"
            class="flex-1 px-4 py-2 bg-red-500 text-white font-medium rounded-lg hover:bg-red-600 transition-colors"
          >
            <svg
              class="w-4 h-4 inline mr-2"
              fill="currentColor"
              viewBox="0 0 20 20"
            >
              <path
                fill-rule="evenodd"
                d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
                clip-rule="evenodd"
              />
            </svg>
            Reject Claim
          </button>
        </div>

        <!-- Show existing review for processed claims -->
        <div v-else-if="selectedClaim?.reviewerComment" class="mb-4">
          <label class="block text-sm font-medium text-gray-700 mb-2"
            >Admin Review</label
          >
          <div class="bg-gray-50 rounded-lg p-4">
            <p class="text-gray-900">{{ selectedClaim.reviewerComment }}</p>
            <p class="text-xs text-gray-500 mt-2">
              Reviewed on {{ formatDate(selectedClaim?.resolvedDate) }} by
              {{ selectedClaim?.reviewerName || "Admin" }}
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { useToast } from "vue-toast-notification";
export default {
  name: "AdminClaims",

  setup() {
    const toast = useToast();

    return {
      toast,
    };
  },
  data() {
    return {
      activeFilter: "all",
      searchQuery: "",
      showModal: false,
      selectedClaim: null,
    };
  },
  computed: {
    filteredClaims() {
      // Safely pull from namespaced claims module
      const all =
        this.$store.state.claims &&
        Array.isArray(this.$store.state.claims.adminClaims)
          ? this.$store.state.claims.adminClaims
          : [];
      const pending = this.$store.getters["claims/adminPendingClaims"] || [];
      const approved = this.$store.getters["claims/adminApprovedClaims"] || [];
      const rejected = this.$store.getters["claims/adminRejectedClaims"] || [];

      let claims = [];

      // Filter by status first
      if (this.activeFilter === "pending") {
        claims = pending;
      } else if (this.activeFilter === "approved") {
        claims = approved;
      } else if (this.activeFilter === "rejected") {
        claims = rejected;
      } else {
        claims = all;
      }

      // Then filter by search query
      if (this.searchQuery.trim()) {
        const query = this.searchQuery.toLowerCase().trim();
        claims = claims.filter(
          (claim) =>
            claim.id.toString().includes(query) ||
            (claim.userEmail || "").toLowerCase().includes(query) ||
            (claim.policyName || "").toLowerCase().includes(query)
        );
      }

      return claims;
    },
  },
  async mounted() {
    await this.loadAdminClaims();
  },
  methods: {
    async loadAdminClaims() {
      try {
        const actionName = this.$store._actions["claims/fetchAllClaims"]
          ? "claims/fetchAllClaims"
          : "fetchAllClaims";
        await this.$store.dispatch(actionName);
      } catch (error) {
        console.error("Error loading admin claims:", error);
      }
    },
    formatDate(dateString) {
      if (!dateString) return "-";
      const date = new Date(dateString);
      return date.toLocaleDateString("en-GB", {
        day: "2-digit",
        month: "short",
        year: "numeric",
      });
    },
    formatAmount(amount) {
      if (!amount) return "0";
      return new Intl.NumberFormat("en-IN").format(amount);
    },
    async updateClaimStatus(claim, newStatus) {
      if (!claim.tempComment?.trim()) {
        toast.warn("Review comment is required.");

        return;
      }

      try {
        const actionName = this.$store._actions["claims/reviewClaim"]
          ? "claims/reviewClaim"
          : "reviewClaim";
        await this.$store.dispatch(actionName, {
          claimId: claim.id,
          status: newStatus,
          reviewComments: claim.tempComment.trim(),
          userPolicyId: claim.userPolicyId,
        });

        // Show success message
        const action = newStatus === "APPROVED" ? "approved" : "rejected";
        toast.success(`Claim #${claim.id} ${action} successfully!`);

        // Close modal if it's open
        if (this.showModal) {
          this.closeModal();
        }
      } catch (error) {
        console.error("Failed to update claim status:", error);
        toast.error("Error updating claim status.");
      }
    },
    openClaimModal(claim) {
      this.selectedClaim = { ...claim, tempComment: "" };
      this.showModal = true;
      document.body.style.overflow = "hidden";
    },
    closeModal() {
      this.showModal = false;
      this.selectedClaim = null;
      document.body.style.overflow = "auto";
    },

    // Download document from Supabase URL
    downloadDocument(documentUrl, claimId) {
      if (!documentUrl) {
        toast.error("No document available for download.");
        return;
      }

      try {
        // Extract filename from URL or create a default one
        const urlParts = documentUrl.split("/");
        const filename =
          urlParts[urlParts.length - 1] || `claim-${claimId}-document`;

        // Create a temporary link element and trigger download
        const link = document.createElement("a");
        link.href = documentUrl;
        link.download = filename;
        link.target = "_blank"; // Fallback to open in new tab if download fails

        // Append to body, click, and remove
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      } catch (error) {
        console.error("Error downloading document:", error);
        // Fallback: open in new tab
        window.open(documentUrl, "_blank");
      }
    },
  },
};
</script>
