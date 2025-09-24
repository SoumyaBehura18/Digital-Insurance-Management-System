<template>
  <button
    type="button"
    role="switch"
    :aria-checked="checked.toString()"
    class="inline-flex h-6 w-12 items-center rounded-full border border-black bg-gray-300
           transition-colors duration-200 focus:outline-none"
    :class="checked ? 'bg-brand-backgroundTheme' : 'bg-gray-300'"
    @click="toggle"
  >
    <span
      class="inline-block w-5 h-5 bg-white rounded-full transform transition-transform duration-200"
      :class="checked ? 'translate-x-6' : 'translate-x-1'"
    ></span>
  </button>
</template>

<script>
import { ref, watch } from "vue";

export default {
  name: "SwitchComponent",
  emits: ["update:modelValue"],
  props: {
    modelValue: {
      type: Boolean,
      default: false,
    },
  },
  setup(props, { emit }) {
    const checked = ref(props.modelValue);

    watch(() => props.modelValue, (newVal) => {
      checked.value = newVal;
    });

    const toggle = () => {
      checked.value = !checked.value;
      emit("update:modelValue", checked.value);
    };

    return { checked, toggle };
  },
};
</script>
