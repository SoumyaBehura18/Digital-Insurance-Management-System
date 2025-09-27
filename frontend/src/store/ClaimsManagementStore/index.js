import { requestWithAuth } from '@/utils/requests.js'

export default {
  namespaced: true,
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
    async fetchClaims({ dispatch }, maybeUserId) {
      const localUserId = parseInt(localStorage.getItem('userId'))
      const userId = Number.isInteger(maybeUserId) ? maybeUserId : localUserId
      if (userId) {
        return dispatch('fetchUserClaims', userId)
      }
    },

    async fetchUserClaims({ commit }, userId) {
      try {
        commit('SET_LOADING', true)
        commit('CLEAR_MESSAGES')
        const response = await requestWithAuth('GET', `/claim/user/${userId}`)
        commit('SET_CLAIMS', response.data)
      } catch (error) {
        commit('SET_ERROR', 'Something went wrong')
      } finally {
        commit('SET_LOADING', false)
      }
    },

    async fetchAllClaims({ commit }) {
      try {
        commit('SET_LOADING', true)
        commit('CLEAR_MESSAGES')
        const response = await requestWithAuth('GET', '/claim/claims')
        commit('SET_ADMIN_CLAIMS', response.data)
      } catch (error) {
        commit('SET_ERROR', 'Something went wrong')
      } finally {
        commit('SET_LOADING', false)
      }
    },

    async fetchPolicies({ commit }) {
    try {
      commit('SET_LOADING', true)
      commit('CLEAR_MESSAGES')

      const userId = parseInt(localStorage.getItem('userId'))
      if (!userId) {
        commit('SET_ERROR', 'User not authenticated')
        return
      }

      // 🔹 Fetch user policies from new endpoint
      const res = await requestWithAuth('GET', `/user/policies/${userId}`)
      const rawPolicies = Array.isArray(res.data) ? res.data : []

      // 🔹 Normalize and also fetch remaining amount
      const normalized = await Promise.all(rawPolicies.map(async (p) => {
        const policyId = p?.policyId
        let availableAmount = p.coverageAmount

        try {
          if (policyId != null) {
            const rem = await requestWithAuth('GET', `/claim/policy/remaining-amount/${policyId}`)
            if (rem?.data?.remainingClaimAmount != null) {
              availableAmount = rem.data.remainingClaimAmount
            }
          }
        } catch (err) {
          console.log("Error fetching remaining amount:", err)
        }

        return {
          id: p.id, // userPolicyId
          policyType: p.policyName || 'Policy',
          policyNumber: `POL-${p.policyId || p.id}`,
          description: p.policyName || '',
          coverageAmount: p.coverageAmount || 0,
          availableAmount
        }
      }))

      commit('SET_POLICIES', normalized)
    } catch (err) {
      console.log("Error fetching policies:", err)
    } finally {
      commit('SET_LOADING', false)
    }
  },

    async submitClaim({ commit }, claimData) {
      try {
        commit('SET_LOADING', true)
        commit('CLEAR_MESSAGES')
        const body = {
          userPolicyId: Number(claimData.userPolicyId),
          claimDate: claimData.claimDate,
          claimAmount: Number(claimData.claimAmount),
          reason: claimData.reason
        }
        const response = await requestWithAuth('POST', '/claim', body)
        commit('ADD_CLAIM', response.data)
        commit('SET_SUCCESS_MESSAGE', `Claim submitted successfully! Claim ID: ${response.data.id}`)
        return response.data
      } catch (error) {
        commit('SET_ERROR', 'Something went wrong')
        throw error
      } finally {
        commit('SET_LOADING', false)
      }
    },

  async reviewClaim({ commit, dispatch }, { claimId, status, reviewComments, userPolicyId }) {
  try {
    commit('SET_LOADING', true)
    commit('CLEAR_MESSAGES')
    const body = { status, reviewComments }
    await requestWithAuth('PUT', `/claim/${claimId}/review`, body)
    commit('SET_SUCCESS_MESSAGE', `Claim ${status.toLowerCase()} successfully`)
    dispatch('fetchAllClaims')

    // ✅ Notify NCB if claim approved
    if (status === 'APPROVED' && userPolicyId) {
      await dispatch('notifyNcb', userPolicyId)
    }

  } catch (error) {
    commit('SET_ERROR', 'Something went wrong')
    throw error
  } finally {
    commit('SET_LOADING', false)
  }
},


  async notifyNcb(_, userPolicyId) {
    try {
      await requestWithAuth('PATCH', `/user/policy/ncb/${userPolicyId}`)
    } catch (error) {
      console.error('Failed to notify NCB for userPolicyId:', userPolicyId, error)
    }
  }
},


  getters: {
    pendingClaims: (state) => state.claims.filter(claim => claim.status === 'PENDING'),
    approvedClaims: (state) => state.claims.filter(claim => claim.status === 'APPROVED'),
    rejectedClaims: (state) => state.claims.filter(claim => claim.status === 'REJECTED'),

    adminPendingClaims: (state) => state.adminClaims.filter(claim => claim.status === 'PENDING'),
    adminApprovedClaims: (state) => state.adminClaims.filter(claim => claim.status === 'APPROVED'),
    adminRejectedClaims: (state) => state.adminClaims.filter(claim => claim.status === 'REJECTED')
  }
}
