<template>
  <div :class="[
    'flex flex-col h-full border-r transition-all duration-300 bg-gray-100',
    isCollapsed ? 'w-16' : 'w-64'
  ]">
    <!-- Header -->
    <div class="p-4 border-b flex items-center justify-between">
      <div v-if="!isCollapsed" class="flex items-center gap-2">
        <Shield class="w-6 h-6 text-brand-backgroundTheme" />
        <span class="font-semibold text-gray-900">InsureCore</span>
      </div>
      <button class="p-2 rounded hover:bg-gray-200 text-gray-900" @click="setIsCollapsed(!isCollapsed)">
        <ChevronRight v-if="isCollapsed" class="w-4 h-4" />
        <ChevronLeft v-else class="w-4 h-4" />
      </button>
    </div>

    <!-- Navigation -->
    <nav class="flex-1 p-4 bg-gray-100">
      <div class="space-y-2">
        <button v-for="item in sidebarItems" :key="item.id" @click="handleClick(item.id)" :class="[
          'w-full flex items-center justify-start rounded transition py-2',
          isCollapsed ? 'px-2' : 'px-4',
          currentPage === `/${item.id}`
            ? 'bg-brand-backgroundTheme text-white'
            : 'text-gray-900 hover:bg-gray-200'

        ]">
          <component :is="item.icon" :class="[
            'w-4 h-4 shrink-0',
            currentPage === `/${item.id}` ? 'text-white' : 'text-gray-900'
          ]" />
          <span v-if="!isCollapsed" class="ml-2 truncate">{{ item.label }}</span>
        </button>
      </div>
    </nav>

    <!-- Footer -->
    <div class="p-4 border-t space-y-2">
      <!-- Logout -->
      <button
        @click="handleLogout()"
        :class="[
          'w-full flex items-center justify-start rounded transition-colors py-2',
          isCollapsed ? 'px-2' : 'px-4',
          'text-gray-900 hover:bg-gray-200'
        ]"
      >
        <LogOut class="w-4 h-4 shrink-0" />
        <span v-if="!isCollapsed" class="ml-2">Logout</span>
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from "vue";
import router from "@/router";
import {
  LayoutDashboard,
  Shield,
  FileText,
  Ticket,
  LogOut,
  ChevronLeft,
  ChevronRight,
} from "lucide-vue-next";

// Props
const props = defineProps({
  currentPage: { type: String, required: true },
  setCurrentPage: { type: Function, required: true },
  onLogout: { type: Function, required: true },
  isCollapsed: { type: Boolean, default: false },
  setIsCollapsed: { type: Function, required: true },
});

// User Sidebar items
const sidebarItems = [
  { id: "dashboard", label: "Dashboard", icon: LayoutDashboard },
  { id: "policies", label: "My Policies", icon: Shield },
  { id: "claims", label: "Claims", icon: FileText },
  { id: "tickets", label: "Support Tickets", icon: Ticket },
];


const handleLogout = () => {

  localStorage.removeItem('currentUser');
  localStorage.removeItem('userId');
  router.push("/");
};

const handleClick = (id) => {
  props.setCurrentPage(id);
};
</script>

<style scoped>
/* Simple toggle */

</style>
