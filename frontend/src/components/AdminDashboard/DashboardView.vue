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
        <div class="kpi-number text-gray-900">{{ policies.totalPoliciesText }}</div>
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
        <router-link to="/admin/users" class="quick-action-card quick-action-card-users">
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
    policies() {
    
    const policiesStoreState = this.$store?.state?.adminPolicyStore || {}
      const policyStoreList= Array.isArray(policiesStoreState.policies) ? policiesStoreState.policies : []

      const totalPoliciesCount = policyStoreList.length || 0

      const activePoliciesDisplayText = totalPoliciesCount ? `${totalPoliciesCount} policy types` : 'No data available for now'
      console.log('Total Policies Count:', totalPoliciesCount);
      console.log('Active Policies Display Text:', activePoliciesDisplayText);
      console.log('Policy Store List:', policyStoreList);
      return {
        totalPoliciesText: String(totalPoliciesCount || 0),
        totalPoliciesCount,
        activePoliciesDisplayText
      }
    },
    // This function takes no input and returns calculated dashboard statistics from claims and tickets data
    stats() {
      const claimsStoreState = this.$store?.state?.claims || {}
      const adminClaimsList = Array.isArray(claimsStoreState.adminClaims) ? claimsStoreState.adminClaims : []
      
      // Count unique policy types from claims data
      // const uniquePolicyNames = new Set()
      // adminClaimsList.forEach(claim => {
      //   if (claim?.policyName) {
      //     uniquePolicyNames.add(claim.policyName)
      //   }
      // }
      
      // Count pending claims
      const pendingClaimsCount = adminClaimsList.filter(claim => 
        (claim?.status || '').toUpperCase() === 'PENDING'
      ).length

      // Get tickets data from admin tickets store
      const ticketsStoreState = this.$store?.state?.adminTickets || {}
      const adminTicketsList = Array.isArray(ticketsStoreState.tickets) ? ticketsStoreState.tickets : []
      
      // Count open tickets
      const openTicketsCount = adminTicketsList.filter(ticket => 
        (ticket?.status || '').toUpperCase() === 'OPEN' || 
        (ticket?.status || '').toUpperCase() === 'PENDING'
      ).length

      const openTicketsDisplayText = openTicketsCount 
        ? `${openTicketsCount} open` 
        : adminTicketsList.length 
          ? 'All resolved' 
          : 'No data available for now'

      return {
        totalClaimsText: String(adminClaimsList.length || 0),
        pendingClaimsText: `${pendingClaimsCount} pending review`,
        totalTicketsText: String(adminTicketsList.length || 0),
        openTicketsText: openTicketsDisplayText
      }
    },
    // This function takes no input and returns policy breakdown data categorized by insurance types
    policyBreakdown() {
      const claimsStoreState = this.$store?.state?.claims || {}
      const adminClaimsList = Array.isArray(claimsStoreState.adminClaims) ? claimsStoreState.adminClaims : []
      
      if (adminClaimsList.length === 0) {
        return []
      }

      // Count each policy type from claims
      const policyTypeCounter = {}
      
      adminClaimsList.forEach(claim => {
        const policyName = claim?.policyName || 'Unknown Policy'
        
        // Determine policy category based on name
        let policyCategory = 'Auto Insurance' // default category
        const lowerCaseName = policyName.toLowerCase()
        
        if (lowerCaseName.includes('health') || lowerCaseName.includes('medical')) {
          policyCategory = 'Health Insurance'
        } else if (lowerCaseName.includes('life') || lowerCaseName.includes('term')) {
          policyCategory = 'Life Insurance'
        } else if (lowerCaseName.includes('auto') || lowerCaseName.includes('vehicle') || lowerCaseName.includes('car')) {
          policyCategory = 'Auto Insurance'
        }
        
        policyTypeCounter[policyCategory] = (policyTypeCounter[policyCategory] || 0) + 1
      })

      // Calculate percentages and format data
      const totalCount = Object.values(policyTypeCounter).reduce((sum, count) => sum + count, 0) || 1
      
      return Object.entries(policyTypeCounter)
        .map(([categoryName, categoryCount]) => ({
          name: categoryName,
          count: categoryCount,
          percent: (categoryCount / totalCount) * 100
        }))
        .filter(item => item.count > 0)
    },
    // This function takes no input and returns total count of all policies from breakdown data
    totalPoliciesFromBreakdown() {
      return this.policyBreakdown.reduce((totalSum, policyItem) => totalSum + policyItem.count, 0)
    },
    
    // This function takes no input and returns CSS conic-gradient style for pie chart visualization
    pieChartStyle() {
      if (this.policyBreakdown.length === 0) return {}
      
      const policyColors = {
        'Health Insurance': '#3b82f6',
        'Life Insurance': '#10b981', 
        'Auto Insurance': '#8b5cf6'
      }
      
      let currentPercent = 0
      const colorGradientStops = []
      
      this.policyBreakdown.forEach(policyItem => {
        const itemColor = policyColors[policyItem.name] || '#6b7280'
        const startPercent = currentPercent
        const endPercent = currentPercent + policyItem.percent
        
        colorGradientStops.push(`${itemColor} ${startPercent}% ${endPercent}%`)
        currentPercent = endPercent
      })
      
      return {
        background: `conic-gradient(${colorGradientStops.join(', ')})`
      }
    },
    // This function takes no input and returns combined list of recent claims and tickets activities
    recentActivities() {
      const claimsStoreState = this.$store?.state?.claims || {}
      const adminClaimsList = Array.isArray(claimsStoreState.adminClaims) ? claimsStoreState.adminClaims : []
      
      const ticketsStoreState = this.$store?.state?.adminTickets || {}
      const adminTicketsList = Array.isArray(ticketsStoreState.tickets) ? ticketsStoreState.tickets : []

      // Parse date string safely
      const parseActivityDate = (dateString) => {
        if (!dateString) return 0
        const parsedTimestamp = Date.parse(dateString)
        return isNaN(parsedTimestamp) ? 0 : parsedTimestamp
      }

      // Get latest 4 claims activities
      const recentClaimsActivities = [...adminClaimsList]
        .sort((firstClaim, secondClaim) => 
          parseActivityDate(secondClaim?.createdAt || secondClaim?.claimDate || secondClaim?.resolvedDate) - 
          parseActivityDate(firstClaim?.createdAt || firstClaim?.claimDate || firstClaim?.resolvedDate)
        )
        .slice(0, 4)
        .map(claim => ({
          title: `New claim submitted for ${claim?.policyName || claim?.policyType || 'Unknown Policy'}`,
          type: 'claim'
        }))

      // Get latest 2 tickets activities
      const recentTicketsActivities = [...adminTicketsList]
        .sort((firstTicket, secondTicket) => 
          parseActivityDate(secondTicket?.createdAt || secondTicket?.createdDate || secondTicket?.dateCreated) - 
          parseActivityDate(firstTicket?.createdAt || firstTicket?.createdDate || firstTicket?.dateCreated)
        )
        .slice(0, 2)
        .map(ticket => ({
          title: `Support ticket: ${ticket?.subject || ticket?.title || 'Customer inquiry'}`,
          type: 'ticket'
        }))

      // Combine activities and return latest 6
      return [...recentClaimsActivities, ...recentTicketsActivities].slice(0, 6)
    }
  },
  // Component methods
  methods: {
    // This function takes dateString as input and returns human-readable time difference from now
    calculateTimeAgo(dateString) {
      try {
        const pastTime = new Date(dateString).getTime()
        const currentTime = Date.now()
        const timeDifference = Math.max(0, currentTime - pastTime)
        const minutesAgo = Math.floor(timeDifference / 60000)
        
        if (minutesAgo < 1) return 'just now'
        if (minutesAgo < 60) return `${minutesAgo} minute${minutesAgo === 1 ? '' : 's'} ago`
        
        const hoursAgo = Math.floor(minutesAgo / 60)
        if (hoursAgo < 24) return `${hoursAgo} hour${hoursAgo === 1 ? '' : 's'} ago`
        
        const daysAgo = Math.floor(hoursAgo / 24)
        return `${daysAgo} day${daysAgo === 1 ? '' : 's'} ago`
      } catch {
        return 'just now'
      }
    }
  },
//   onMounted() {
//     // Fetch initial data for claims and tickets
//     const store = this.$store
//     if (store._actions["adminPolicyStore/fetchPolicies"]) {
//       store.dispatch("adminPolicyStore/fetchPolicies")
//     }
// }
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