import { createStore } from 'vuex'
import { makeRequestWithToken } from '@/utils/requests.js'

export default createStore({
  state: {
    claims: [],
    adminClaims: [],
  policies: [],
    loading: false,
    error: null,
    successMessage: null
  },

  mutations: {
    SET_CLAIMS(state, claims) {
      state.claims = claims
    },
    SET_ADMIN_CLAIMS(state, adminClaims) {
      state.adminClaims = adminClaims
    },
    SET_POLICIES(state, policies) {
      state.policies = policies
    },
    ADD_CLAIM(state, claim) {
      state.claims.push(claim)
    },
    SET_LOADING(state, loading) {
      state.loading = loading
    },
    SET_ERROR(state, error) {
      state.error = error
    },
    SET_SUCCESS_MESSAGE(state, message) {
      state.successMessage = message
    },
    CLEAR_MESSAGES(state) {
      state.error = null
      state.successMessage = null
    }
  },

  actions: {
    // Back-compat: some views dispatch fetchClaims; route to user claims
    async fetchClaims({ dispatch }, maybeUserId) {
      const localUserId = parseInt(localStorage.getItem('userId'))
      const userId = Number.isInteger(maybeUserId) ? maybeUserId : localUserId
      if (userId) {
        return dispatch('fetchUserClaims', userId)
      }
    },

    // Fetch user-specific claims
    async fetchUserClaims({ commit }, userId) {
      try {
        commit('SET_LOADING', true)
        commit('CLEAR_MESSAGES')
        const response = await makeRequestWithToken('GET', `/claim/user/${userId}`)
        commit('SET_CLAIMS', response.data)
      } catch (error) {
        commit('SET_ERROR', 'Failed to load claims')
        console.error('Error fetching user claims:', error)
      } finally {
        commit('SET_LOADING', false)
      }
    },

    // Fetch all claims (for admin)
    async fetchAllClaims({ commit }) {
      try {
        console.log('🔍 fetchAllClaims: Starting API call...')
        commit('SET_LOADING', true)
        commit('CLEAR_MESSAGES')
        
        console.log('🔍 Making API request to /claim/claims')
        const response = await makeRequestWithToken('GET', '/claim/claims')
        
        console.log('🔍 API Response received:', response)
        console.log('🔍 Response data:', response.data)
        console.log('🔍 Response status:', response.status)
        
        commit('SET_ADMIN_CLAIMS', response.data)
        console.log('🔍 Claims committed to store successfully')
        
      } catch (error) {
        console.error('❌ Error fetching admin claims:', error)
        console.error('❌ Error response:', error.response)
        console.error('❌ Error message:', error.message)
        console.error('❌ Error code:', error.code)
        console.error('❌ Error config:', error.config)
        
        // Check if it's a network error
        if (error.message === 'Network Error') {
          console.error('❌ NETWORK ERROR: Backend server might be down or CORS issue')
          console.error('❌ Check if backend is running on http://localhost:9090')
        }
        
        commit('SET_ERROR', 'Failed to load admin claims')
      } finally {
        commit('SET_LOADING', false)
        console.log('🔍 fetchAllClaims: Loading state set to false')
      }
    },

    // Fetch active policies for the logged-in user and attach remaining amount
    async fetchPolicies({ commit }) {
      try {
        commit('SET_LOADING', true)
        commit('CLEAR_MESSAGES')

        const userId = parseInt(localStorage.getItem('userId'))
        if (!userId) {
          commit('SET_ERROR', 'User not authenticated. Please log in again.')
          return
        }

        // Get user's policies
        const res = await makeRequestWithToken('GET', `/claim/policy/${userId}`)
        const rawPolicies = Array.isArray(res.data) ? res.data : []

        // For each policy, get remaining amount and normalize to UI shape
        const normalized = await Promise.all(rawPolicies.map(async (up) => {
          const policyId = up?.policy?.id
          const name = up?.policy?.name || 'Policy'
          const description = up?.policy?.description || ''
          const coverageAmt = up?.policy?.coverageAmt || 0

          let availableAmount = coverageAmt
          try {
            if (policyId != null) {
              const rem = await makeRequestWithToken('GET', `/claim/policy/remaining-amount/${policyId}`)
              if (rem?.data?.remainingClaimAmount != null) {
                availableAmount = rem.data.remainingClaimAmount
              }
            }
          } catch (e) {
            // If remaining amount fails, fall back to coverage amount
            console.warn('Failed to fetch remaining amount for policy', policyId, e?.message)
          }

          return {
            id: up?.id, // userPolicyId used by the form
            policyType: name, // UI displays name here
            policyNumber: up?.policy?.policyNumber || `POL-${policyId || up?.id}`,
            description,
            coverageAmount: coverageAmt,
            availableAmount
          }
        }))

        commit('SET_POLICIES', normalized)
      } catch (error) {
        commit('SET_ERROR', 'Failed to load policies')
        console.error('Error fetching policies:', error)
      } finally {
        commit('SET_LOADING', false)
      }
    },

    // Submit a new claim
    async submitClaim({ commit }, claimData) {
      try {
        commit('SET_LOADING', true)
        commit('CLEAR_MESSAGES')
        // Enforce the exact payload the backend expects
        const body = {
          userPolicyId: Number(claimData.userPolicyId),
          claimDate: claimData.claimDate,
          claimAmount: Number(claimData.claimAmount),
          reason: claimData.reason
        }
        const response = await makeRequestWithToken('POST', '/claim', body)
        commit('ADD_CLAIM', response.data)
        commit('SET_SUCCESS_MESSAGE', `Claim submitted successfully! Claim ID: ${response.data.id}`)
        return response.data
      } catch (error) {
        const errorMessage = error.response?.data?.message || 'Failed to submit claim'
        commit('SET_ERROR', errorMessage)
        console.error('Error submitting claim:', error)
        throw error
      } finally {
        commit('SET_LOADING', false)
      }
    },

    // Review claim (admin action)
    async reviewClaim({ commit, dispatch }, { claimId, status, reviewComments }) {
      try {
        commit('SET_LOADING', true)
        commit('CLEAR_MESSAGES')
        const body = { status, reviewComments }
        await makeRequestWithToken('PUT', `/claim/${claimId}/review`, body)
        commit('SET_SUCCESS_MESSAGE', `Claim ${status.toLowerCase()} successfully`)
        // Refresh admin claims after update
        dispatch('fetchAllClaims')
      } catch (error) {
        const errorMessage = error.response?.data?.message || `Failed to ${status.toLowerCase()} claim`
        commit('SET_ERROR', errorMessage)
        console.error('Error reviewing claim:', error)
        throw error
      } finally {
        commit('SET_LOADING', false)
      }
    },

    clearMessages({ commit }) {
      commit('CLEAR_MESSAGES')
    }
  },

  getters: {
    // User claims getters
    pendingClaims: (state) => state.claims.filter(claim => claim.status === 'PENDING'),
    approvedClaims: (state) => state.claims.filter(claim => claim.status === 'APPROVED'),
    rejectedClaims: (state) => state.claims.filter(claim => claim.status === 'REJECTED'),
    
    // Admin claims getters
    adminPendingClaims: (state) => state.adminClaims.filter(claim => claim.status === 'PENDING'),
    adminApprovedClaims: (state) => state.adminClaims.filter(claim => claim.status === 'APPROVED'),
    adminRejectedClaims: (state) => state.adminClaims.filter(claim => claim.status === 'REJECTED')
  }
})
