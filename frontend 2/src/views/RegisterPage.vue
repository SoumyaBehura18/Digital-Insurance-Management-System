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
          <input 
            type="text" 
            v-model="name"
            placeholder="Enter your name"
            class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-500 outline-none"
            required
          />
        </div>

        <!-- Email -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Email</label>
          <input 
            type="email" 
            v-model="email"
            placeholder="Enter your email"
            class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-500 outline-none"
            required
          />
        </div>

        <!-- Age -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Age</label>
          <input 
            type="number" 
            v-model="age"
            placeholder="Enter your age"
            class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-500 outline-none"
            required
          />
        </div>

        <!-- Phone -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Phone</label>
          <input 
            type="tel" 
            v-model="phone"
            placeholder="Enter your phone number"
            class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-500 outline-none"
            required
          />
        </div>

        <!-- Address -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Address</label>
          <input 
            type="text" 
            v-model="address"
            placeholder="Enter your address"
            class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-500 outline-none"
            required
          />
        </div>

        <!-- Password -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Password</label>
          <input 
            type="password" 
            v-model="password"
            placeholder="Enter your password"
            class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-500 outline-none"
            required
          />
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
            <option value="CAR">Car</option>
            <option value="BIKE">Bike</option>
            <option value="HEAVY_VEHICLE">Heavy Vehicle</option>
          </select>
        </div>

        <!-- Vehicle Age -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Vehicle Age</label>
          <input 
            type="number" 
            v-model="vehicleAge"
            placeholder="Enter your vehicle's age"
            class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-500 outline-none"
            required
          />
        </div>

        <!-- Register Button -->
        <button 
          type="submit"
          class="w-full bg-blue-600 text-white py-2 rounded-md hover:bg-blue-700 transition"
        >
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
import axios from "axios";
import { useRouter } from "vue-router";

const router = useRouter();

const name = ref("");
const email = ref("");
const age = ref("");
const phone = ref("");
const address = ref("");
const password = ref("");
const smoking = ref(""); // "yes" / "no"
const preexisting = ref([]); // array of conditions
const vehicleType = ref("");
const vehicleAge = ref("");
const errorMessage = ref("");
const successMessage = ref("");

const handleRegister = async () => {
  try {
    const response = await axios.post("http://localhost:9090/register", {
      name: name.value,
      email: email.value,
      age: Number(age.value),
      phone: phone.value,
      address: address.value,
      password: password.value,
      smokingDrinking: smoking.value === "yes",
      preexistingConditions: preexisting.value, // already uppercase values
      vehicleType: vehicleType.value,
      vehicleAge: Number(vehicleAge.value) || 0
    });

    successMessage.value = "Registration successful! Redirecting to login...";
    console.log("User registered:", response.data);

    setTimeout(() => {
      router.push("/login");
    }, 1500);
  } catch (error) {
    errorMessage.value =
      error.response?.data?.message || "Something went wrong during registration.";
  }
};
</script>
