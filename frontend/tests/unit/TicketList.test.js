import { describe, it, expect, vi, beforeEach } from "vitest";
import { mount } from "@vue/test-utils";
import TicketsList from "@/components/TicketComponents/TicketList.vue";
import { formatDate } from "@/utils/helperFunctions";

// Mock components
const MockBaseButton = {
  name: "BaseButton",
  template:
    '<button @click="$emit(\'click\')" :class="variant" :size="size"><slot /></button>',
  props: ["variant", "size"],
  emits: ["click"],
};

const MockTicketDetailsModal = {
  name: "TicketDetailsModal",
  template: '<div v-if="isOpen" data-testid="ticket-details-modal"></div>',
  props: ["isOpen", "ticket"],
  emits: ["close", "update-ticket", "resolve-ticket", "close-ticket"],
};

const MockPencil = {
  name: "Pencil",
  template: '<svg data-testid="pencil-icon"></svg>',
};

const MockEye = {
  name: "Eye",
  template: '<svg data-testid="eye-icon" v-bind="$attrs"></svg>',
};

vi.mock("lucide-vue-next", () => {
  return {
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

// Mock helper functions
vi.mock("@/utils/helperFunctions", () => ({
  getStatusClassIcon: vi.fn((status) => {
    const icons = {
      OPEN: "CircleDot",
      RESOLVED: "CheckCircle",
      CLOSED: "XCircle",
    };
    return icons[status] || "CircleDot";
  }),
  getStatusClasses: vi.fn((status) => {
    const classes = {
      OPEN: "bg-blue-100 text-blue-800",
      RESOLVED: "bg-green-100 text-green-800",
      CLOSED: "bg-gray-100 text-gray-800",
    };
    return classes[status] || "bg-blue-100 text-blue-800";
  }),
  formatDate: vi.fn((date) => {
    if (!date) return "Invalid Date";
    return new Date(date).toLocaleDateString();
  }),
  formatDateTime: vi.fn((date) => {
    if (!date) return "Invalid Date";
    const d = new Date(date);
    return `${d.toLocaleDateString()} ${d.toLocaleTimeString([], {
      hour: "2-digit",
      minute: "2-digit",
    })}`;
  }),
}));

describe("TicketsList.vue", () => {
  let wrapper;
  let mockTickets;

  beforeEach(() => {
    mockTickets = [
      {
        id: 1,
        subject: "Login Issue",
        description: "Cannot log into my account after password reset",
        status: "OPEN",
        createdAt: "2024-01-15T10:30:00Z",
        updatedAt: "2024-01-15T10:30:00Z",
      },
      {
        id: 2,
        subject: "Payment Problem",
        description: "Payment failed during checkout process with credit card",
        status: "RESOLVED",
        createdAt: "2024-01-14T14:20:00Z",
        updatedAt: "2024-01-16T09:15:00Z",
      },
      {
        id: 3,
        subject: "Feature Request",
        description:
          "Would like to see dark mode option in the application settings",
        status: "CLOSED",
        createdAt: "2024-01-10T16:45:00Z",
        updatedAt: "2024-01-12T11:30:00Z",
      },
    ];

    wrapper = mount(TicketsList, {
      props: {
        tickets: mockTickets,
        ticketSelected: null,
      },
      global: {
        components: {
          BaseButton: MockBaseButton,
          TicketDetailsModal: MockTicketDetailsModal,
          Pencil: MockPencil,
        },
      },
    });
  });

  describe("Component Rendering", () => {
    it("should render without errors", () => {
      expect(wrapper.exists()).toBe(true);
    });

    it("should render the correct header", () => {
      expect(wrapper.text()).toContain("Your Support Tickets");
    });

    it("should render tickets when tickets array is not empty", () => {
      const ticketCards = wrapper.findAll(".border.border-gray-200.rounded-lg");
      expect(ticketCards).toHaveLength(mockTickets.length);
    });

    it("should display ticket information correctly", () => {
      const firstTicket = mockTickets[0];
      expect(wrapper.text()).toContain(firstTicket.subject);
      expect(wrapper.text()).toContain(firstTicket.description);
      expect(wrapper.text()).toContain(firstTicket.status);
    });
  });

  describe("Empty State", () => {
    beforeEach(() => {
      wrapper = mount(TicketsList, {
        props: {
          tickets: [],
          ticketSelected: null,
        },
        global: {
          components: {
            BaseButton: MockBaseButton,
            TicketDetailsModal: MockTicketDetailsModal,
          },
        },
      });
    });

    it("should show empty state when no tickets exist", () => {
      expect(wrapper.text()).toContain("No support tickets yet");
      expect(wrapper.text()).toContain("Create Your First Ticket");
    });

    it("should render empty state SVG icon", () => {
      const svgIcon = wrapper.find("svg");
      expect(svgIcon.exists()).toBe(true);
    });

    it("should emit create-first-ticket when button is clicked", async () => {
      const createButton = wrapper.findComponent(MockBaseButton);
      await createButton.trigger("click");

      expect(wrapper.emitted("create-first-ticket")).toBeTruthy();
      expect(wrapper.emitted("create-first-ticket")).toHaveLength(1);
    });
  });

  describe("Ticket Actions", () => {
    it("should render View and Edit buttons for each ticket", () => {
      const buttons = wrapper.findAllComponents(MockBaseButton);
      // 2 buttons per ticket (View + Edit) * 3 tickets = 6 buttons
      expect(buttons).toHaveLength(6);
    });

    it("should have correct button text and icons", () => {
      const buttons = wrapper.findAllComponents(MockBaseButton);

      // Check first ticket's buttons
      expect(buttons[0].text()).toContain("View");
      expect(buttons[0].find('[data-testid="eye-icon"]').exists()).toBe(true);

      expect(buttons[1].text()).toContain("Edit");
      expect(buttons[1].find('[data-testid="pencil-icon"]').exists()).toBe(
        true
      );
    });

    it("should apply correct variants to action buttons", () => {
      const buttons = wrapper.findAllComponents(MockBaseButton);

      // View buttons should have 'theme' variant
      expect(buttons[0].props("variant")).toBe("theme");
      expect(buttons[2].props("variant")).toBe("theme");
      expect(buttons[4].props("variant")).toBe("theme");

      // Edit buttons should have 'outline' variant
      expect(buttons[1].props("variant")).toBe("outline");
      expect(buttons[3].props("variant")).toBe("outline");
      expect(buttons[5].props("variant")).toBe("outline");
    });

    it("should emit update-ticket when edit button is clicked", async () => {
      const editButton = wrapper.findAllComponents(MockBaseButton)[1]; // First edit button
      await editButton.trigger("click");

      expect(wrapper.emitted("update-ticket")).toBeTruthy();
      expect(wrapper.emitted("update-ticket")[0][0]).toEqual(mockTickets[0]);
    });
  });

  describe("Ticket Details Modal", () => {
    it("should not show modal initially", () => {
      expect(wrapper.vm.isModalOpen).toBe(false);
      expect(wrapper.vm.selectedTicket).toBe(null);
    });

    it("should open modal when view button is clicked", async () => {
      const viewButton = wrapper.findAllComponents(MockBaseButton)[0]; // First view button
      await viewButton.trigger("click");

      expect(wrapper.vm.isModalOpen).toBe(true);
      expect(wrapper.vm.selectedTicket).toEqual(mockTickets[0]);
    });

    it("should emit view-ticket when view button is clicked", async () => {
      const viewButton = wrapper.findAllComponents(MockBaseButton)[0];
      await viewButton.trigger("click");

      expect(wrapper.emitted("view-ticket")).toBeTruthy();
      expect(wrapper.emitted("view-ticket")[0][0]).toBe(mockTickets[0].id);
    });

    it("should close modal when handleCloseModal is called", async () => {
      // First open the modal
      await wrapper.vm.handleViewTicket(mockTickets[0]);
      expect(wrapper.vm.isModalOpen).toBe(true);

      // Then close it
      await wrapper.vm.handleCloseModal();
      expect(wrapper.vm.isModalOpen).toBe(false);
      expect(wrapper.vm.selectedTicket).toBe(null);
    });

    it("should pass correct props to TicketDetailsModal", async () => {
      await wrapper.vm.handleViewTicket(mockTickets[0]);
      await wrapper.vm.$nextTick();

      const modal = wrapper.findComponent(MockTicketDetailsModal);
      expect(modal.props("isOpen")).toBe(true);
      expect(modal.props("ticket")).toEqual(mockTickets[0]);
    });
  });

  describe("Status Display", () => {
    it("should display ticket status with correct styling", () => {
      const statusElements = wrapper.findAll(".inline-flex.items-center");
      expect(statusElements.length).toBeGreaterThan(0);
    });

    it("should handle tickets without status (default to OPEN)", async () => {
      const ticketWithoutStatus = { ...mockTickets[0] };
      delete ticketWithoutStatus.status;

      const wrapperWithoutStatus = mount(TicketsList, {
        props: {
          tickets: [ticketWithoutStatus],
          ticketSelected: null,
        },
        global: {
          components: {
            BaseButton: MockBaseButton,
            TicketDetailsModal: MockTicketDetailsModal,
            Pencil: MockPencil,
          },
        },
      });

      expect(wrapperWithoutStatus.text()).toContain("OPEN");
    });
  });

  describe("Date Formatting", () => {
    it("should format dates correctly", () => {
      expect(formatDate).toHaveBeenCalled();
    });
  });

  describe("Event Handling", () => {
    it("should handle modal events correctly", async () => {
      await wrapper.vm.handleViewTicket(mockTickets[1]);

      // Test update ticket from modal
      await wrapper.vm.handleUpdateTicket(mockTickets[1]);
      expect(wrapper.emitted("update-ticket")).toBeTruthy();
      expect(wrapper.emitted("update-ticket")[0][0]).toEqual(mockTickets[1]);

      // Test resolve ticket from modal
      await wrapper.vm.handleResolveTicket(mockTickets[1]);
      expect(wrapper.emitted("resolve-ticket")).toBeTruthy();
      expect(wrapper.emitted("resolve-ticket")[0][0]).toEqual(mockTickets[1]);

      // Test close ticket from modal
      await wrapper.vm.handleCloseTicket(mockTickets[1]);
      expect(wrapper.emitted("close-ticket")).toBeTruthy();
      expect(wrapper.emitted("close-ticket")[0][0]).toEqual(mockTickets[1]);
    });
  });

  describe("Props Validation", () => {
    it("should handle undefined tickets prop", () => {
      const wrapperWithUndefined = mount(TicketsList, {
        props: {
          ticketSelected: null,
        },
        global: {
          components: {
            BaseButton: MockBaseButton,
            TicketDetailsModal: MockTicketDetailsModal,
          },
        },
      });

      expect(wrapperWithUndefined.exists()).toBe(true);
      expect(wrapperWithUndefined.text()).toContain("No support tickets yet");
    });

    it("should handle ticketSelected prop", async () => {
      const wrapperWithSelected = mount(TicketsList, {
        props: {
          tickets: mockTickets,
          ticketSelected: mockTickets[1],
        },
        global: {
          components: {
            BaseButton: MockBaseButton,
            TicketDetailsModal: MockTicketDetailsModal,
            Pencil: MockPencil,
          },
        },
      });

      expect(wrapperWithSelected.props("ticketSelected")).toEqual(
        mockTickets[1]
      );
    });
  });

  describe("UI/UX Elements", () => {
    it("should truncate long descriptions", () => {
      const descriptionElements = wrapper.findAll(
        ".text-sm.text-gray-500.mt-1.w-64.truncate"
      );
      expect(descriptionElements.length).toBeGreaterThan(0);
    });

    it("should have hover effects on ticket cards", () => {
      const ticketCards = wrapper.findAll(".hover\\:shadow-sm");
      expect(ticketCards.length).toBe(mockTickets.length);
    });

    it("should have proper spacing and layout classes", () => {
      expect(wrapper.find(".space-y-4").exists()).toBe(true);
      expect(wrapper.find(".bg-gray-50.rounded-lg.shadow").exists()).toBe(true);
    });
  });

  describe("Accessibility", () => {
    it("should have proper button roles and accessibility", () => {
      const buttons = wrapper.findAllComponents(MockBaseButton);
      buttons.forEach((button) => {
        expect(button.element.tagName).toBe("BUTTON");
      });
    });

    it("should have proper semantic structure", () => {
      expect(wrapper.find("main").exists()).toBe(false); // Not in this component
      expect(wrapper.find("h3").exists()).toBe(true); // Header exists
    });
  });

  describe("Component State", () => {
    it("should maintain internal state correctly", async () => {
      expect(wrapper.vm.isModalOpen).toBe(false);
      expect(wrapper.vm.selectedTicket).toBe(null);

      await wrapper.vm.handleViewTicket(mockTickets[2]);

      expect(wrapper.vm.isModalOpen).toBe(true);
      expect(wrapper.vm.selectedTicket).toEqual(mockTickets[2]);
    });

    it("should reset state when modal is closed", async () => {
      await wrapper.vm.handleViewTicket(mockTickets[0]);
      expect(wrapper.vm.selectedTicket).toEqual(mockTickets[0]);

      await wrapper.vm.handleCloseModal();
      expect(wrapper.vm.selectedTicket).toBe(null);
    });
  });
});
