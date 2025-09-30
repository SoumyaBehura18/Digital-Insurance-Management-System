<template>
  <header class="bg-background border-b border-border p-4">
    <div class="flex items-center justify-between">
      <!-- Logo / Brand (hidden on mobile) -->
      <div class="hidden sm:flex items-center gap-2">
        <span class="font-semibold text-lg">InsureCore</span>
      </div>

      <!-- User Info -->
      <div class="flex items-center gap-4 ml-auto">
        <!-- Desktop: show name/email + dropdown -->
        <div class="hidden sm:flex items-center gap-4">
          <div class="text-right">
            <p class="font-medium truncate max-w-[150px]">{{ name }}</p>
            <p class="text-sm text-brand-textTheme text-muted-foreground truncate max-w-[150px]">
              {{ email }}
            </p>
          </div>
          <ProfileDropdown />
        </div>

        <!-- Mobile: show dropdown only -->
        <div class="sm:hidden">
          <ProfileDropdown />
        </div>
      </div>
    </div>
  </header>
</template>

<script setup>
import { computed, ref } from "vue";
import ProfileDropdown from "./ProfileDropdown.vue";

let stored = null;
if (typeof window !== "undefined") {
  stored = localStorage.getItem("currentUser");
}
const user = ref(stored ? JSON.parse(stored) : null);

const name = computed(() => user.value?.name || "");
const email = computed(() => user.value?.email || "");
</script>
