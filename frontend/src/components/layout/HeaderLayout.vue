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
        <AvatarComponent :user-name="name" />
      </div>
    </div>
  </header>
</template>

<script setup>
import { computed, ref } from "vue";
import { useStore } from "vuex";
import AvatarComponent from "../AvatarComponent.vue";

const store = useStore();
const user = ref(JSON.parse(localStorage.getItem("currentUser")));
const currentUser = computed(() => store.getters["user/getCurrentUser"]);
console.log("Current User from Store:", currentUser.value);   
const email = user.value?.email;
const name = user.value?.name;
</script>
