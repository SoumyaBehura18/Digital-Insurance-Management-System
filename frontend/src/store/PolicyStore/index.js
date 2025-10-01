import { makeRequestWithToken } from '@/utils/requests'

const state = {
  allPolicies: [],
  vehiclePolicies: [],
  lifePolicies: [],
  healthPolicies: [],
  loading: false,
  error: null
}

const getters = {
  getAllPolicies: (state) => state.allPolicies,
  getVehiclePolicies: (state) => state.vehiclePolicies,
  getLifePolicies: (state) => state.lifePolicies,
  getHealthPolicies: (state) => state.healthPolicies,
  isLoading: (state) => state.loading,
  getError: (state) => state.error
}

const mutations = {
  SET_ALL_POLICIES(state, policies) {
    state.allPolicies = policies
  },
  SET_VEHICLE_POLICIES(state, policies) {
    state.vehiclePolicies = policies
  },
  SET_LIFE_POLICIES(state, policies) {
    state.lifePolicies = policies
  },
  SET_HEALTH_POLICIES(state, policies) {
    state.healthPolicies = policies
  },
  SET_LOADING(state, status) {
    state.loading = status
  },
  SET_ERROR(state, error) {
    state.error = error
  }
}

const actions = {
  // Fetch all policies available for a user
  async fetchAllPolicies({ commit }, policyRequest) {
    commit('SET_LOADING', true)
    try {
      const response = await makeRequestWithToken(
        'POST',
        `/policies/allPolicies`,
        policyRequest
      )
      commit('SET_ALL_POLICIES', response.data)
      commit('SET_ERROR', null)
    } catch (error) {
      commit('SET_ERROR', error.response?.data || 'Failed to fetch all policies')
      commit('SET_ALL_POLICIES', [])
    } finally {
      commit('SET_LOADING', false)
    }
  },

  // Fetch only vehicle policies
  async fetchVehiclePolicies({ commit }, policyRequest) {
    commit('SET_LOADING', true)
    try {
      const response = await makeRequestWithToken(
        'POST',
        `/policies/vehiclePolicies`,
        policyRequest
      )
      commit('SET_VEHICLE_POLICIES', response.data)
      commit('SET_ERROR', null)
    } catch (error) {
      commit('SET_ERROR', error.response?.data || 'Failed to fetch vehicle policies')
      commit('SET_VEHICLE_POLICIES', [])
    } finally {
      commit('SET_LOADING', false)
    }
  },

  // Fetch only life policies
  async fetchLifePolicies({ commit }, policyRequest) {
    commit('SET_LOADING', true)
    try {
      const response = await makeRequestWithToken(
        'POST',
        `/policies/lifePolicies`,
        policyRequest
      )
      commit('SET_LIFE_POLICIES', response.data)
      commit('SET_ERROR', null)
    } catch (error) {
      commit('SET_ERROR', error.response?.data || 'Failed to fetch life policies')
      commit('SET_LIFE_POLICIES', [])
    } finally {
      commit('SET_LOADING', false)
    }
  },

  // Fetch only health policies
  async fetchHealthPolicies({ commit }, policyRequest) {
    commit('SET_LOADING', true)
    try {
      const response = await makeRequestWithToken(
        'POST',
        `/policies/healthPolicies`,
        policyRequest
      )
      commit('SET_HEALTH_POLICIES', response.data)
      commit('SET_ERROR', null)
    } catch (error) {
      commit('SET_ERROR', error.response?.data || 'Failed to fetch health policies')
      commit('SET_HEALTH_POLICIES', [])
    } finally {
      commit('SET_LOADING', false)
    }
  }
}

export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions
}
