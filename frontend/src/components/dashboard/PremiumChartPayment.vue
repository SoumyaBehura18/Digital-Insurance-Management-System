<template>
  <div class="bg-white rounded-lg shadow p-4">
    <h2 class="font-semibold mb-2">Premium Payments</h2>
    <div id="premiumLine" class="h-64"></div>
  </div>
</template>

<script setup>
import { watchEffect } from "vue";
import ApexCharts from "apexcharts";

const props = defineProps({ policies: Array });

const data = () => props.policies.map(p => ({
  month: new Date(p.startDate).toLocaleString("default", { month: "short" }),
  amount: p.premiumPaid || 0
}));

watchEffect(() => {
  const chartData = data();
  if (chartData.length && document.querySelector("#premiumLine")) {
    new ApexCharts(document.querySelector("#premiumLine"), {
      chart: { type: "line", height: 250 },
      series: [{ name: "Premium", data: chartData.map(d => d.amount) }],
      xaxis: { categories: chartData.map(d => d.month) },
      stroke: { curve: "smooth", width: 3 },
      colors: ["#6C63FF"]
    }).render();
  }
});
</script>
