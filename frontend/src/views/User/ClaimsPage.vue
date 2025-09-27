<template>
  <div class="flex flex-col h-screen">
    <!-- Header -->
    <HeaderLayout
      :user="user"
      :isCollapsed="isCollapsed"
      :setIsCollapsed="setIsCollapsed"
      class="w-full"
    />
 
    <!-- Page Content -->
    <main class="flex-1 p-6 overflow-auto">
      <template v-if="route.path === '/claims'">
        <ClaimList />
      </template>
      <template v-else-if="route.path === '/submit-claim'">
        <SubmitClaim />
      </template>
    </main>
  </div>
</template>
 
<script setup>
import HeaderLayout from "@/components/layout/HeaderLayout.vue";
import ClaimList from "@/components/ClaimsManagement/ClaimList.vue";
import SubmitClaim from "@/components/ClaimsManagement/SubmitClaim.vue";
import { ref } from "vue";
import { useRoute } from "vue-router";
 
const route = useRoute();
 
const isCollapsed = ref(true);
 
const setIsCollapsed = (val) => {
  isCollapsed.value = val;
};
 
const user = ref({
  name: "John Doe",
  email: "john.doe@example.com",
});
</script>
 
 