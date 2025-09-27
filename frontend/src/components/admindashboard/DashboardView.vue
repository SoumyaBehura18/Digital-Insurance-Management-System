<template>
  <div class="space-y-6">
    <!-- Top: Title and subtitle -->
    <div>
      <h1 class="text-3xl font-bold text-gray-900">Admin Dashboard</h1>
      <p class="text-gray-500 mt-2">Overview</p>
    </div>

    <!-- KPI cards -->
  <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
      <!-- Total Policies -->
      <div class="kpi-card kpi-card-policies">
        <div class="flex items-center justify-between">
          <h3 class="text-sm font-medium text-gray-600">Total Policies</h3>
          <span class="kpi-icon-policies">📄</span>
        </div>
        <div class="kpi-number text-gray-900">{{ stats.totalPoliciesText }}</div>
        <div class="text-sm text-gray-600">{{ stats.activePoliciesText }}</div>
      </div>

      <!-- Total Claims -->
      <div class="kpi-card kpi-card-claims">
        <div class="flex items-center justify-between">
          <h3 class="text-sm font-medium text-gray-600">Total Claims</h3>
          <span class="kpi-icon-claims">ℹ️</span>
        </div>
        <div class="kpi-number text-gray-900">{{ stats.totalClaimsText }}</div>
        <div class="text-sm text-gray-600">{{ stats.pendingClaimsText }}</div>
      </div>

      

      <!-- Support Tickets -->
      <div class="kpi-card kpi-card-tickets">
        <div class="flex items-center justify-between">
          <h3 class="text-sm font-medium text-gray-600">Support Tickets</h3>
          <span class="kpi-icon-tickets">💬</span>
        </div>
        <div class="kpi-number text-gray-900">{{ stats.totalTicketsText }}</div>
        <div class="text-sm text-gray-600">{{ stats.openTicketsText }}</div>
      </div>
    </div>

    <!-- Middle: Policy breakdown & Recent activities -->
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
      <!-- Policy Breakdown Panel -->
      <div class="section-card">
        <h3 class="section-title">
          <span class="section-icon text-blue-500">📊</span>
          Policy Breakdown by Type
        </h3>
        <div v-if="policyBreakdown.length" class="space-y-6">
          <!-- Pie Chart -->
          <div class="flex justify-center">
            <div class="pie-chart-container">
              <div 
                class="pie-chart"
                :style="pieChartStyle"
              ></div>
              <div class="pie-chart-center">
                <div class="text-2xl font-bold text-gray-800">{{ totalPoliciesFromBreakdown }}</div>
                <div class="text-xs text-gray-500">Total</div>
              </div>
            </div>
          </div>
          
          <!-- Extended Legend -->
          <div class="grid grid-cols-1 gap-4">
            <div
              v-for="item in policyBreakdown"
              :key="item.name"
              class="flex items-center justify-between p-4 rounded-xl bg-gray-50 border-l-4"
              :class="{
                'border-green-500': item.name.includes('Life'),
                'border-blue-500': item.name.includes('Health'),
                'border-purple-500': !item.name.includes('Life') && !item.name.includes('Health')
              }"
            >
              <div class="flex items-center gap-3">
                <span 
                  :class="[
                    'w-5 h-5 rounded-full',
                    item.name.includes('Life') ? 'bg-green-500' : 
                    item.name.includes('Health') ? 'bg-blue-500' : 'bg-purple-500'
                  ]"
                ></span>
                <div>
                  <div class="font-semibold text-gray-800">{{ item.name }}</div>
                  <div class="text-sm text-gray-600">{{ item.percent.toFixed(1) }}% of total claims</div>
                </div>
              </div>
              <div class="text-right">
                <div class="text-2xl font-bold text-gray-900">{{ item.count }}</div>
                <div class="text-sm text-gray-500">Claims</div>
              </div>
            </div>
          </div>
        </div>
        <p v-else class="text-gray-400 text-sm">No data available for now</p>
      </div>

      <!-- Recent Activities Panel -->
      <div class="section-card">
        <h3 class="section-title">
          <span class="section-icon text-blue-500">🕒</span>
          Recent Activities
        </h3>
        <div v-if="recentActivities.length" class="space-y-3">
          <div 
            v-for="(act, i) in recentActivities" 
            :key="i" 
            class="activity-item-simple"
          >
            <div class="activity-number">{{ i + 1 }}</div>
            <div class="flex-1">
              <div class="activity-text">{{ act.title }}</div>
            </div>
          </div>
        </div>
        <p v-else class="text-gray-400 text-sm">No data available for now</p>
      </div>
    </div>

    <!-- Bottom: Quick actions -->
    <div class="section-card">
      <h3 class="section-title">
        <span class="section-icon text-purple-500">⚙️</span>
        Quick Actions
      </h3>
      <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
        <router-link to="/admin/policies" class="quick-action-card quick-action-card-policies">
          <div class="quick-action-icon quick-action-icon-policies">+</div>
          <div class="quick-action-title quick-action-title-policies">Create New Policy</div>
          <div class="quick-action-desc">Add a new insurance policy to the catalog</div>
        </router-link>
        <router-link to="/admin/claims" class="quick-action-card quick-action-card-claims">
          <div class="quick-action-icon quick-action-icon-claims">👁️</div>
          <div class="quick-action-title quick-action-title-claims">Review Claims</div>
          <div class="quick-action-desc">Process pending insurance claims</div>
        </router-link>
        <router-link to="/admin/tickets" class="quick-action-card quick-action-card-users">
          <div class="quick-action-icon quick-action-icon-users">👥</div>
          <div class="quick-action-title quick-action-title-users">Manage Users</div>
          <div class="quick-action-desc">View and manage customer accounts</div>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'DashboardView',
  computed: {
    // Derived dashboard stats from Vuex stores only
    stats() {
      const claimsState = this.$store?.state?.claims || {}
      const adminClaims = Array.isArray(claimsState.adminClaims) ? claimsState.adminClaims : []
      
      // Count unique policies from claims data
      const uniquePolicyNames = new Set()
      for (const claim of adminClaims) {
        if (claim?.policyName) {
          uniquePolicyNames.add(claim.policyName)
        }
      }
      const totalPoliciesCount = uniquePolicyNames.size

      // For now, we can't determine "active" policies from claims data alone
      // This would require a proper policy management endpoint
      const activePoliciesText = totalPoliciesCount ? `${totalPoliciesCount} policy types` : 'No data available for now'

      const pendingClaims = adminClaims.filter(c => (c?.status || '').toUpperCase() === 'PENDING').length

      // Tickets not yet wired
      const totalTickets = 0
      const openTickets = 0

      return {
        totalPoliciesText: String(totalPoliciesCount || 0),
        activePoliciesText,
        totalClaimsText: String(adminClaims.length || 0),
        pendingClaimsText: `${pendingClaims} pending review`,
        totalTicketsText: String(totalTickets),
        openTicketsText: openTickets ? `${openTickets} open` : 'No data available for now'
      }
    },
    policyBreakdown() {
      // Derive policy breakdown from claims data (since PolicyStore is for user recommendations)
      const claimsState = this.$store?.state?.claims || {}
      const adminClaims = Array.isArray(claimsState.adminClaims) ? claimsState.adminClaims : []
      
      if (!adminClaims.length) {
        return []
      }

      // Count policies by type from claims data
      const policyTypeCounts = {}
      
      for (const claim of adminClaims) {
        const policyName = claim?.policyName || 'Unknown Policy'
        
        // Categorize by policy name patterns
        let category = 'Auto Insurance' // default
        if (policyName.toLowerCase().includes('health') || policyName.toLowerCase().includes('medical')) {
          category = 'Health Insurance'
        } else if (policyName.toLowerCase().includes('life') || policyName.toLowerCase().includes('term')) {
          category = 'Life Insurance'
        } else if (policyName.toLowerCase().includes('auto') || policyName.toLowerCase().includes('vehicle') || policyName.toLowerCase().includes('car')) {
          category = 'Auto Insurance'
        }
        
        policyTypeCounts[category] = (policyTypeCounts[category] || 0) + 1
      }

      // Convert to breakdown format
      const total = Object.values(policyTypeCounts).reduce((sum, count) => sum + count, 0) || 1
      
      return Object.entries(policyTypeCounts).map(([name, count]) => ({
        name,
        count,
        percent: (count / total) * 100
      })).filter(item => item.count > 0)
    },
    totalPoliciesFromBreakdown() {
      return this.policyBreakdown.reduce((sum, item) => sum + item.count, 0)
    },
    pieChartStyle() {
      if (!this.policyBreakdown.length) return {}
      
      const colors = {
        'Health Insurance': '#3b82f6',
        'Life Insurance': '#10b981', 
        'Auto Insurance': '#8b5cf6'
      }
      
      let cumulativePercent = 0
      const gradientStops = []
      
      this.policyBreakdown.forEach(item => {
        const color = colors[item.name] || '#6b7280'
        const startPercent = cumulativePercent
        const endPercent = cumulativePercent + item.percent
        
        gradientStops.push(`${color} ${startPercent}% ${endPercent}%`)
        cumulativePercent = endPercent
      })
      
      return {
        background: `conic-gradient(${gradientStops.join(', ')})`
      }
    },
    recentActivities() {
      const claimsState = this.$store?.state?.claims || {}
      const adminClaims = Array.isArray(claimsState.adminClaims) ? claimsState.adminClaims : []

      const parseDate = (d) => {
        if (!d) return 0
        const ts = Date.parse(d)
        return isNaN(ts) ? 0 : ts
      }

      // Show recent claims (latest 6)
      const claimActs = [...adminClaims]
        .sort((a, b) => parseDate(b?.createdAt || b?.claimDate || b?.resolvedDate) - parseDate(a?.createdAt || a?.claimDate || a?.resolvedDate))
        .slice(0, 6)
        .map(c => ({
          title: `New claim submitted for ${c?.policyName || c?.policyType || 'Unknown Policy'}`
        }))

      return claimActs
    }
  },
  methods: {
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

<style scoped>
.kpi-card {
  border-radius: 16px;
  padding: 24px;
  position: relative;
  overflow: hidden;
}

.kpi-card-policies {
  background: linear-gradient(135deg, #ecfdf5 0%, #d1fae5 100%);
}

.kpi-card-claims {
  background: linear-gradient(135deg, #eff6ff 0%, #dbeafe 100%);
}

.kpi-card-users {
  background: linear-gradient(135deg, #faf5ff 0%, #f3e8ff 100%);
}

.kpi-card-tickets {
  background: linear-gradient(135deg, #fff7ed 0%, #fed7aa 100%);
}

.kpi-icon-policies {
  color: #10b981;
  font-size: 24px;
}

.kpi-icon-claims {
  color: #3b82f6;
  font-size: 24px;
}

.kpi-icon-users {
  color: #8b5cf6;
  font-size: 24px;
}

.kpi-icon-tickets {
  color: #f97316;
  font-size: 24px;
}

.kpi-number {
  font-size: 48px;
  font-weight: 700;
  line-height: 1;
  margin: 16px 0 8px 0;
}

.section-card {
  background: #ffffff;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1), 0 1px 2px 0 rgba(0, 0, 0, 0.06);
}

.section-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 20px;
}

.section-icon {
  font-size: 20px;
}

.policy-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 0;
  border-bottom: 1px solid #f3f4f6;
}

.policy-item:last-child {
  border-bottom: none;
}

.policy-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.policy-dot {
  width: 12px;
  height: 12px;
  border-radius: 50%;
}

.policy-dot-life {
  background-color: #10b981;
}

.policy-dot-health {
  background-color: #3b82f6;
}

.policy-dot-auto {
  background-color: #8b5cf6;
}

.policy-progress {
  width: 200px;
  height: 8px;
  background-color: #f3f4f6;
  border-radius: 4px;
  margin-left: 16px;
  overflow: hidden;
}

.policy-progress-bar {
  height: 100%;
  border-radius: 4px;
  transition: width 0.3s ease;
}

.policy-progress-bar-life {
  background: linear-gradient(90deg, #10b981 0%, #34d399 100%);
}

.policy-progress-bar-health {
  background: linear-gradient(90deg, #3b82f6 0%, #60a5fa 100%);
}

.policy-progress-bar-auto {
  background: linear-gradient(90deg, #8b5cf6 0%, #a78bfa 100%);
}

.policy-stats {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 14px;
}

.policy-percentage {
  color: #6b7280;
  font-weight: 500;
}

.policy-count {
  color: #111827;
  font-weight: 600;
}

.activity-item {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 12px;
  border-radius: 12px;
  margin-bottom: 8px;
}

.activity-item:last-child {
  margin-bottom: 0;
}

.activity-claims {
  background-color: #dbeafe;
}

.activity-policies {
  background-color: #d1fae5;
}

.activity-tickets {
  background-color: #fed7aa;
}

.activity-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  margin-top: 6px;
  flex-shrink: 0;
}

.activity-dot-claims {
  background-color: #3b82f6;
}

.activity-dot-policies {
  background-color: #10b981;
}

.activity-dot-tickets {
  background-color: #f97316;
}

.activity-text {
  font-size: 14px;
  color: #111827;
  font-weight: 500;
}

.activity-time {
  font-size: 12px;
  color: #6b7280;
  margin-top: 2px;
}

.quick-action-card {
  background: #ffffff;
  border: 2px dashed #e5e7eb;
  border-radius: 16px;
  padding: 24px;
  text-align: center;
  transition: all 0.2s ease;
  cursor: pointer;
  text-decoration: none;
  display: block;
}

.quick-action-card:hover {
  border-color: #d1d5db;
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
}

.quick-action-card-policies {
  border-color: #10b981;
  background: linear-gradient(135deg, #ecfdf5 0%, #d1fae5 100%);
}

.quick-action-card-claims {
  border-color: #3b82f6;
  background: linear-gradient(135deg, #eff6ff 0%, #dbeafe 100%);
}

.quick-action-card-users {
  border-color: #8b5cf6;
  background: linear-gradient(135deg, #faf5ff 0%, #f3e8ff 100%);
}

.quick-action-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 16px;
  font-size: 24px;
}

.quick-action-icon-policies {
  background-color: #10b981;
  color: white;
}

.quick-action-icon-claims {
  background-color: #3b82f6;
  color: white;
}

.quick-action-icon-users {
  background-color: #8b5cf6;
  color: white;
}

.quick-action-title {
  font-size: 18px;
  font-weight: 600;
  color: #111827;
  margin-bottom: 8px;
}

.quick-action-title-policies {
  color: #065f46;
}

.quick-action-title-claims {
  color: #1e40af;
}

.quick-action-title-users {
  color: #6b21a8;
}

.quick-action-desc {
  font-size: 14px;
  color: #6b7280;
}

/* Pie Chart Styles */
.pie-chart-container {
  position: relative;
  width: 140px;
  height: 140px;
}

.pie-chart {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  position: relative;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.pie-chart-center {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: white;
  border-radius: 50%;
  width: 70px;
  height: 70px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

/* Simple Activity Styles */
.activity-item-simple {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  border-radius: 8px;
  background-color: #f8fafc;
  border: 1px solid #e2e8f0;
  transition: all 0.2s ease;
}

.activity-item-simple:hover {
  background-color: #f1f5f9;
  border-color: #cbd5e1;
}

.activity-number {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background-color: #3b82f6;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 600;
  flex-shrink: 0;
}
</style>