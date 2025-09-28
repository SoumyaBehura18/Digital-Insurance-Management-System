<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-50">
    <div class="bg-white shadow-md rounded-xl w-full max-w-lg p-8">
      <!-- Logo & Title -->
      <div class="flex items-center justify-center mb-6">
        <Shield class="w-8 h-8 text-blue-600 rounded-full mr-2" />
        <h1 class="text-xl font-semibold">InsureCore</h1>
      </div>

      <!-- Heading -->
      <h2 class="text-center text-lg font-medium mb-1">Create Account</h2>
      <p class="text-center text-gray-500 mb-6">Fill in your details to get started</p>

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
            class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-500 outline-none" required />
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

        <!-- Password -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Password</label>
          <input v-model="password" type="password" placeholder="Enter your password"
            class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-500 outline-none" required />
          <!-- Inline error -->
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
          <select v-model="vehicleType" class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-500 outline-none">
            <option value="">Select type</option>
            <option value="NONE">No Vehicle</option>
            <option value="CAR">Car</option>
            <option value="BIKE">Bike</option>
            <option value="HEAVY_VEHICLE">Heavy Vehicle</option>
          </select>
        </div>

        <!-- Vehicle Age (only if not NONE) -->
        <div v-if="vehicleType && vehicleType !== 'NONE'">
          <label class="block text-sm font-medium text-gray-700 mb-1">Vehicle Age</label>
          <input v-model="vehicleAge" type="number" placeholder="Enter your vehicle's age"
            class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-500 outline-none" />
        </div>

        <!-- Register Button -->
        <button type="submit" class="w-full bg-blue-600 text-white py-2 rounded-md hover:bg-blue-700 transition">
          Register
        </button>
      </form>

      <!-- Messages -->
      <p v-if="errorMessage" class="mt-2 text-red-600 text-sm">{{ errorMessage }}</p>
      <p v-if="successMessage" class="mt-2 text-green-600 text-sm">{{ successMessage }}</p>

      <!-- Login Link -->
      <p class="mt-4 text-center text-gray-600">
        Already have an account?
        <a href="/login" class="text-blue-600 font-medium hover:underline">Login</a>
      </p>
    </div>
  </div>
</template>

<script setup>
import { Shield } from 'lucide-vue-next'
import { ref } from "vue";
import { useRouter } from "vue-router";
import { useStore } from "vuex";

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

// ✅ Password validation regex
const passwordRegex = /^(?=.*[A-Z])(?=.*[!@#$%^&*(),.?":{}|<>]).{8,}$/;

const handleRegister = async () => {
  // Validate password
  if (!passwordRegex.test(password.value)) {
    errorMessage.value = "Password must be at least 8 characters, include one uppercase letter and one special character.";
    return;
  }

  // Validate phone number
  if (!/^\d{10}$/.test(phone.value)) {
    errorMessage.value = "Phone number must be exactly 10 digits.";
    return;
  }

  // Vehicle logic
  let vehicleTypeValue = vehicleType.value === "NONE" ? null : vehicleType.value;
  let vehicleAgeValue = vehicleType.value === "NONE" ? 0 : Number(vehicleAge.value) || 0;

  try {
    const userData = {
      name: name.value,
      email: email.value,
      age: Number(age.value),
      phone: phone.value,
      address: address.value,
      password: password.value,
      smokingDrinking: smoking.value === "yes",
      preexistingConditions: preexisting.value,
      vehicleType: vehicleTypeValue,
      vehicleAge: vehicleType.value === "NONE" ? Number.MAX_SAFE_INTEGER : Number(vehicleAge.value) || 0,
      roleType: "USER"
    };

    await store.dispatch("user/createUser", userData);

    successMessage.value = "Registration successful! Redirecting to login...";
    errorMessage.value = "";
    setTimeout(() => {
      router.push("/login");
    }, 1500);
  } catch (err) {
    errorMessage.value = err.response?.data?.message || "Something went wrong during registration.";
  }
};
</script>
