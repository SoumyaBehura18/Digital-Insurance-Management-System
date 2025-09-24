<template>
  <button
    :class="[
      'rounded-lg font-medium focus:outline-none focus:ring-2 focus:ring-offset-2 transition-colors duration-200',
      sizeClasses,
      variantClasses,
      { 'opacity-50 cursor-not-allowed': disabled },
      customClass,
    ]"
    :disabled="disabled"
    @click="$emit('click', $event)"
  >
    <slot />
  </button>
</template>

<script setup>
import { computed } from "vue";

const props = defineProps({
  variant: {
    type: String,
    default: "primary", // primary | secondary | outline | ghost
  },
  size: {
    type: String,
    default: "md", // sm | md | lg
  },
  disabled: {
    type: Boolean,
    default: false,
  },
  customClass: {
    type: [String, Array, Object],
    default: "",
  },
});

const emit = defineEmits(["click"]);

const sizeClasses = computed(() => {
  switch (props.size) {
    case "sm":
      return "px-3 py-1.5 text-sm";
    case "lg":
      return "px-6 py-3 text-base";
    default:
      return "px-4 py-2 text-sm";
  }
});

const variantClasses = computed(() => {
  switch (props.variant) {
    case "secondary":
      return "bg-gray-100 text-gray-900 hover:bg-gray-200 focus:ring-gray-500";
    case "outline":
      return "border border-gray-300 text-gray-700 bg-white hover:bg-gray-50 focus:ring-indigo-500";
    case "ghost":
      return "bg-transparent text-gray-700 hover:bg-gray-100 focus:ring-gray-500";
    case "theme":
      return "bg-theme text-white hover:bg-theme-hover";
    default: // primary
      return "bg-indigo-600 text-white hover:bg-indigo-700 focus:ring-indigo-500";
  }
});
</script>
