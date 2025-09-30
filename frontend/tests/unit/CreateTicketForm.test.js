import { describe, it, expect, vi, beforeEach } from "vitest";
import { mount } from "@vue/test-utils";
import { createStore } from "vuex";
import CreateTicketForm from "@/components/TicketComponents/SupportForm.vue";

// Mock components
const MockBaseButton = {
  name: "BaseButton",
  template:
    '<button @click="$emit(\'click\')" :type="type" :variant="variant" :disabled="disabled" :class="customClass"><slot /></button>',
  props: ["type", "variant", "disabled", "customClass"],
  emits: ["click"],
};

describe("CreateTicketForm.vue", () => {
  let wrapper;
  let mockStore;
  let mockPolicies, mockClaims;

  beforeEach(() => {
    mockPolicies = [
      { id: 1, policyName: "Health Insurance Policy" },
      { id: 2, policyName: "Auto Insurance Policy" },
      { id: 3, policyName: "Life Insurance Policy" },
    ];

    mockClaims = [
      { id: 1, claimNumber: "CLAIM001" },
      { id: 2, claimNumber: "CLAIM002" },
      { id: 3, claimNumber: "CLAIM003" },
    ];

    mockStore = createStore({
      modules: {
        tickets: {
          namespaced: true,
          actions: {
            createTicket: vi.fn().mockResolvedValue({ id: 1, success: true }),
            updateTicket: vi.fn().mockResolvedValue({ id: 1, success: true }),
            fetchTicketById: vi.fn().mockResolvedValue({
              id: 1,
              subject: "Existing Ticket",
              description: "Existing Description",
              policyId: 1,
              claimId: 2,
            }),
          },
        },
        userPolicies: {
          namespaced: true,
          getters: {
            getPolicies: () => mockPolicies,
            getError: () => null,
          },
        },
      },
    });

    wrapper = mount(CreateTicketForm, {
      props: {
        userId: 1,
        ticket: null,
        policies: mockPolicies,
        claims: mockClaims,
      },
      global: {
        plugins: [mockStore],
        components: {
          BaseButton: MockBaseButton,
        },
      },
    });
  });

  describe("Component Rendering", () => {
    it("should render without errors", () => {
      expect(wrapper.exists()).toBe(true);
    });

    it("should render form header correctly", () => {
      expect(wrapper.text()).toContain("Create Support Ticket");
      expect(wrapper.text()).toContain(
        "Describe your issue and we'll help you resolve it"
      );
    });

    it("should render all form fields", () => {
      expect(wrapper.find("#subject").exists()).toBe(true);
      expect(wrapper.find("#description").exists()).toBe(true);
      expect(wrapper.find("#policy").exists()).toBe(true);
      expect(wrapper.find("#claim").exists()).toBe(true);
    });

    it("should render action buttons", () => {
      const buttons = wrapper.findAllComponents(MockBaseButton);
      expect(buttons).toHaveLength(2);
      expect(buttons[0].text()).toBe("Cancel");
      expect(buttons[1].text()).toBe("Submit Ticket");
    });
  });

  describe("Form Fields", () => {
    it("should have correct labels", () => {
      expect(wrapper.text()).toContain("Title");
      expect(wrapper.text()).toContain("Description");
      expect(wrapper.text()).toContain("Related Policy (Optional)");
      expect(wrapper.text()).toContain("Related Claim (Optional)");
    });

    it("should have correct placeholders", () => {
      const subjectInput = wrapper.find("#subject");
      const descriptionTextarea = wrapper.find("#description");

      expect(subjectInput.attributes("placeholder")).toBe(
        "Brief description of your issue"
      );
      expect(descriptionTextarea.attributes("placeholder")).toBe(
        "Provide detailed information about your issue"
      );
    });

    it("should populate policy options", () => {
      const policySelect = wrapper.find("#policy");
      const options = policySelect.findAll("option");

      expect(options).toHaveLength(mockPolicies.length + 1); // +1 for placeholder
      expect(options[1].text()).toBe("Health Insurance Policy");
      expect(options[2].text()).toBe("Auto Insurance Policy");
      expect(options[3].text()).toBe("Life Insurance Policy");
    });

    it("should populate claim options", () => {
      const claimSelect = wrapper.find("#claim");
      const options = claimSelect.findAll("option");

      expect(options).toHaveLength(mockClaims.length + 1); // +1 for placeholder
      expect(options[1].text()).toBe("1");
      expect(options[2].text()).toBe("2");
      expect(options[3].text()).toBe("3");
    });

    it("should mark required fields as required", () => {
      expect(wrapper.find("#subject").attributes("required")).toBeDefined();
      expect(wrapper.find("#description").attributes("required")).toBeDefined();
    });
  });

  describe("Form Validation", () => {
    it("should prevent form submission if nothing changed", async () => {
      const existingTicket = {
        id: 1,
        subject: "Existing Ticket",
        description: "Existing Description",
        policyId: 1,
        claimId: 2,
      };

      wrapper = mount(CreateTicketForm, {
        props: {
          userId: 1,
          ticket: existingTicket,
          policies: mockPolicies,
          claims: mockClaims,
        },
        global: {
          plugins: [mockStore],
          components: { BaseButton: MockBaseButton },
        },
      });

      await wrapper.find("form").trigger("submit");

      const updatedEvents = wrapper.emitted("ticket-updated") || [];
      const createdEvents = wrapper.emitted("ticket-created") || [];

      updatedEvents.forEach((e) => expect(e).toHaveLength(0));
      createdEvents.forEach((e) => expect(e).toHaveLength(0));
    });

    it("should allow form submission when required fields are filled", async () => {
      await wrapper.find("#subject").setValue("Test Subject");
      await wrapper.find("#description").setValue("Test Description");

      const form = wrapper.find("form");
      await form.trigger("submit");

      expect(
        mockStore._modules.root._children.tickets._rawModule.actions
          .createTicket
      ).toHaveBeenCalled();
    });
  });

  describe("Form Interaction", () => {
    it("should update form data when inputs change", async () => {
      await wrapper.find("#subject").setValue("New Subject");
      await wrapper.find("#description").setValue("New Description");
      await wrapper.find("#policy").setValue("1");
      await wrapper.find("#claim").setValue("2");

      expect(wrapper.vm.form.subject).toBe("New Subject");
      expect(wrapper.vm.form.description).toBe("New Description");
      expect(wrapper.vm.form.relatedPolicy).toBe(1);
      expect(wrapper.vm.form.relatedClaim).toBe(2);
    });

    it("should emit cancel event when cancel button is clicked", async () => {
      const cancelButton = wrapper.findAllComponents(MockBaseButton)[0];
      await cancelButton.trigger("click");

      expect(wrapper.emitted("cancel")).toBeTruthy();
      expect(wrapper.emitted("cancel")).toHaveLength(1);
    });
  });

  describe("Ticket Creation", () => {
    it("should create ticket with correct data", async () => {
      await wrapper.find("#subject").setValue("Login Issue");
      await wrapper.find("#description").setValue("Cannot access my account");
      await wrapper.find("#policy").setValue("1");
      await wrapper.find("#claim").setValue("2");

      const form = wrapper.find("form");
      await form.trigger("submit");

      expect(
        mockStore._modules.root._children.tickets._rawModule.actions
          .createTicket
      ).toHaveBeenCalledWith(expect.any(Object), {
        subject: "Login Issue",
        description: "Cannot access my account",
        policyId: 1,
        claimId: 2,
        userId: 1,
      });
    });

    it("should handle null policy and claim values", async () => {
      await wrapper.find("#subject").setValue("Test Subject");
      await wrapper.find("#description").setValue("Test Description");
      await wrapper.find("#policy").setValue(""); // empty selection
      await wrapper.find("#claim").setValue(""); // empty selection

      await wrapper.find("form").trigger("submit");

      expect(
        mockStore._modules.root._children.tickets._rawModule.actions
          .createTicket
      ).toHaveBeenCalledWith(expect.any(Object), {
        subject: "Test Subject",
        description: "Test Description",
        policyId: null,
        claimId: null,
        userId: 1,
      });
    });

    it("should emit ticket-created after successful creation", async () => {
      await wrapper.find("#subject").setValue("Test Subject");
      await wrapper.find("#description").setValue("Test Description");

      const form = wrapper.find("form");
      await form.trigger("submit");
      await wrapper.vm.$nextTick();

      expect(wrapper.emitted("ticket-created")).toBeTruthy();
    });

    it("should reset form after successful submission", async () => {
      await wrapper.find("#subject").setValue("Test Subject");
      await wrapper.find("#description").setValue("Test Description");

      const form = wrapper.find("form");
      await form.trigger("submit");
      await wrapper.vm.$nextTick();

      expect(wrapper.vm.form.subject).toBe("");
      expect(wrapper.vm.form.description).toBe("");
      expect(wrapper.vm.form.relatedPolicy).toBe("");
      expect(wrapper.vm.form.relatedClaim).toBe("");
    });
  });

  describe("Ticket Update Mode", () => {
    beforeEach(async () => {
      const existingTicket = {
        id: 1,
        subject: "Existing Subject",
        description: "Existing Description",
        policyId: 1,
        claimId: 2,
      };

      wrapper = mount(CreateTicketForm, {
        props: {
          userId: 1,
          ticket: existingTicket,
          policies: mockPolicies,
          claims: mockClaims,
        },
        global: {
          plugins: [mockStore],
          components: {
            BaseButton: MockBaseButton,
          },
        },
      });
    });

    it("should show update mode header when ticket prop is provided", () => {
      const submitButton = wrapper.findAllComponents(MockBaseButton)[1];
      expect(submitButton.text()).toBe("Update Ticket");
    });

    it("should populate form with existing ticket data", async () => {
      await wrapper.vm.$nextTick();

      expect(wrapper.vm.form.subject).toBe("Existing Ticket");
      expect(wrapper.vm.form.description).toBe("Existing Description");
      expect(wrapper.vm.form.relatedPolicy).toBe(1);
      expect(wrapper.vm.form.relatedClaim).toBe(2);
    });

    it("should call updateTicket action when updating", async () => {
      await wrapper.find("#subject").setValue("Updated Subject");

      const form = wrapper.find("form");
      await form.trigger("submit");

      expect(
        mockStore._modules.root._children.tickets._rawModule.actions
          .updateTicket
      ).toHaveBeenCalled();
    });

    it("should emit ticket-updated after successful update", async () => {
      const form = wrapper.find("form");
      await form.trigger("submit");
      await wrapper.vm.$nextTick();

      expect(wrapper.emitted("ticket-updated")).toBeTruthy();
    });
  });

  describe("Loading States", () => {
    it("should show submitting state during form submission", async () => {
      // Mock a delayed response
      const delayedPromise = new Promise((resolve) => setTimeout(resolve, 100));
      mockStore._modules.root._children.tickets._rawModule.actions.createTicket.mockReturnValue(
        delayedPromise
      );

      await wrapper.find("#subject").setValue("Test Subject");
      await wrapper.find("#description").setValue("Test Description");

      const form = wrapper.find("form");
      form.trigger("submit"); // Don't await to check loading state

      await wrapper.vm.$nextTick();

      expect(wrapper.vm.isSubmitting).toBe(true);
      const submitButton = wrapper.findAllComponents(MockBaseButton)[1];
      expect(submitButton.props("disabled")).toBe(true);
      expect(submitButton.text()).toBe("Submitting...");
    });

    it("should reset submitting state after completion", async () => {
      await wrapper.find("#subject").setValue("Test Subject");
      await wrapper.find("#description").setValue("Test Description");

      const form = wrapper.find("form");
      await form.trigger("submit");

      expect(wrapper.vm.isSubmitting).toBe(false);
    });
  });

  describe("Error Handling", () => {
    it("should handle ticket creation error", async () => {
      const consoleSpy = vi
        .spyOn(console, "error")
        .mockImplementation(() => {});
      const alertSpy = vi.spyOn(window, "alert").mockImplementation(() => {});

      mockStore._modules.root._children.tickets._rawModule.actions.createTicket.mockRejectedValue(
        new Error("Creation failed")
      );

      await wrapper.find("#subject").setValue("Test Subject");
      await wrapper.find("#description").setValue("Test Description");

      const form = wrapper.find("form");
      await form.trigger("submit");

      expect(consoleSpy).toHaveBeenCalledWith(
        "Error submitting ticket:",
        expect.any(Error)
      );
      expect(alertSpy).toHaveBeenCalledWith(
        "Error submitting ticket. Please try again."
      );

      consoleSpy.mockRestore();
      alertSpy.mockRestore();
    });

    it("should handle ticket update error", async () => {
      const consoleSpy = vi
        .spyOn(console, "error")
        .mockImplementation(() => {});
      const alertSpy = vi.spyOn(window, "alert").mockImplementation(() => {});

      const existingTicket = {
        id: 1,
        subject: "Test Subject",
        description: "Test Description",
      };

      wrapper = mount(CreateTicketForm, {
        props: {
          userId: 1,
          ticket: existingTicket,
          policies: mockPolicies,
          claims: mockClaims,
        },
        global: {
          plugins: [mockStore],
          components: { BaseButton: MockBaseButton },
        },
      });

      // Mock the updateTicket action to reject
      mockStore._modules.root._children.tickets._rawModule.actions.updateTicket.mockRejectedValue(
        new Error("Update failed")
      );

      // Change a field to mark form as dirty
      await wrapper.find("#subject").setValue("Updated Subject");

      const form = wrapper.find("form");
      await form.trigger("submit");

      expect(consoleSpy).toHaveBeenCalledWith(
        "Error submitting ticket:",
        expect.any(Error)
      );
      expect(alertSpy).toHaveBeenCalledWith(
        "Error submitting ticket. Please try again."
      );

      consoleSpy.mockRestore();
      alertSpy.mockRestore();
    });
  });

  describe("Props Validation", () => {
    it("should handle missing userId prop", () => {
      const wrapperWithoutUserId = mount(CreateTicketForm, {
        props: {
          ticket: null,
          policies: [],
          claims: [],
        },
        global: {
          plugins: [mockStore],
          components: {
            BaseButton: MockBaseButton,
          },
        },
      });

      expect(wrapperWithoutUserId.exists()).toBe(true);
    });

    it("should handle empty policies and claims arrays", () => {
      const wrapperWithEmptyArrays = mount(CreateTicketForm, {
        props: {
          userId: 1,
          ticket: null,
          policies: [],
          claims: [],
        },
        global: {
          plugins: [mockStore],
          components: {
            BaseButton: MockBaseButton,
          },
        },
      });

      const policyOptions = wrapperWithEmptyArrays
        .find("#policy")
        .findAll("option");
      const claimOptions = wrapperWithEmptyArrays
        .find("#claim")
        .findAll("option");

      expect(policyOptions).toHaveLength(1); // Just the placeholder
      expect(claimOptions).toHaveLength(1); // Just the placeholder
    });
  });

  describe("Watchers", () => {
    it("should watch for ticket prop changes", async () => {
      const newTicket = { id: 2, subject: "New Ticket" };

      await wrapper.setProps({ ticket: newTicket });

      expect(
        mockStore._modules.root._children.tickets._rawModule.actions
          .fetchTicketById
      ).toHaveBeenCalledWith(expect.any(Object), newTicket.id);
    });
  });

  describe("Form Styling", () => {
    it("should apply correct CSS classes to form elements", () => {
      const subjectInput = wrapper.find("#subject");
      expect(subjectInput.classes()).toContain("w-full");
      expect(subjectInput.classes()).toContain("px-3");
      expect(subjectInput.classes()).toContain("py-2");
      expect(subjectInput.classes()).toContain("border");
      expect(subjectInput.classes()).toContain("border-gray-300");
      expect(subjectInput.classes()).toContain("rounded-lg");
    });

    it("should have proper grid layout for policy and claim selects", () => {
      const gridContainer = wrapper.find(
        ".grid.grid-cols-1.md\\:grid-cols-2.gap-6"
      );
      expect(gridContainer.exists()).toBe(true);
    });

    it("should have proper button styling", () => {
      const buttons = wrapper.findAllComponents(MockBaseButton);
      expect(buttons[0].props("variant")).toBe("outline");
      expect(buttons[1].props("variant")).toBe("primary");
      expect(buttons[1].props("customClass")).toContain("bg-indigo-600");
    });
  });

  describe("Accessibility", () => {
    it("should have proper labels associated with form controls", () => {
      const subjectLabel = wrapper.find('label[for="subject"]');
      const descriptionLabel = wrapper.find('label[for="description"]');
      const policyLabel = wrapper.find('label[for="policy"]');
      const claimLabel = wrapper.find('label[for="claim"]');

      expect(subjectLabel.exists()).toBe(true);
      expect(descriptionLabel.exists()).toBe(true);
      expect(policyLabel.exists()).toBe(true);
      expect(claimLabel.exists()).toBe(true);
    });

    it("should have proper form structure", () => {
      expect(wrapper.find("form").exists()).toBe(true);
      expect(wrapper.find("form").attributes("novalidate")).toBeUndefined(); // Should allow browser validation
    });
  });
});
