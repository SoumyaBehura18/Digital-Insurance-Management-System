<template>
  <div class="w-full mx-auto p-6">
    <!-- Header -->
    <div class="flex justify-between items-center pb-6 border-b mb-8">
      <h1 class="text-2xl font-semibold">Claims Management</h1>
      <div class="flex rounded overflow-hidden">
        <router-link
          to="/claims"
          class="px-4 py-2 bg-brand-backgroundTheme text-white text-sm hover:bg-brand-hover"
        >
          View Claims
        </router-link>
        <router-link
          to="/submit-claim"
          class="px-4 py-2 border text-sm text-brand-textTheme hover:bg-gray-50"
        >
          Submit Claim
        </router-link>
      </div>
    </div>

    <!-- Claims Section -->
    <div class="bg-white rounded border">
      <div class="p-4 border-b">
        <h2 class="text-lg font-semibold">Your Claims</h2>
      </div>

      <!-- Loading State -->
      <div
        v-if="$store.state.claims?.loading"
        class="py-12 text-center text-gray-500"
      >
        Loading your claims...
      </div>

      <!-- Error State -->
      <div
        v-if="$store.state.claims?.error"
        class="py-12 text-center text-red-600"
      >
        <p class="mb-4">{{ $store.state.claims.error }}</p>
        <button
          @click="loadClaims"
          class="px-4 py-2 bg-brand-backgroundTheme text-white rounded hover:bg-brand-hover"
        >
          Try Again
        </button>
      </div>

      <!-- Empty State -->
      <div
        v-if="
          !$store.state.claims?.loading &&
          !$store.state.claims?.error &&
          !($store.state.claims?.claims || []).length
        "
        class="py-12 text-center text-gray-500"
      >
        You have not submitted any claims yet.
      </div>

      <!-- Claims Desktop Table -->
      <div
        v-if="
          !$store.state.claims?.loading &&
          !$store.state.claims?.error &&
          ($store.state.claims?.claims || []).length
        "
        class="overflow-x-auto"
      >
        <table class="hidden md:table w-full text-sm">
          <thead class="bg-gray-50">
            <tr>
              <th class="px-4 py-2 text-left text-xs font-medium uppercase">Claim ID</th>
              <th class="px-4 py-2 text-left text-xs font-medium uppercase">Policy Name</th>
              <th class="px-4 py-2 text-left text-xs font-medium uppercase">Claim Amount</th>
              <th class="px-4 py-2 text-left text-xs font-medium uppercase">Date</th>
              <th class="px-4 py-2 text-left text-xs font-medium uppercase">Status</th>
              <th class="px-4 py-2 text-left text-xs font-medium uppercase">Document</th>
              <th class="px-4 py-2 text-left text-xs font-medium uppercase">Reason</th>
              <th class="px-4 py-2 text-left text-xs font-medium uppercase">Admin Review</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="claim in $store.state.claims?.claims || []"
              :key="claim.id"
              class="hover:bg-gray-50 border-b border-gray-100"
            >
              <td class="px-4 py-4 font-medium">{{ claim.id }}</td>
              <td class="px-4 py-4 font-medium">
                {{ claim.policyName || getPolicyLabel(claim.userPolicyId) }}
              </td>
              <td class="px-4 py-4 font-semibold text-green-600">
                ₹{{ formatAmount(claim.claimAmount) }}
              </td>
              <td class="px-4 py-4 text-gray-500">
                {{ formatDate(claim.claimDate) }}
              </td>
              <td class="px-4 py-4">
                <span
                  class="inline-flex items-center px-2 py-0.5 rounded text-xs"
                  :class="{
                    'bg-yellow-100 text-yellow-800': claim.status === 'PENDING',
                    'bg-green-100 text-green-800': claim.status === 'APPROVED',
                    'bg-red-100 text-red-800': claim.status === 'REJECTED',
                  }"
                >
                  <span
                    class="w-2 h-2 rounded-full mr-1"
                    :class="{
                      'bg-yellow-600': claim.status === 'PENDING',
                      'bg-green-600': claim.status === 'APPROVED',
                      'bg-red-600': claim.status === 'REJECTED',
                    }"
                  ></span>
                  {{ claim.status }}
                </span>
              </td>
              <td class="px-4 py-4">
                <button
                  v-if="claim.documentLink"
                  @click="downloadDocument(claim.documentLink, claim.id)"
                  class="inline-flex items-center px-2 py-1 text-xs font-medium text-blue-600 bg-blue-50 rounded hover:bg-blue-100 transition-colors"
                >
                  View
                </button>
                <span
                  v-else
                  class="inline-flex items-center px-2 py-1 text-xs font-medium text-gray-400 bg-gray-50 rounded"
                >
                  No doc
                </span>
              </td>
              <td class="px-4 py-4 truncate max-w-xs">{{ claim.reason }}</td>
              <td class="px-4 py-4">
                <button
                  v-if="claim.reviewerComment"
                  @click="showCommentModal(claim)"
                  class="px-2 py-1 text-xs text-brand-backgroundTheme bg-purple-50 rounded hover:bg-purple-100"
                >
                  View Comment
                </button>
                <span v-else class="text-xs text-gray-400 italic">No comments yet</span>
              </td>
            </tr>
          </tbody>
        </table>

        <!-- Mobile Card Layout -->
        <div class="space-y-4 md:hidden">
          <div
            v-for="claim in $store.state.claims?.claims || []"
            :key="claim.id"
            class="border rounded p-4 bg-white shadow-sm"
          >
            <div class="flex justify-between items-center mb-2">
              <span class="font-semibold">#{{ claim.id }}</span>
              <span
                class="inline-flex items-center px-2 py-0.5 rounded text-xs"
                :class="{
                  'bg-yellow-100 text-yellow-800': claim.status === 'PENDING',
                  'bg-green-100 text-green-800': claim.status === 'APPROVED',
                  'bg-red-100 text-red-800': claim.status === 'REJECTED',
                }"
              >
                {{ claim.status }}
              </span>
            </div>
            <p class="text-sm text-gray-600"><b>Policy:</b> {{ claim.policyName || getPolicyLabel(claim.userPolicyId) }}</p>
            <p class="text-sm text-gray-600"><b>Amount:</b> ₹{{ formatAmount(claim.claimAmount) }}</p>
            <p class="text-sm text-gray-600"><b>Date:</b> {{ formatDate(claim.claimDate) }}</p>
            <p class="text-sm text-gray-600"><b>Reason:</b> {{ claim.reason || "-" }}</p>
            <div class="mt-2 flex items-center gap-2">
              <button
                v-if="claim.documentLink"
                @click="downloadDocument(claim.documentLink, claim.id)"
                class="px-2 py-1 text-xs text-blue-600 bg-blue-50 rounded hover:bg-blue-100"
              >
                View Doc
              </button>
              <span v-else class="text-xs text-gray-400">No doc</span>

              <button
                v-if="claim.reviewerComment"
                @click="showCommentModal(claim)"
                class="px-2 py-1 text-xs text-purple-600 bg-purple-50 rounded hover:bg-purple-100"
              >
                Comment
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Comment Modal -->
    <div
      v-if="showModal"
      class="fixed inset-0 bg-black/50 flex items-center justify-center z-50"
      @click="closeModal"
    >
      <div class="bg-white rounded p-6 w-full max-w-lg mx-4 sm:mx-auto" @click.stop>
        <div class="flex justify-between items-start mb-4">
          <div>
            <h3 class="text-lg font-semibold">Admin Review</h3>
            <p class="text-sm text-gray-600">Claim #{{ selectedClaim?.id }}</p>
          </div>
          <button @click="closeModal" class="text-gray-400 hover:text-gray-600">✕</button>
        </div>

        <div class="mb-4 bg-gray-50 rounded p-4">
          <div class="flex items-center gap-2 mb-2">
            <div
              class="w-3 h-3 rounded-full"
              :class="{
                'bg-green-400': selectedClaim?.status === 'APPROVED',
                'bg-red-400': selectedClaim?.status === 'REJECTED',
              }"
            ></div>
            <span class="text-sm font-medium">
              {{ selectedClaim?.status === "APPROVED" ? "Approved" : "Rejected" }}
            </span>
          </div>
          <p class="text-sm">{{ selectedClaim?.reviewerComment }}</p>
        </div>

        <div class="flex justify-between text-xs text-gray-500">
          <span v-if="selectedClaim?.resolvedDate">Reviewed on {{ formatDate(selectedClaim.resolvedDate) }}</span>
          <span>Claim Amount: ₹{{ formatAmount(selectedClaim?.claimAmount) }}</span>
        </div>

        <div class="mt-6 flex justify-end">
          <button
            @click="closeModal"
            class="px-4 py-2 bg-brand-backgroundTheme text-white rounded hover:bg-brand-hover"
          >
            Close
          </button>
        </div>
      </div>
    </div>
  </div>
</template>


<script>
export default {
  name: "ClaimList",
  data() {
    return {
      showModal: false,
      selectedClaim: null,
    };
  },
  computed: {
    userId() {
      const currentUser = JSON.parse(localStorage.getItem("currentUser"));
      return currentUser?.userId || null;
    },
  },
  async mounted() {
    // Load policies via namespaced module if available
    const uid = this.userId;
    if (uid && this.$store._modulesNamespaceMap["policies/"]) {
      await this.$store.dispatch("policies/fetchUserPolicies", uid);
    } else if (this.$store._actions["fetchPolicies"]) {
      await this.$store.dispatch("fetchPolicies");
    }
    await this.loadClaims();
  },

  beforeUnmount() {
    // Restore body scroll if component is destroyed while modal is open
    document.body.style.overflow = "auto";
  },
  methods: {
    async loadClaims() {
      if (!this.userId) {
        console.error("User not authenticated");
        return;
      }
      if (this.$store._actions["claims/fetchClaims"]) {
        await this.$store.dispatch("claims/fetchClaims", this.userId);
      } else {
        await this.$store.dispatch("fetchClaims", this.userId);
      }
    },

    formatDate(dateString) {
      if (!dateString) return "-";
      const date = new Date(dateString);
      return date.toLocaleDateString("en-US", {
        day: "2-digit",
        month: "2-digit",
        year: "numeric",
      });
    },

    formatAmount(amount) {
      if (!amount) return "0";
      return new Intl.NumberFormat("en-IN").format(amount);
    },

    getPolicyLabel(userPolicyId) {
      const listFromClaims = this.$store?.state?.claims?.policies || [];
      const listFromPolicies =
        this.$store?.state?.policies?.policies ||
        this.$store?.state?.policies ||
        [];
      const list =
        Array.isArray(listFromClaims) && listFromClaims.length
          ? listFromClaims
          : Array.isArray(listFromPolicies)
          ? listFromPolicies
          : [];
      const policy = list.find((p) => p.id === userPolicyId);
      if (!policy) return `Policy ${userPolicyId}`;
      return policy.policyType || `Policy ${userPolicyId}`;
    },

    showCommentModal(claim) {
      this.selectedClaim = claim;
      this.showModal = true;
      // Prevent body scroll when modal is open
      document.body.style.overflow = "hidden";
    },

    closeModal() {
      this.showModal = false;
      this.selectedClaim = null;
      // Restore body scroll
      document.body.style.overflow = "auto";
    },

    /**
     * DOCUMENT VIEW FUNCTIONALITY
     * Opens the supporting document for a claim from Supabase storage in a new tab
     * 
     * @param {string} documentUrl - The public URL of the document in Supabase
     * @param {number} claimId - The ID of the claim (used for filename)
     */
    downloadDocument(documentUrl, claimId) {
      // Validate that document URL exists
      if (!documentUrl) {
        alert('No document available for this claim');
        return;
      }

      try {
        // Extract filename from URL or create a default one
        // Supabase URLs typically end with the actual filename
        const urlParts = documentUrl.split('/');
        const filename = urlParts[urlParts.length - 1] || `claim-${claimId}-document`;
        
        // Create a temporary anchor element to view document in new tab
        const link = document.createElement('a');
        link.href = documentUrl;
        link.target = '_blank'; // Open in new tab to view the document
        
        // Temporarily add to DOM, click, and remove
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        
        console.log(`Document opened for viewing for claim ${claimId}: ${filename}`);
        
      } catch (error) {
        console.error('Error opening document:', error);
        // Fallback: open in new tab directly
        window.open(documentUrl, '_blank');
      }
    },
  },
};
</script>
