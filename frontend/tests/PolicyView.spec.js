// tests/PolicyView.spec.js
import { mount } from "@vue/test-utils";
import { describe, it, expect, beforeEach } from "vitest";
import PoliciesPage from "@/views/User/PoliciesPage.vue";

// Mock child components
const stubs = {
  PolicyCatalog: { template: "<div class='policy-catalog-mock'>PolicyCatalog</div>" },
  MyPolicies: { template: "<div class='my-policies-mock'>MyPolicies</div>" },
  HeaderLayout: { template: "<div class='header-layout-mock'>Header</div>" },
  PurchaseModal: true, // just stub modal
};

describe("PolicyView.vue", () => {
  let wrapper;

  beforeEach(() => {
    wrapper = mount(PoliciesPage, {
      global: { stubs },
    });
  });

  it("renders header layout", () => {
    expect(wrapper.find(".header-layout-mock").exists()).toBe(true);
  });

  it("defaults to catalog view", () => {
    expect(wrapper.find(".policy-catalog-mock").exists()).toBe(true);
    expect(wrapper.find(".my-policies-mock").exists()).toBe(false);
  });

  it("switches to My Policies view when button clicked", async () => {
    const myPoliciesButton = wrapper
      .findAll("button")
      .find((btn) => btn.text() === "My Policies");

    await myPoliciesButton.trigger("click");

    expect(wrapper.find(".my-policies-mock").exists()).toBe(true);
    expect(wrapper.find(".policy-catalog-mock").exists()).toBe(false);
  });

  it("switches back to catalog view", async () => {
    const myPoliciesButton = wrapper
      .findAll("button")
      .find((btn) => btn.text() === "My Policies");

    await myPoliciesButton.trigger("click");

    const catalogButton = wrapper
      .findAll("button")
      .find((btn) => btn.text() === "Browse Policies");

    await catalogButton.trigger("click");

    expect(wrapper.find(".policy-catalog-mock").exists()).toBe(true);
    expect(wrapper.find(".my-policies-mock").exists()).toBe(false);
  });

  it("header collapse toggle works", async () => {
    expect(wrapper.vm.isCollapsed).toBe(false);
    wrapper.vm.setIsCollapsed(true);
    await wrapper.vm.$nextTick();
    expect(wrapper.vm.isCollapsed).toBe(true);
  });
});
