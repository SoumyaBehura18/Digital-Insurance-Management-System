<template>
  <div
    class="min-h-screen flex items-center justify-center bg-gray-100 text-center"
  >
    <div>
      <div class="grid items-center justify-center m-5">
        <h1 class="text-9xl font-bold text-indigo-600">404</h1>
        <p class="text-xl mt-4 text-gray-700">Page Not Found</p>
      </div>
      <div class="w-full flex justify-center mt-6">
        <BaseButton variant="primary" @click="this.$router.push('/dashboard')">
          Go Back Home</BaseButton
        >
      </div>
    </div>
  </div>
</template>

<script setup>
import BaseButton from "@/components/BaseButton.vue";
</script>
