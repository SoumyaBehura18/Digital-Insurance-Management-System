<template>
  <div class="flex flex-col h-screen">
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
              <th class="px-4 py-3 text-left font-semibold text-gray-700">Renewal Rate</th>
              <th class="px-4 py-3 text-left font-semibold text-gray-700">Duration</th>
              <th class="px-4 py-3 text-center font-semibold text-gray-700">Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="(policy, index) in policies"
              :key="index"
              class="border-t hover:bg-gray-50"
            >
              <td class="px-4 py-3">{{ policy.name }}</td>
              <td class="px-4 py-3">
                <span class="px-2 py-1 rounded-full text-xs font-medium bg-gray-100 text-gray-700">
                  {{ policy.type }}
                </span>
              </td>
              <td class="px-4 py-3">{{ policy.coverageAmt }}</td>
              <td class="px-4 py-3">{{ policy.premiumRate }}</td>
              <td class="px-4 py-3">{{ policy.renewalRate }}</td>
              <td class="px-4 py-3">{{ policy.durationMonths }}</td>
              <td class="px-4 py-3 flex justify-center space-x-3">
                <button class="text-gray-600 hover:text-indigo-600">
                  <Pencil class="w-4 h-4" />
                </button>
                <button
                  class="text-gray-600 hover:text-red-600"
                  @click="removePolicy(policy.id, index)"
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
            <input v-model="newPolicy.policyName" type="text" placeholder="Enter policy name"
              class="w-full border rounded-lg px-3 py-2 focus:ring focus:outline-none" />
          </div>

          <!-- Policy Type -->
          <div>
            <label class="block text-gray-900 font-medium mb-1">Policy Type</label>
            <select v-model="newPolicy.policyType"
              class="w-full border rounded-lg px-3 py-2 focus:ring focus:outline-none">
              <option disabled value="">Select policy type</option>
              <option value="LIFE">Life Insurance</option>
              <option value="HEALTH">Health Insurance</option>
              <option value="VEHICLE">Vehicle Insurance</option>
            </select>
          </div>

          <!-- Coverage -->
          <div>
            <label class="block text-gray-900 font-medium mb-1">Coverage Amount ($)</label>
            <input v-model="newPolicy.coverageAmount" type="number" placeholder="Enter coverage amount"
              class="w-full border rounded-lg px-3 py-2 focus:ring focus:outline-none" />
          </div>

          <!-- Premium -->
          <div>
            <label class="block text-gray-900 font-medium mb-1">Annual Premium ($)</label>
            <input v-model="newPolicy.annualPremium" type="number" placeholder="Enter annual premium"
              class="w-full border rounded-lg px-3 py-2 focus:ring focus:outline-none" />
          </div>

          <!-- Duration -->
          <div class="col-span-2">
            <label class="block text-gray-900 font-medium mb-1">Policy Duration (Months)</label>
            <input v-model="newPolicy.policyDuration" type="number" placeholder="e.g., 12"
              class="w-full border rounded-lg px-3 py-2 focus:ring focus:outline-none" />
          </div>
        </div>

        <!-- Renewal Rate -->
        <div class="mt-4">
          <label class="block text-gray-900 font-medium mb-1">Renewal Rate ($)</label>
          <input v-model="newPolicy.renewalRate" type="number" placeholder="Enter renewal rate"
            class="w-full border rounded-lg px-3 py-2 focus:ring focus:outline-none" />
        </div>

        <!-- Eligibility Criteria -->
        <div class="mt-6">
          <label class="block text-gray-900 font-medium mb-2">Eligibility Criteria</label>

          <!-- Vehicle -->
          <div v-if="newPolicy.policyType === 'VEHICLE'" class="p-4 border rounded-lg bg-gray-50">
            <label class="block text-sm font-semibold mb-2">Vehicle Age</label>
            <input v-model="newPolicy.vehicleAge" type="number" placeholder="Enter vehicle age"
              class="w-full border rounded-lg px-3 py-2 focus:ring focus:outline-none" />
          </div>

          <!-- Health -->
          <div v-if="newPolicy.policyType === 'HEALTH'">
            <div v-for="(condition, index) in newPolicy.conditions" :key="index"
              class="p-4 border rounded-lg mb-3 bg-gray-50">
              <p class="text-sm font-semibold mb-2">Condition {{ index + 1 }}</p>

              <div class="grid grid-cols-2 gap-3">
                <!-- Condition Enum -->
                <select v-model="condition.condition" class="border rounded-lg px-2 py-2">
                  <option disabled value="">Select condition</option>
                  <option value="TB">TB</option>
                  <option value="BP">BP</option>
                  <option value="DIABETES">Diabetes</option>
                  <option value="CANCER">Cancer</option>
                </select>

                <!-- Extra Premium -->
                <input v-model="condition.extraPremium" type="number" placeholder="Extra premium"
                  class="border rounded-lg px-2 py-2" />
              </div>

              <button class="text-gray-600 hover:text-red-600" @click="removeCondition(index)">
                <Trash class="w-4 h-4" />
              </button>
            </div>

            <button type="button" @click="addCondition"
              class="text-indigo-600 hover:underline text-sm font-medium">
              + Add Condition
            </button>
          </div>

          <!-- Life -->
          <p v-if="newPolicy.policyType === 'LIFE'" class="text-gray-500 text-sm">
            No eligibility criteria for Life policies.
          </p>
        </div>

        <!-- Footer -->
        <div class="flex justify-end space-x-3 mt-6">
          <button @click="showCreatePolicy = false"
            class="border border-gray-400 px-6 py-2 rounded-lg hover:bg-gray-100">
            Cancel
          </button>
          <button @click="createPolicy"
            class="bg-indigo-600 text-white px-6 py-2 rounded-lg hover:bg-indigo-700">
            Create Policy
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from "vue";
import axios from "axios";
import { useStore } from "vuex";
import { Pencil, Trash } from "lucide-vue-next";

const showCreatePolicy = ref(false);
const policies = ref([]);

const store = useStore();
const currentUser = store.getters["user/getCurrentUser"];
const token = currentUser?.token;

const newPolicy = reactive({
  policyName: "",
  policyType: "",
  coverageAmount: "",
  annualPremium: "",
  policyDuration: "",
  renewalRate: "",
  vehicleAge: "",
  conditions: [],
});

// Add condition
const addCondition = () => {
  newPolicy.conditions.push({ condition: "", extraPremium: 0 });
};

// Remove condition
const removeCondition = (index) => {
  newPolicy.conditions.splice(index, 1);
};

// Create policy
const createPolicy = async () => {
  try {
    const token = localStorage.getItem("authToken");
    if (!token) {
      alert("No token found. Please login again.");
      return;
    }

    // Step 1: Build base policy payload
    let payload = {
      name: newPolicy.policyName,
      type: newPolicy.policyType,
      coverageAmt: newPolicy.coverageAmount,
      premiumRate: newPolicy.annualPremium,
      durationMonths: newPolicy.policyDuration,
      renewalRate: newPolicy.renewalRate,
    };

    // Step 2: Create base policy
    const response = await axios.post(
      "http://localhost:9090/api/admin/policies",
      payload,
      { headers: { Authorization: `Bearer ${token}` } }
    );

    const created = response.data; // <-- now you have policy.id

    // Step 3: Call premium API depending on type
    if (newPolicy.policyType === "LIFE") {
      await axios.post(
        `http://localhost:9090/api/admin/policies/${created.id}/life-premium`,
        {
          premiumRate: newPolicy.annualPremium,
          renewalRate: newPolicy.renewalRate,
        },
        { headers: { Authorization: `Bearer ${token}` } }
      );
    }

    if (newPolicy.policyType === "HEALTH") {
      await axios.post(
        `http://localhost:9090/api/admin/policies/${created.id}/health-premium`,
        {
          premiumRate: newPolicy.annualPremium,
          renewalRate: newPolicy.renewalRate,
          conditionPremiums: newPolicy.conditions.map((c) => ({
            condition: c.condition,
            extraPremium: c.extraPremium,
          })),
        },
        { headers: { Authorization: `Bearer ${token}` } }
      );
    }

    if (newPolicy.policyType === "VEHICLE") {
      await axios.post(
        `http://localhost:9090/api/admin/policies/${created.id}/vehicle-premium`,
        {
          premiumRate: newPolicy.annualPremium,
          renewalRate: newPolicy.renewalRate,
          vehicleAge: newPolicy.vehicleAge,
        },
        { headers: { Authorization: `Bearer ${token}` } }
      );
    }

    // Step 4: Update UI
    policies.value.push(created);
    alert("Policy created successfully!");
    resetNewPolicy();
    showCreatePolicy.value = false;
  } catch (error) {
    console.error("Error creating policy:", error);
    alert("Failed to create policy");
  }
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
  });
};

// Delete policy
const removePolicy = async (policyId, index) => {
  if (confirm("Are you sure you want to delete this policy?")) {
    try {
      const token = localStorage.getItem("authToken");
      await axios.delete(`http://localhost:9090/api/admin/policies/${policyId}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      policies.value.splice(index, 1);
    } catch (error) {
      console.error("Error deleting policy:", error);
      alert("Failed to delete policy");
    }
  }
};

// Load policies on mount
onMounted(async () => {
  try {
    const token = localStorage.getItem("authToken");
    const response = await axios.get("http://localhost:9090/api/admin/policies", {
      headers: { Authorization: `Bearer ${token}` },
    });
    policies.value = response.data;
  } catch (error) {
    console.error("Error fetching policies:", error);
  }
});
</script>
