import { requestWithAuth } from "@/utils/requests";

const state = {
  tickets: [], // list of UserTickets
  individualTicket: null,
  loading: false,
  error: null,
};

const getters = {
  getTickets: (state) => state.policies,
  getIndividualTicket: (state) => state.policy,
  isLoading: (state) => state.loading,
  getError: (state) => state.error,
};

const mutations = {
  SET_TICKETS(state, tickets) {
    state.tickets = tickets;
  },
  SET_INDIVIDUAL_TICKET(state, individualTicket) {
    state.individualTicket = individualTicket;
  },
  SET_LOADING(state, status) {
    state.loading = status;
  },
  SET_ERROR(state, error) {
    state.error = error;
  },
};

const actions = {
  // Fetch all tickets for the current user
  async fetchUserTickets({ commit }, userId) {
    console.log("Action fired with userId:", userId);
    commit("SET_LOADING", true);
    try {
      const response = await requestWithAuth("GET", `/tickets/user/${userId}`);
      console.log("API Response:", response.data);
      commit("SET_TICKETS", response.data);
      commit("SET_ERROR", null);
      return response.data;
    } catch (error) {
      console.error("API Error:", error);
      commit("SET_ERROR", error.response?.data || "Failed to fetch tickets");
      commit("SET_TICKETS", []);
    } finally {
      commit("SET_LOADING", false);
    }
  },

  // Fetch a single policy by ID
  async fetchTicketById({ commit }, ticketId) {
    commit("SET_LOADING", true);
    try {
      // Optionally update the store
      let response;
      const index = state.tickets.findIndex((p) => p.id === ticketId);
      if (index !== -1) {
        commit("SET_INDIVIDUAL_TICKET", state.tickets[index]);
        response = state.tickets[index];
        // state.policies[index] = response.data;
      } else {
        response = await requestWithAuth("GET", `/tickets/${ticketId}`);
        state.tickets.push(response.data);
      }
      commit("SET_ERROR", null);

      return response;
    } catch (error) {
      commit("SET_ERROR", error.response?.data || "Failed to fetch ticket");
    } finally {
      commit("SET_LOADING", false);
    }
  },

  // Add a new policy
  async createTicket({ commit, state }, ticketData) {
    commit("SET_LOADING", true);
    try {
      const response = await requestWithAuth("POST", "/tickets", ticketData);
      commit("SET_TICKETS", [...state.policies, response.data]);
      commit("SET_ERROR", null);
    } catch (error) {
      commit("SET_ERROR", error.response?.data || "Failed to create ticket");
    } finally {
      commit("SET_LOADING", false);
    }
  },

  async updateTicket({ commit, state }, { ticketData, ticketId }) {
    commit("SET_LOADING", true);
    try {
      const response = await requestWithAuth(
        "PATCH",
        `/tickets/${ticketId}`,
        ticketData
      );
      commit("SET_TICKETS", [...state.tickets, response.data]); // fixed typo state.policies → state.tickets
      commit("SET_ERROR", null);
    } catch (error) {
      commit("SET_ERROR", error.response?.data || "Failed to update ticket");
    } finally {
      commit("SET_LOADING", false);
    }
  },

  async updateTicketStatus({ commit, state }, status, ticketId) {
    commit("SET_LOADING", true);
    try {
      const response = await requestWithAuth(
        "PATCH",
        `/${ticketId}/status`,
        status
      );
      commit("SET_TICKETS", [...state.policies, response.data]);
      commit("SET_ERROR", null);
    } catch (error) {
      commit("SET_ERROR", error.response?.data || "Failed to create ticket");
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
