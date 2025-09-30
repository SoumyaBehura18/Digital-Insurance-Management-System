<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-50">
    <div class="bg-white shadow-md rounded-xl w-full max-w-lg p-8">
      <!-- Logo & Title -->
      <div class="flex items-center justify-center mb-6">
        <img src="Icon.png" alt="Shield Icon" class="w-8 h-8" />
        <h1 class="text-xl font-semibold">InsureCore</h1>
      </div>

      <!-- Heading -->
      <h2 class="text-center text-lg font-medium mb-1">
        {{ isEdit ? "Edit User Details" : "Create Account" }}
      </h2>
      <p v-if="!isEdit" class="text-center text-gray-500 mb-6">
        Fill in your details to get started
      </p>

      <!-- Form -->
      <form @submit.prevent="handleRegister" class="space-y-4">
        <!-- Name -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Name</label>
          <input v-model="name" type="text" placeholder="Enter your name"
            class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-500 outline-none" required />
        </div>

        <!-- Email -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Email</label>
          <input v-model="email" type="email" placeholder="Enter your email"
            class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-500 outline-none"
            :disabled="isEdit" required />
        </div>

        <!-- Age -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Age</label>
          <input v-model="age" type="number" placeholder="Enter your age"
            class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-500 outline-none" required />
        </div>

        <!-- Phone -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Phone</label>
          <input v-model="phone" type="tel" placeholder="Enter your phone number"
            class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-500 outline-none" required />
          <p v-if="phone && !/^[0-9]{10}$/.test(phone)" class="text-red-600 text-xs mt-1">
            Phone number must be exactly 10 digits.
          </p>
        </div>

        <!-- Address -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Address</label>
          <input v-model="address" type="text" placeholder="Enter your address"
            class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-500 outline-none" required />
        </div>

       <!-- Password (only show on Register, hide in Edit mode) -->
<div v-if="!isEdit">
  <label class="block text-sm font-medium text-gray-700 mb-1">Password</label>
  <input 
    v-model="password" 
    type="password" 
    placeholder="Enter your password"
    class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-500 outline-none"
    required
  />
  <p v-if="password && !passwordRegex.test(password)" class="text-red-600 text-xs mt-1">
    Password must be at least 8 characters, include one uppercase letter and one special character.
  </p>
</div>


        <!-- Smoking -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Do you smoke or drink?</label>
          <select v-model="smoking" class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-500 outline-none">
            <option value="">Select option</option>
            <option value="yes">Yes</option>
            <option value="no">No</option>
          </select>
        </div>

        <!-- Pre-existing Conditions -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Pre-existing Conditions</label>
          <div class="flex flex-wrap gap-4">
            <label><input type="checkbox" value="CANCER" v-model="preexisting" /> Cancer</label>
            <label><input type="checkbox" value="DIABETES" v-model="preexisting" /> Diabetes</label>
            <label><input type="checkbox" value="TB" v-model="preexisting" /> TB</label>
            <label><input type="checkbox" value="BP" v-model="preexisting" /> BP</label>
          </div>
        </div>

        <!-- Vehicle Type -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Vehicle Type</label>
          <select v-model="vehicleType" class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-500 outline-none" required>
            <option value="">Select type</option>
            <option value="NULL">No Vehicle</option>
            <option value="CAR">Car</option>
            <option value="BIKE">Bike</option>
            <option value="HEAVY_VEHICLE">Heavy Vehicle</option>
          </select>
        </div>

        <!-- Vehicle Age (only if not NONE) -->
        <div v-if="vehicleType && vehicleType !== 'NULL'">
          <label class="block text-sm font-medium text-gray-700 mb-1">Vehicle Age</label>
          <input v-model="vehicleAge" type="number" placeholder="Enter your vehicle's age"
            class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-500 outline-none" />
        </div>

        <!-- Submit Button -->
        <button type="submit" class="w-full bg-blue-600 text-white py-2 rounded-md hover:bg-blue-700 transition">
          {{ isEdit ? "Save Changes" : "Register" }}
        </button>
      </form>

      <!-- Messages -->
      <p v-if="errorMessage" class="mt-2 text-red-600 text-sm">{{ errorMessage }}</p>
      <p v-if="successMessage" class="mt-2 text-green-600 text-sm">{{ successMessage }}</p>

      <!-- Login Link -->
      <p v-if="!isEdit" class="mt-4 text-center text-gray-600">
        Already have an account?
        <a href="/login" class="text-blue-600 font-medium hover:underline">Login</a>
      </p>
    </div>
  </div>
</template>

<script setup>
import { Shield } from 'lucide-vue-next'
import { ref, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";
import { useStore } from "vuex";

const route = useRoute();
const isEdit = ref(route.query.mode === "edit");

const router = useRouter();
const store = useStore();

const name = ref("");
const email = ref("");
const age = ref("");
const phone = ref("");
const address = ref("");
const password = ref("");
const smoking = ref(""); 
const preexisting = ref([]); 
const vehicleType = ref("");
const vehicleAge = ref("");
const errorMessage = ref("");
const successMessage = ref("");

//  Password validation regex
const passwordRegex = /^(?=.*[A-Z])(?=.*[!@#$%^&*(),.?":{}|<>]).{8,}$/;

// ----------------------------
// Prefill User Data for Edit
// ----------------------------
onMounted(async () => {
  if (isEdit.value) {
    try {
      const storedUser = JSON.parse(localStorage.getItem("currentUser"));
      if (storedUser?.id) {
        const user = await store.dispatch("user/fetchUserById", storedUser.id);

        // Prefill the form
        if (user) {
          name.value = user.name || "";
          email.value = user.email || "";
          age.value = user.age || "";
          phone.value = user.phone || "";
          address.value = user.address || "";
          smoking.value = user.smokingDrinking ? "yes" : "no";
          preexisting.value = user.preexistingConditions || [];
          vehicleType.value = user.vehicleType || "NULL";
          vehicleAge.value = user.vehicleAge || 0;
        }
      }
    } catch (e) {
      console.error("Failed to fetch user details", e);
      errorMessage.value = "Could not load user details for editing.";
    }
  }
});

// ----------------------------
// Handle Register / Update
// ----------------------------
const handleRegister = async () => {
  errorMessage.value = "";
  successMessage.value = "";

  try {
    const userData = {
      name: name.value,
      email: email.value,
      age: Number(age.value),
      phone: phone.value,
      address: address.value,
      smokingDrinking: smoking.value === "yes",
      preexistingConditions: preexisting.value,
      vehicleType: vehicleType.value,
      vehicleAge: Number(vehicleAge.value) || 0,
      roleType: "USER",
      ...(password.value ? { password: password.value } : {}) // include only if provided
    };

    if (isEdit.value) {
      // 🔹 Update user
      await store.dispatch("user/updateUser", userData);
      successMessage.value = "Profile updated successfully!";
      setTimeout(() => router.push("/dashboard"), 1200);
    } else {
      // 🔹 Register new user
      await store.dispatch("user/createUser", userData);
      successMessage.value = "Registration successful! Redirecting to login...";
      setTimeout(() => router.push("/login"), 1500);
    }
  } catch (err) {
    errorMessage.value =
      err.response?.data?.message ||
      (isEdit.value ? "Update failed" : "Registration failed");
  }
};
</script>
 