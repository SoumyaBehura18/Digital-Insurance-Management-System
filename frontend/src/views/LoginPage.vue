<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-50">
    <div class="bg-white shadow-md rounded-xl w-full max-w-md p-8">

      <!-- Logo & Title -->
      <div class="flex items-center justify-center mb-6">
        <Shield class="w-8 h-8 text-blue-600 rounded-full mr-2" />
        <h1 class="text-xl font-semibold">InsureCore</h1>
      </div>

      <!-- Heading -->
      <h2 class="text-center text-lg font-medium mb-1">Welcome Back</h2>
      <p class="text-center text-gray-500 mb-6">Sign in to your account</p>

      <!-- Form -->
      <form @submit.prevent="handleLogin" class="space-y-4">

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

        <!-- Login Button -->
        <button 
          type="submit"
          :disabled="isLoading"
          class="w-full bg-blue-600 text-white py-2 rounded-md hover:bg-blue-700 transition disabled:opacity-50"
        >
          {{ isLoading ? 'Logging in...' : 'Login' }}
        </button>
      </form>

      <!-- Register Link -->
      <p class="mt-4 text-center text-gray-600">
        New user? 
        <a href="/register" class="text-blue-600 font-medium hover:underline">Register</a>
      </p>

    </div>
  </div>
</template>
<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
import { Shield } from 'lucide-vue-next'

const email = ref('')
const password = ref('')
const isLoading = ref(false)
const router = useRouter()

const handleLogin = async () => {
  // Prevent multiple calls
  if (isLoading.value) return

  if (!email.value || !password.value) {
    alert('Please enter email and password')
    return
  }

  isLoading.value = true

  try {
    const response = await axios.post('http://localhost:9090/login', {
      email: email.value,
      password: password.value
    })
    
    // below structure is for testing without backend.

    // const response = {
    //   data: {
    //     token: 'dummy-jwt-token',
    //     role: 'admin', // Change this to 'user' to test user redirection
    //     userId: 1
    //   }
    // }

    // Save JWT token
    localStorage.setItem('token', response.data.token)
    localStorage.setItem('role', response.data.role)
    localStorage.setItem('userId', response.data.userId)
    
    alert('Login successful!')

    // Role-based redirection with new route structure
    if (response.data.role === 'admin') {
      router.push('/admin/dashboard')
    } else {
      router.push('/dashboard')
    }

  } catch (error) {
    console.error('Login failed:', error.response?.data || error.message)
    if (error.response?.data?.error) {
      alert(`Login failed: ${error.response.data.error}`)
    } else {
      alert('Login failed. Please try again.')
    }
  } finally {
    isLoading.value = false
  }
}
</script>
