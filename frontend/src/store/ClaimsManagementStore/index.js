import { requestWithAuth } from "@/utils/requests.js";

export default {
  namespaced: true,

  // Application state for claims management
  state: {
    claims: [],
    adminClaims: [],
    policies: [],
    loading: false,
    error: null,
    successMessage: null,
  },

  // State update functions
  mutations: {
    // This function takes claimsList array as input and updates user claims in state
    SET_CLAIMS(state, claimsList) {
      state.claims = claimsList;
    },

    // This function takes adminClaimsList array as input and updates admin claims in state
    SET_ADMIN_CLAIMS(state, adminClaimsList) {
      state.adminClaims = adminClaimsList;
    },

    // This function takes policiesList array as input and updates policies in state
    SET_POLICIES(state, policiesList) {
      state.policies = policiesList;
    },

    // This function takes newClaim object as input and adds it to user claims array
    ADD_CLAIM(state, newClaim) {
      state.claims.push(newClaim);
    },

    // This function takes loadingStatus boolean as input and updates loading state
    SET_LOADING(state, loadingStatus) {
      state.loading = loadingStatus;
    },

    // This function takes errorMessage string as input and sets error message in state
    SET_ERROR(state, errorMessage) {
      state.error = errorMessage;
    },

    // This function takes message string as input and sets success message in state
    SET_SUCCESS_MESSAGE(state, message) {
      state.successMessage = message;
    },

    // This function takes no input and clears both error and success messages from state
    CLEAR_MESSAGES(state) {
      state.error = null;
      state.successMessage = null;
    },
  },

  // API action functions
  actions: {
    // This function takes optional userId as input and fetches claims for that user or current logged-in user
    async fetchClaims({ dispatch }, providedUserId) {
      const currentUser = JSON.parse(
        localStorage.getItem("currentUser") || "{}"
      );
      const currentUserId = parseInt(currentUser.userId);
      const targetUserId = Number.isInteger(providedUserId)
        ? providedUserId
        : currentUserId;

      if (targetUserId) {
        return dispatch("fetchUserClaims", targetUserId);
      }
    },

    // This function takes userId as input and retrieves all claims belonging to that specific user
    async fetchUserClaims({ commit }, userId) {
      try {
        commit("SET_LOADING", true);
        commit("CLEAR_MESSAGES");

        const response = await requestWithAuth("GET", `/claim/user/${userId}`);
        commit("SET_CLAIMS", response.data);
      } catch (error) {
        commit("SET_ERROR", "Failed to load user claims");
      } finally {
        commit("SET_LOADING", false);
      }
    },

    // This function takes no input and retrieves all claims from database for admin dashboard view
    async fetchAllClaims({ commit }) {
      try {
        commit("SET_LOADING", true);
        commit("CLEAR_MESSAGES");

        const response = await requestWithAuth("GET", "/claim/claims");
        commit("SET_ADMIN_CLAIMS", response.data);
      } catch (error) {
        commit("SET_ERROR", "Failed to load admin claims");
      } finally {
        commit("SET_LOADING", false);
      }
    },

    // This function takes no input and fetches all active/renewed policies for current logged-in user
    async fetchPolicies({ commit }) {
      try {
        commit("SET_LOADING", true);
        commit("CLEAR_MESSAGES");

        const currentUser = JSON.parse(
          localStorage.getItem("currentUser") || "{}"
        );
        const currentUserId = parseInt(currentUser.userId);
        if (!currentUserId) {
          commit("SET_ERROR", "User not authenticated");
          return;
        }

        // Get user policies from API
        const policiesResponse = await requestWithAuth(
          "GET",
          `/user/policies/${currentUserId}`
        );
        const rawPoliciesList = Array.isArray(policiesResponse.data)
          ? policiesResponse.data
          : [];

        // Get user's claims to calculate remaining amounts
        let userClaims = [];
        try {
          const claimsResponse = await requestWithAuth(
            "GET",
            `/claim/user/${currentUserId}`
          );
          userClaims = Array.isArray(claimsResponse.data)
            ? claimsResponse.data
            : [];
        } catch (error) {
          console.log(
            "Could not fetch user claims for available amount calculation"
          );
        }

        // Process each valid policy and calculate remaining claim amount
        const processedPolicies = rawPoliciesList
          .filter((policy) => policy && policy.id) // Only valid policies
          .filter(
            (policy) =>
              policy.status === "ACTIVE" || policy.status === "RENEWED"
          ) // Only active policies
          .map((policy) => {
            const policyId = policy.id;
            const coverageAmount = policy.coverageAmount || 0;

            // Calculate total approved claims for this policy
            const policyApprovedClaims = userClaims.filter(
              (claim) =>
                claim.userPolicyId === policyId &&
                (claim.status === "APPROVED" || claim.status === "PENDING")
            );
            const approvedClaimsTotal = policyApprovedClaims.reduce(
              (total, claim) => total + (claim.claimAmount || 0),
              0
            );

            // Calculate remaining available amount
            const availableAmount = Math.max(
              0,
              coverageAmount - approvedClaimsTotal
            );

            console.log(
              `Policy ${policyId}: Coverage=${coverageAmount}, Approved Claims=${approvedClaimsTotal}, Available=${availableAmount}`
            );

            // Format policy data
            return {
              id: policy.id || policy.policyId,
              policyType: policy.policyName || "Policy",
              policyNumber: `POL-${policy.policyId || policy.id}`,
              description: policy.policyName || "",
              coverageAmount: coverageAmount,
              availableAmount: availableAmount,
            };
          });

        commit("SET_POLICIES", processedPolicies);
      } catch (error) {
        commit("SET_ERROR", "Failed to load policies");
      } finally {
        commit("SET_LOADING", false);
      }
    },

    // This function takes FormData as input and submits a claim with document attachment
    async submitClaimWithDocument({ commit }, formData) {
      try {
        commit("SET_LOADING", true);
        commit("CLEAR_MESSAGES");

        // Use fetch for multipart/form-data instead of our regular request helper
        // Now using the main /claim endpoint since document is mandatory
        const currentUser = JSON.parse(
          localStorage.getItem("currentUser") || "{}"
        );
        const token = currentUser.token || localStorage.getItem("token");

        const response = await fetch("http://localhost:9090/claim", {
          method: "POST",
          headers: {
            Authorization: `Bearer ${token}`,
          },
          body: formData,
        });

        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }

        const responseData = await response.json();
        commit("ADD_CLAIM", responseData);
        commit(
          "SET_SUCCESS_MESSAGE",
          `Claim submitted successfully with document! Claim ID: ${responseData.id}`
        );

        return responseData;
      } catch (error) {
        console.error("Submit claim with document error:", error);
        commit("SET_ERROR", "Failed to submit claim with document");
        throw error;
      } finally {
        commit("SET_LOADING", false);
      }
    },

    // This function takes claim review data as input and updates claim status to approved or rejected
    async reviewClaim(
      { commit, dispatch },
      { claimId, status, reviewComments, userPolicyId }
    ) {
      try {
        commit("SET_LOADING", true);
        commit("CLEAR_MESSAGES");

        const reviewPayload = { status, reviewComments };
        await requestWithAuth("PUT", `/claim/${claimId}/review`, reviewPayload);

        commit(
          "SET_SUCCESS_MESSAGE",
          `Claim ${status.toLowerCase()} successfully`
        );
        dispatch("fetchAllClaims");

        // Update no claim bonus if claim approved
        if (status === "APPROVED" && userPolicyId) {
          await dispatch("updateNoClaimBonus", userPolicyId);
        }
      } catch (error) {
        commit("SET_ERROR", "Failed to review claim");
        throw error;
      } finally {
        commit("SET_LOADING", false);
      }
    },

    // This function takes userPolicyId as input and updates no claim bonus status for that policy
    async updateNoClaimBonus(context, userPolicyId) {
      try {
        await requestWithAuth("PATCH", `/user/policy/ncb/${userPolicyId}`);
      } catch (error) {
        
      }
    },
  },

  // Computed values from state
  getters: {
    // User claims by status
    pendingClaims: (state) =>
      state.claims.filter((claim) => claim.status === "PENDING"),
    approvedClaims: (state) =>
      state.claims.filter((claim) => claim.status === "APPROVED"),
    rejectedClaims: (state) =>
      state.claims.filter((claim) => claim.status === "REJECTED"),
    allClaims: (state) => state.claims,

    // Admin claims by status
    adminPendingClaims: (state) =>
      state.adminClaims.filter((claim) => claim.status === "PENDING"),
    adminApprovedClaims: (state) =>
      state.adminClaims.filter((claim) => claim.status === "APPROVED"),
    adminRejectedClaims: (state) =>
      state.adminClaims.filter((claim) => claim.status === "REJECTED"),
  },
};
