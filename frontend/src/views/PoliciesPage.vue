<template>
  <div class="flex flex-col h-screen">
    <HeaderLayout
      :user="user"
      :isCollapsed="isCollapsed"
      :setIsCollapsed="setIsCollapsed"
      class="w-full"
    />
    <div class="space-y-6 p-6">
    <!-- Header with View Buttons -->
    <div class="flex items-center justify-between">
      <h1>{{ view === "catalog" ? "Policy Catalog" : "My Policies" }}</h1>
      <div class="flex gap-2">
        <button
          :class="view === 'catalog' ? 'bg-brand-backgroundTheme text-white px-4 py-2 rounded-xl' : 'border border-gray-300 px-4 py-2 rounded-xl'"
          @click="view = 'catalog'">
          Browse Policies
        </button>
        <button
          :class="view === 'my-policies' ? 'bg-brand-backgroundTheme text-white px-4 py-2 rounded-xl' : 'border border-gray-300 px-4 py-2 rounded-xl'"
          @click="view = 'my-policies'">
          My Policies
        </button>
      </div>
    </div>

    <!-- Catalog View -->
    <PolicyCatalog v-if="view === 'catalog'" @purchase="showPurchaseModal = $event" />

    <MyPolicies v-if="view === 'my-policies'" @renew="showPurchaseModal = $event" />

    <!-- Use the new PurchaseModal component -->
    <PurchaseModal
      :policy="showPurchaseModal"
      @close="showPurchaseModal = null"
      @purchase="purchasePolicy"
    />
  </div>
  </div>
</template>
<script setup>
import { ref } from "vue";
import PurchaseModal from "@/components/PoliciesComponents/PurchaseModal.vue";
import HeaderLayout from "../components/layout/HeaderLayout.vue";
import MyPolicies from "@/components/PoliciesComponents/MyPolicies.vue";
import PolicyCatalog from "@/components/PoliciesComponents/PolicyCatalog.vue";

const user = ref({
  name: "John Doe",
  email: "john.doe@example.com",
});

// Local States
const view = ref("catalog");
const showPurchaseModal = ref(null);

const userPolicies = ref([]); // mock or Vuex state in future

// Purchase handler called when modal emits "purchase"
function purchasePolicy(policy) {
  const newPolicy = {
    ...policy,
    id: Date.now().toString(),
    startDate: new Date().toISOString().split("T")[0],
    endDate: new Date(Date.now() + 365*24*60*60*1000).toISOString().split("T")[0],
    status: "ACTIVE",
  };

  userPolicies.value.push(newPolicy);
  showPurchaseModal.value = null;

  // Show alert
  alert(`Policy "${policy.name}" purchased successfully!`);
}
</script>
