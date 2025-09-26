<template>
  <div class="flex flex-col h-screen">
    <!-- Header -->
    <HeaderLayout
      :user="user"
      :isCollapsed="isCollapsed"
      :setIsCollapsed="setIsCollapsed"
      class="w-full"
    />

    <!-- Content -->
    <div class="space-y-6 p-6">
      <!-- Header with View Buttons -->
      <div class="flex items-center justify-between">
        <h1>{{ view === "catalog" ? "Policy Catalog" : "My Policies" }}</h1>
        <div class="flex gap-2">
          <button
            :class="
              view === 'catalog'
                ? 'bg-brand-backgroundTheme text-white px-4 py-2 rounded-xl'
                : 'border border-gray-300 px-4 py-2 rounded-xl'
            "
            @click="view = 'catalog'"
          >
            Browse Policies
          </button>
          <button
            :class="
              view === 'my-policies'
                ? 'bg-brand-backgroundTheme text-white px-4 py-2 rounded-xl'
                : 'border border-gray-300 px-4 py-2 rounded-xl'
            "
            @click="view = 'my-policies'"
          >
            My Policies
          </button>
        </div>
      </div>

      <!-- Policy Catalog -->
      <PolicyCatalog v-if="view === 'catalog'" />

      <!-- My Policies -->
      <MyPolicies v-if="view === 'my-policies'" />
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import HeaderLayout from "@/components/layout/HeaderLayout.vue";
import PolicyCatalog from "@/components/PoliciesComponents/PolicyCatalog.vue";
import MyPolicies from "@/components/PoliciesComponents/MyPolicies.vue";

// Mock user (replace with Vuex/store if needed)
const user = ref({ name: "John Doe", email: "john.doe@example.com" });

// State for header collapse
const isCollapsed = ref(false);
function setIsCollapsed(value) {
  isCollapsed.value = value;
}

// View toggle state
const view = ref("catalog");
</script>
