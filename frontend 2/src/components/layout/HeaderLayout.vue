<template>
  <header class="bg-background border-b border-border p-4">
    <div class="flex items-center justify-between">
      <!-- Logo / Brand -->
      <div class="flex items-center gap-2">
        <span class="font-semibold text-lg">InsureCore</span>
      </div>

      <!-- User Info -->
      <div class="flex items-center gap-4">
        <div class="text-right">
          <p class="font-medium">{{ props.user?.name }}</p>
          <p class="text-sm text-brand-textTheme text-muted-foreground">{{ props.user?.email }}</p>
        </div>
        <AvatarComponent :user-name="props.user?.name" />
      </div>
    </div>
  </header>
</template>

<script setup>
import AvatarComponent from "../AvatarComponent.vue";

const props = defineProps({
  user: { type: Object, default: null },
  isCollapsed: { type: Boolean, default: false },
  setIsCollapsed: { type: Function, required: true },
});
</script>
