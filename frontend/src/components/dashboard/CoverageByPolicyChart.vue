<template>
  <div class="bg-white rounded-lg shadow p-4">
    <h2 class="font-semibold mb-2">Coverage by Policy</h2>
    <div id="coverageBar" class="h-64"></div>
  </div>
</template>

<script setup>
import { watchEffect } from "vue";
import ApexCharts from "apexcharts";

const props = defineProps({
  userPolicies: Array, // active or renewed user policies
  allPolicies: Array   // base policies with coverage amounts
});

// Prepare chart data
const data = () => {
  const labels = [];
  const series = [];

  props.userPolicies.forEach(userPolicy => {
    const basePolicy = props.allPolicies.find(bp => bp.policyId === userPolicy.policyId);
    if (!basePolicy) return; // skip if base policy not found

    labels.push(userPolicy.policyName);         // show the user policy name
    series.push(basePolicy.coverage || 0);   // coverage amount from base policy
  });

  return { labels, series };
};

watchEffect(() => {
  const { labels, series } = data();
  const el = document.querySelector("#coverageBar");

  if (!labels.length || !el) return;

  // Destroy previous chart if exists
  if (el.__apexcharts__) el.__apexcharts__.destroy();

  el.__apexcharts__ = new ApexCharts(el, {
    chart: { type: "bar", height: 250 },
    series: [{ name: "Coverage", data: series }],
    xaxis: { categories: labels },
    colors: ["#6C63FF"],
    dataLabels: { enabled: true, formatter: val => `₹${val.toLocaleString()}` }
  });

  el.__apexcharts__.render();
});
</script>
