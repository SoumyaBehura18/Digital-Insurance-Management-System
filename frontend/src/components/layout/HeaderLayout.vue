<template>
  <header class="bg-background border-b border-border p-4">
    <div class="flex items-center justify-between">
      <!-- <div class="flex items-center gap-4">
       Mobile menu
        <button
          @click="toggleCollapsed"
          class="lg:hidden bg-transparent p-2 rounded hover:bg-gray-200"
        >
          <Menu class="w-5 h-5" />
        </button>

      Logo (visible only on mobile)
        <div class="lg:hidden flex items-center gap-2">
          <Shield class="w-6 h-6 text-primary" />
          <span class="font-semibold">InsureCore</span>
        </div>
      </div> -->

      <!-- User Info -->
      <div class="flex items-center gap-4">
        <div class="text-right">
          <p class="font-medium">{{ props.user?.name }}</p>
          <p class="text-sm text-muted-foreground">{{ props.user?.email }}</p>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup>
// import { Menu, Shield } from "lucide-vue-next";

const props = defineProps({
  user: {
    type: Object,
    default: null,
  },
  isCollapsed: {
    type: Boolean,
    default: false,
  },
  setIsCollapsed: {
    type: Function,
    required: true,
  },
});

// const toggleCollapsed = () => {
//   props.setIsCollapsed(!props.isCollapsed);
// };
</script>
