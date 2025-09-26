<template>
  <div class="flex gap-4">
    <button @click="$emit('navigate', 'policies')" class="flex items-center gap-2 bg-brand-backgroundTheme text-white px-4 py-2 rounded">
      <Plus class="w-4 h-4" /> Purchase Policy
    </button>
    <button @click="$emit('navigate', 'claims')" class="flex items-center gap-2 border px-4 py-2 rounded">
      <FileText class="w-4 h-4" /> Raise Claim
    </button>
  </div>
</template>

<script setup>
import { Plus, FileText } from "lucide-vue-next";
</script>
