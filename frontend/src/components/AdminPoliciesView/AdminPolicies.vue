<template>
  <div class="flex flex-col h-screen">
    <!-- Header -->
    <!-- <HeaderLayout
      :user="user"
      :isCollapsed="isCollapsed"
      :setIsCollapsed="setIsCollapsed"
      class="w-full"
    /> -->

    <!-- Main Content -->
    <main class="flex-1 p-6">
      <!-- Page Header -->
      <header class="flex justify-between items-center mb-2">
        <div>
          <h1 class="text-2xl font-bold">Manage Policies</h1>
          <p class="text-gray-500 text-sm">View and manage all insurance policies</p>
        </div>
        <button
          class="px-4 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 flex items-center gap-2"
          @click="showCreatePolicy = true"
        >
          <span class="text-lg">+</span> Create New Policy
        </button>
      </header>

      <!-- Policies Table -->
      <div class="bg-white shadow rounded-xl overflow-hidden mt-6">
        <!-- Card Header -->
        <div class="p-4 border-b">
          <h2 class="font-medium text-gray-800">All Policies ({{ policies.length }})</h2>
        </div>

        <table class="w-full text-sm">
          <thead class="bg-gray-50">
            <tr>
              <th class="px-4 py-3 text-left font-semibold text-gray-700">Policy Name</th>
              <th class="px-4 py-3 text-left font-semibold text-gray-700">Type</th>
              <th class="px-4 py-3 text-left font-semibold text-gray-700">Coverage</th>
              <th class="px-4 py-3 text-left font-semibold text-gray-700">Premium</th>
              <th class="px-4 py-3 text-left font-semibold text-gray-700">Duration</th>
              <th class="px-4 py-3 text-left font-semibold text-gray-700">Status</th>
              <th class="px-4 py-3 text-left font-semibold text-gray-700">User</th>
              <th class="px-4 py-3 text-center font-semibold text-gray-700">Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="(policy, index) in policies"
              :key="index"
              class="border-t hover:bg-gray-50"
            >
              <td class="px-4 py-3">{{ policy.policyName }}</td>
              <td class="px-4 py-3">
                <span
                  class="px-2 py-1 rounded-full text-xs font-medium bg-gray-100 text-gray-700"
                >
                  {{ policy.policyType }}
                </span>
              </td>
              <td class="px-4 py-3">{{ policy.coverageAmount }}</td>
              <td class="px-4 py-3">{{ policy.annualPremium }}</td>
              <td class="px-4 py-3">{{ policy.policyDuration }}</td>
              <td class="px-4 py-3">
                <span
                  :class="policy.status === 'ACTIVE'
                    ? 'bg-indigo-100 text-indigo-600 px-2 py-1 rounded-full text-xs font-semibold'
                    : 'bg-gray-200 text-gray-600 px-2 py-1 rounded-full text-xs font-semibold'"
                >
                  {{ policy.status }}
                </span>
              </td>
              <td class="px-4 py-3">{{ policy.user || 'N/A' }}</td>
              <td class="px-4 py-3 flex justify-center space-x-3">
                <button class="text-gray-600 hover:text-indigo-600">
                  <Pencil class="w-4 h-4" />
                </button>
                <button
                  class="text-gray-600 hover:text-red-600"
                  @click="removePolicy(index)"
                >
                  <Trash class="w-4 h-4" />
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </main>

    <!-- Create Policy Modal -->
<div
  v-if="showCreatePolicy"
  class="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50"
>
  <div class="bg-white shadow-xl rounded-xl p-6 max-w-2xl w-full relative">
    <!-- Header -->
    <div class="flex justify-between items-center mb-6">
      <h2 class="text-xl font-bold text-gray-900">Create New Policy</h2>
      <button class="text-gray-500 hover:text-gray-700" @click="showCreatePolicy = false">✕</button>
    </div>

    <!-- Form -->
    <div class="grid grid-cols-2 gap-4">
      <!-- Policy Name -->
      <div>
        <label class="block text-gray-900 font-medium mb-1">Policy Name</label>
        <input
          v-model="newPolicy.policyName"
          type="text"
          placeholder="Enter policy name"
          class="w-full border rounded-lg px-3 py-2 focus:ring focus:outline-none"
        />
      </div>

      <!-- Policy Type -->
      <div>
        <label class="block text-gray-900 font-medium mb-1">Policy Type</label>
        <select
          v-model="newPolicy.policyType"
          class="w-full border rounded-lg px-3 py-2 focus:ring focus:outline-none"
        >
          <option disabled value="">Select policy type</option>
          <option value="Life">Life Insurance</option>
          <option value="Health">Health Insurance</option>
          <option value="Auto">Auto Insurance</option>
        </select>
      </div>

      <!-- Coverage -->
      <div>
        <label class="block text-gray-900 font-medium mb-1">Coverage Amount ($)</label>
        <input
          v-model="newPolicy.coverageAmount"
          type="number"
          placeholder="Enter coverage amount"
          class="w-full border rounded-lg px-3 py-2 focus:ring focus:outline-none"
        />
      </div>

      <!-- Premium -->
      <div>
        <label class="block text-gray-900 font-medium mb-1">Annual Premium ($)</label>
        <input
          v-model="newPolicy.annualPremium"
          type="number"
          placeholder="Enter annual premium"
          class="w-full border rounded-lg px-3 py-2 focus:ring focus:outline-none"
        />
      </div>

      <!-- Duration (full width row) -->
      <div class="col-span-2">
        <label class="block text-gray-900 font-medium mb-1">Policy Duration</label>
        <input
          v-model="newPolicy.policyDuration"
          type="text"
          placeholder="e.g., 1 year, 10 years"
          class="w-full border rounded-lg px-3 py-2 focus:ring focus:outline-none"
        />
      </div>
    </div>

   <!-- Eligibility Criteria -->
<div class="mt-6">
  <label class="block text-gray-900 font-medium mb-2">Eligibility Criteria</label>

  <div
    v-for="(criteria, index) in newPolicy.eligibilityCriteria"
    :key="index"
    class="p-4 border rounded-lg mb-3 bg-gray-50"
  >
    <p class="text-sm font-semibold mb-2">Criteria {{ index + 1 }}</p>
    <div class="grid grid-cols-3 gap-3 items-center">
      <!-- Field -->
      <select v-model="criteria.field" class="border rounded-lg px-2 py-2">
        <option disabled value="">Select field</option>
        <option
          v-for="field in getFieldsByType(newPolicy.policyType)"
          :key="field"
          :value="field"
        >
          {{ field }}
        </option>
      </select>

      <!-- Operator -->
      <select v-model="criteria.operator" class="border rounded-lg px-2 py-2">
        <option value="Greater Than">Greater Than</option>
        <option value="Less Than">Less Than</option>
        <option value="Equal">Equal</option>
        <option value="Between">Between</option>
      </select>

      <!-- Value -->
      <input
        v-model="criteria.value"
        type="text"
        placeholder="Enter value"
        class="border rounded-lg px-2 py-2"
      />
    </div>

    <!-- Remove button -->
    <button
      type="button"
      @click="removeCriteria(index)"
      class="mt-2 text-red-500 hover:text-red-700 text-sm flex items-center"
    >
      🗑 Remove
    </button>
  </div>

  <!-- ✅ Add Criteria Button -->
  <button
    type="button"
    @click="addCriteria"
    class="text-indigo-600 hover:underline text-sm font-medium"
  >
    + Add Criteria
  </button>
</div>

    

    <!-- Footer -->
    <div class="flex justify-end space-x-3 mt-6">
      <button
        @click="showCreatePolicy = false"
        class="border border-gray-400 px-6 py-2 rounded-lg hover:bg-gray-100"
      >
        Cancel
      </button>
      <button
        @click="createPolicy"
        class="bg-indigo-600 text-white px-6 py-2 rounded-lg hover:bg-indigo-700"
      >
        Create Policy
      </button>
    </div>
  </div>
</div>

  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import HeaderLayout from "@/components/layout/HeaderLayout.vue";
import { Pencil, Trash } from "lucide-vue-next";

const showCreatePolicy = ref(false);

const policies = reactive([
  {
    policyName: "Comprehensive Life Insurance",
    policyType: "Life",
    coverageAmount: "$500,000",
    annualPremium: "$2,400/year",
    policyDuration: "20 years",
    status: "ACTIVE",
    user: "User 1",
  },
  {
    policyName: "Premium Health Coverage",
    policyType: "Health",
    coverageAmount: "$100,000",
    annualPremium: "$1,200/year",
    policyDuration: "1 year",
    status: "ACTIVE",
    user: "User 1",
  },
  {
    policyName: "Auto Protection Plus",
    policyType: "Auto",
    coverageAmount: "$50,000",
    annualPremium: "$800/year",
    policyDuration: "1 year",
    status: "EXPIRED",
    user: "User 2",
  },
  {
    policyName: "Family Life Insurance",
    policyType: "Life",
    coverageAmount: "$750,000",
    annualPremium: "$3,200/year",
    policyDuration: "25 years",
    status: "ACTIVE",
    user: "User 2",
  },
]);

const newPolicy = reactive({
  policyName: "",
  policyType: "",
  coverageAmount: "",
  annualPremium: "",
  policyDuration: "",
  status: "ACTIVE",
  user: "User 3",
  eligibilityCriteria: [], // ✅ FIX: initialize array here
});


const createPolicy = () => {
  if (!newPolicy.policyName || !newPolicy.policyType) {
    alert("Please fill all required fields");
    return;
  }
  policies.push({ ...newPolicy });
  alert("Policy created successfully!");
  // Reset form
  newPolicy.policyName = "";
  newPolicy.policyType = "";
  newPolicy.coverageAmount = "";
  newPolicy.annualPremium = "";
  newPolicy.policyDuration = "";
  newPolicy.status = "ACTIVE";
  newPolicy.user = "User 3";
  showCreatePolicy.value = false;
};
const fieldsByType = {
  Life: ["Age", "Income", "Health Status", "Smoking Status"],
  Health: ["Age", "Income", "Pre Existing Conditions", "BMI"],
  Auto: ["Age", "Driving Experience", "Accident History", "Vehicle Age"],
};

const getFieldsByType = (type) => fieldsByType[type] || [];
const addCriteria = () => {
  newPolicy.eligibilityCriteria.push({
    field: "",
    operator: "Greater Than",
    value: "",
  });
};
const removeCriteria = (index) => {
  // guard just in case
  if (index >= 0 && index < newPolicy.eligibilityCriteria.length) {
    newPolicy.eligibilityCriteria.splice(index, 1);
  }
};
const removePolicy = (index) => {
  if (confirm("Are you sure you want to delete this policy?")) {
    policies.splice(index, 1);
  }
};

</script>
