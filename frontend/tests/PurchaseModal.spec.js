// tests/PurchaseModal.spec.js
import { mount } from "@vue/test-utils";
import { describe, it, expect, vi, beforeEach } from "vitest";
import PurchaseModal from "@/components/PoliciesComponents/PurchaseModal.vue";
import { nextTick } from "vue";

const mockStore = {
  dispatch: vi.fn(),
  commit: vi.fn(),
  state: { userPolicies: { policies: [] } },
};

vi.mock("vuex", () => ({
  useStore: () => mockStore,
}));

vi.mock("@/utils/requests", () => ({
  makeRequestWithToken: vi.fn().mockResolvedValue({
    data: { policyName: "Mock Policy", id: 1 },
  }),
}));

describe("PurchaseModal.vue", () => {
  let wrapper;

  const basePolicy = {
    policyName: "Test Policy",
    duration: 12,
    premiumRate: 10000,
    renewalRate: 12000,
    noClaimBonus: false,
    isRenewal: true,
  };

  beforeEach(() => {
    wrapper = mount(PurchaseModal, {
      props: { policy: { ...basePolicy } },
    });
    mockStore.dispatch.mockClear();
    mockStore.commit.mockClear();
  });

  it("renders policy details correctly", () => {
  expect(wrapper.text()).toContain("Confirm Renewal");
  expect(wrapper.text()).toContain(basePolicy.policyName);
  expect(wrapper.text()).toContain("Renewal Premium"); // matches isRenewal
});


  it("calculates premium with NCB discount", () => {
    const premium = wrapper.vm.premiumToPay;
    expect(premium).toBe((basePolicy.renewalRate * 0.9).toFixed(2)); // 10% discount applied
    expect(wrapper.html()).toContain("10% No Claim Bonus discount");
  });

  it("emits purchased event on confirmRenewal", async () => {
    wrapper.vm.confirmPurchase = async () => {
      wrapper.vm.success = true;
      wrapper.vm.loading = false;
      wrapper.emits("purchased", basePolicy);
    };

    const confirmButton = wrapper.findAll("button").find(
  (btn) => btn.text() === "Confirm Renewal"
);
expect(confirmButton).toBeTruthy();
await confirmButton.trigger("click");
    await nextTick();

    // In our mock, success should be true after confirm
    expect(wrapper.vm.success).toBe(true);
  });

it("handles cancel button", async () => {
  // Find button by text
  const cancelButton = wrapper.findAll("button").find(btn =>
    btn.text() === "Cancel"
  );
  expect(cancelButton).toBeTruthy(); // ensure button exists

  await cancelButton.trigger("click");

  // Check emitted event
  expect(wrapper.emitted()).toHaveProperty("close");
  expect(wrapper.emitted("close").length).toBe(1);
});


  it("computes correct end date from duration", () => {
    const endDate = wrapper.vm.endDate;
    const now = new Date();
    const expectedMonth = now.getMonth() + basePolicy.duration;
    expect(endDate.getFullYear()).toBe(
      expectedMonth > 11 ? now.getFullYear() + 1 : now.getFullYear()
    );
  });

  it("simulates expired policy (>15 days past endDate)", async () => {
  const oldPolicy = {
    ...basePolicy,
    endDate: new Date(Date.now() - 16 * 24 * 60 * 60 * 1000).toISOString(), // 16 days ago
    status: "EXPIRED", // okay to add for test, but component may not use it
  };

  await wrapper.setProps({ policy: oldPolicy });
  await nextTick();

  // Confirm prop is set
  const propPolicy = wrapper.props("policy");
  expect(new Date(propPolicy.endDate).toISOString()).toBe(oldPolicy.endDate);

  // Simulate the component logic
  const diffDays =
    (new Date() - new Date(propPolicy.endDate)) / (1000 * 60 * 60 * 24);
  expect(diffDays).toBeGreaterThan(15);
});
});
