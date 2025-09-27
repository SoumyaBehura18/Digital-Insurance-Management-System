<template>
  <div class="bg-white rounded-lg shadow p-4">
    <h2 class="font-semibold mb-2">Policy Distribution</h2>
    <h3 class="mb-2 text-brand-textTheme">Distribution of your insurance policies</h3>
    <div id="policyPie" class="h-64"></div>
  </div>
</template>

<script setup>
import { watchEffect } from "vue";
import ApexCharts from "apexcharts";

const props = defineProps({ policies: Array });

const data = () => {
  const counts = {};
  props.policies.forEach(p => counts[p.policyType] = (counts[p.policyType] || 0) + 1);
  return Object.entries(counts).map(([name, value]) => ({ name, value }));
};

watchEffect(() => {
  const chartData = data();
  if (chartData.length && document.querySelector("#policyPie")) {
    new ApexCharts(document.querySelector("#policyPie"), {
      chart: { type: "pie", height: 250 },
      labels: chartData.map(d => d.name),
      series: chartData.map(d => d.value),
      colors: ["#6C63FF", "#8B7EFF", "#A599FF"]
    }).render();
  }
});
</script>
