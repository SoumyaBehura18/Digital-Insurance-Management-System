<template>
  <div class="flex flex-col h-screen">
    <!-- Header -->
    <HeaderLayout
      :user="user"
      :isCollapsed="isCollapsed"
      :setIsCollapsed="setIsCollapsed"
      class="w-full"
    />

    <div class="space-y-6 p-6">
      <!-- Welcome Section -->
      <div>
        <h1>Welcome back, {{ user?.name }}!</h1>
        <p class="text-brand-textTheme">Here's your insurance overview</p>
      </div>

      <!-- KPI Cards -->
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <div class="bg-white rounded-lg shadow p-6 flex justify-between items-center">
          <div>
            <p class="text-sm text-brand-textTheme">Active Policies</p>
            <p class="text-2xl font-bold">{{ activePolicies.length }}</p>
          </div>
          <Shield class="w-8 h-8 text-brand-backgroundTheme"/>
        </div>

        <div class="bg-white rounded-lg shadow p-6 flex justify-between items-center">
          <div>
            <p class="text-sm text-brand-textTheme">Total Claims</p>
            <p class="text-2xl font-bold">{{ claims.length }}</p>
          </div>
          <FileText class="w-8 h-8 text-brand-backgroundTheme"/>
        </div>

        <div class="bg-white rounded-lg shadow p-6 flex justify-between items-center">
          <div>
            <p class="text-sm text-brand-textTheme">Pending Tickets</p>
            <p class="text-2xl font-bold">{{ pendingTickets.length }}</p>
          </div>
          <Ticket class="w-8 h-8 text-brand-backgroundTheme"/>
        </div>

        <div class="bg-white rounded-lg shadow p-6 flex justify-between items-center">
          <div>
            <p class="text-sm text-brand-textTheme">Total Coverage</p>
            <p class="text-2xl font-bold">
              ${{ totalCoverage.toLocaleString() }}
            </p>
          </div>
          <TrendingUp class="w-8 h-8 text-brand-backgroundTheme"/>
        </div>
      </div>

      <!-- Charts Section -->
      <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <!-- Policy Distribution Pie Chart -->
        <div class="bg-white rounded-lg shadow p-4">
          <h2 class="font-semibold mb-2">Policy Distribution</h2>
          <h3 class="mb-2 text-brand-textTheme">Distribution of your insurance policy</h3>
          <div id="policyPie" class="h-64"></div>
        </div>

        <!-- Premium Payments Line Chart -->
        <div class="bg-white rounded-lg shadow p-4">
          <h2 class="font-semibold mb-2">Premium Payments</h2>
          <div id="premiumLine" class="h-64"></div>
        </div>

        <!-- Claims Status Bar Chart -->
        <div class="bg-white rounded-lg shadow p-4">
          <h2 class="font-semibold mb-2">Claims Status</h2>
          <div id="claimsBar" class="h-64"></div>
        </div>

        <!-- Coverage by Policy Type -->
        <div class="bg-white rounded-lg shadow p-4">
          <h2 class="font-semibold mb-2">Coverage by Policy Type</h2>
          <div id="coverageBar" class="h-64"></div>
        </div>
      </div>

      <!-- Quick Actions -->
      <div class="flex gap-4">
        <button
          @click="goToPage('policies')"
          class="flex items-center gap-2 bg-brand-backgroundTheme text-white px-4 py-2 rounded"
        >
          <Plus class="w-4 h-4"/>
          Purchase Policy
        </button>
        <button
          @click="goToPage('claims')"
          class="flex items-center gap-2 border px-4 py-2 rounded"
        >
          <FileText class="w-4 h-4"/>
          Raise Claim
        </button>
      </div>

      <!-- Recent Activity -->
      <div class="bg-white rounded-lg shadow p-4">
        <h2 class="font-semibold mb-4">Recent Activity</h2>
        <div class="space-y-4">
          <div
            v-for="policy in userPolicies.slice(0, 3)"
            :key="policy.id"
            class="flex items-center gap-4 p-4 border rounded-lg"
          >
            <Shield class="w-6 h-6 text-brand-backgroundTheme"/>
            <div class="flex-1">
              <p class="font-medium">{{ policy.name }}</p>
              <p class="text-sm text-brand-textTheme">
                Started on {{ new Date(policy.startDate).toLocaleDateString() }}
              </p>
            </div>
            <span
              :class="[
                'px-2 py-1 rounded text-sm font-medium',
                policy.status === 'ACTIVE'
                  ? 'bg-brand-backgroundTheme text-white'
                  : 'bg-gray-200 text-gray-700'
              ]"
            >
              {{ policy.status }}
            </span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import HeaderLayout from "../components/layout/HeaderLayout.vue";
import { ref, onMounted, watchEffect} from "vue";
import ApexCharts from "apexcharts";
import { Shield, FileText, Ticket, TrendingUp, Plus } from "lucide-vue-next";
import { useRouter } from "vue-router";

const router = useRouter();
const goToPage = (page) => router.push(`/${page}`);

const isCollapsed = ref(true);
const setIsCollapsed = (val) => (isCollapsed.value = val);

const user = ref({
  name: "John Doe",
  email: "john.doe@example.com",
});

// Props
const props = defineProps({
  userPolicies: { type: Array, default: () => [] },
  claims: { type: Array, default: () => [] },
  tickets: { type: Array, default: () => [] },
});

// Reactive data
const activePolicies = ref([]);
const pendingTickets = ref([]);
const totalCoverage = ref(0);
const policyDistributionData = ref([]);
const premiumData = ref([]);
const claimsStatusData = ref([]);
const coverageData = ref([]);

const generateData = () => {
  // Hardcoded Active Policies
  activePolicies.value = [
    { id: 1, name: "Life Protect", type: "Life", status: "ACTIVE", coverageAmount: 500000 },
    { id: 2, name: "Health Shield", type: "Health", status: "ACTIVE", coverageAmount: 200000 },
    { id: 3, name: "Car Secure", type: "Vehicle", status: "ACTIVE", coverageAmount: 150000 },
  ];

  // Hardcoded Pending Tickets
  pendingTickets.value = [
    { id: 1, title: "Ticket 1", status: "Open" },
    { id: 2, title: "Ticket 2", status: "Open" },
  ];

  // Hardcoded Total Coverage
  totalCoverage.value = activePolicies.value.reduce((sum, p) => sum + p.coverageAmount, 0);

  // Hardcoded Policy Distribution
  policyDistributionData.value = [
    { name: "Life", value: 1 },
    { name: "Health", value: 1 },
    { name: "Vehicle", value: 1 },
  ];

  // Hardcoded Premium Data
  premiumData.value = [
    { month: "Jan", amount: 2400 },
    { month: "Feb", amount: 2400 },
    { month: "Mar", amount: 3600 },
    { month: "Apr", amount: 3600 },
    { month: "May", amount: 3600 },
    { month: "Jun", amount: 4400 },
    { month: "Jul", amount: 4400 },
    { month: "Aug", amount: 4400 },
    { month: "Sep", amount: 4400 },
    { month: "Oct", amount: 4400 },
    { month: "Nov", amount: 4400 },
    { month: "Dec", amount: 4400 },
  ];

  // Hardcoded Claims Status
  claimsStatusData.value = [
    { status: "Pending", count: 2 },
    { status: "Approved", count: 1 },
    { status: "Rejected", count: 1 },
  ];

  // Hardcoded Coverage by Policy Type (in thousands)
  coverageData.value = [
    { type: "Life", coverage: 500 },
    { type: "Health", coverage: 200 },
    { type: "Vehicle", coverage: 150 },
  ];
};



onMounted(() => {
  generateData(); // populate all data first
});

watchEffect(() => {
  console.log(policyDistributionData.value.length)
  // Policy Pie
  if (policyDistributionData.value.length && document.querySelector("#policyPie")) {
    new ApexCharts(document.querySelector("#policyPie"), {
      chart: { type: "pie", height: 250 },
      labels: policyDistributionData.value.map(d => d.name),
      series: policyDistributionData.value.map(d => d.value),
      colors: ["#6C63FF","#8B7EFF","#A599FF"]
    }).render();
  }

  console.log(premiumData.value.length)
  // Premium Line
  if (premiumData.value.length && document.querySelector("#premiumLine")) {
    new ApexCharts(document.querySelector("#premiumLine"), {
      chart: { type: "line", height: 250 },
      series: [{ name: "Premium", data: premiumData.value.map(d => d.amount) }],
      xaxis: { categories: premiumData.value.map(d => d.month) },
      stroke: { curve: 'smooth', width: 3 },
      colors: ["#6C63FF"]
    }).render();
  }

  // Claims Bar
  if (claimsStatusData.value.length && document.querySelector("#claimsBar")) {
    new ApexCharts(document.querySelector("#claimsBar"), {
      chart: { type: "bar", height: 250 },
      series: [{ data: claimsStatusData.value.map(d => d.count) }],
      xaxis: { categories: claimsStatusData.value.map(d => d.status) },
      colors: ["#FFA500","#6C63FF","#EF4444"]
    }).render();
  }

  // Coverage Bar
  if (coverageData.value.length && document.querySelector("#coverageBar")) {
    new ApexCharts(document.querySelector("#coverageBar"), {
      chart: { type: "bar", height: 250 },
      series: [{ data: coverageData.value.map(d => d.coverage) }],
      xaxis: { categories: coverageData.value.map(d => d.type) },
      colors: ["#6C63FF","#8B7EFF","#A599FF"]
    }).render();
  }
});
</script>

