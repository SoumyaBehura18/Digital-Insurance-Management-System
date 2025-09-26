<template>
  <div class="bg-white rounded-lg shadow p-6 flex justify-between items-center">
    <div>
      <p class="text-sm text-brand-textTheme">{{ title }}</p>
      <p class="text-2xl font-bold">{{ value }}</p>
    </div>
    <component :is="iconComponent" class="w-8 h-8 text-brand-backgroundTheme" />
  </div>
</template>

<script setup>
import { Shield, FileText, Ticket, TrendingUp } from "lucide-vue-next";

const icons = { Shield, FileText, Ticket, TrendingUp };

const props = defineProps({
  title: String,
  value: [String, Number],
  icon: String
});

const iconComponent = icons[props.icon];
</script>
