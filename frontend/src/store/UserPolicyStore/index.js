import { requestWithAuth } from "@/utils/requests";

const state = {
  policies: [], // list of UserPolicyResponse
  loading: false,
  error: null,
};

const getters = {
  getPolicies: (state) => state.policies,
  isLoading: (state) => state.loading,
  getError: (state) => state.error,
};

const mutations = {
  SET_POLICIES(state, policies) {
    state.policies = policies;
  },
  SET_LOADING(state, status) {
    state.loading = status;
  },
  SET_ERROR(state, error) {
    state.error = error;
  },
};

const actions = {
  // Fetch all policies for the current user
  async fetchUserPolicies({ commit }, userId) {
    console.log("Action fired with userId:", userId);
    commit("SET_LOADING", true);
    try {
      const response = await requestWithAuth("GET", `/user/policies/${userId}`);
      console.log("API Response:", response.data);
      commit("SET_POLICIES", response.data);
      commit("SET_ERROR", null);
    } catch (error) {
      console.error("API Error:", error);
      commit("SET_ERROR", error.response?.data || "Failed to fetch policies");
      commit("SET_POLICIES", []);
    } finally {
      commit("SET_LOADING", false);
    }
  },

  // Fetch a single policy by ID
  async fetchPolicyById({ commit }, policyId) {
    commit("SET_LOADING", true);
    try {
      const response = await requestWithAuth("GET", `/user/policy/${policyId}`);
      // Optionally update the store
      const index = state.policies.findIndex((p) => p.id === policyId);
      if (index !== -1) state.policies[index] = response.data;
      else state.policies.push(response.data);
      commit("SET_ERROR", null);
    } catch (error) {
      commit("SET_ERROR", error.response?.data || "Failed to fetch policy");
    } finally {
      commit("SET_LOADING", false);
    }
  },

  // Add a new policy
  async createPolicy({ commit, state }, policyData) {
    commit("SET_LOADING", true);
    try {
      const response = await requestWithAuth(
        "POST",
        "/user/policy/purchase",
        policyData
      );
      commit("SET_POLICIES", [...state.policies, response.data]);
      commit("SET_ERROR", null);
    } catch (error) {
      commit("SET_ERROR", error.response?.data || "Failed to create policy");
    } finally {
      commit("SET_LOADING", false);
    }
  },
  async renewPolicy({ commit, state }, policy) {
    commit("SET_LOADING", true);
    try {
      // Build payload for renewal
      const payload = {
        premiumRate: policy.renewalRate || policy.premiumPaid,
        policyStatus: "RENEWED",
      };

      console.log("Renewal payload:", payload);

      // Hit the backend renewal endpoint
      const response = await requestWithAuth(
        "PATCH",
        `/user/policy/status/${policy.id}`,
        payload
      );

      console.log("Renewal response:", response.data);

      // Update the store: find the policy and update it
      const index = state.policies.findIndex((p) => p.id === policy.id);
      if (index !== -1) {
        state.policies[index] = {
          ...state.policies[index],
          ...response.data,
        };
        commit("SET_POLICIES", [...state.policies]);
      }

      commit("SET_ERROR", null);
      return response.data;
    } catch (err) {
      commit("SET_ERROR", err.response?.data || "Renewal failed");
      throw err;
    } finally {
      commit("SET_LOADING", false);
    }
  },
  async expirePolicy({ commit }, policy) {
    try {
      // Prepare payload
      const payload = {
        policyStatus: "EXPIRED",
        premiumPaid: 0.0,
      };

      const response = await requestWithAuth(
        "PATCH",
        `/user/policy/status/${policy.id}`,
        payload
      );

      // Update local store
      const updatedPolicies = [...state.policies];
      const index = updatedPolicies.findIndex((p) => p.id === policy.id);
      if (index !== -1)
        updatedPolicies[index] = {
          ...updatedPolicies[index],
          ...response.data,
        };
      commit("SET_POLICIES", updatedPolicies);

      return response.data;
    } catch (error) {
      console.error("Failed to expire policy:", error);
      return null;
    }
  },
  async cancelPolicy({ commit, state }, policy) {
    console.log("Cancelling policy:", policy);
    commit("SET_LOADING", true);
    try {
      const payload = {
        premiumPaid:policy.premiumPaid,
        policyStatus: "CANCELLED",
        // premium remains the same
      };
      const response = await requestWithAuth(
        "PATCH",
        `/user/policy/status/${policy.id}`,
        payload
      );
      console.log("Cancel response:", response.data);
      commit("SET_ERROR", null);

      // Update policy in store
      const updatedPolicies = state.policies.map((p) =>
        p.policyId === payload.policyId || p.id === payload.policyId
          ? { ...p, status: "CANCELLED" }
          : p
      );
       commit("SET_POLICIES", updatedPolicies);
    } catch (error) {
      console.error(error);
    } finally {
      commit("SET_LOADING", false);
    }
  },
};

export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions,
};
