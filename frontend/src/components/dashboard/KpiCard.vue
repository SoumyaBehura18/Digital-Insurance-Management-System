<template>
  <div
    class="rounded-xl shadow-md p-6 flex justify-between items-center transition transform hover:-translate-y-1 hover:shadow-lg"
    :class="bgMap[color]"
  >
    <!-- Text Section -->
    <div>
      <p class="text-sm text-gray-600 font-medium tracking-wide">
        {{ title }}
      </p>
      <p class="text-3xl font-extrabold text-gray-900 mt-1">
        {{ value }}
      </p>
    </div>

    <!-- Icon Section -->
    <div
      class="flex items-center justify-center w-12 h-12 rounded-full"
      :class="iconBgMap[color]"
    >
      <component :is="iconComponent" class="w-6 h-6" :class="iconColorMap[color]" />
    </div>
  </div>
</template>

<script setup>
import { Shield, FileText, Ticket, TrendingUp } from "lucide-vue-next";

const icons = { Shield, FileText, Ticket, TrendingUp };

const props = defineProps({
  title: String,
  value: [String, Number],
  icon: String,
  color: {
    type: String,
    default: "blue", // fallback
  },
});

const iconComponent = icons[props.icon];

// Color maps
const bgMap = {
  green: "bg-green-50",
  blue: "bg-blue-50",
  purple: "bg-purple-50",
  yellow: "bg-yellow-50",
};

const iconBgMap = {
  green: "bg-green-100",
  blue: "bg-blue-100",
  purple: "bg-purple-100",
  yellow: "bg-yellow-100",
};

const iconColorMap = {
  green: "text-green-600",
  blue: "text-blue-600",
  purple: "text-purple-600",
  yellow: "text-yellow-600",
};
</script>
