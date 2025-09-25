import { createStore } from 'vuex'

// Mock data imports (need to be replaced with real API calls later)
import claimsData from './sample-data/claims.json'
import policiesData from './sample-data/policies.json'
import adminClaimsData from './sample-data/admin-claims.json'

export default createStore({
  state: {
    policies: [],
    claims: [],
    adminClaims: [],
    loading: false,
    error: null,
    successMessage: null
  },

  mutations: {
    SET_POLICIES(state, policies) {
      state.policies = policies
    },
    SET_CLAIMS(state, claims) {
      state.claims = claims
    },
    SET_ADMIN_CLAIMS(state, adminClaims) {
      state.adminClaims = adminClaims
    },
    ADD_CLAIM(state, claim) {
      state.claims.push(claim)
    },
    UPDATE_CLAIM(state, updatedClaim) {
      const index = state.claims.findIndex(claim => claim.id === updatedClaim.id)
      if (index !== -1) {
        state.claims[index] = { ...state.claims[index], ...updatedClaim }
      }
    },
    UPDATE_ADMIN_CLAIM(state, updatedClaim) {
      const index = state.adminClaims.findIndex(claim => claim.id === updatedClaim.id)
      if (index !== -1) {
        state.adminClaims[index] = { ...state.adminClaims[index], ...updatedClaim }
      }
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
    async fetchPolicies({ commit }) {
      try {
        commit('SET_LOADING', true)
        const response = await claimService.getAllPolicies()
        commit('SET_POLICIES', response.data)
      } catch (error) {
        commit('SET_ERROR', 'Failed to load policies')
      } finally {
        commit('SET_LOADING', false)
      }
    },

    async fetchClaims({ commit }, userId = null) {
      try {
        commit('SET_LOADING', true)
        const response = userId 
          ? await claimService.getUserClaims(userId)
          : await claimService.getAllClaims()
        commit('SET_CLAIMS', response.data)
      } catch (error) {
        commit('SET_ERROR', 'Failed to load claims')
      } finally {
        commit('SET_LOADING', false)
      }
    },

    async submitClaim({ commit }, claimData) {
      try {
        commit('SET_LOADING', true)
        commit('CLEAR_MESSAGES')
        const response = await claimService.submitClaim(claimData)
        commit('ADD_CLAIM', response.data)
        commit('SET_SUCCESS_MESSAGE', `Claim submitted successfully! Claim ID: ${response.data.id}`)
        return response.data
      } catch (error) {
        commit('SET_ERROR', error.message || 'Failed to submit claim')
        throw error
      } finally {
        commit('SET_LOADING', false)
      }
    },

    async updateClaimStatus({ commit }, { claimId, status, reviewerComment }) {
      try {
        commit('SET_LOADING', true)
        const response = await claimService.updateClaimStatus(claimId, status, reviewerComment)
        commit('UPDATE_CLAIM', response.data)
        commit('SET_SUCCESS_MESSAGE', `Claim ${status.toLowerCase()} successfully`)
      } catch (error) {
        commit('SET_ERROR', `Failed to ${status.toLowerCase()} claim`)
        throw error
      } finally {
        commit('SET_LOADING', false)
      }
    },

    async fetchAdminClaims({ commit }) {
      try {
        commit('SET_LOADING', true)
        const response = await claimService.getAllAdminClaims()
        commit('SET_ADMIN_CLAIMS', response.data)
      } catch (error) {
        commit('SET_ERROR', 'Failed to load admin claims')
      } finally {
        commit('SET_LOADING', false)
      }
    },

    async updateAdminClaimStatus({ commit }, { claimId, status, reviewerComment }) {
      try {
        commit('SET_LOADING', true)
        const response = await claimService.updateAdminClaimStatus(claimId, status, reviewerComment)
        commit('UPDATE_ADMIN_CLAIM', response.data)
        commit('SET_SUCCESS_MESSAGE', `Claim ${status.toLowerCase()} successfully`)
      } catch (error) {
        commit('SET_ERROR', `Failed to ${status.toLowerCase()} claim`)
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
    pendingClaims: (state) => state.claims.filter(claim => claim.status === 'PENDING'),
    approvedClaims: (state) => state.claims.filter(claim => claim.status === 'APPROVED'),
    rejectedClaims: (state) => state.claims.filter(claim => claim.status === 'REJECTED'),
    
    // Admin getters
    adminPendingClaims: (state) => state.adminClaims.filter(claim => claim.status === 'PENDING'),
    adminApprovedClaims: (state) => state.adminClaims.filter(claim => claim.status === 'APPROVED'),
    adminRejectedClaims: (state) => state.adminClaims.filter(claim => claim.status === 'REJECTED'),
    adminClaimsByPriority: (state) => (priority) => state.adminClaims.filter(claim => claim.priority === priority)
  }
})

/* ------------------------------------
   claimService (sample implementation)
   Later need to be replaced with Axios calls
------------------------------------ */
let claims = [...claimsData]
let policies = [...policiesData]
let adminClaims = [...adminClaimsData]

const delay = (ms = 500) => new Promise(resolve => setTimeout(resolve, ms))

const getNextId = () => Math.max(...claims.map(c => c.id)) + 1

export const claimService = {
  async getUserClaims(userId) {
    await delay()
    const userClaims = claims.filter(c => c.userId === userId)
    return { data: userClaims, status: 200, message: 'Claims retrieved successfully' }
  },

  async submitClaim(newClaim) {
    await delay()
    const claim = {
      id: getNextId(),
      ...newClaim,
      status: 'PENDING',
      reviewerComment: null,
      resolvedDate: null,
      claimDate: new Date().toISOString().split('T')[0]
    }
    claims.push(claim)
    return { data: claim, status: 201, message: 'Claim submitted successfully' }
  },

  async getAllClaims() {
    await delay()
    return { data: claims, status: 200, message: 'All claims retrieved successfully' }
  },

  async updateClaimStatus(claimId, status, reviewerComment = null) {
    await delay()
    const idx = claims.findIndex(c => c.id === claimId)
    if (idx === -1) throw { data: null, status: 404, message: 'Claim not found' }

    claims[idx].status = status
    claims[idx].reviewerComment = reviewerComment
    claims[idx].resolvedDate = status !== 'PENDING' ? new Date().toISOString().split('T')[0] : null

    return { data: claims[idx], status: 200, message: 'Claim status updated successfully' }
  },

  async getClaimById(claimId) {
    await delay()
    const claim = claims.find(c => c.id === claimId)
    if (!claim) throw { data: null, status: 404, message: 'Claim not found' }
    return { data: claim, status: 200, message: 'Claim retrieved successfully' }
  },

  async getAllPolicies() {
    await delay()
    return { data: policies, status: 200, message: 'Policies retrieved successfully' }
  },

  async getPolicyById(policyId) {
    await delay()
    const policy = policies.find(p => p.id === policyId)
    if (!policy) throw { data: null, status: 404, message: 'Policy not found' }
    return { data: policy, status: 200, message: 'Policy retrieved successfully' }
  },

  // Admin Claims Methods
  async getAllAdminClaims() {
    await delay()
    return { data: adminClaims, status: 200, message: 'Admin claims retrieved successfully' }
  },

  async updateAdminClaimStatus(claimId, status, reviewerComment = null) {
    await delay()
    const idx = adminClaims.findIndex(c => c.id === claimId)
    if (idx === -1) throw { data: null, status: 404, message: 'Claim not found' }

    adminClaims[idx].status = status
    adminClaims[idx].reviewerComment = reviewerComment
    adminClaims[idx].resolvedDate = status !== 'PENDING' ? new Date().toISOString().split('T')[0] : null

    return { data: adminClaims[idx], status: 200, message: 'Admin claim status updated successfully' }
  },

  async getAdminClaimById(claimId) {
    await delay()
    const claim = adminClaims.find(c => c.id === claimId)
    if (!claim) throw { data: null, status: 404, message: 'Admin claim not found' }
    return { data: claim, status: 200, message: 'Admin claim retrieved successfully' }
  }
}
