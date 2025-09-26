<template>
  <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
    <PolicyDistributionChart :policies="policies" />
    <PremiumPaymentsChart :policies="policies" />
    <ClaimsStatusChart :claims="claims" />
    <CoverageByPolicyChart :user-policies="policies" :all-policies="allPolicies" />
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
  allPolicies: Array
});
</script>
