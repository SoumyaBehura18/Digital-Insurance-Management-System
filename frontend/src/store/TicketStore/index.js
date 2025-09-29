import { requestWithAuth } from "@/utils/requests";

const state = {
  tickets: [], // list of UserTickets
  individualTicket: null,
  loading: false,
  error: null,
};

const getters = {
  getTickets: (state) => state.tickets,
  getIndividualTicket: (state) => state.tickets,
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
  async fetchAllTickets({ commit }) {
    commit("SET_LOADING", true);
    try {
      const response = await requestWithAuth("GET", `/tickets/all`);
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

  async updateTicket({ commit, state }, { ticketData, ticketId }) {
    commit("SET_LOADING", true);
    try {
      const response = await requestWithAuth(
        "PATCH",
        `/tickets/${ticketId}`,
        ticketData
      );
      commit("SET_TICKETS", [...state.tickets, response.data]);
      commit("SET_ERROR", null);
    } catch (error) {
      commit("SET_ERROR", error.response?.data || "Failed to update ticket");
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
