// tests/integration/SupportTicketsIntegration.test.js
import { describe, it, expect, vi, beforeEach } from "vitest";
import { mount } from "@vue/test-utils";
import { createStore } from "vuex";
import SupportTickets from "@/views/User/TicketsPage.vue";
import {
  MockComponents,
  mockLocalStorage,
  generateTickets,
  generatePolicies,
  generateClaims,
} from "../test-setup.js";

describe("SupportTickets Integration Tests", () => {
  let wrapper;
  let store;
  let mockTickets, mockPolicies, mockClaims;

  beforeEach(() => {
    mockTickets = generateTickets(5);
    mockPolicies = generatePolicies(3);
    mockClaims = generateClaims(4);

    store = createStore({
      modules: {
        tickets: {
          namespaced: true,
          state: { tickets: mockTickets },
          actions: {
            fetchUserTickets: vi.fn().mockResolvedValue(mockTickets),
            createTicket: vi.fn().mockImplementation((context, ticketData) => {
              const newTicket = {
                ...ticketData,
                id: Date.now(),
                createdAt: new Date(),
              };
              mockTickets.unshift(newTicket);
              return Promise.resolve(newTicket);
            }),
            updateTicket: vi
              .fn()
              .mockImplementation((context, { ticketData, ticketId }) => {
                const ticketIndex = mockTickets.findIndex(
                  (t) => t.id === ticketId
                );
                if (ticketIndex !== -1) {
                  mockTickets[ticketIndex] = {
                    ...mockTickets[ticketIndex],
                    ...ticketData,
                  };
                }
                return Promise.resolve(mockTickets[ticketIndex]);
              }),
            fetchTicketById: vi.fn().mockImplementation((context, ticketId) => {
              return Promise.resolve(
                mockTickets.find((t) => t.id === ticketId)
              );
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
          state: { claims: mockClaims },
          actions: {
            fetchClaims: vi.fn().mockResolvedValue(mockClaims),
          },
        },
      },
      state: {
        policies: mockPolicies,
        claims: { claims: mockClaims },
      },
      _modulesNamespaceMap: {
        "policies/": true,
        "tickets/": true,
        "userPolicies/": true,
        "claims/": true,
      },
      _actions: {
        "claims/fetchClaims": true,
        fetchPolicies: true,
        fetchClaims: true,
      },
    });

    mockLocalStorage.store = { currentUser: JSON.stringify({ id: 1 }) };

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

    wrapper = mount(SupportTickets, {
      global: {
        plugins: [store],
        components: MockComponents,
      },
    });
  });

  describe("Complete User Flow", () => {
    it("should handle complete ticket creation flow", async () => {
      // Start in tickets view
      expect(wrapper.vm.activeView).toBe("tickets");

      // Click raise ticket button
      const raiseTicketBtn = wrapper.findAllComponents(
        MockComponents.BaseButton
      )[2];
      await raiseTicketBtn.trigger("click");

      expect(wrapper.vm.activeView).toBe("create");
      expect(wrapper.find('[data-testid="create-ticket-form"]').exists()).toBe(
        true
      );

      // Simulate ticket creation
      const newTicketData = {
        subject: "Integration Test Ticket",
        description: "This is an integration test",
      };

      wrapper.vm.handleTicketCreated(newTicketData);

      // Should return to tickets view
      expect(wrapper.vm.activeView).toBe("tickets");

      // New ticket should be in the list
      expect(wrapper.vm.tickets[0].subject).toBe("Integration Test Ticket");
    });

    it("should handle complete ticket update flow", async () => {
      // Start with existing tickets
      expect(wrapper.vm.tickets).toHaveLength(5);

      const ticketToUpdate = wrapper.vm.tickets[0];

      // Simulate clicking update on a ticket
      wrapper.vm.handleUpdateTicket(ticketToUpdate);

      expect(wrapper.vm.activeView).toBe("create");
      expect(wrapper.vm.selectedTicket).toEqual(ticketToUpdate);

      // Simulate ticket update completion
      await wrapper.vm.handleTicketUpdated();

      expect(wrapper.vm.activeView).toBe("tickets");
    });

    it("should handle ticket status changes", async () => {
      const ticketToResolve = { ...wrapper.vm.tickets[0] };

      await wrapper.vm.handleResolveTicket(ticketToResolve);

      expect(wrapper.vm.tickets[0].status).toBe("RESOLVED");
      expect(wrapper.vm.tickets[0].updatedAt).toBeInstanceOf(Date);
    });

    it("should navigate between views correctly", async () => {
      // Start in tickets view
      expect(wrapper.vm.activeView).toBe("tickets");

      // Go to create view
      await wrapper.vm.setActiveView("create");
      expect(wrapper.vm.activeView).toBe("create");

      // Back arrow should appear
      await wrapper.vm.$nextTick();
      const arrowBack = wrapper.find('[data-testid="arrow-left"]');
      expect(arrowBack.exists()).toBe(true);

      // Click back arrow
      await arrowBack.trigger("click");
      expect(wrapper.vm.activeView).toBe("tickets");
    });
  });

  describe("Data Loading Integration", () => {
    it("should load all required data on mount", async () => {
      expect(
        store._modules.root._children.tickets._rawModule.actions
          .fetchUserTickets
      ).toHaveBeenCalledWith(expect.any(Object), 1);
      expect(
        store._modules.root._children.userPolicies._rawModule.actions
          .fetchUserPolicies
      ).toHaveBeenCalledWith(expect.any(Object), 1);
    });

    it("should handle data loading errors gracefully", async () => {
      const consoleSpy = vi
        .spyOn(console, "error")
        .mockImplementation(() => {});

      // Create store with failing actions
      const failingStore = createStore({
        modules: {
          tickets: {
            namespaced: true,
            actions: {
              fetchUserTickets: vi
                .fn()
                .mockRejectedValue(new Error("Network error")),
            },
          },
        },
      });

      const errorWrapper = mount(SupportTickets, {
        global: {
          plugins: [failingStore],
          components: MockComponents,
        },
      });

      await errorWrapper.vm.fetchUserTickets();

      expect(consoleSpy).toHaveBeenCalledWith(
        "Error fetching tickets:",
        expect.any(Error)
      );

      consoleSpy.mockRestore();
    });
  });

  describe("State Management Integration", () => {
    it("should maintain consistent state across operations", async () => {
      const initialTicketCount = wrapper.vm.tickets.length;

      // Create new ticket
      const newTicket = {
        subject: "State Test Ticket",
        description: "Testing state consistency",
      };

      wrapper.vm.handleTicketCreated(newTicket);

      expect(wrapper.vm.tickets).toHaveLength(initialTicketCount + 1);
      expect(wrapper.vm.tickets[0].subject).toBe("State Test Ticket");

      // Update the ticket
      const updatedTicket = {
        ...wrapper.vm.tickets[0],
        subject: "Updated Title",
      };
      wrapper.vm.handleUpdateTicket(updatedTicket);

      expect(wrapper.vm.selectedTicket.subject).toBe("Updated Title");
    });

    it("should handle empty data states", async () => {
      // Test with empty tickets
      wrapper.vm.tickets = [];
      await wrapper.vm.$nextTick();

      const ticketsList = wrapper.findComponent(MockComponents.TicketList);
      expect(ticketsList.props("tickets")).toEqual([]);

      // Create first ticket
      wrapper.vm.handleTicketCreated({
        subject: "First Ticket",
        description: "First ticket description",
      });

      expect(wrapper.vm.tickets).toHaveLength(1);
    });
  });

  describe("User Interaction Flow", () => {
    it("should handle rapid navigation changes", async () => {
      // Rapid view switching
      wrapper.vm.setActiveView("create");
      wrapper.vm.setActiveView("tickets");
      wrapper.vm.setActiveView("create");

      await wrapper.vm.$nextTick();
      expect(wrapper.vm.activeView).toBe("create");

      // Should still render correct component
      expect(wrapper.find('[data-testid="create-ticket-form"]').exists()).toBe(
        true
      );
    });

    it("should preserve form state during navigation", async () => {
      wrapper.vm.setActiveView("create");

      // Simulate selecting a ticket for editing
      wrapper.vm.selectedTicket = mockTickets[0];

      // Navigate away and back
      wrapper.vm.setActiveView("tickets");
      wrapper.vm.setActiveView("create");

      // Selected ticket should still be there
      expect(wrapper.vm.selectedTicket).toEqual(mockTickets[0]);
    });
  });

  describe("Error Recovery", () => {
    it("should recover from network errors", async () => {
      const consoleSpy = vi
        .spyOn(console, "error")
        .mockImplementation(() => {});

      // Simulate network error during ticket creation
      const failingCreateAction = vi
        .fn()
        .mockRejectedValue(new Error("Network error"));
      store._modules.root._children.tickets._rawModule.actions.createTicket =
        failingCreateAction;

      try {
        await wrapper.vm.handleTicketCreated({
          subject: "Test Ticket",
          description: "Test Description",
        });
      } catch (error) {
        // Should handle error gracefully
        expect(error.message).toBe("Network error");
      }

      consoleSpy.mockRestore();
    });

    it("should handle malformed data gracefully", async () => {
      // Test with malformed ticket data
      const malformedTicket = {
        // Missing required fields
        description: "Only description",
      };

      // Should not crash when handling malformed data
      expect(() => {
        wrapper.vm.handleTicketCreated(malformedTicket);
      }).not.toThrow();
    });
  });

  describe("Performance Considerations", () => {
    it("should handle large datasets efficiently", async () => {
      // Generate large dataset
      const largeTicketSet = generateTickets(100);
      wrapper.vm.tickets = largeTicketSet;

      await wrapper.vm.$nextTick();

      // Component should still be responsive
      expect(wrapper.exists()).toBe(true);
      expect(wrapper.vm.tickets).toHaveLength(100);

      // Operations should complete without hanging
      const resolvePromises = largeTicketSet
        .slice(0, 10)
        .map((ticket) => wrapper.vm.handleResolveTicket(ticket));

      await Promise.all(resolvePromises);

      // Should complete successfully
      expect(
        wrapper.vm.tickets.slice(0, 10).every((t) => t.status === "RESOLVED")
      ).toBe(true);
    });
  });

  describe("Component Communication", () => {
    it("should properly communicate between parent and child components", async () => {
      const ticketsList = wrapper.findComponent(MockComponents.TicketList);
      const createForm = wrapper.findComponent(MockComponents.SupportForm);

      // Check props are passed correctly
      expect(ticketsList.props("tickets")).toEqual(wrapper.vm.tickets);

      wrapper.vm.setActiveView("create");
      await wrapper.vm.$nextTick();

      const updatedCreateForm = wrapper.findComponent(
        MockComponents.SupportForm
      );
      expect(updatedCreateForm.props("userId")).toBe(1);
      expect(updatedCreateForm.props("policies")).toEqual(wrapper.vm.policies);
      expect(updatedCreateForm.props("claims")).toEqual(wrapper.vm.claims);
    });

    it("should handle component events correctly", async () => {
      wrapper.vm.setActiveView("tickets");

      const ticketsList = wrapper.findComponent(MockComponents.TicketList);

      // Simulate events from child components
      await ticketsList.vm.$emit("update-ticket", mockTickets[0]);
      await ticketsList.vm.$emit("resolve-ticket", mockTickets[1]);
      await ticketsList.vm.$emit("close-ticket", mockTickets[2]);

      // Parent should respond appropriately
      expect(wrapper.vm.selectedTicket).toEqual(mockTickets[0]);
      expect(wrapper.vm.activeView).toBe("create");
    });
  });
});
