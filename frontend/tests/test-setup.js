// test-setup.js - Common test utilities and global setup
import { vi } from "vitest";
import { createStore as vuexCreateStore } from "vuex";

// Global test utilities
export const createMockUser = (overrides = {}) => ({
  id: 1,
  name: "John Doe",
  email: "john.doe@example.com",
  ...overrides,
});

export const createMockTicket = (overrides = {}) => ({
  id: 1,
  subject: "Test Ticket",
  description: "Test Description",
  status: "OPEN",
  createdAt: "2024-01-15T10:30:00Z",
  updatedAt: "2024-01-15T10:30:00Z",
  userId: 1,
  policyId: null,
  claimId: null,
  ...overrides,
});

export const createMockPolicy = (overrides = {}) => ({
  id: 1,
  policyName: "Test Policy",
  policyNumber: "POL001",
  ...overrides,
});

export const createMockClaim = (overrides = {}) => ({
  id: 1,
  claimNumber: "CLAIM001",
  status: "PENDING",
  ...overrides,
});

// Mock localStorage
export const mockLocalStorage = (() => {
  let store = {};
  return {
    getItem: vi.fn((key) => store[key] || null),
    setItem: vi.fn((key, value) => {
      store[key] = value;
    }),
    removeItem: vi.fn((key) => {
      delete store[key];
    }),
    clear: vi.fn(() => {
      store = {};
    }),
    get store() {
      return store;
    },
    set store(newStore) {
      store = newStore;
    },
  };
})();

// Setup localStorage mock globally
Object.defineProperty(window, "localStorage", {
  value: mockLocalStorage,
  writable: true,
});

// Mock Vuex store factory
export const createMockStore = (modules = {}) => {
  const defaultModules = {
    tickets: {
      namespaced: true,
      state: {
        tickets: [],
        loading: false,
        error: null,
      },
      actions: {
        fetchUserTickets: vi.fn().mockResolvedValue([]),
        createTicket: vi.fn().mockResolvedValue({ id: 1, success: true }),
        updateTicket: vi.fn().mockResolvedValue({ id: 1, success: true }),
        fetchTicketById: vi.fn().mockResolvedValue(createMockTicket()),
      },
      getters: {
        getTickets: (state) => state.tickets,
        getLoading: (state) => state.loading,
        getError: (state) => state.error,
      },
    },
    userPolicies: {
      namespaced: true,
      state: {
        policies: [],
        loading: false,
        error: null,
      },
      actions: {
        fetchUserPolicies: vi.fn().mockResolvedValue([]),
      },
      getters: {
        getPolicies: (state) => state.policies,
        getError: (state) => state.error,
      },
    },
    claims: {
      namespaced: true,
      state: {
        claims: [],
        loading: false,
        error: null,
      },
      actions: {
        fetchClaims: vi.fn().mockResolvedValue([]),
      },
      getters: {
        getClaims: (state) => state.claims,
        getError: (state) => state.error,
      },
    },
  };

  // return {
  //   modules: { ...defaultModules, ...modules },
  //   state: {},
  //   _modulesNamespaceMap: {
  //     "tickets/": true,
  //     "userPolicies/": true,
  //     "claims/": true,
  //     "policies/": true,
  //   },
  //   _actions: {
  //     "tickets/fetchUserTickets": true,
  //     "tickets/createTicket": true,
  //     "tickets/updateTicket": true,
  //     "tickets/fetchTicketById": true,
  //     "userPolicies/fetchUserPolicies": true,
  //     "claims/fetchClaims": true,
  //     fetchPolicies: true,
  //     fetchClaims: true,
  //   },
  // };

  return vuexCreateStore({
    modules: {
      ...defaultModules,
      ...modules,
    },
  });
};

// Common component mocks
export const MockComponents = {
  BaseButton: {
    name: "BaseButton",
    template: `
      <button 
        @click="$emit('click')" 
        :type="type" 
        :variant="variant" 
        :size="size"
        :disabled="disabled" 
        :class="[variant, size, customClass]"
        data-testid="base-button"
      >
        <slot />
      </button>
    `,
    props: ["type", "variant", "size", "disabled", "customClass"],
    emits: ["click"],
  },

  HeaderLayout: {
    name: "HeaderLayout",
    template: `
      <header data-testid="header-layout" class="header">
        <slot />
      </header>
    `,
    props: ["user", "isCollapsed", "setIsCollapsed"],
  },

  TicketList: {
    name: "TicketList",
    template: `
      <div data-testid="tickets-list">
        <div v-if="tickets.length === 0" data-testid="empty-state">
          No tickets
        </div>
        <div v-else data-testid="tickets-container">
          <div 
            v-for="ticket in tickets" 
            :key="ticket.id" 
            data-testid="ticket-item"
          >
            {{ ticket.subject }}
          </div>
        </div>
      </div>
    `,
    props: ["tickets", "ticketSelected"],
    emits: [
      "create-first-ticket",
      "update-ticket",
      "resolve-ticket",
      "close-ticket",
      "view-ticket",
    ],
  },

  SupportForm: {
    name: "SupportForm",
    template: `
      <div data-testid="create-ticket-form">
        <form data-testid="ticket-form">
          <input data-testid="subject-input" :value="ticket?.subject || ''" />
          <textarea data-testid="description-input" :value="ticket?.description || ''"></textarea>
        </form>
      </div>
    `,
    props: ["userId", "ticket", "policies", "claims"],
    emits: ["ticket-created", "ticket-updated", "cancel"],
  },

  TicketDetailsModal: {
    name: "TicketDetailsModal",
    template: `
      <div 
        v-if="isOpen" 
        data-testid="ticket-details-modal"
        class="modal"
      >
        <div data-testid="modal-content">
          <h2>{{ ticket?.subject }}</h2>
          <p>{{ ticket?.description }}</p>
        </div>
      </div>
    `,
    props: ["isOpen", "ticket"],
    emits: ["close", "update-ticket", "resolve-ticket", "close-ticket"],
  },
};

// Helper functions mock
export const mockHelperFunctions = {
  getStatusClassIcon: vi.fn((status) => {
    const iconMap = {
      OPEN: "CircleDot",
      RESOLVED: "CheckCircle",
      CLOSED: "XCircle",
    };
    return iconMap[status] || "CircleDot";
  }),

  getStatusClasses: vi.fn((status) => {
    const classMap = {
      OPEN: "bg-blue-100 text-blue-800",
      RESOLVED: "bg-green-100 text-green-800",
      CLOSED: "bg-gray-100 text-gray-800",
    };
    return classMap[status] || "bg-blue-100 text-blue-800";
  }),

  formatDate: vi.fn((date) => {
    if (!date) return "Invalid Date";
    try {
      return new Date(date).toLocaleDateString("en-US", {
        year: "numeric",
        month: "short",
        day: "numeric",
      });
    } catch {
      return "Invalid Date";
    }
  }),

  formatDateTime: vi.fn((date) => {
    if (!date) return "Invalid Date";
    try {
      return new Date(date).toLocaleDateString("en-US", {
        year: "numeric",
        month: "short",
        day: "numeric",
        hour: "2-digit",
        minute: "2-digit",
      });
    } catch {
      return "Invalid Date";
    }
  }),
};

// Test data generators
export const generateTickets = (count = 3) => {
  return Array.from({ length: count }, (_, index) =>
    createMockTicket({
      id: index + 1,
      subject: `Test Ticket ${index + 1}`,
      description: `Description for ticket ${index + 1}`,
      status: ["OPEN", "RESOLVED", "CLOSED"][index % 3],
    })
  );
};

export const generatePolicies = (count = 3) => {
  return Array.from({ length: count }, (_, index) =>
    createMockPolicy({
      id: index + 1,
      policyName: `Policy ${index + 1}`,
      policyNumber: `POL00${index + 1}`,
    })
  );
};

export const generateClaims = (count = 3) => {
  return Array.from({ length: count }, (_, index) =>
    createMockClaim({
      id: index + 1,
      claimNumber: `CLAIM00${index + 1}`,
      status: ["PENDING", "APPROVED", "REJECTED"][index % 3],
    })
  );
};

// Async utilities for testing
export const waitForNextTick = () =>
  new Promise((resolve) => setTimeout(resolve, 0));

export const waitForCondition = async (condition, timeout = 1000) => {
  const startTime = Date.now();
  while (Date.now() - startTime < timeout) {
    if (await condition()) {
      return true;
    }
    await waitForNextTick();
  }
  throw new Error(`Condition not met within ${timeout}ms`);
};

// Error simulation utilities
export const simulateError = (message = "Test error") => {
  return vi.fn().mockRejectedValue(new Error(message));
};
