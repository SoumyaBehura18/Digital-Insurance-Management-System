<template>
  <div class="max-w-6xl mx-auto p-6">
    <!-- Header -->
    <div class="flex justify-between items-center pb-6 border-b border-gray-200 mb-8">
      <h1 class="text-2xl font-semibold text-gray-900">Claims Management</h1>
      <div class="flex rounded-lg overflow-hidden">
        <router-link 
          to="/claims" 
          class="px-6 py-2 bg-indigo-500 text-white text-sm font-medium hover:bg-indigo-600 transition-colors"
        >
          View Claims
        </router-link>
        <router-link 
          to="/submit-claim" 
          class="px-6 py-2 bg-white text-gray-700 border border-l-0 border-gray-300 text-sm font-medium hover:bg-gray-50 transition-colors"
        >
          Submit Claim
        </router-link>
      </div>
    </div>

    <!-- Claims Section -->
    <div class="bg-white rounded-lg border border-gray-200">
      <div class="p-6 border-b border-gray-200">
        <h2 class="text-lg font-semibold text-gray-900">Your Claims</h2>
      </div>

      <!-- Loading State -->
      <div v-if="$store.state.loading" class="text-center py-12">
        <p class="text-gray-500">Loading your claims...</p>
      </div>

      <!-- Error State -->
      <div v-if="$store.state.error" class="text-center py-12">
        <p class="text-red-600 mb-4">{{ $store.state.error }}</p>
        <button 
          @click="loadClaims" 
          class="px-4 py-2 bg-indigo-500 text-white rounded-md hover:bg-indigo-600 transition-colors"
        >
          Try Again
        </button>
      </div>

      <!-- Empty State -->
      <div v-if="!$store.state.loading && !$store.state.error && $store.state.claims.length === 0" class="text-center py-12">
        <p class="text-gray-500">You have not submitted any claims yet.</p>
      </div>

      <!-- Claims Table -->
      <div v-if="!$store.state.loading && !$store.state.error && $store.state.claims.length > 0" class="overflow-x-auto">
        <table class="w-full">
          <thead class="bg-gray-50">
            <tr>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Policy Name
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Claim Amount
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Date
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Status
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Reason
              </th>
            </tr>
          </thead>
          <tbody class="bg-white divide-y divide-gray-200">
            <tr v-for="claim in $store.state.claims" :key="claim.id" class="hover:bg-gray-50">
              <td class="px-6 py-4 text-sm font-medium text-gray-900">
                {{ getPolicyName(claim.userPolicyId) }}
              </td>
              <td class="px-6 py-4 text-sm font-semibold text-green-600">
                ${{ formatAmount(claim.claimAmount) }}
              </td>
              <td class="px-6 py-4 text-sm text-gray-500">
                {{ formatDate(claim.claimDate) }}
              </td>
              <td class="px-6 py-4 text-sm">
                <span 
                  class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium uppercase"
                  :class="{
                    'bg-yellow-100 text-yellow-800': claim.status === 'PENDING',
                    'bg-green-100 text-green-800': claim.status === 'APPROVED',
                    'bg-red-100 text-red-800': claim.status === 'REJECTED'
                  }"
                >
                  <span 
                    class="w-1.5 h-1.5 rounded-full mr-1.5"
                    :class="{
                      'bg-yellow-600': claim.status === 'PENDING',
                      'bg-green-600': claim.status === 'APPROVED',
                      'bg-red-600': claim.status === 'REJECTED'
                    }"
                  ></span>
                  {{ claim.status }}
                </span>
              </td>
              <td class="px-6 py-4 text-sm text-gray-700 max-w-xs truncate">
                {{ claim.reason }}
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ClaimList',
  props: {
    userId: {
      type: Number,
      default: 1
    }
  },
  async mounted() {
    await this.$store.dispatch('fetchPolicies');
    await this.loadClaims();
  },
  methods: {
    async loadClaims() {
      await this.$store.dispatch('fetchClaims', this.userId);
    },

    formatDate(dateString) {
      if (!dateString) return '-';
      const date = new Date(dateString);
      return date.toLocaleDateString('en-US', { 
        day: '2-digit', 
        month: '2-digit', 
        year: 'numeric' 
      });
    },

    formatAmount(amount) {
      return amount.toLocaleString('en-US', { 
        minimumFractionDigits: 0, 
        maximumFractionDigits: 0 
      });
    },

    getPolicyName(policyId) {
      const policy = this.$store.state.policies.find(p => p.id === policyId);
      return policy ? policy.name : `Policy ${policyId}`;
    }
  }
};
</script>