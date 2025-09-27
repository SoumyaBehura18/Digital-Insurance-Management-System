<template>
  <div class="bg-white rounded-lg shadow p-4">
    <h2 class="font-semibold mb-2">Claims Status</h2>
    <div id="claimsBar" class="h-64"></div>
  </div>
</template>

<script setup>
import { watchEffect } from "vue";
import ApexCharts from "apexcharts";

const props = defineProps({ claims: Array });

const data = () => {
  const counts = { PENDING: 0, APPROVED: 0, REJECTED: 0 };
  props.claims.forEach(c => counts[c.status] = (counts[c.status] || 0) + 1);
  return Object.entries(counts).map(([status, count]) => ({ status, count }));
};

watchEffect(() => {
  const chartData = data();
  if (chartData.length && document.querySelector("#claimsBar")) {
    new ApexCharts(document.querySelector("#claimsBar"), {
      chart: { type: "bar", height: 250 },
      series: [{ data: chartData.map(d => d.count) }],
      xaxis: { categories: chartData.map(d => d.status) },
      colors: ["#FFA500", "#6C63FF", "#EF4444"]
    }).render();
  }
});
</script>
