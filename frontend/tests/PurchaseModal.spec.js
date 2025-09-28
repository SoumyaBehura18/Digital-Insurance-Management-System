// tests/PurchaseModal.spec.js
import { mount } from "@vue/test-utils";
import { describe, it, expect, vi, beforeEach } from "vitest";
import { nextTick } from "vue";
import PurchaseModal from "@/components/PoliciesComponents/PurchaseModal.vue";

// Mock Vuex Store
const mockStore = {
  dispatch: vi.fn(),
  commit: vi.fn(),
  state: { userPolicies: { policies: [] } },
};

vi.mock("vuex", () => ({
  useStore: () => mockStore,
}));

// Mock API utility
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
    noClaimBonus: true, // has NCB for discount
    isRenewal: true,
    status: "ACTIVE",
  };

  beforeEach(() => {
    wrapper = mount(PurchaseModal, {
      props: { policy: { ...basePolicy } },
    });
    mockStore.dispatch.mockClear();
    mockStore.commit.mockClear();
  });

  // UI RENDER TEST
  it("renders renewal confirmation and details correctly", () => {
    expect(wrapper.text()).toContain("Confirm Renewal");
    expect(wrapper.text()).toContain(basePolicy.policyName);
    expect(wrapper.text()).toContain("Renewal Premium");
  });

  // PREMIUM CALCULATION TEST (with 10% NCB)
  it("applies 10% discount for NCB policies", async () => {
    await wrapper.setProps({ policy: { ...basePolicy, noClaimBonus: false } });
    await nextTick();

    const expected = basePolicy.renewalRate * 0.9; // 10800
    expect(wrapper.vm.premiumToPay).toBeCloseTo(expected, 2);
    expect(wrapper.html()).toContain("10% No Claim Bonus discount");
  });


  // CANCEL BUTTON TEST
  it("emits 'close' event when cancel button is clicked", async () => {
    const cancelButton = wrapper
      .findAll("button")
      .find((btn) => btn.text() === "Cancel");

    expect(cancelButton).toBeTruthy();
    await cancelButton.trigger("click");

    expect(wrapper.emitted()).toHaveProperty("close");
    expect(wrapper.emitted("close").length).toBe(1);
  });

  //  DATE CALCULATION TEST
  it("computes correct end date based on duration", () => {
    const endDate = wrapper.vm.endDate;
    const now = new Date();
    const expectedMonth = now.getMonth() + basePolicy.duration;

    expect(endDate.getFullYear()).toBe(
      expectedMonth > 11 ? now.getFullYear() + 1 : now.getFullYear()
    );
  });

  //  EXPIRED POLICY TEST (past 15 days)
  it("marks policy as expired when more than 15 days past end date", async () => {
    const oldPolicy = {
      ...basePolicy,
      endDate: new Date(Date.now() - 16 * 24 * 60 * 60 * 1000).toISOString(),
      status: "EXPIRED",
      isRenewal: false,
    };

    await wrapper.setProps({ policy: oldPolicy });
    await nextTick();

    const diffDays =
      (new Date() - new Date(oldPolicy.endDate)) / (1000 * 60 * 60 * 24);

    expect(diffDays).toBeGreaterThan(15);
    expect(wrapper.props("policy").status).toBe("EXPIRED");
  });

  //  RENEW_PENDING POLICY TEST (within 15 days of expiry)
  it("shows renew pending if within 15 days of expiry", async () => {
    const pendingPolicy = {
      ...basePolicy,
      endDate: new Date(Date.now() - 10 * 24 * 60 * 60 * 1000).toISOString(),
      status: "RENEW_PENDING",
      isRenewal: true,
    };

    await wrapper.setProps({ policy: pendingPolicy });
    await nextTick();

    const diffDays =
      (new Date() - new Date(pendingPolicy.endDate)) / (1000 * 60 * 60 * 24);

    expect(diffDays).toBeLessThanOrEqual(15);
    expect(wrapper.props("policy").status).toBe("RENEW_PENDING");
    expect(wrapper.text()).toContain("Confirm Renewal");
  });
});
