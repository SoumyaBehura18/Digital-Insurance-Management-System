<template>
  <div class="flex flex-col h-screen">
    <!-- Main Content -->
    <main class="flex-1 p-6">
      <!-- Page Header -->
      <header class="flex justify-between items-center mb-6">
        <div>
          <h1 class="text-2xl font-bold">Manage Policies</h1>
          <p class="text-gray-500 text-sm">
            View and manage all insurance policies
          </p>
        </div>
        <button
          class="px-4 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 flex items-center gap-2"
          @click="showCreatePolicy = true"
        >
          <span class="text-lg">+</span> Create New Policy
        </button>
      </header>

      <!-- Policies Table (Claims Style, no Actions) -->
      <div class="bg-white shadow rounded-xl p-6">
        <!-- Header with Search -->
        <div
          class="flex flex-col md:flex-row md:items-center md:justify-between gap-4 mb-4"
        >
          <h2 class="text-lg font-semibold text-gray-800">
            Policies ({{ policies.length }})
          </h2>
          <input
            type="text"
            v-model="search"
            placeholder="Search by name, type, or coverage..."
            class="flex-1 border border-gray-300 rounded-lg px-4 py-2 text-sm focus:ring-2 focus:ring-indigo-400 outline-none"
          />
        </div>

        <!-- Table -->
        <div class="overflow-x-auto">
          <table class="w-full text-sm">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-4 py-3 text-left font-semibold text-gray-600">
                  ID
                </th>
                <th class="px-4 py-3 text-left font-semibold text-gray-600">
                  Name
                </th>
                <th class="px-4 py-3 text-left font-semibold text-gray-600">
                  Type
                </th>
                <th class="px-4 py-3 text-left font-semibold text-gray-600">
                  Coverage
                </th>
                <th class="px-4 py-3 text-left font-semibold text-gray-600">
                  Duration
                </th>
                <th class="px-4 py-3 text-left font-semibold text-gray-600">
                  Description
                </th>
              </tr>
            </thead>
            <tbody>
              <tr
                v-for="(policy, index) in filteredPolicies"
                :key="index"
                class="border-t hover:bg-gray-50 transition"
              >
                <td class="px-4 py-3">#{{ policy.policyId }}</td>
                <td class="px-4 py-3 font-medium text-gray-900">
                  {{ policy.policyName }}
                </td>
                <td class="px-4 py-3">
                  <span
                    class="px-2 py-1 rounded-full text-xs font-semibold"
                    :class="{
                      'bg-pink-100 text-pink-700': policy.policyType === 'LIFE',
                      'bg-green-100 text-green-700':
                        policy.policyType === 'HEALTH',
                      'bg-blue-100 text-blue-700':
                        policy.policyType === 'VEHICLE',
                    }"
                  >
                    {{ policy.policyType }}
                  </span>
                </td>
                <td class="px-4 py-3 text-gray-700">₹{{ policy.coverage }}</td>
                <td class="px-4 py-3 text-gray-700">
                  {{ policy.duration }} months
                </td>
                <td class="px-4 py-3 text-gray-500 truncate max-w-[200px]">
                  {{ policy.description || "No description" }}
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </main>

    <!-- Create Policy Modal -->
    <div
      v-if="showCreatePolicy"
      class="fixed inset-0 z-50 flex items-center justify-center bg-black/60 backdrop-blur-sm px-4"
    >
      <div
        class="bg-white shadow-2xl rounded-2xl w-full max-w-3xl relative flex flex-col max-h-[90vh]"
      >
        <!-- Header -->
        <div
          class="flex justify-between items-center px-6 py-4 border-b sticky top-0 bg-white z-10"
        >
          <h2 class="text-xl md:text-2xl font-bold text-gray-900">
            Create New Policy
          </h2>
          <button
            class="text-gray-500 hover:text-gray-700 w-8 h-8 flex items-center justify-center rounded-full hover:bg-gray-100"
            @click="showCreatePolicy = false"
          >
            ✕
          </button>
        </div>

        <!-- Scrollable Body -->
        <div class="overflow-y-auto px-6 py-4 flex-1">
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <!-- Policy Name -->
            <div>
              <label class="block text-gray-800 font-medium mb-1"
                >Policy Name</label
              >
              <input
                v-model="newPolicy.policyName"
                type="text"
                placeholder="Enter policy name"
                class="w-full border rounded-lg px-3 py-2 focus:ring-2 focus:ring-indigo-400 outline-none"
              />
            </div>

            <!-- Policy Type -->
            <div>
              <label class="block text-gray-800 font-medium mb-1"
                >Policy Type</label
              >
              <select
                v-model="newPolicy.policyType"
                class="w-full border rounded-lg px-3 py-2 focus:ring-2 focus:ring-indigo-400 outline-none"
              >
                <option disabled value="">Select policy type</option>
                <option value="LIFE">Life Insurance</option>
                <option value="HEALTH">Health Insurance</option>
                <option value="VEHICLE">Vehicle Insurance</option>
              </select>
            </div>

            <!-- Coverage -->
            <div>
              <label class="block text-gray-800 font-medium mb-1"
                >Coverage Amount (₹)</label
              >
              <input
                v-model="newPolicy.coverageAmount"
                type="number"
                placeholder="Enter coverage amount"
                class="w-full border rounded-lg px-3 py-2 focus:ring-2 focus:ring-indigo-400 outline-none"
              />
            </div>

            <!-- Premium -->
            <div>
              <label class="block text-gray-800 font-medium mb-1"
                >Annual Premium (₹)</label
              >
              <input
                v-model="newPolicy.annualPremium"
                type="number"
                placeholder="Enter annual premium"
                class="w-full border rounded-lg px-3 py-2 focus:ring-2 focus:ring-indigo-400 outline-none"
              />
            </div>

            <!-- Duration -->
            <div>
              <label class="block text-gray-800 font-medium mb-1"
                >Policy Duration (Months)</label
              >
              <input
                v-model="newPolicy.policyDuration"
                type="number"
                placeholder="e.g., 12"
                class="w-full border rounded-lg px-3 py-2 focus:ring-2 focus:ring-indigo-400 outline-none"
              />
            </div>

            <!-- Renewal Rate -->
            <div>
              <label class="block text-gray-800 font-medium mb-1"
                >Renewal Rate (₹)</label
              >
              <input
                v-model="newPolicy.renewalRate"
                type="number"
                placeholder="Enter renewal rate"
                class="w-full border rounded-lg px-3 py-2 focus:ring-2 focus:ring-indigo-400 outline-none"
              />
            </div>

            <!-- Description -->
            <div class="col-span-1 md:col-span-2">
              <label class="block text-gray-800 font-medium mb-1"
                >Description</label
              >
              <textarea
                v-model="newPolicy.description"
                placeholder="Enter policy description"
                class="w-full border rounded-lg px-3 py-2 focus:ring-2 focus:ring-indigo-400 outline-none min-h-[80px]"
              ></textarea>
            </div>
          </div>

          <!-- Eligibility Criteria -->
          <div class="mt-6 border-t pt-4">
            <label class="block text-gray-900 font-semibold mb-3"
              >Eligibility Criteria</label
            >

            <!-- Vehicle -->
            <div
              v-if="newPolicy.policyType === 'VEHICLE'"
              class="p-4 border rounded-lg bg-gray-50"
            >
              <label class="block text-sm font-semibold mb-2"
                >Vehicle Age</label
              >
              <input
                v-model="newPolicy.vehicleAge"
                type="number"
                placeholder="Enter vehicle age"
                class="w-full border rounded-lg px-3 py-2 focus:ring-2 focus:ring-indigo-400 outline-none"
              />
            </div>

            <!-- Health -->
            <div v-if="newPolicy.policyType === 'HEALTH'">
              <div
                v-for="(condition, index) in newPolicy.conditions"
                :key="index"
                class="p-4 border rounded-lg mb-3 bg-gray-50"
              >
                <p class="text-sm font-semibold mb-2">
                  Condition {{ index + 1 }}
                </p>
                <div class="grid grid-cols-1 md:grid-cols-2 gap-3">
                  <select
                    v-model="condition.condition"
                    class="border rounded-lg px-2 py-2"
                  >
                    <option disabled value="">Select condition</option>
                    <option value="TB">TB</option>
                    <option value="BP">BP</option>
                    <option value="DIABETES">Diabetes</option>
                    <option value="CANCER">Cancer</option>
                  </select>
                  <input
                    v-model="condition.extraPremium"
                    type="number"
                    placeholder="Extra premium"
                    class="border rounded-lg px-2 py-2"
                  />
                </div>
                <button
                  class="text-gray-600 hover:text-red-600 mt-2"
                  @click="removeCondition(index)"
                >
                  <Trash class="w-4 h-4" />
                </button>
              </div>
              <button
                type="button"
                @click="addCondition"
                class="text-indigo-600 hover:underline text-sm font-medium"
              >
                + Add Condition
              </button>
            </div>

            <!-- Life -->
            <p
              v-if="newPolicy.policyType === 'LIFE'"
              class="text-gray-500 text-sm"
            >
              No eligibility criteria for Life policies.
            </p>
          </div>
        </div>

        <!-- Footer -->
        <div
          class="flex justify-end space-x-3 px-6 py-4 border-t sticky bottom-0 bg-white"
        >
          <button
            @click="showCreatePolicy = false"
            class="border border-gray-400 px-6 py-2 rounded-lg hover:bg-gray-100"
          >
            Cancel
          </button>
          <button
            @click="createPolicy"
            class="bg-indigo-600 text-white px-6 py-2 rounded-lg shadow hover:bg-indigo-700"
          >
            Create Policy
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { reactive, ref, computed, onMounted } from "vue";
import { useStore } from "vuex";
import { Trash } from "lucide-vue-next";
import { useToast } from "vue-toast-notification";

const store = useStore();
const toast = useToast();

// Modal & form state
const showCreatePolicy = ref(false);
const search = ref("");
const activeFilter = ref("All");

const newPolicy = reactive({
  policyName: "",
  policyType: "",
  coverageAmount: "",
  annualPremium: "",
  policyDuration: "",
  renewalRate: "",
  vehicleAge: "",
  conditions: [],
  description: "",
});

// Policies from store
const policies = computed(() => store.getters["adminPolicyStore/getPolicies"]);

// Filtered policies for table
const filteredPolicies = computed(() => {
  return policies.value.filter((p) => {
    const matchesSearch =
      p.policyName.toLowerCase().includes(search.value.toLowerCase()) ||
      p.policyType.toLowerCase().includes(search.value.toLowerCase());
    const matchesFilter =
      activeFilter.value === "All" || p.policyType === activeFilter.value;
    return matchesSearch && matchesFilter;
  });
});

// Add / Remove conditions for Health policies
const addCondition = () => {
  newPolicy.conditions.push({ condition: "", extraPremium: 0 });
};

const removeCondition = (index) => {
  newPolicy.conditions.splice(index, 1);
};

// Reset form
const resetNewPolicy = () => {
  Object.assign(newPolicy, {
    policyName: "",
    policyType: "",
    coverageAmount: "",
    annualPremium: "",
    policyDuration: "",
    renewalRate: "",
    vehicleAge: "",
    conditions: [],
    description: "",
  });
};

// Create policy using store action
const createPolicy = async () => {
  try {
    await store.dispatch("adminPolicyStore/createPolicy", newPolicy);
    toast.success("Policy created successfully!", { position: "top-right" });
    resetNewPolicy();
    showCreatePolicy.value = false;
  } catch (error) {
    console.error("Error creating policy:", error);
    toast.error("Failed to create policy. Please try again.", {
      position: "top-right",
    });
  }
};

// Load all policies on mount
onMounted(() => {
  store.dispatch("adminPolicyStore/fetchPolicies");
});
</script>
