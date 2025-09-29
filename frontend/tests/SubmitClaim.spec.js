// tests/SubmitClaim.spec.js  
// Simple tests for the SubmitClaim component

import { mount } from "@vue/test-utils";
import { describe, it, expect, vi, beforeEach } from "vitest";
import SubmitClaim from "@/components/ClaimsManagement/SubmitClaim.vue";

// Simple mock store with _actions property
const mockStore = {
  dispatch: vi.fn().mockResolvedValue({ success: true }),
  commit: vi.fn(),
  state: {
    claims: {
      policies: [
        { id: 1, policyNumber: "POL001", policyType: "Health", availableAmount: 100000 }
      ],
      loading: false,
      error: null
    }
  },
  _actions: {
    'claims/fetchPolicies': vi.fn(),
    'claims/clearMessages': vi.fn(),
    'claims/submitClaim': vi.fn()
  }
};

vi.mock("vuex", () => ({
  useStore: () => mockStore,
}));

// Mock router
vi.mock("vue-router", () => ({
  useRouter: () => ({
    push: vi.fn()
  })
}));

describe("SubmitClaim.vue", () => {
  let wrapper;

  beforeEach(() => {
    wrapper = mount(SubmitClaim, {
      global: {
        stubs: {
          'router-link': { template: '<a><slot /></a>' }
        },
        provide: {
          $store: mockStore
        },
        mocks: {
          $store: mockStore
        }
      }
    });
    mockStore.dispatch.mockClear();
  });

  // Test 1: Component renders
  it("renders the submit claim form", () => {
    expect(wrapper.exists()).toBe(true);
    expect(wrapper.text()).toContain("Submit New Claim");
  });

  // Test 2: Form fields exist
  it("has required form fields", () => {
    expect(wrapper.find('select').exists()).toBe(true); // Policy selector
    expect(wrapper.find('input[type="date"]').exists()).toBe(true); // Date field
    expect(wrapper.find('input[type="number"]').exists()).toBe(true); // Amount field
    expect(wrapper.find('textarea').exists()).toBe(true); // Reason field
  });

  // Test 3: Form can be filled
  it("allows filling form fields", async () => {
    const amountInput = wrapper.find('input[type="number"]');
    await amountInput.setValue('50000');
    expect(amountInput.element.value).toBe('50000');

    const reasonField = wrapper.find('textarea');
    await reasonField.setValue('Medical emergency treatment costs');
    expect(reasonField.element.value).toBe('Medical emergency treatment costs');
  });

  // Test 4: Submit button exists
  it("has a submit button", () => {
    const submitButton = wrapper.find('button[type="submit"]');
    expect(submitButton.exists()).toBe(true);
    expect(submitButton.text()).toMatch(/submit/i);
  });

  // Test 5: Navigation links exist
  it("has navigation links", () => {
    expect(wrapper.text()).toContain("View Claims");
    expect(wrapper.text()).toContain("Submit Claim");
  });
});
