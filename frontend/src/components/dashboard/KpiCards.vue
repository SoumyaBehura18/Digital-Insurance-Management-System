<template>
  <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
    <KpiCard
      title="Active Policies"
      :value="activePolicies.length"
      icon="Shield"
      color="green"
    />
    <KpiCard
      title="Total Claims"
      :value="claims.length"
      icon="FileText"
      color="blue"
    />
    <KpiCard
      title="Pending Tickets"
      :value="pendingTickets.length"
      icon="Ticket"
      color="purple"
    />
    <KpiCard
      title="Total Coverage"
      :value="'Rs.' + totalCoverage.toLocaleString()"
      icon="TrendingUp"
      color="yellow"
    />
  </div>
</template>


<script setup>
import KpiCard from "./KpiCard.vue";

const props = defineProps({
  activePolicies: Array,
  claims: Array,
  pendingTickets: Array,
  totalCoverage: Number
});
</script>
