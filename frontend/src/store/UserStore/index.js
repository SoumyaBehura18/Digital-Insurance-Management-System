import {
  makeRequestWithToken,
  makeRequestWithoutToken,
  requestWithAuth,
} from "@/utils/requests";
const state = {
  currentUser: null, // logged-in user
  users: [], // all users (if admin view)
  loading: false,
  error: null,
};

const getters = {
  getCurrentUser: (state) => state.currentUser,
  getUsers: (state) => state.users,
  isLoading: (state) => state.loading,
  getError: (state) => state.error,
};

const mutations = {
  SET_CURRENT_USER(state, user) {
    state.currentUser = user;
  },
  SET_USERS(state, users) {
    state.users = users;
  },
  SET_LOADING(state, status) {
    state.loading = status;
  },
  SET_ERROR(state, error) {
    state.error = error;
  },
};

const actions = {
  // Add this action to your user store module

  async fetchUsersByIds({ commit }, userIds) {
    commit("SET_LOADING", true);
    try {
      const queryString = userIds.map((id) => `ids=${id}`).join("&");
      const url = `/getUsersByIds?${queryString}`;

      const response = await makeRequestWithToken("GET", url);

      commit("SET_ERROR", null);

      return response.data;
    } catch (error) {
      commit("SET_ERROR", error.response?.data || "Failed to fetch users");
      throw error;
    } finally {
      commit("SET_LOADING", false);
    }
  },
  async fetchUserById({ commit }, userId) {
    commit("SET_LOADING", true);
    try {
      const response = await makeRequestWithToken("GET", `/users/${userId}`);
      commit("SET_CURRENT_USER", response.data);
      localStorage.setItem("currentUser", JSON.stringify(response.data));
      commit("SET_ERROR", null);
      commit("SET_LOADING", false);
      return response.data;
    } catch (error) {
      commit("SET_ERROR", error.response?.data || "Failed to fetch user");
      commit("SET_CURRENT_USER", null);
    } finally {
      commit("SET_LOADING", false);
    }
  },

  async fetchAllUsers({ commit }) {
    commit("SET_LOADING", true);
    try {
      const response = await makeRequestWithToken("GET", `/getAllUsers`);
      commit("SET_USERS", response.data);
      commit("SET_ERROR", null);
    } catch (error) {
      commit("SET_ERROR", error.response?.data || "Failed to fetch users");
      commit("SET_USERS", []);
    } finally {
      commit("SET_LOADING", false);
    }
  },

  async createUser({ commit }, userData) {
    commit("SET_LOADING", true);
    try {
      const response = await makeRequestWithoutToken(
        "POST",
        `/register`,
        userData
      );
      commit("SET_CURRENT_USER", response.data);
      localStorage.setItem("currentUser", JSON.stringify(response.data));
      commit("SET_ERROR", null);
    } catch (error) {
      commit("SET_ERROR", error.response?.data || "Failed to create user");
    } finally {
      commit("SET_LOADING", false);
    }
  },

  async loginUser({ commit }, credentials) {
    commit("SET_LOADING", true);
    try {
      const response = await makeRequestWithoutToken(
        "POST",
        `/login`,
        credentials
      );
      const raw = response.data || {};
      const normalizedUser = {
        id: raw.id || (raw.userId != null ? parseInt(raw.userId) : undefined),
        userId:
          raw.userId != null
            ? parseInt(raw.userId)
            : raw.id != null
            ? raw.id
            : undefined,
        role: raw.role || "user",
        token: raw.token,
        ...raw,
      };
      commit("SET_CURRENT_USER", normalizedUser);
      localStorage.setItem("currentUser", JSON.stringify(normalizedUser));

      commit("SET_ERROR", null);
    } catch (error) {
      commit("SET_ERROR", error.response?.data || "Invalid credentials");
      commit("SET_CURRENT_USER", null);
    } finally {
      commit("SET_LOADING", false);
    }
  },

  logoutUser({ commit }) {
    commit("SET_CURRENT_USER", null);
    commit("SET_USERS", []);
    localStorage.removeItem("currentUser");
    localStorage.removeItem("userId");
  },
  async updateUserRole({ commit }, { userId, role }) {
    const payload = {
      roleType: role,
    };
    commit("SET_LOADING", true);
    try {
      const response = await requestWithAuth(
        "PATCH",
        `/updateUserRole/${userId}`,
        payload
      );
      commit("SET_ERROR", null);
      return response.data;
    } catch (error) {
      commit("SET_ERROR", error.response?.data || "Failed to update user role");
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
