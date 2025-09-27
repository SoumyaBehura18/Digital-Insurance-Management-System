<template>
  <div class="space-y-6">
    <!-- Top: Title and subtitle -->
    <div>
      <h1 class="text-2xl font-bold">Admin Dashboard</h1>
      <p class="text-gray-500">Overview of your insurance management system</p>
    </div>

    <!-- KPI cards -->
    <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
      <div class="rounded border p-4">
        <div class="flex items-center justify-between">
          <h3 class="text-sm text-gray-600">Total Policies</h3>
          <span class="text-gray-400" aria-hidden="true">📄</span>
        </div>
        <div class="mt-4 text-3xl font-semibold text-brand-backgroundTheme">{{ stats.totalPoliciesText }}</div>
        <div class="mt-1 text-xs text-gray-500">{{ stats.activePoliciesText }}</div>
      </div>
      <div class="rounded border p-4">
        <div class="flex items-center justify-between">
          <h3 class="text-sm text-gray-600">Total Claims</h3>
          <span class="text-gray-400" aria-hidden="true">ℹ️</span>
        </div>
        <div class="mt-4 text-3xl font-semibold text-brand-backgroundTheme">{{ stats.totalClaimsText }}</div>
        <div class="mt-1 text-xs text-gray-500">{{ stats.pendingClaimsText }}</div>
      </div>
      <div class="rounded border p-4">
        <div class="flex items-center justify-between">
          <h3 class="text-sm text-gray-600">Total Users</h3>
          <span class="text-gray-400" aria-hidden="true">👥</span>
        </div>
        <div class="mt-4 text-3xl font-semibold text-brand-backgroundTheme">{{ stats.totalUsersText }}</div>
        <div class="mt-1 text-xs text-gray-500">{{ stats.registeredUsersText }}</div>
      </div>
      <div class="rounded border p-4">
        <div class="flex items-center justify-between">
          <h3 class="text-sm text-gray-600">Support Tickets</h3>
          <span class="text-gray-400" aria-hidden="true">💬</span>
        </div>
        <div class="mt-4 text-3xl font-semibold text-brand-backgroundTheme">{{ stats.totalTicketsText }}</div>
        <div class="mt-1 text-xs text-gray-500">{{ stats.openTicketsText }}</div>
      </div>
    </div>

    <!-- Middle: Policy breakdown & Recent activities -->
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-4">
      <!-- Policy Breakdown Panel -->
      <div class="rounded border p-4">
        <h3 class="text-lg font-semibold mb-3">Policy Breakdown by Type</h3>
        <div v-if="policyBreakdown.length" class="divide-y">
          <div
            v-for="item in policyBreakdown"
            :key="item.name"
            class="flex items-center justify-between py-3"
          >
            <div class="flex items-center gap-3">
              <span class="h-3 w-3 rounded-full bg-brand-backgroundTheme inline-block"></span>
              <span>{{ item.name }}</span>
            </div>
            <div class="text-sm text-gray-600">
              {{ (item.percent).toFixed(1) }}% <span class="font-semibold ml-2">{{ item.count }}</span>
            </div>
          </div>
        </div>
        <p v-else class="text-gray-400 text-sm">No data available for now</p>
      </div>

      <!-- Recent Activities Panel -->
      <div class="rounded border p-4">
        <h3 class="text-lg font-semibold mb-3">Recent Activities</h3>
        <ul v-if="recentActivities.length" class="divide-y">
          <li v-for="(act, i) in recentActivities" :key="i" class="py-3 flex items-start gap-3">
            <span class="h-2.5 w-2.5 rounded-full bg-brand-backgroundTheme mt-1.5"></span>
            <div class="flex-1">
              <p class="text-sm">{{ act.title }}</p>
              <p class="text-xs text-gray-500">{{ act.when }}</p>
            </div>
          </li>
        </ul>
        <p v-else class="text-gray-400 text-sm">No data available for now</p>
      </div>
    </div>

    <!-- Bottom: Quick actions -->
    <div class="rounded border p-4">
      <h3 class="text-lg font-semibold mb-3">Quick Actions</h3>
      <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
  <router-link to="/admin/policies" class="rounded border p-4 block hover:bg-gray-50">
          <div class="text-base font-semibold">Create New Policy</div>
          <div class="text-sm text-gray-500 mt-1">Add a new insurance policy to the catalog</div>
  </router-link>
  <router-link to="/admin/claims" class="rounded border p-4 block hover:bg-gray-50">
          <div class="text-base font-semibold">Review Claims</div>
          <div class="text-sm text-gray-500 mt-1">Process pending insurance claims</div>
  </router-link>
  <router-link to="/admin/tickets" class="rounded border p-4 block hover:bg-gray-50">
          <div class="text-base font-semibold">Manage Users</div>
          <div class="text-sm text-gray-500 mt-1">View and manage customer accounts</div>
  </router-link>
      </div>
    </div>
  </div>
</template>

<script>
import { makeRequestWithToken } from '@/utils/requests'

export default {
  name: 'DashboardView',
  data() {
    return {
      stats: {
        totalPoliciesText: '0',
        activePoliciesText: 'No data available for now',
        totalClaimsText: '0',
        pendingClaimsText: 'No data available for now',
        totalUsersText: '0',
        registeredUsersText: 'No data available for now',
        totalTicketsText: '0',
        openTicketsText: 'No data available for now',
      },
      policyBreakdown: [],
      recentActivities: []
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    async loadData() {
      try {
        // Policies: use new endpoint for totals and breakdown by type
        try {
          const policiesRes = await makeRequestWithToken('GET', '/claim/policies')
          const policies = Array.isArray(policiesRes.data) ? policiesRes.data : []

          const activeCount = policies.filter(p => p.status === 'ACTIVE').length
          this.stats.totalPoliciesText = String(policies.length)
          this.stats.activePoliciesText = `${activeCount} active policies`

          // Breakdown by policy.type (HEALTH, LIFE, else VEHICLE)
          const buckets = { HEALTH: 0, LIFE: 0, VEHICLE: 0 }
          for (const up of policies) {
            const t = up?.policy?.type
            const key = t === 'HEALTH' ? 'HEALTH' : (t === 'LIFE' ? 'LIFE' : 'VEHICLE')
            buckets[key]++
          }
          const totalPolicies = policies.length || 1
          const items = [
            { name: 'Life Insurance', count: buckets.LIFE, percent: (buckets.LIFE / totalPolicies) * 100 },
            { name: 'Health Insurance', count: buckets.HEALTH, percent: (buckets.HEALTH / totalPolicies) * 100 },
            { name: 'Auto Insurance', count: buckets.VEHICLE, percent: (buckets.VEHICLE / totalPolicies) * 100 }
          ].filter(i => i.count > 0)
          this.policyBreakdown = items
        } catch (e) {
          // keep placeholders if policies API not available
        }

        // Claims: use existing admin endpoint
        const claimsRes = await makeRequestWithToken('GET', '/claim/claims')
        const claims = Array.isArray(claimsRes.data) ? claimsRes.data : []
        const pending = claims.filter(c => c.status === 'PENDING')
        const approved = claims.filter(c => c.status === 'APPROVED')

        this.stats.totalClaimsText = String(claims.length)
        this.stats.pendingClaimsText = `${pending.length} pending review`

        // If no policies breakdown available, fallback to claims aggregation by policyName
        if (!this.policyBreakdown.length) {
          const counts = {}
          for (const c of claims) {
            const name = c.policyName || 'Unknown Policy'
            counts[name] = (counts[name] || 0) + 1
          }
          const total = Object.values(counts).reduce((a, b) => a + b, 0)
          this.policyBreakdown = Object.entries(counts).map(([name, count]) => ({
            name,
            count,
            percent: total ? (count / total) * 100 : 0
          }))
        }

        // Recent activities (simple derivation from claims)
        this.recentActivities = claims.slice(0, 6).map(c => ({
          title: `New claim submitted for ${c.policyName || 'Unknown Policy'}`,
          when: c.createdAt ? this.timeAgo(c.createdAt) : 'Just now'
        }))
      } catch (e) {
        // leave defaults and placeholders
      }

      // Policies and Users APIs: not present in current context
      // Keep placeholders and show "No data available for now"
      this.stats.totalPoliciesText = this.stats.totalPoliciesText || '0'
      this.stats.activePoliciesText = this.stats.activePoliciesText || 'No data available for now'
      this.stats.totalUsersText = this.stats.totalUsersText || '0'
      this.stats.registeredUsersText = this.stats.registeredUsersText || 'No data available for now'
      this.stats.totalTicketsText = this.stats.totalTicketsText || '0'
      this.stats.openTicketsText = this.stats.openTicketsText || 'No data available for now'
    },
    timeAgo(iso) {
      try {
        const then = new Date(iso).getTime()
        const now = Date.now()
        const diff = Math.max(0, now - then)
        const minutes = Math.floor(diff / 60000)
        if (minutes < 1) return 'just now'
        if (minutes < 60) return `${minutes} minute${minutes===1?'':'s'} ago`
        const hours = Math.floor(minutes / 60)
        if (hours < 24) return `${hours} hour${hours===1?'':'s'} ago`
        const days = Math.floor(hours / 24)
        return `${days} day${days===1?'':'s'} ago`
      } catch {
        return 'just now'
      }
    }
  }
}
</script>
