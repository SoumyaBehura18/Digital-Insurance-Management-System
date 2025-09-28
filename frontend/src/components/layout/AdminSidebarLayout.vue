<template>
  <div :class="[
    'flex flex-col h-full border-r transition-all duration-300 bg-gray-100',
    isCollapsed ? 'w-16' : 'w-64'
  ]">
    <!-- Header -->
    <div class="p-4 border-b flex items-center justify-between">
      <div v-if="!isCollapsed" class="flex items-center gap-2">
        <Shield class="w-6 h-6 text-brand-backgroundTheme" />
        <span class="font-semibold text-gray-900">InsureCore Admin</span>
      </div>
      <button class="p-2 rounded hover:bg-gray-200 text-gray-900" @click="setIsCollapsed(!isCollapsed)">
        <ChevronRight v-if="isCollapsed" class="w-4 h-4" />
        <ChevronLeft v-else class="w-4 h-4" />
      </button>
    </div>

    <!-- Navigation -->
    <nav class="flex-1 p-4 bg-gray-100">
      <div class="space-y-2">
        <button v-for="item in adminSidebarItems" :key="item.id" @click="handleClick(item.id)" :class="[
          'w-full flex items-center justify-start rounded transition py-2',
          isCollapsed ? 'px-2' : 'px-4',
          currentPage === `/admin/${item.id}`
            ? 'bg-brand-backgroundTheme text-white'
            : 'text-gray-900 hover:bg-gray-200'

        ]">
          <component :is="item.icon" :class="[
            'w-4 h-4 shrink-0',
            currentPage === `/admin/${item.id}` ? 'text-white' : 'text-gray-900'
          ]" />
          <span v-if="!isCollapsed" class="ml-2 truncate">{{ item.label }}</span>
        </button>
      </div>
    </nav>

    <!-- Footer -->
    <div class="p-4 border-t space-y-2">
      <!-- Dark Mode Toggle -->
      <div v-if="!isCollapsed" class="flex items-center justify-between">
        <div class="flex items-center gap-2">
          <Sun class="w-4 h-4 text-gray-900" />
          <span class="text-sm text-gray-900">Dark Mode</span>
        </div>
        <input type="checkbox" class="toggle" v-model="darkModeLocal" @change="setIsDarkMode(darkModeLocal)" />
      </div>

      <div v-else class="flex justify-center">
        <button class="p-2 rounded hover:bg-gray-200 text-gray-900" @click="setIsDarkMode(!isDarkMode)">
          <Sun v-if="isDarkMode" class="w-4 h-4" />
          <Moon v-else class="w-4 h-4" />
        </button>
      </div>

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
  HeadphonesIcon,
  Users,
  LogOut,
  ChevronLeft,
  ChevronRight,
  Sun,
  Moon,
} from "lucide-vue-next";

// Props
const props = defineProps({
  currentPage: { type: String, required: true },
  setCurrentPage: { type: Function, required: true },
  onLogout: { type: Function, required: true },
  isDarkMode: { type: Boolean, default: false },
  setIsDarkMode: { type: Function, required: true },
  isCollapsed: { type: Boolean, default: false },
  setIsCollapsed: { type: Function, required: true },
});

// Admin Sidebar items
const adminSidebarItems = [
  { id: "dashboard", label: "Dashboard", icon: LayoutDashboard },
  { id: "policies", label: "Manage Policies", icon: Shield },
  { id: "claims", label: "Manage Claims", icon: FileText },
  { id: "tickets", label: "Support Tickets", icon: HeadphonesIcon },
  { id: "users", label: "Users", icon: Users },
];

// Local dark mode sync
const darkModeLocal = ref(props.isDarkMode);
watch(
  () => props.isDarkMode,
  (val) => {
    darkModeLocal.value = val;
  }
);

const handleLogout = () => {
  // props.onLogout();
  // Clear all auth-related localStorage
  localStorage.removeItem('currentUser');
  localStorage.removeItem('userId');
  // Redirect to login page
  router.push('/login');
};

const handleClick = (id) => {
  props.setCurrentPage(id);
  // push to route when clicking sidebar item
  const target = `/admin/${id}`;
  if (router.currentRoute.value.path !== target) {
    router.push(target);
  }
};
</script>

<style scoped>
/* Simple toggle */
.toggle {
  width: 2rem;
  height: 1rem;
  appearance: none;
  background: #d1d5db;
  border-radius: 9999px;
  position: relative;
  cursor: pointer;
  transition: background 0.3s;
}

.toggle:checked {
  background: #4f46e5;
}

.toggle::after {
  content: '';
  width: 0.875rem;
  height: 0.875rem;
  background: white;
  border-radius: 9999px;
  position: absolute;
  top: 0.0625rem;
  left: 0.0625rem;
  transition: transform 0.3s;
}

.toggle:checked::after {
  transform: translateX(1rem);
}
</style>
