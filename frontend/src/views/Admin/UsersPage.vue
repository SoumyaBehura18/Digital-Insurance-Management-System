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
      <h1 class="text-2xl font-bold mb-6">Users Management</h1>

      <!-- Loader -->
      <div v-if="loading" class="text-center py-6 text-gray-500">
        Loading users...
      </div>

      <!-- Error -->
      <div
        v-else-if="error"
        class="text-red-600 text-center bg-red-50 py-2 rounded-lg mb-4"
      >
        {{ error }}
      </div>

      <!-- Users Table -->
      <div v-else class="bg-white shadow-md rounded-lg overflow-hidden">
        <table class="min-w-full border-collapse">
          <thead class="bg-gray-50 text-[#6c63ff]">
            <tr>
              <th class="px-4 py-3 text-left font-semibold">ID</th>
              <th class="px-4 py-3 text-left font-semibold">Name</th>
              <th class="px-4 py-3 text-left font-semibold">Email</th>
              <th class="px-4 py-3 text-left font-semibold">Role</th>
              <th class="px-4 py-3 text-left font-semibold">Phone No</th>
              <th class="px-4 py-3 text-left font-semibold">Age</th>
              <th class="px-4 py-3 text-center font-semibold">Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="(userItem, index) in users"
              :key="userItem.id"
              :class="index % 2 === 0 ? 'bg-white' : 'bg-purple-50'"
              class="hover:bg-purple-100 transition-colors"
            >
              <td class="px-4 py-3 text-gray-800">{{ userItem.id }}</td>
              <td class="px-4 py-3 text-gray-800 font-medium">
                {{ userItem.name }}
              </td>
              <td class="px-4 py-3 text-gray-700">{{ userItem.email }}</td>
              <td class="px-4 py-3">
                <span
                  :class="{
                    'bg-green-100 text-green-800': userItem.roleType === 'ADMIN',
                    'bg-blue-100 text-blue-800': userItem.roleType === 'USER',
                  }"
                  class="px-3 py-1 rounded-full text-xs font-semibold"
                >
                  {{ userItem.roleType }}
                </span>
              </td>
              <td class="px-4 py-3 text-gray-700">{{ userItem.phone }}</td>
              <td class="px-4 py-3 text-gray-700">{{ userItem.age }}</td>
              <td class="px-4 py-3 text-center">
                <button
                  class="bg-purple-500 hover:bg-purple-600 text-white text-xs font-medium px-3 py-1 rounded-full transition"
                  @click="openUpdateModal(userItem)"
                >
                  Update
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- Update Modal -->
      <div
        v-if="selectedUser"
        class="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50"
      >
        <div class="bg-white p-6 rounded-lg w-[24rem] space-y-4 shadow-xl">
          <h3 class="font-bold text-lg">Update User Role</h3>
          <p class="text-gray-600">
            Update the role for <strong>{{ selectedUser.name }}</strong
            >.
          </p>

          <select v-model="newRole" class="border rounded-lg w-full px-3 py-2">
            <option value="USER">USER</option>
            <option value="ADMIN">ADMIN</option>
          </select>

          <div class="flex justify-end gap-2 mt-4">
            <button
              class="border px-4 py-2 rounded"
              @click="selectedUser = null"
            >
              Cancel
            </button>
            <button
              class="bg-brand-backgroundTheme text-white px-4 py-2 rounded"
              :disabled="updating"
              @click="updateRole"
            >
              {{ updating ? "Updating..." : "Update Role" }}
            </button>
          </div>

          <div v-if="updateError" class="text-red-500 text-sm">
            {{ updateError }}
          </div>
          <div v-if="updateSuccess" class="text-green-500 text-sm">
            Role updated successfully!
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import HeaderLayout from "@/components/layout/HeaderLayout.vue";
import { ref, computed, onMounted } from "vue";
import { useStore } from "vuex";

const store = useStore();

// Sidebar
const isCollapsed = ref(true);
const setIsCollapsed = (val) => {
  isCollapsed.value = val;
};

// Local user for header
const user = ref({
  name: "Admin User",
  email: "admin@example.com",
});

// Computed state from Vuex
const users = computed(() => store.getters["user/getUsers"]);
const loading = computed(() => store.getters["user/isLoading"]);
const error = computed(() => store.getters["user/getError"]);

// Modal states
const selectedUser = ref(null);
const newRole = ref(null);
const updating = ref(false);
const updateError = ref(null);
const updateSuccess = ref(false);

// Open modal
function openUpdateModal(user) {
  selectedUser.value = user;
  newRole.value = user.roleType;
  updateError.value = null;
  updateSuccess.value = false;
}

// Update role API
async function updateRole() {
  if (!selectedUser.value) return;

  updating.value = true;
  updateError.value = null;
  updateSuccess.value = false;

  try {
    await store.dispatch("user/updateUserRole", {
      userId: selectedUser.value.id,
      role: newRole.value,
    });

    // Refresh user list
    await store.dispatch("user/fetchAllUsers");
    updateSuccess.value = true;

    // Close modal after short delay
    setTimeout(() => {
      selectedUser.value = null;
    }, 1000);
  } catch (err) {
    updateError.value = err.response?.data || "Failed to update role";
  } finally {
    updating.value = false;
  }
}

// Fetch all users on mount
onMounted(() => {
  store.dispatch("user/fetchAllUsers");
});
</script>

<style scoped>
table {
  border-collapse: collapse;
  width: 100%;
}
th,
td {
  border-bottom: 1px solid #e5e7eb;
}
</style>
