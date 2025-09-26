import { requestWithAuth } from '@/utils/requests'

const state = {
  policies: [],       // list of UserPolicyResponse
  loading: false,
  error: null
}

const getters = {
  getPolicies: (state) => state.policies,
  isLoading: (state) => state.loading,
  getError: (state) => state.error
}

const mutations = {
  SET_POLICIES(state, policies) {
    state.policies = policies
  },
  SET_LOADING(state, status) {
    state.loading = status
  },
  SET_ERROR(state, error) {
    state.error = error
  }
}

const actions = {
  // Fetch all policies for the current user
async fetchUserPolicies({ commit }, userId) {
  console.log("Action fired with userId:", userId);
  commit('SET_LOADING', true);
  try {
  const response = await requestWithAuth('GET', `/user/policies/${userId}`);
    console.log("API Response:", response.data);
    commit('SET_POLICIES', response.data);
    commit('SET_ERROR', null);
  } catch (error) {
    console.error("API Error:", error);
    commit('SET_ERROR', error.response?.data || 'Failed to fetch policies');
    commit('SET_POLICIES', []);
  } finally {
    commit('SET_LOADING', false);
  }
},

  // Fetch a single policy by ID
  async fetchPolicyById({ commit }, policyId) {
    commit('SET_LOADING', true)
    try {
  const response = await requestWithAuth('GET', `/user/policy/${policyId}`)
      // Optionally update the store
      const index = state.policies.findIndex(p => p.id === policyId)
      if (index !== -1) state.policies[index] = response.data
      else state.policies.push(response.data)
      commit('SET_ERROR', null)
    } catch (error) {
      commit('SET_ERROR', error.response?.data || 'Failed to fetch policy')
    } finally {
      commit('SET_LOADING', false)
    }
  },

  // Add a new policy
  async createPolicy({ commit, state }, policyData) {
    commit('SET_LOADING', true)
    try {
  const response = await requestWithAuth('POST', '/user/policy/purchase', policyData)
      commit('SET_POLICIES', [...state.policies, response.data])
      commit('SET_ERROR', null)
    } catch (error) {
      commit('SET_ERROR', error.response?.data || 'Failed to create policy')
    } finally {
      commit('SET_LOADING', false)
    }
  },

}

export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions
}
