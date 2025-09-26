import { makeRequestWithToken, makeRequestWithoutToken } from '@/utils/requests';
const state = {
  currentUser: null,       // logged-in user
  users: [],               // all users (if admin view)
  loading: false,
  error: null
};

const getters = {
  getCurrentUser: (state) => state.currentUser,
  getUsers: (state) => state.users,
  isLoading: (state) => state.loading,
  getError: (state) => state.error
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
  }
};

const actions = {
  async fetchUserById({ commit }, userId) {
    commit('SET_LOADING', true);
    try {
      const response = await makeRequestWithToken("GET", `/users/${userId}`);
      commit('SET_CURRENT_USER', response.data);
      localStorage.setItem('currentUser', JSON.stringify(response.data));
      commit('SET_ERROR', null);
    } catch (error) {
      commit('SET_ERROR', error.response?.data || 'Failed to fetch user');
      commit('SET_CURRENT_USER', null);
    } finally {
      commit('SET_LOADING', false);
    }
  },

  async fetchAllUsers({ commit }) {
    commit('SET_LOADING', true);
    try {
      const response = await makeRequestWithToken("GET", `/users`);
      commit('SET_USERS', response.data);
      commit('SET_ERROR', null);
    } catch (error) {
      commit('SET_ERROR', error.response?.data || 'Failed to fetch users');
      commit('SET_USERS', []);
    } finally {
      commit('SET_LOADING', false);
    }
  },

  async createUser({ commit }, userData) {
    commit('SET_LOADING', true);
    try {
      const response = await makeRequestWithToken("POST", `/users`, userData);
      commit('SET_CURRENT_USER', response.data);
      localStorage.setItem('currentUser', JSON.stringify(response.data));
      commit('SET_ERROR', null);
    } catch (error) {
      commit('SET_ERROR', error.response?.data || 'Failed to create user');
    } finally {
      commit('SET_LOADING', false);
    }
  },

  async loginUser({ commit }, credentials) {
    commit('SET_LOADING', true);
    try {
      const response = await makeRequestWithoutToken("POST", `/login`, credentials);
      commit('SET_CURRENT_USER', response.data);
      localStorage.setItem('currentUser', JSON.stringify(response.data)); // store full user object
      localStorage.setItem('authToken', response.data.token); // store token separately for auth requests
      commit('SET_ERROR', null);
    } catch (error) {
      commit('SET_ERROR', error.response?.data || 'Invalid credentials');
      commit('SET_CURRENT_USER', null);
    } finally {
      commit('SET_LOADING', false);
    }
  },

  logoutUser({ commit }) {
    commit('SET_CURRENT_USER', null);
    commit('SET_USERS', []);
    localStorage.removeItem('currentUser');
    localStorage.removeItem('authToken');
  }
};


export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions
};
