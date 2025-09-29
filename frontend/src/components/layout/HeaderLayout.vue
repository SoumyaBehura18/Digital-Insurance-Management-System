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
          <p class="font-medium">{{ name }}</p>
          <p class="text-sm text-brand-textTheme text-muted-foreground">
            {{ email }}
          </p>
        </div>
        <!-- <AvatarComponent :user-name="name" /> -->
        <ProfileDropdown />
      </div>
    </div>
  </header>
</template>

<script setup>
import { computed, ref } from "vue";
import { useStore } from "vuex";
import AvatarComponent from "../AvatarComponent.vue";
import ProfileDropdown from "./ProfileDropdown.vue"; // if you added it

const store = useStore();

// safely read and parse currentUser from localStorage
let stored = null;
if (typeof window !== "undefined") {
  stored = localStorage.getItem("currentUser");
}
const user = ref(stored ? JSON.parse(stored) : null);

// make name/email computed so they update reactively and avoid "unused import" errors
const email = computed(() => user.value?.email || "");
const name = computed(() => user.value?.name || "");
</script>
