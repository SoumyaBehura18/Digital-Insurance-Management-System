<template>
  <div class="w-full mx-auto p-6">
    <!-- Header -->
    <div class="flex justify-between items-center pb-6 border-b mb-8">
      <h1 class="text-2xl font-semibold">Claims Management</h1>
      <div class="flex rounded overflow-hidden">
        <router-link 
          to="/claims" 
          class="px-4 py-2 bg-white text-brand-textTheme border text-sm hover:bg-gray-50"
        >
          View Claims
        </router-link>
        <router-link 
          to="/submit-claim" 
          class="px-4 py-2 bg-brand-backgroundTheme text-white text-sm border border-l-0 border-brand-backgroundTheme hover:bg-brand-hover"
        >
          Submit Claim
        </router-link>
      </div>
    </div>

    <!-- Form Container -->
    <div class="bg-gray-50 -mx-6 min-h-screen px-6 py-10 flex justify-center">
      <div class="bg-white rounded p-8 shadow max-w-2xl w-full">
        <!-- Form Header -->
        <div class="mb-6">
          <h2 class="text-2xl font-semibold mb-1">Submit New Claim</h2>
          <p class="text-gray-500 text-sm">Fill out the form to submit a new insurance claim</p>
        </div>
        
        <!-- Form -->
        <form @submit.prevent="submitClaim" class="space-y-6">
          <!-- Policy Selection -->
          <div>
            <label for="policyId" class="block text-sm font-medium mb-1">Select Policy *</label>
            <select 
              id="policyId"
              v-model="form.userPolicyId" 
              required 
              class="w-full px-3 py-2 border rounded focus:ring-2 focus:ring-brand-backgroundTheme"
            >
              <option value="" class="text-gray-500">Choose an active policy</option>
              <option v-for="policy in ($store.state.claims?.policies || [])" :key="policy.id" :value="policy.id">
                {{ policy.policyNumber }} - {{ policy.policyType }} 
              </option>
            </select>
            <p class="mt-1 text-xs text-gray-500">Select the policy you want to claim against</p>
          </div>

          <!-- Claim Date -->
          <div>
            <label for="claimDate" class="block text-sm font-medium mb-1">Claim Date *</label>
            <input
              id="claimDate"
              type="date"
              v-model="form.claimDate"
              required
              class="w-full px-3 py-2 border rounded focus:ring-2 focus:ring-brand-backgroundTheme"
            />
            <p class="mt-1 text-xs text-gray-500">Select the date of the incident/expense</p>
          </div>

          <!-- Policy Details -->
          <div v-if="selectedPolicy" class="bg-blue-50 border rounded p-4">
            <div class="flex items-center mb-2">
              <svg class="w-5 h-5 text-brand-backgroundTheme mr-2" fill="currentColor" viewBox="0 0 20 20">
                <path fill-rule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2v3a1 1 0 001 1h1a1 1 0 100-2v-3a1 1 0 00-1-1H9z" clip-rule="evenodd"/>
              </svg>
              <h3 class="text-lg font-medium">{{ selectedPolicy.policyType }}</h3>
            </div>
            <p class="text-sm text-brand-textTheme mb-3">{{ selectedPolicy.description }}</p>
            <div class="grid grid-cols-2 gap-4 text-sm">
              <div>
                <p class="text-brand-backgroundTheme">Coverage Amount:</p>
                <p class="text-lg font-semibold">₹{{ formatAmount(selectedPolicy.coverageAmount) }}</p>
              </div>
              <div>
                <p class="text-brand-backgroundTheme">Available:</p>
                <p class="text-lg font-semibold">₹{{ formatAmount(selectedPolicy.availableAmount) }}</p>
              </div>
            </div>
          </div>

          <!-- Claim Amount -->
          <div>
            <label for="claimAmount" class="block text-sm font-medium mb-1">Claim Amount *</label>
            <div class="relative">
              <span class="absolute left-3 top-2.5 text-gray-500">₹</span>
              <input 
                id="claimAmount"
                type="number" 
                v-model="form.claimAmount" 
                required 
                min="1" 
                :max="selectedPolicy ? selectedPolicy.availableAmount : 1000000"
                @input="enforceMax"
                class="w-full pl-7 pr-3 py-2 border rounded focus:ring-2 focus:ring-brand-backgroundTheme"
                placeholder="Enter claim amount"
              />
            </div>
            <p class="mt-1 text-xs text-gray-500">
              Enter the amount you wish to claim
              <span v-if="selectedPolicy" class="text-brand-backgroundTheme font-medium">
                (Max: ₹{{ formatAmount(selectedPolicy.availableAmount) }})
              </span>
            </p>
          </div>

          <!-- Reason -->
          <div>
            <label for="reason" class="block text-sm font-medium mb-1">Reason for Claim *</label>
            <textarea 
              id="reason"
              v-model="form.reason" 
              required 
              rows="4"
              minlength="15"
              maxlength="500"
              class="w-full px-3 py-2 border rounded focus:ring-2 focus:ring-brand-backgroundTheme resize-y"
              placeholder="Please provide detailed reason for your claim"
            ></textarea>
            <div class="flex justify-between mt-1 text-xs text-gray-500">
              <p>Provide a detailed explanation (min 15 characters)</p>
              <p class="text-gray-400">{{ form.reason.length }}/500</p>
            </div>
          </div>

          <!-- Success & Error Messages -->
          <div v-if="$store.state.claims?.successMessage" class="p-3 bg-green-100 border text-green-700 rounded">
            {{ $store.state.claims.successMessage }}
          </div>
          <div v-if="$store.state.claims?.error" class="p-3 bg-red-100 border text-red-700 rounded">
            {{ $store.state.claims.error }}
          </div>

          <!-- Claim Guidelines -->
          <div class="bg-amber-50 border rounded p-4 text-sm">
            <div class="flex items-start">
              <svg class="w-5 h-5 text-amber-600 mr-2 flex-shrink-0" fill="currentColor" viewBox="0 0 20 20">
                <path fill-rule="evenodd" d="M8.257 3.099c.765-1.36 2.722-1.36 3.486 0l5.58 9.92c.75 1.334-.213 2.98-1.742 2.98H4.42c-1.53 0-2.493-1.646-1.743-2.98l5.58-9.92zM11 13a1 1 0 11-2 0 1 1 0 012 0zm-1-8a1 1 0 00-1 1v3a1 1 0 002 0V6a1 1 0 00-1-1z" clip-rule="evenodd"/>
              </svg>
              <div>
                <h4 class="font-medium mb-1 text-amber-800">Claim Processing Guidelines</h4>
                <ul class="space-y-1 text-amber-700">
                  <li>• Claims are subject to policy terms</li>
                  <li>• Processing takes 7–10 business days</li>
                  <li>• False claims may result in cancellation</li>
                </ul>
              </div>
            </div>
          </div>

          <!-- Submit Button -->
          <div>
            <button 
              type="submit" 
              :disabled="$store.state.claims?.loading"
              class="w-full px-6 py-2 bg-brand-backgroundTheme text-white rounded hover:bg-brand-hover disabled:opacity-50"
            >
              {{ $store.state.claims?.loading ? 'Submitting...' : 'Submit Claim' }}
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
  
  // Component data
  data() {
    return {
      form: {
        userPolicyId: '',
        claimDate: new Date().toISOString().slice(0, 10), // Today's date
        claimAmount: '',
        reason: ''
      }
    };
  },
  
  // Computed properties
  computed: {
    // This function gets current user ID from localStorage currentUser object
    currentUserId() {
      const currentUser = JSON.parse(localStorage.getItem('currentUser') || '{}')
      return parseInt(currentUser.userId) || null;
    },
    
    // This function takes no input and returns selected policy object based on form selection
    selectedPolicy() {
      if (!this.form.userPolicyId) return null;
      
      const policiesList = this.$store?.state?.claims?.policies || []
      return policiesList.find(policy => policy.id === parseInt(this.form.userPolicyId));
    }
  },
  // Watch for form changes to clear messages
  watch: {
    'form.userPolicyId'() {
      this.clearStoreMessages();
    },
    'form.claimAmount'() {
      this.clearStoreMessages();
    },
    'form.reason'() {
      this.clearStoreMessages();
    }
  },
  
  // Component lifecycle - load policies when component mounts
  async mounted() {
    await this.loadUserPolicies();
  },
  // Component methods
  methods: {
    // This function takes amount as input and returns formatted number in Indian currency format
    formatAmount(amount) {
      if (!amount) return '0';
      return new Intl.NumberFormat('en-IN').format(amount);
    },
    
    // This function takes no input and validates that claim amount does not exceed policy available amount
    enforceMax() {
      if (!this.selectedPolicy) return;
      
      const maxAmount = this.selectedPolicy.availableAmount;
      const currentAmount = parseFloat(this.form.claimAmount);
      
      if (!isNaN(currentAmount) && currentAmount > maxAmount) {
        this.form.claimAmount = maxAmount;
      }
      if (currentAmount < 0) {
        this.form.claimAmount = 0;
      }
    },
    
    // This function takes no input and loads all available policies for current user from store
    async loadUserPolicies() {
      if (this.$store._actions['claims/fetchPolicies']) {
        await this.$store.dispatch('claims/fetchPolicies')
      } else if (this.$store._actions['fetchPolicies']) {
        await this.$store.dispatch('fetchPolicies')
      }
    },
    
    // This function takes no input and clears all error and success messages from store
    clearStoreMessages() {
      if (this.$store._actions['claims/clearMessages']) {
        this.$store.dispatch('claims/clearMessages');
      } else if (this.$store._actions['clearMessages']) {
        this.$store.dispatch('clearMessages');
      }
    },
    // This function takes no input and processes the entire claim form submission with validation
    async submitClaim() {
      try {
        // Check if user is logged in
        if (!this.currentUserId) {
          this.setStoreError('User not authenticated. Please log in again.');
          return;
        }

        // Check claim amount doesn't exceed available amount
        if (this.selectedPolicy && parseInt(this.form.claimAmount) > this.selectedPolicy.availableAmount) {
          this.setStoreError(`Claim amount cannot exceed available amount of ₹${this.formatAmount(this.selectedPolicy.availableAmount)}`);
          return;
        }

        // Check reason length
        if (!this.form.reason || this.form.reason.trim().length < 15) {
          this.setStoreError('Reason must be at least 15 characters long.');
          return;
        }

        // Prepare claim data
        const claimData = {
          userPolicyId: parseInt(this.form.userPolicyId),
          claimDate: this.form.claimDate,
          claimAmount: parseFloat(this.form.claimAmount),
          reason: this.form.reason
        };

        // Submit claim to store
        await this.submitClaimToStore(claimData);
        
        // Reset form after successful submission
        this.resetForm();

        // Navigate to claims list after 2 seconds
        setTimeout(() => {
          if (this.$router) {
            this.$router.push('/claims');
          }
        }, 2000);

      } catch (error) {
        console.log('Failed to submit claim');
      }
    },
    
    // This function takes claimData object as input and sends it to store for API submission
    async submitClaimToStore(claimData) {
      if (this.$store._actions['claims/submitClaim']) {
        await this.$store.dispatch('claims/submitClaim', claimData)
      } else if (this.$store._actions['submitClaim']) {
        await this.$store.dispatch('submitClaim', claimData)
      }
    },
    
    // This function takes errorMessage string as input and sets it in store for display
    setStoreError(errorMessage) {
      if (this.$store._mutations['claims/SET_ERROR']) {
        this.$store.commit('claims/SET_ERROR', errorMessage);
      } else if (this.$store._mutations['SET_ERROR']) {
        this.$store.commit('SET_ERROR', errorMessage);
      }
    },
    
    // This function takes no input and resets all form fields to their initial empty state
    resetForm() {
      this.form = {
        userPolicyId: '',
        claimDate: new Date().toISOString().slice(0, 10),
        claimAmount: '',
        reason: ''
      };
    }
  }
};
</script>