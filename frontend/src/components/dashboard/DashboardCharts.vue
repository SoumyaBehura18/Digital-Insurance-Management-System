<template>
  <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
    <!-- Policy Distribution -->
    <div class="bg-white rounded-xl shadow-md p-6 hover:shadow-lg transition">
      <h2 class="text-lg font-semibold text-gray-800 mb-4">
        Policy Distribution
      </h2>
      <PolicyDistributionChart :policies="policies" />
    </div>

    <!-- Premium Payments -->
    <div class="bg-white rounded-xl shadow-md p-6 hover:shadow-lg transition">
      <h2 class="text-lg font-semibold text-gray-800 mb-4">
        Premium Payments
      </h2>
      <PremiumPaymentsChart :policies="policies" />
    </div>

    <!-- Claims Status -->
    <div class="bg-white rounded-xl shadow-md p-6 hover:shadow-lg transition">
      <h2 class="text-lg font-semibold text-gray-800 mb-4">
        Claims Status
      </h2>
      <ClaimsStatusChart :claims="claims" />
    </div>

    <!-- Coverage by Policy -->
    <div class="bg-white rounded-xl shadow-md p-6 hover:shadow-lg transition">
      <h2 class="text-lg font-semibold text-gray-800 mb-4">
        Coverage by Policy
      </h2>
      <CoverageByPolicyChart
        :user-policies="policies"
        :all-policies="allPolicies"
      />
    </div>
  </div>
</template>

<script setup>
import PolicyDistributionChart from "./PolicyDistributionChart.vue";
import PremiumPaymentsChart from "./PremiumChartPayment.vue";
import ClaimsStatusChart from "./ClaimStatusChart.vue";
import CoverageByPolicyChart from "./CoverageByPolicyChart.vue";

const props = defineProps({
  policies: Array,
  claims: Array,
  allPolicies: Array,
});
</script>
