<template>
  <div class="flex flex-wrap gap-4">
    <!-- Primary Action -->
    <button
      @click="$emit('navigate', 'policies')"
      class="flex items-center gap-2 px-5 py-3 rounded-full font-medium text-white 
             bg-gradient-to-r from-purple-500 to-indigo-600 shadow-md 
             hover:shadow-lg hover:-translate-y-0.5 transition"
    >
      <span class="flex items-center justify-center w-8 h-8 rounded-full bg-white/20">
        <Plus class="w-4 h-4" />
      </span>
      Purchase Policy
    </button>

    <!-- Secondary Action -->
    <button
      @click="$emit('navigate', 'claims')"
      class="flex items-center gap-2 px-5 py-3 rounded-full font-medium border border-gray-300 
             text-gray-700 bg-white shadow-sm hover:bg-gray-100 hover:shadow-md 
             transition"
    >
      <span class="flex items-center justify-center w-8 h-8 rounded-full bg-gray-100">
        <FileText class="w-4 h-4 text-gray-600" />
      </span>
      Raise Claim
    </button>
  </div>
</template>

<script setup>
import { Plus, FileText } from "lucide-vue-next";
</script>
