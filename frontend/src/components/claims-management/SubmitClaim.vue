<template>
  <div class="max-w-6xl mx-auto p-6">
    <!-- Header -->
    <div class="flex justify-between items-center pb-6 border-b border-gray-200 mb-8">
      <h1 class="text-2xl font-semibold text-gray-900">Claims Management</h1>
      <div class="flex rounded-lg overflow-hidden">
        <router-link 
          to="/claims" 
          class="px-6 py-2 bg-white text-gray-700 border border-gray-300 text-sm font-medium hover:bg-gray-50 transition-colors"
        >
          View Claims
        </router-link>
        <router-link 
          to="/submit-claim" 
          class="px-6 py-2 bg-indigo-500 text-white text-sm font-medium hover:bg-indigo-600 transition-colors border border-l-0 border-indigo-500"
        >
          Submit Claim
        </router-link>
      </div>
    </div>

    <!-- Form Container -->
    <div class="bg-gray-50 -mx-6 min-h-screen px-6 py-10 flex justify-center items-start">
      <div class="bg-white rounded-xl p-10 shadow-lg max-w-2xl w-full">
        <!-- Form Header -->
        <div class="mb-8">
          <h2 class="text-2xl font-semibold text-gray-900 mb-2">Submit New Claim</h2>
          <p class="text-gray-500">Fill out the form to submit a new insurance claim</p>
        </div>
        
        <!-- Form -->
        <form @submit.prevent="submitClaim" class="space-y-6">
          <!-- Policy Selection -->
          <div>
            <label for="policyId" class="block text-sm font-medium text-gray-700 mb-2">
              Select Policy
            </label>
            <select 
              id="policyId"
              v-model="form.userPolicyId" 
              required 
              class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 text-gray-900 bg-white"
            >
              <option value="" class="text-gray-500">Choose an active policy</option>
              <option v-for="policy in $store.state.policies" :key="policy.id" :value="policy.id">
                {{ policy.policyNumber }} - {{ policy.policyType }} 
              </option>
            </select>
          </div>

          <!-- Claim Amount -->
          <div>
            <label for="claimAmount" class="block text-sm font-medium text-gray-700 mb-2">
              Claim Amount
            </label>
            <input 
              id="claimAmount"
              type="number" 
              v-model="form.claimAmount" 
              required 
              min="1" 
              max="1000000"
              class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 text-gray-900"
              placeholder="Enter claim amount"
            />
          </div>

          <!-- Reason -->
          <div>
            <label for="reason" class="block text-sm font-medium text-gray-700 mb-2">
              Reason for Claim
            </label>
            <textarea 
              id="reason"
              v-model="form.reason" 
              required 
              rows="4"
              class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 text-gray-900 resize-y"
              placeholder="Describe the reason for your claim..."
            ></textarea>
          </div>

          <!-- Success Message -->
          <div v-if="$store.state.successMessage" class="p-4 bg-green-100 border border-green-400 text-green-700 rounded-lg">
            {{ $store.state.successMessage }}
          </div>

          <!-- Error Message -->
          <div v-if="$store.state.error" class="p-4 bg-red-100 border border-red-400 text-red-700 rounded-lg">
            {{ $store.state.error }}
          </div>

          <!-- Submit Button -->
          <div class="pt-4">
            <button 
              type="submit" 
              :disabled="$store.state.loading"
              class="w-full px-8 py-3 bg-indigo-500 text-white font-medium rounded-lg hover:bg-indigo-600 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50 disabled:cursor-not-allowed transition-colors duration-200"
            >
              {{ $store.state.loading ? 'Submitting...' : 'Submit Claim' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SubmitClaim',
  props: {
    userId: {
      type: Number,
      default: 1
    }
  },
  data() {
    return {
      form: {
        userPolicyId: '',
        claimAmount: '',
        reason: ''
      }
    };
  },
  watch: {
    'form.userPolicyId'() {
      this.$store.dispatch('clearMessages');
    },
    'form.claimAmount'() {
      this.$store.dispatch('clearMessages');
    },
    'form.reason'() {
      this.$store.dispatch('clearMessages');
    }
  },
  async mounted() {
    await this.$store.dispatch('fetchPolicies');
  },
  methods: {
    async submitClaim() {
      try {
        const claimData = {
          userPolicyId: parseInt(this.form.userPolicyId),
          userId: this.userId,
          claimAmount: parseInt(this.form.claimAmount),
          reason: this.form.reason
        };

        await this.$store.dispatch('submitClaim', claimData);
        
        this.form = {
          userPolicyId: '',
          claimAmount: '',
          reason: ''
        };

        setTimeout(() => {
          if (this.$router) {
            this.$router.push('/claims');
          }
        }, 2000);

      } catch (error) {
        console.error('Failed to submit claim:', error);
      }
    }
  },
  
};
</script>