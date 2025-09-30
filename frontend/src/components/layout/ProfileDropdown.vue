<template>
  <div class="relative" ref="root">
    <!-- Button -->
    <button
      @click="toggle"
      class="flex items-center gap-2 p-2 hover:bg-gray-50 rounded-full focus:outline-none"
      aria-haspopup="true"
      :aria-expanded="open.toString()"
    >
      <!-- Avatar only (no duplicate name/email) -->
      <div
        class="w-9 h-9 rounded-full bg-gray-200 flex items-center justify-center text-sm text-gray-600"
      >
        {{ avatarInitials }}
      </div>
    </button>

    <!-- Dropdown -->
    <transition name="fade">
      <div
        v-if="open"
        class="absolute right-0 mt-2 w-44 bg-white border rounded-md shadow-lg z-50 overflow-hidden"
        role="menu"
      >
        <!-- Optional: show user info at top -->
        <div class="px-4 py-2 border-b text-sm text-gray-700">
          <p class="font-medium">{{ userName }}</p>
          <p class="text-xs text-gray-500">{{ userEmail }}</p>
        </div>

        <button
          class="w-full text-left px-4 py-2 hover:bg-gray-50"
          @click="handleEdit"
          role="menuitem"
        >
          Edit Profile
        </button>
        <button
          class="w-full text-left px-4 py-2 hover:bg-gray-50 text-red-600"
          @click="handleLogout"
          role="menuitem"
        >
          Logout
        </button>
      </div>
    </transition>
  </div>
</template>


<script setup>
import { ref, computed, onMounted, onBeforeUnmount } from "vue";
import { useRouter } from "vue-router";
import { useStore } from "vuex";

const router = useRouter();
const store = useStore();

const root = ref(null);
const open = ref(false);

//  Get user from store OR localStorage
const user = computed(() => {
  return (
    store.state.user?.currentUser ||
    store.state.auth?.user ||
    JSON.parse(localStorage.getItem("currentUser")) ||
    {}
  );
});

const userName = computed(() => user.value.name || "User");
const userEmail = computed(() => user.value.email || "user@example.com");

const avatarInitials = computed(() => {
  const name = user.value.name || userEmail.value;
  return (
    (name.split(" ").map((s) => s[0]).slice(0, 2).join("") || "?").toUpperCase()
  );
});

function toggle() {
  open.value = !open.value;
}
function close() {
  open.value = false;
}

function handleEdit() {
  close();
  router.push({ name: "Register", query: { mode: "edit" } });
}

async function handleLogout() {
  close();

  try {
    await Promise.allSettled([
      store.dispatch?.("auth/logout"),
      store.dispatch?.("user/logout"),
    ]);
  } catch (e) {
    // ignore logout errors
    console.warn("Logout error (ignored):", e);
  }

  try {
    localStorage.removeItem("token");
    localStorage.removeItem("currentUser");
  } catch (e) {
    // ignore storage errors
    console.warn("LocalStorage clear error (ignored):", e);
  }

  try {
    if (store.commit) {
      store.commit("auth/CLEAR_AUTH", null, { root: true });
      store.commit("user/CLEAR_USER", null, { root: true });
    }
  } catch (e) {
    // ignore commit errors
    console.warn("Store commit error (ignored):", e);
  }

  router.push("/login");
}

function onDocumentClick(e) {
  if (!root.value) return;
  if (!root.value.contains(e.target)) close();
}
function onKeyDown(e) {
  if (e.key === "Escape") close();
}

onMounted(() => {
  document.addEventListener("click", onDocumentClick);
  document.addEventListener("keydown", onKeyDown);
});
onBeforeUnmount(() => {
  document.removeEventListener("click", onDocumentClick);
  document.removeEventListener("keydown", onKeyDown);
});
</script>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.15s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
button:focus {
  outline: 2px solid rgba(59, 130, 246, 0.25);
}
</style>
