import { mount, flushPromises } from "@vue/test-utils";
import axios from "axios";
import AdminPolicies from "@/components/AdminPoliciesView/AdminPolicies.vue";

// mock axios
jest.mock("axios");

describe("AdminPolicies.vue", () => {
  let wrapper;

  beforeEach(() => {
    localStorage.setItem("currentUser", JSON.stringify({ token: "fake-jwt-token" }));
    axios.get.mockResolvedValue({ data: [] }); // default empty
    wrapper = mount(AdminPolicies, {
      global: {
        mocks: {
          $store: {
            getters: { "user/getCurrentUser": { token: "fake-jwt-token" } }
          }
        }
      }
    });
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it("renders Manage Policies header", () => {
    expect(wrapper.text()).toContain("Manage Policies");
    expect(wrapper.text()).toContain("View and manage all insurance policies");
  });

  it("loads policies on mount", async () => {
    const mockPolicies = [
      {
        id: 1,
        name: "Health Policy",
        type: "HEALTH",
        coverageAmt: 10000,
        premiumRate: 2000,
        renewalRate: 500,
        durationMonths: 12
      }
    ];
    axios.get.mockResolvedValueOnce({ data: mockPolicies });

    wrapper = mount(AdminPolicies, {
      global: {
        mocks: {
          $store: {
            getters: { "user/getCurrentUser": { token: "fake-jwt-token" } }
          }
        }
      }
    });

    await flushPromises();

    expect(wrapper.text()).toContain("Health Policy");
    expect(wrapper.findAll("tbody tr")).toHaveLength(1);
  });

  it("opens create policy modal when button clicked", async () => {
    expect(wrapper.find("h2.text-xl").exists()).toBe(false); // modal header not visible

    await wrapper.find("button").trigger("click"); // first button is "Create New Policy"

    expect(wrapper.find("h2.text-xl").text()).toBe("Create New Policy");
  });

  it("adds condition when + Add Condition clicked", async () => {
    await wrapper.find("button").trigger("click"); // open modal
    const addBtn = wrapper.find("button.text-indigo-600"); // + Add Condition
    await addBtn.trigger("click");

    expect(wrapper.vm.newPolicy.conditions).toHaveLength(1);
  });

  it("removes condition when trash button clicked", async () => {
    await wrapper.find("button").trigger("click"); // open modal
    wrapper.vm.newPolicy.conditions.push({ condition: "BP", extraPremium: 200 });
    await wrapper.vm.$nextTick();

    const removeBtn = wrapper.find("button.text-gray-600");
    await removeBtn.trigger("click");

    expect(wrapper.vm.newPolicy.conditions).toHaveLength(0);
  });

  it("creates a Life policy via API", async () => {
    await wrapper.find("button").trigger("click"); // open modal
    wrapper.vm.newPolicy.policyName = "Life Policy";
    wrapper.vm.newPolicy.policyType = "LIFE";
    wrapper.vm.newPolicy.coverageAmount = 50000;
    wrapper.vm.newPolicy.annualPremium = 3000;
    wrapper.vm.newPolicy.policyDuration = 12;
    wrapper.vm.newPolicy.renewalRate = 600;

    axios.post.mockResolvedValueOnce({ data: { id: 1, name: "Life Policy" } }); // base policy
    axios.post.mockResolvedValueOnce({}); // life-premium call

    await wrapper.vm.createPolicy();
    await flushPromises();

    expect(axios.post).toHaveBeenCalledWith(
      "http://localhost:9090/api/admin/policies",
      expect.objectContaining({ name: "Life Policy", type: "LIFE" }),
      expect.any(Object)
    );
    expect(wrapper.vm.policies).toHaveLength(1);
  });

  it("shows error if API fails on createPolicy", async () => {
    axios.post.mockRejectedValueOnce(new Error("API error"));

    await wrapper.find("button").trigger("click"); // open modal
    wrapper.vm.newPolicy.policyName = "Broken Policy";
    wrapper.vm.newPolicy.policyType = "LIFE";

    const spy = jest.spyOn(window, "alert").mockImplementation(() => {});
    await wrapper.vm.createPolicy();

    expect(spy).toHaveBeenCalledWith("Failed to create policy");
  });
});
