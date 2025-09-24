<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-50">
    <div class="text-center">
      <div class="text-9xl font-bold text-gray-300 mb-4">404</div>
      <h1 class="text-2xl font-bold text-gray-900 mb-2">Page Not Found</h1>
      <p class="text-gray-600 mb-8">The page you're looking for doesn't exist.</p>
      <button 
        @click="goHome"
        class="bg-brand-backgroundTheme text-white px-6 py-2 rounded-lg hover:bg-blue-700 transition-colors"
      >
        Go Home
      </button>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const goHome = () => {
  const userRole = localStorage.getItem('role')
  
  if (userRole === 'admin') {
    router.push('/admin/dashboard')
  } else if (userRole === 'user') {
    router.push('/dashboard')
  } else {
    router.push('/')
  }
}
</script>
