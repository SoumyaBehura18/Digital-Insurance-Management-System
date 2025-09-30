import { mount, flushPromises } from "@vue/test-utils";
import { describe, it, expect, vi, beforeEach } from "vitest";
import PoliciesComponent from "../src/components/AdminPoliciesView/AdminPolicies.vue";
import * as vuex from 'vuex';

const mockPolicies = [
  { policyId: 1, policyName: "Life Protect", policyType: "LIFE", coverage: 100000, duration: 12, description: "Life policy" },
  { policyId: 2, policyName: "Health Plus", policyType: "HEALTH", coverage: 50000, duration: 24, description: "Health policy" },
  { policyId: 3, policyName: "Car Secure", policyType: "VEHICLE", coverage: 30000, duration: 36, description: "Vehicle policy" },
];

// Mock Vuex store
const createStore = () => ({
  getters: {
    "adminPolicyStore/getPolicies": mockPolicies
  },
  dispatch: vi.fn()
});

// Mock the useStore function
vi.mock('vuex', async () => {
  const actual = await vi.importActual('vuex');
  return {
    ...actual,
    useStore: vi.fn()
  };
});

describe("AdminPolicies.vue", () => {
  let wrapper;
  let mockStore;

  beforeEach(() => {
    mockStore = createStore();
    // Set up the mock implementation for useStore
    vuex.useStore.mockImplementation(() => mockStore);
    
    // Mock window.alert
    global.alert = vi.fn();
    
    wrapper = mount(PoliciesComponent, {
      global: {
        // No need for $store mocks anymore
      }
    });
  });

  it("renders the table with policies", () => {
    const rows = wrapper.findAll("tbody tr");
    expect(rows).toHaveLength(mockPolicies.length);
    expect(rows[0].text()).toContain("Life Protect");
    expect(rows[1].text()).toContain("Health Plus");
  });

  it("opens and closes the create policy modal", async () => {
    expect(wrapper.find('.fixed.inset-0.z-50').exists()).toBe(false);

    // Open modal by clicking the "Create New Policy" button
    await wrapper.find("button").trigger("click");
    expect(wrapper.find('.fixed.inset-0.z-50').exists()).toBe(true);

    // Close modal by clicking the X button
    const closeButtons = wrapper.findAll('button');
    const closeButton = closeButtons.find(btn => btn.text().includes('✕'));
    await closeButton.trigger("click");
    expect(wrapper.find('.fixed.inset-0.z-50').exists()).toBe(false);
  });

  it("filters policies by search input", async () => {
    const input = wrapper.find('input[type="text"]');
    await input.setValue("car");
    await flushPromises();

    const rows = wrapper.findAll("tbody tr");
    expect(rows).toHaveLength(1);
    expect(rows[0].text()).toContain("Car Secure");
  });

  it("calls createPolicy action when form is submitted", async () => {
    // Open modal
    await wrapper.find("button").trigger("click");
    await flushPromises();

    // Click create button (even with empty form)
    const createButton = wrapper.findAll('button').find(btn => btn.text().includes('Create Policy'));
    await createButton.trigger("click");
    await flushPromises();

    // Check that createPolicy action was dispatched
    const createPolicyCall = mockStore.dispatch.mock.calls.find(call => 
      call[0] === "adminPolicyStore/createPolicy"
    );
    expect(createPolicyCall).toBeDefined();
    
    // Verify it was called with the newPolicy object (even if empty)
    expect(createPolicyCall[1]).toEqual(expect.objectContaining({
      policyName: "",
      policyType: "",
      coverageAmount: "",
      annualPremium: "",
      policyDuration: "",
      renewalRate: ""
    }));
  });
});
