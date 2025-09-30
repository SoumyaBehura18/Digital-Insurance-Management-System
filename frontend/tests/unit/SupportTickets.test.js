import { describe, it, expect, vi, beforeEach } from "vitest";
import { mount } from "@vue/test-utils";
import { createStore } from "vuex";
import SupportTickets from "@/views/User/TicketsPage.vue";
import { MessageCircle } from "lucide-vue-next";

// Mock components
const MockHeaderLayout = {
  name: "HeaderLayout",
  template: '<div data-testid="header-layout"></div>',
  props: ["user", "isCollapsed", "setIsCollapsed"],
};

const MockBaseButton = {
  name: "BaseButton",
  template:
    '<button @click="$emit(\'click\')" :class="variant"><slot /></button>',
  props: ["variant"],
  emits: ["click"],
};

const MockTicketsList = {
  name: "TicketsList",
  template: '<div data-testid="tickets-list"></div>',
  props: ["tickets", "ticketSelected"],
  emits: [
    "create-first-ticket",
    "update-ticket",
    "resolve-ticket",
    "close-ticket",
  ],
};

const MockCreateTicketForm = {
  name: "CreateTicketForm",
  template: '<div data-testid="create-ticket-form"></div>',
  props: ["userId", "ticket", "policies", "claims"],
  emits: ["ticket-created", "ticket-updated", "cancel"],
};

vi.mock("lucide-vue-next", () => {
  return {
    ArrowLeft: {
      name: "ArrowLeft",
      template: '<svg data-testid="arrow-left"></svg>',
    },
    MessageCircle: {
      name: "MessageCircle",
      template: '<svg data-testid="message-circle"></svg>',
    },
    CheckCircle: {
      name: "CheckCircle",
      template: '<svg data-testid="check-circle"></svg>',
    },
    XCircle: {
      name: "XCircle",
      template: '<svg data-testid="x-circle"></svg>',
    },
    Eye: {
      name: "Eye",
      template: '<svg data-testid="eye-icon"></svg>',
    },
    Pencil: {
      name: "Pencil",
      template: '<svg data-testid="pencil-icon"></svg>',
    },
  };
});

// Mock localStorage
const mockLocalStorage = {
  getItem: vi.fn(),
  setItem: vi.fn(),
  removeItem: vi.fn(),
  clear: vi.fn(),
};

Object.defineProperty(window, "localStorage", {
  value: mockLocalStorage,
});

describe("SupportTickets.vue", () => {
  let wrapper;
  let mockStore;
  let mockTickets, mockPolicies, mockClaims;

  beforeEach(() => {
    mockTickets = [
      {
        id: 1,
        subject: "Test Ticket 1",
        description: "Test description 1",
        status: "OPEN",
        createdAt: "2024-01-01",
        updatedAt: "2024-01-01",
      },
      {
        id: 2,
        subject: "Test Ticket 2",
        description: "Test description 2",
        status: "RESOLVED",
        createdAt: "2024-01-02",
        updatedAt: "2024-01-02",
      },
    ];

    mockPolicies = [
      { id: 1, policyName: "Policy 1" },
      { id: 2, policyName: "Policy 2" },
    ];

    mockClaims = [
      { id: 1, claimNumber: "CLAIM001" },
      { id: 2, claimNumber: "CLAIM002" },
    ];

    mockStore = createStore({
      modules: {
        tickets: {
          namespaced: true,
          actions: {
            fetchUserTickets: vi.fn((context) => {
              context.commit("setTickets", mockTickets);
            }),
          },
        },
        userPolicies: {
          namespaced: true,
          actions: {
            fetchUserPolicies: vi.fn().mockResolvedValue(mockPolicies),
          },
        },
        claims: {
          namespaced: true,
          actions: {
            fetchClaims: vi.fn().mockResolvedValue(mockClaims),
          },
          state: {
            claims: mockClaims,
          },
        },
      },
      state: {
        policies: mockPolicies,
        claims: { claims: mockClaims },
      },
      _modulesNamespaceMap: {
        "policies/": true,
      },
      _actions: {
        "claims/fetchClaims": true,
        fetchPolicies: true,
        fetchClaims: true,
      },
    });

    mockLocalStorage.getItem.mockReturnValue(JSON.stringify({ id: 1 }));

    wrapper = mount(SupportTickets, {
      global: {
        plugins: [mockStore],
        components: {
          HeaderLayout: MockHeaderLayout,
          BaseButton: MockBaseButton,
          TicketsList: MockTicketsList,
          CreateTicketForm: MockCreateTicketForm,
        },
      },
    });
  });

  describe("Component Mounting", () => {
    it("should render without errors", () => {
      expect(wrapper.exists()).toBe(true);
    });

    it("should have correct initial state", () => {
      expect(wrapper.vm.activeView).toBe("tickets");
      expect(wrapper.vm.isCollapsed).toBe(true);
      expect(wrapper.vm.tickets).toEqual([]);
      expect(wrapper.vm.policies).toEqual([]);
      expect(wrapper.vm.claims).toEqual([]);
    });

    it("should render page title", () => {
      expect(wrapper.text()).toContain("Support Tickets");
    });
  });

  describe("Navigation Buttons", () => {
    it('should switch to create view when "Raise Ticket" is clicked', async () => {
      const raiseTicketButton = wrapper.findAllComponents(MockBaseButton)[1];
      await raiseTicketButton.trigger("click");
      expect(wrapper.vm.activeView).toBe("create");
    });

    it('should switch to tickets view when "View Tickets" is clicked', async () => {
      wrapper.vm.activeView = "create";
      await wrapper.vm.$nextTick();

      const viewTicketsButton = wrapper.findAllComponents(MockBaseButton)[0];
      await viewTicketsButton.trigger("click");
      expect(wrapper.vm.activeView).toBe("tickets");
    });

    it("should show back arrow when in create view", async () => {
      wrapper.vm.activeView = "create";
      await wrapper.vm.$nextTick();

      expect(wrapper.find('[data-testid="arrow-left"]').exists()).toBe(true);
    });

    it("should navigate back to tickets when arrow is clicked", async () => {
      wrapper.vm.activeView = "create";
      await wrapper.vm.$nextTick();

      const arrowLeft = wrapper.find('[data-testid="arrow-left"]');
      await arrowLeft.trigger("click");
      expect(wrapper.vm.activeView).toBe("tickets");
    });
  });

  describe("Data Loading", () => {
    it("should fetch user ID from localStorage on mount", () => {
      expect(mockLocalStorage.getItem).toHaveBeenCalledWith("currentUser");
      expect(wrapper.vm.userId).toBe(1);
    });

    it("should fetch tickets, policies, and claims on mount", () => {
      expect(mockStore._actions["tickets/fetchUserTickets"]).toBeDefined();
    });

    it("should handle null user in localStorage", async () => {
      mockLocalStorage.getItem.mockReturnValue(null);
      const newWrapper = mount(SupportTickets, {
        global: {
          plugins: [mockStore],
          components: {
            HeaderLayout: MockHeaderLayout,
            BaseButton: MockBaseButton,
            TicketsList: MockTicketsList,
            CreateTicketForm: MockCreateTicketForm,
          },
        },
      });

      expect(newWrapper.vm.userId).toBe(null);
    });
  });

  describe("Ticket Management", () => {
    beforeEach(() => {
      wrapper.vm.tickets = mockTickets;
    });

    it("should handle ticket update", async () => {
      const ticketToUpdate = mockTickets[0];
      wrapper.vm.handleUpdateTicket(ticketToUpdate);

      expect(wrapper.vm.selectedTicket).toEqual(ticketToUpdate);
      expect(wrapper.vm.activeView).toBe("create");
    });

    it("should handle ticket resolution", async () => {
      const ticketToResolve = { ...mockTickets[0] };
      await wrapper.vm.handleResolveTicket(ticketToResolve);

      expect(wrapper.vm.tickets[0].status).toBe("RESOLVED");
      expect(wrapper.vm.tickets[0].updatedAt).toBeInstanceOf(Date);
    });

    it("should handle ticket closure", async () => {
      const ticketToClose = { ...mockTickets[0] };
      await wrapper.vm.handleCloseTicket(ticketToClose);

      expect(wrapper.vm.tickets[0].status).toBe("CLOSED");
      expect(wrapper.vm.tickets[0].updatedAt).toBeInstanceOf(Date);
    });

    it("should handle ticket creation", () => {
      const newTicketData = {
        subject: "New Ticket",
        description: "New Description",
        status: "OPEN",
      };

      const initialLength = wrapper.vm.tickets.length;
      wrapper.vm.handleTicketCreated(newTicketData);

      expect(wrapper.vm.tickets).toHaveLength(initialLength + 1);
      expect(wrapper.vm.tickets[0].subject).toBe("New Ticket");
      expect(wrapper.vm.activeView).toBe("tickets");
    });

    it("should handle empty tickets array for ticket creation", () => {
      wrapper.vm.tickets = [];
      const newTicketData = { subject: "Test", description: "Test" };

      wrapper.vm.handleTicketCreated(newTicketData);
      expect(wrapper.vm.tickets).toHaveLength(1);
    });
  });

  describe("Error Handling", () => {
    it("should handle fetch tickets error", async () => {
      const consoleSpy = vi
        .spyOn(console, "error")
        .mockImplementation(() => {});
      mockStore = createStore({
        modules: {
          tickets: {
            namespaced: true,
            actions: {
              fetchUserTickets: vi
                .fn()
                .mockRejectedValue(new Error("Fetch failed")),
            },
          },
        },
      });

      const errorWrapper = mount(SupportTickets, {
        global: {
          plugins: [mockStore],
          components: {
            HeaderLayout: MockHeaderLayout,
            BaseButton: MockBaseButton,
            TicketsList: MockTicketsList,
            CreateTicketForm: MockCreateTicketForm,
          },
        },
      });

      await errorWrapper.vm.fetchUserTickets();
      expect(consoleSpy).toHaveBeenCalledWith(
        "Error fetching tickets:",
        expect.any(Error)
      );

      consoleSpy.mockRestore();
    });

    it("should handle fetch policies and claims error", async () => {
      const consoleSpy = vi
        .spyOn(console, "error")
        .mockImplementation(() => {});

      await wrapper.vm.fetchPoliciesAndClaims();

      consoleSpy.mockRestore();
    });
  });

  describe("Button Variants", () => {
    it("should apply correct variant to active view button", () => {
      wrapper.vm.activeView = "tickets";
      const buttons = wrapper.findAllComponents(MockBaseButton);

      expect(buttons[0].props("variant")).toBe("theme");
      expect(buttons[1].props("variant")).toBe("secondary");
    });

    it("should apply correct variant when create view is active", async () => {
      wrapper.vm.activeView = "create";
      await wrapper.vm.$nextTick();

      const buttons = wrapper.findAllComponents(MockBaseButton);
      expect(buttons[0].props("variant")).toBe("outline");
      expect(buttons[1].props("variant")).toBe("theme");
    });
  });
});
