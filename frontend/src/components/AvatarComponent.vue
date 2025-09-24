<template>
  <div
    class="w-10 h-10 bg-gray-300 rounded-full flex items-center justify-center"
  >
    <span class="text-gray-600 font-medium">{{ initials }}</span>
  </div>
</template>

<script setup>
import { computed } from "vue";

const props = defineProps({
  userName: {
    type: String,
    required: true,
  },
});

const initials = computed(() => {
  return props.userName
    .split(" ")
    .map((word) => word.charAt(0))
    .join("")
    .toUpperCase()
    .substring(0, 2);
});
</script>
