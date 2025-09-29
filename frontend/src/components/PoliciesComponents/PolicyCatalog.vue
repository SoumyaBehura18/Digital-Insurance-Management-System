<template>
  <div>
    <!-- Filter -->
    <div class="flex items-center gap-4 mb-8">
      <div class="flex items-center gap-2">
        <Funnel class="w-5 h-5 text-gray-500" />
        <select
          v-model="filter"
          class="w-32 border border-gray-200 rounded-lg bg-white px-3 py-2 text-sm 
                 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent 
                 dark:bg-gray-800 dark:border-gray-600 dark:text-white"
        >
          <option value="All">All Types</option>
          <option value="Life">Life</option>
          <option value="Health">Health</option>
          <option value="Vehicle">Vehicle</option>
        </select>
      </div>
    </div>

    <!-- Loader -->
    <div v-if="loading" class="text-center py-12 text-gray-500">
      Loading policies...
    </div>

    <!-- Error -->
    <div
      v-if="error"
      class="text-red-500 text-center py-8 bg-red-50 rounded-lg border border-red-200"
    >
      {{ error }}
    </div>

    <!-- Policy Cards -->
    <div v-if="filteredPolicies.length === 0" class="text-center py-12 text-gray-500">
      No current policies available.
    </div>
    <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      <div
        v-for="(policy, index) in filteredPolicies"
        :key="index"
        class="relative rounded-2xl p-6 shadow-sm border hover:shadow-lg 
               transition-all duration-300 hover:-translate-y-1 overflow-hidden"
        :class="borderBgMap[getType(policy)]"
      >
        <!-- Header -->
        <div class="flex items-start justify-between mb-6">
          <div class="flex-1">
            <h2 class="text-xl font-semibold text-gray-900 mb-3 leading-tight">
              {{ policy.policyName }}
            </h2>
            <span
              class="inline-block px-3 py-1 rounded-full text-sm font-medium"
              :class="chipMap[getType(policy)]"
            >
              {{ getType(policy) }}
            </span>
          </div>

          <!-- Correct Icon -->
          <div class="p-3 rounded-full" :class="iconBgMap[getType(policy)]">
            <component
              :is="iconMap[getType(policy)]"
              class="w-6 h-6"
              :class="iconColorMap[getType(policy)]"
            />
          </div>
        </div>

        <!-- Policy Details -->
        <div class="space-y-4 mb-6">
          <div class="flex items-center justify-between">
            <span class="text-sm font-medium text-gray-700">Coverage</span>
            <span class="font-bold text-lg text-gray-900">
              Rs.{{ formatAmount(policy.coverage) }}
            </span>
          </div>
          <div class="flex items-center justify-between">
            <span class="text-sm font-medium text-gray-700">Premium</span>
            <span class="font-bold text-lg text-gray-900">
              Rs.{{ policy.premiumRate }}/year
            </span>
          </div>
          <div class="flex items-center justify-between">
            <span class="text-sm font-medium text-gray-700">Duration</span>
            <span class="font-bold text-lg text-gray-900">
              {{ formatDuration(policy.duration) }}
            </span>
          </div>
        </div>

        <!-- Purchase Button -->
        <button
          class="w-full text-white py-3 px-4 rounded-xl font-medium transition-all duration-300 
                 hover:shadow-lg hover:-translate-y-0.5 flex items-center justify-center gap-2"
          :class="btnMap[getType(policy)]"
          @click="openPurchaseModal(policy)"
        >
          <FileTextIcon class="w-4 h-4" />
          Purchase Policy
        </button>
      </div>
    </div>

    <!-- Purchase Modal -->
    <PurchaseModal
      v-if="selectedPolicy"
      :policy="selectedPolicy"
      @close="selectedPolicy = null"
      @purchase="handlePurchase"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from "vue";
import { useStore } from "vuex";
import {
  Funnel,
  Heart,
  Shield,
  Car,
  FileText,
  Calendar,
} from "lucide-vue-next";
import PurchaseModal from "./PurchaseModal.vue";

const HeartIcon = Heart;
const ShieldIcon = Shield;
const CarIcon = Car;
const FileTextIcon = FileText;
const CalendarIcon = Calendar;

const store = useStore();
const filter = ref("All");
const selectedPolicy = ref(null);

const storedUser = localStorage.getItem("currentUser");
const user = storedUser ? JSON.parse(storedUser) : null;

const policyRequest = ref({
  smokingDrinking: user?.smokingDrinking || false,
  vehicleType: user?.vehicleType || "CAR",
  vehicleAge: user?.vehicleAge || 0,
  preexistingConditions: user?.preexistingConditions || [],
});

const loading = computed(() => store.getters["policies/isLoading"]);
const error = computed(() => store.getters["policies/getError"]);

const filteredPolicies = computed(() => {
  switch (filter.value) {
    case "All":
      return store.getters["policies/getAllPolicies"];
    case "Life":
      return store.getters["policies/getLifePolicies"];
    case "Health":
      return store.getters["policies/getHealthPolicies"];
    case "Vehicle":
      return store.getters["policies/getVehiclePolicies"];
    default:
      return [];
  }
});

// --- Helpers ---
const getType = (policy) => (policy?.policyType || "").toUpperCase();

const iconMap = {
  HEALTH: HeartIcon,
  LIFE: ShieldIcon,
  VEHICLE: CarIcon,
};

const borderBgMap = {
  HEALTH: "border-green-500 bg-green-50",
  LIFE: "border-pink-500 bg-pink-50",
  VEHICLE: "border-blue-500 bg-blue-50",
};

const chipMap = {
  HEALTH: "bg-green-200 text-green-800",
  LIFE: "bg-pink-200 text-pink-800",
  VEHICLE: "bg-blue-200 text-blue-800",
};

const iconBgMap = {
  HEALTH: "bg-green-200",
  LIFE: "bg-pink-200",
  VEHICLE: "bg-blue-200",
};

const iconColorMap = {
  HEALTH: "text-green-600",
  LIFE: "text-pink-600",
  VEHICLE: "text-blue-600",
};

const btnMap = {
  HEALTH: "bg-green-500 hover:bg-green-600",
  LIFE: "bg-pink-500 hover:bg-pink-600",
  VEHICLE: "bg-blue-500 hover:bg-blue-600",
};

function formatAmount(amount) {
  if (typeof amount === "string") return amount;
  return new Intl.NumberFormat().format(amount);
}

function formatDuration(duration) {
  if (duration >= 12) {
    const years = Math.floor(duration / 12);
    return years === 1 ? "1 year" : `${years} years`;
  }
  return `${duration} months`;
}

async function fetchPoliciesByFilter(selectedFilter) {
  if (!user) return;

  store.commit("policies/SET_ALL_POLICIES", []);
  store.commit("policies/SET_LIFE_POLICIES", []);
  store.commit("policies/SET_HEALTH_POLICIES", []);
  store.commit("policies/SET_VEHICLE_POLICIES", []);

  switch (selectedFilter) {
    case "All":
      await store.dispatch("policies/fetchAllPolicies", policyRequest.value);
      break;
    case "Life":
      await store.dispatch("policies/fetchLifePolicies", policyRequest.value);
      break;
    case "Health":
      await store.dispatch("policies/fetchHealthPolicies", policyRequest.value);
      break;
    case "Vehicle":
      await store.dispatch("policies/fetchVehiclePolicies", policyRequest.value);
      break;
  }
}

function openPurchaseModal(policy) {
  selectedPolicy.value = policy;
}

function handlePurchase(policy) {
  console.log("Purchased Policy:", policy);
  selectedPolicy.value = null;
}

onMounted(async () => {
  await fetchPoliciesByFilter("All");
});

watch(filter, async (newFilter) => {
  await fetchPoliciesByFilter(newFilter);
});
</script>
