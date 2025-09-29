import {
  makeRequestWithToken,
  makeRequestWithoutToken,
  requestWithAuth,
} from "@/utils/requests";
const state = {
  user:null,
  currentUser: null, // logged-in user
  users: [], // all users (if admin view)
  loading: false,
  error: null,
};

const getters = {
  getUser: (state) => state.user,
  getCurrentUser: (state) => state.currentUser,
  getUsers: (state) => state.users,
  isLoading: (state) => state.loading,
  getError: (state) => state.error,
};

const mutations = {
  SET_USER(state, user) {
    state.user = user;
  },
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
  /**
   * This function takes updated user data as input,
   * sends a PUT request to the backend to update the user,
   * and updates local storage with the new user data if successful.
   */
  async updateUser({ commit, state }, userData) {
    try {
      // Get the current user's token and id from local storage
      const currentUser = JSON.parse(localStorage.getItem("currentUser"));
      const token = currentUser?.token;
      const userId = currentUser?.id || currentUser?.userId;

      // Prepare the request URL and headers

      const url = `/updateUserDetails/${userId}`;

      const response = await makeRequestWithToken("PUT", url, userData);

      // If the update is successful, update local storage and state
      if (response) {
        const updatedUser = await response.data;

        // Update local storage with new user data
        localStorage.setItem(
          "currentUser",
          JSON.stringify({
            ...currentUser,
            ...updatedUser,
            token, // keep the token
            userId: updatedUser.id, // update userId if changed
            role: updatedUser.roleType?.toLowerCase() || currentUser.role,
          })
        );

        // Optionally, commit a mutation to update user in Vuex state
        commit("SET_USER", updatedUser);

        return updatedUser;
      } else {
        // If update fails, throw error with message from backend
        console.log(response)
        const errorData = await response.data;
        throw new Error(errorData.message || "Failed to update user");
      }
    } catch (error) {
      // Simple error handling: log and rethrow
      console.error("Error updating user:", error);
      throw error;
    }
  },

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
      const response = await makeRequestWithToken("GET", `/getUserDetailsById/${userId}`);
      commit("SET_USER", response.data);
      commit("SET_ERROR", null);
      commit("SET_LOADING", false);
      return response.data;
    } catch (error) {
      commit("SET_ERROR", error.response?.data || "Failed to fetch user");
      commit("SET_USER", null);
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
    console.log("Creating user with data:", userData);
    commit("SET_LOADING", true);
    try {
      const response = await makeRequestWithoutToken(
        "POST",
        `/register`,
        userData
      );
      console.log("User created:", response.data);
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
