import { makeRequestWithToken, requestWithAuth } from "@/utils/requests";

const state = {
  policies: [],
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
  ADD_POLICY(state, policy) {
    state.policies.push(policy);
  },
  SET_LOADING(state, status) {
    state.loading = status;
  },
  SET_ERROR(state, error) {
    state.error = error;
  },
};

const actions = {
  async fetchPolicies({ commit }) {
    commit("SET_LOADING", true);
    try {
      const response = await makeRequestWithToken("GET", "/api/admin/policies");
      // Normalize API fields to match table
      const mapped = response.data.map((p) => ({
        policyId: p.policyId,
        policyName: p.policyName,
        policyType: p.policyType,
        coverage: p.coverage,
        duration: p.duration,
        description: p.description,
      }));
      commit("SET_POLICIES", mapped);
      commit("SET_ERROR", null);
    } catch (error) {
      commit("SET_ERROR", error.response?.data || "Failed to fetch policies");
      commit("SET_POLICIES", []);
    } finally {
      commit("SET_LOADING", false);
    }
  },

  async createPolicy({ commit }, newPolicy) {
    commit("SET_LOADING", true);
    try {
      // Create policy
      const payload = {
        name: newPolicy.policyName,
        type: newPolicy.policyType,
        coverageAmt: newPolicy.coverageAmount,
        premiumRate: newPolicy.annualPremium,
        durationMonths: newPolicy.policyDuration,
        renewalRate: newPolicy.renewalRate,
        description: newPolicy.description,
      };

      const response = await requestWithAuth("POST", "/api/admin/policies", payload);
      const created = response.data;

      // Handle type-specific premium setup
      if (newPolicy.policyType === "LIFE") {
        await requestWithAuth(
          "POST",
          `/api/admin/policies/${created.id}/life-premium`,
          { premiumRate: newPolicy.annualPremium, renewalRate: newPolicy.renewalRate }
        );
      } else if (newPolicy.policyType === "HEALTH") {
        await requestWithAuth(
          "POST",
          `/api/admin/policies/${created.id}/health-premium`,
          {
            premiumRate: newPolicy.annualPremium,
            renewalRate: newPolicy.renewalRate,
            conditionPremiums: newPolicy.conditions.map((c) => ({
              condition: c.condition,
              extraPremium: c.extraPremium,
            })),
          }
        );
      } else if (newPolicy.policyType === "VEHICLE") {
        await requestWithAuth(
          "POST",
          `/api/admin/policies/${created.id}/vehicle-premium`,
          {
            premiumRate: newPolicy.annualPremium,
            renewalRate: newPolicy.renewalRate,
            vehicleAge: newPolicy.vehicleAge,
          }
        );
      }


      // Map API response to table fields
      const mappedPolicy = {
        policyId: created.id,
        policyName: created.name,
        policyType: created.type,
        coverage: created.coverageAmt,
        duration: created.durationMonths,
        description: created.description,
      };

      commit("ADD_POLICY", mappedPolicy);
      commit("SET_ERROR", null);
      return mappedPolicy;
    } catch (error) {
      commit("SET_ERROR", error.response?.data || "Failed to create policy");
      throw error;
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
