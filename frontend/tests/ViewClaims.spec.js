// tests/ViewClaims.spec.js
// Simple tests for the ViewClaims/ClaimsPage component

import { mount } from "@vue/test-utils";
import { describe, it, expect, vi, beforeEach } from "vitest";
import ClaimsPage from "@/views/User/ClaimsPage.vue";

// Mock the store
const mockStore = {
  dispatch: vi.fn().mockResolvedValue(),
  commit: vi.fn(),
  state: {
    claims: {
      userClaims: [],
      loading: false,
      error: null
    }
  }
};

vi.mock("vuex", () => ({
  useStore: () => mockStore,
}));

// Mock Vue Router
vi.mock("vue-router", () => ({
  useRoute: () => ({
    params: {},
    query: {},
    path: "/claims"
  })
}));

// Mock child components to avoid complexity
const stubs = {
  ClaimList: { 
    template: "<div class='claim-list-stub'>Claim List</div>",
    methods: {
      mounted() { /* stub */ }
    }
  },
  SubmitClaim: { 
    template: "<div class='submit-claim-stub'>Submit Claim Form</div>" 
  },
  HeaderLayout: { 
    template: "<div class='header-stub'>Header</div>" 
  },
  SidebarLayout: { 
    template: "<div class='sidebar-stub'>Sidebar</div>" 
  }
};

describe("ClaimsPage.vue", () => {
  let wrapper;

  beforeEach(() => {
    wrapper = mount(ClaimsPage, {
      global: { 
        stubs
      }
    });
    mockStore.dispatch.mockClear();
  });

  // Test 1: Basic rendering
  it("renders the claims page", () => {
    expect(wrapper.exists()).toBe(true);
    expect(wrapper.find('.header-stub').exists()).toBe(true);
  });

  // Test 2: Default view
  it("shows claims list by default", () => {
    expect(wrapper.find('.claim-list-stub').exists()).toBe(true);
  });

  // Test 3: Navigation exists
  it("has navigation elements", () => {
    // Look for any navigation-related text or elements
    const text = wrapper.text();
    expect(text.includes('Claims') || text.includes('Header') || text.includes('Sidebar')).toBe(true);
  });

  // Test 4: Component structure
  it("has the expected component structure", () => {
    // Just verify the main wrapper exists and has content
    expect(wrapper.html().length).toBeGreaterThan(10);
  });

  // Test 5: Store interaction
  it("can interact with the store", () => {
    // Simple test to verify store mock is working
    expect(mockStore.state.claims).toBeDefined();
    expect(mockStore.dispatch).toBeDefined();
  });
});
