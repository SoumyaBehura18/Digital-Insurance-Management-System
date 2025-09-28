// tests/AdminClaims.spec.js
// Simple tests for the AdminClaims component

import { mount } from "@vue/test-utils";
import { describe, it, expect, vi, beforeEach } from "vitest";
import AdminClaims from "@/components/ClaimsManagement/AdminClaims.vue";

// Simple mock store with _actions property
const mockStore = {
  dispatch: vi.fn().mockResolvedValue(),
  commit: vi.fn(),
  state: {
    claims: {
      adminClaims: [
        { id: 1, userEmail: "user@test.com", policyName: "Health Plan", claimAmount: 50000, status: "PENDING" }
      ],
      loading: false,
      error: null
    }
  },
  getters: {
    "claims/adminPendingClaims": [{ id: 1, status: "PENDING" }],
    "claims/adminApprovedClaims": [],
    "claims/adminRejectedClaims": []
  },
  _actions: {
    'claims/fetchAllClaims': vi.fn(),
    'claims/reviewClaim': vi.fn()
  }
};

vi.mock("vuex", () => ({
  useStore: () => mockStore,
}));

describe("AdminClaims.vue", () => {
  let wrapper;

  beforeEach(() => {
    wrapper = mount(AdminClaims, {
      global: {
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
  it("renders the admin claims page", () => {
    expect(wrapper.exists()).toBe(true);
    expect(wrapper.text()).toContain("Manage Claims");
  });

  // Test 2: Has statistics cards
  it("displays claim statistics", () => {
    expect(wrapper.text()).toContain("Total Claims");
    expect(wrapper.text()).toContain("Pending");
    expect(wrapper.text()).toContain("Approved");
    expect(wrapper.text()).toContain("Rejected");
  });

  // Test 3: Has search/filter interface elements
  it("has search and filter functionality", () => {
    // Look for any input field or filter interface elements
    const inputs = wrapper.findAll('input');
    const selects = wrapper.findAll('select');  
    const buttons = wrapper.findAll('button');
    
    // Should have some interactive elements (inputs, selects, or buttons)
    const totalInteractiveElements = inputs.length + selects.length + buttons.length;
    expect(totalInteractiveElements).toBeGreaterThan(0);
  });

  // Test 4: Has claims table
  it("displays claims table", () => {
    expect(wrapper.find('table').exists()).toBe(true);
    expect(wrapper.text()).toContain("Claim ID");
    expect(wrapper.text()).toContain("Customer Email");
    expect(wrapper.text()).toContain("Policy");
    expect(wrapper.text()).toContain("Amount");
    expect(wrapper.text()).toContain("Status");
    expect(wrapper.text()).toContain("Actions");
  });

  // Test 5: Has action buttons
  it("shows action buttons for claims", () => {
    const buttons = wrapper.findAll('button');
    expect(buttons.length).toBeGreaterThan(0);
    
    // Look for common button text
    const buttonTexts = buttons.map(btn => btn.text()).join(' ');
    expect(buttonTexts).toMatch(/view|approve|reject/i);
  });
});
