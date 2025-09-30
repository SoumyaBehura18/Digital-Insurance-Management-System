<template>
  <div>
    <!-- Mobile Hamburger Button -->
    <button
      class="sm:hidden fixed top-4 left-4 z-50 p-2 bg-white rounded shadow-md"
      @click="mobileOpen = !mobileOpen"
    >
      <svg
        v-if="!mobileOpen"
        class="w-6 h-6 text-gray-800"
        fill="none"
        stroke="currentColor"
        viewBox="0 0 24 24"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
          d="M4 6h16M4 12h16M4 18h16"></path>
      </svg>
      <svg
        v-else
        class="w-6 h-6 text-gray-800"
        fill="none"
        stroke="currentColor"
        viewBox="0 0 24 24"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
          d="M6 18L18 6M6 6l12 12"></path>
      </svg>
    </button>

    <!-- Mobile Overlay -->
    <div
      v-show="mobileOpen"
      class="fixed inset-0 bg-black bg-opacity-30 z-40 sm:hidden"
      @click="mobileOpen = false"
    ></div>

    <!-- Sidebar -->
    <div
      :class="[
        'flex flex-col h-full border-r transition-all duration-300 bg-gray-100 fixed z-50 top-0 left-0 sm:relative sm:translate-x-0',
        isCollapsed ? 'w-16' : 'w-64',
        mobileOpen ? 'translate-x-0' : '-translate-x-full',
        'sm:translate-x-0'
      ]"
      class="h-screen overflow-y-auto"
    >
      <!-- Header -->
      <div class="p-4 border-b flex items-center justify-between">
        <div v-if="!isCollapsed" class="flex items-center gap-2">
          <img src="Icon.png" alt="Shield Icon" class="w-8 h-8" />
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
          <button
            v-for="item in sidebarItems"
            :key="item.id"
            @click="handleClick(item.id)"
            :class="[
              'w-full flex items-center justify-start rounded transition py-2',
              isCollapsed ? 'px-2' : 'px-4',
              currentPage === `/${item.id}`
                ? 'bg-brand-backgroundTheme text-white'
                : 'text-gray-900 hover:bg-gray-200'
            ]"
          >
            <component
              :is="item.icon"
              :class="[
                'w-4 h-4 shrink-0',
                currentPage === `/${item.id}` ? 'text-white' : 'text-gray-900'
              ]"
            />
            <span v-if="!isCollapsed" class="ml-2 truncate">{{ item.label }}</span>
          </button>
        </div>
      </nav>

      <!-- Footer -->
      <div class="p-4 border-t space-y-2">
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
  </div>
</template>

<script setup>
import { ref } from "vue";
import router from "@/router";
import { LayoutDashboard, Shield, FileText, Ticket, LogOut, ChevronLeft, ChevronRight } from "lucide-vue-next";

// Props
const props = defineProps({
  currentPage: { type: String, required: true },
  setCurrentPage: { type: Function, required: true },
  onLogout: { type: Function, required: true },
  isCollapsed: { type: Boolean, default: false },
  setIsCollapsed: { type: Function, required: true },
});

// Sidebar items
const sidebarItems = [
  { id: "dashboard", label: "Dashboard", icon: LayoutDashboard },
  { id: "policies", label: "My Policies", icon: Shield },
  { id: "claims", label: "Claims", icon: FileText },
  { id: "tickets", label: "Support Tickets", icon: Ticket },
];

// Mobile sidebar state
const mobileOpen = ref(false);

const handleLogout = () => {
  localStorage.removeItem("currentUser");
  localStorage.removeItem("userId");
  router.push("/");
};

const handleClick = (id) => {
  props.setCurrentPage(id);
  mobileOpen.value = false; // close sidebar on mobile after click
};
</script>
