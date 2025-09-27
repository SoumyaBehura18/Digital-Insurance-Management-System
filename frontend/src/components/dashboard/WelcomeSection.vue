<template>
  <div>
    <h1>Welcome back, {{ user?.name }}!</h1>
    <p class="text-brand-textTheme">Here's your insurance overview</p>
  </div>
</template>

<script setup>
const props = defineProps({
  user: Object
});
</script>
