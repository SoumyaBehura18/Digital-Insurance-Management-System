<template>
  <div class="bg-white rounded-lg shadow">
    <!-- Form Header -->
    <div class="px-6 py-4 border-b border-gray-200">
      <h2 class="text-lg font-semibold text-gray-900">Create Support Ticket</h2>
      <p class="text-sm text-gray-500 mt-1">
        Describe your issue and we'll help you resolve it
      </p>
    </div>

    <!-- Form Content -->
    <form @submit.prevent="handleSubmit" class="p-6">
      <div class="space-y-6">
        <!-- Subject Field -->
        <div>
          <label
            for="subject"
            class="block text-sm font-medium text-gray-900 mb-2"
          >
            Title
          </label>
          <input
            id="subject"
            v-model="form.subject"
            type="text"
            placeholder="Brief description of your issue"
            class="w-full px-3 py-2 border border-gray-300 rounded-lg bg-gray-50 text-gray-900 placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
            required
          />
        </div>

        <!-- Description Field -->
        <div>
          <label
            for="description"
            class="block text-sm font-medium text-gray-900 mb-2"
          >
            Description
          </label>
          <textarea
            id="description"
            v-model="form.description"
            rows="4"
            placeholder="Provide detailed information about your issue"
            class="w-full px-3 py-2 border border-gray-300 rounded-lg bg-gray-50 text-gray-900 placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent resize-none"
            required
          ></textarea>
        </div>

        <!-- Related Policy and Claim Fields -->
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <!-- Related Policy -->
          <div>
            <label
              for="policy"
              class="block text-sm font-medium text-gray-900 mb-2"
            >
              Related Policy (Optional)
            </label>
            <div class="relative">
              <select
                id="policy"
                v-model="form.relatedPolicy"
                class="w-full px-3 py-2 border border-gray-300 rounded-lg bg-gray-50 text-gray-500 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent appearance-none"
              >
                <option value="" disabled>Select a policy if relevant</option>
                <option
                  v-for="policy in props.policies"
                  :key="policy.id"
                  :value="policy.id"
                >
                  {{ policy.policyName }}
                </option>
              </select>
              <div
                class="absolute inset-y-0 right-0 flex items-center px-2 pointer-events-none"
              >
                <svg
                  class="w-4 h-4 text-gray-400"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M19 9l-7 7-7-7"
                  ></path>
                </svg>
              </div>
            </div>
          </div>

          <!-- Related Claim -->
          <div>
            <label
              for="claim"
              class="block text-sm font-medium text-gray-900 mb-2"
            >
              Related Claim (Optional)
            </label>
            <div class="relative">
              <select
                id="claim"
                v-model="form.relatedClaim"
                class="w-full px-3 py-2 border border-gray-300 rounded-lg bg-gray-50 text-gray-500 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent appearance-none"
              >
                <option value="" disabled>Select a claim if relevant</option>
                <option
                  v-for="claim in props.claims"
                  :key="claim.id"
                  :value="claim.id"
                >
                  {{ claim.id }}
                </option>
              </select>
              <div
                class="absolute inset-y-0 right-0 flex items-center px-2 pointer-events-none"
              >
                <svg
                  class="w-4 h-4 text-gray-400"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M19 9l-7 7-7-7"
                  ></path>
                </svg>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Form Actions -->
      <div class="mt-8 flex gap-3">
        <BaseButton
          type="button"
          variant="outline"
          @click="$emit('cancel')"
          class="flex-1"
        >
          Cancel
        </BaseButton>
        <BaseButton
          type="submit"
          variant="primary"
          customClass="bg-indigo-600 hover:bg-indigo-700 flex-1"
          :disabled="isSubmitting"
        >
          {{
            props.ticket
              ? "Update Ticket"
              : isSubmitting
              ? "Submitting..."
              : "Submit Ticket"
          }}
        </BaseButton>
      </div>
    </form>
  </div>
</template>

<script setup>
import { reactive, computed, ref, watch } from "vue";
import BaseButton from "@/components/BaseButton.vue";
import { useStore } from "vuex";

const props = defineProps({
  userId: {
    type: Number,
    required: true,
  },
  ticket: {
    type: Object,
    default: null,
  },
  policies: {
    type: Array,
    default: () => [],
  },
  claims: {
    type: Array,
    default: () => [],
  },
});

// Emits
const emit = defineEmits(["ticket-created", "cancel"]);
const store = useStore();

// const policies = computed(() => store.getters["userPolicies/getPolicies"]);
// const claims = computed(() => store.getters["userClaims/getClaims"]);
const error = computed(() => store.getters["userPolicies/getError"]);

console.log(
  "The value of claims and policies are:",
  props.claims,
  props.policies
);
// Form state
const form = reactive({
  subject: props.ticket?.subject || "",
  description: props.ticket?.description || "",
  relatedPolicy: props.ticket?.relatedPolicy || "",
  relatedClaim: props.ticket?.relatedClaim || "",
});

console.log("The form is: ", props.ticket);

watch(
  () => props.ticket,
  async () => {
    const response = await store.dispatch(
      "tickets/fetchTicketById",
      props.ticket.id
    );

    if (response) {
      console.log("The response is: ", response);
      form.subject = response.subject || "";
      form.description = response.description || "";
      form.relatedPolicy = response.policyId || "";
      form.relatedClaim = response.claimId || "";
    }
  },
  { immediate: true }
);

const isSubmitting = ref(false);

// Methods
const handleSubmit = async () => {
  if (props.userId) {
    isSubmitting.value = true;

    console.log("---", form.relatedClaim, form.relatedPolicy);

    try {
      // Create ticket object to emit
      const newTicket = {
        subject: form.subject,
        description: form.description,
        policyId: form.relatedPolicy !== "" ? form.relatedPolicy : null,
        claimId: form.relatedClaim !== "" ? form.relatedClaim : null,
        userId: props.userId,
      };

      console.log("The new ticket is: ", newTicket);

      if (props.ticket) {
        await store.dispatch("tickets/updateTicket", {
          ticketData: { ...newTicket, status: "OPEN" },
          ticketId: props.ticket.id,
        });
        emit("ticket-updated");
      } else {
        const result = await store.dispatch("tickets/createTicket", newTicket);
        console.log("Ticket created:", result);
        // Emit the new ticket to parent
        emit("ticket-created", newTicket);
      }

      // Reset form after successful submission
      Object.keys(form).forEach((key) => {
        form[key] = "";
      });
    } catch (error) {
      console.error("Error submitting ticket:", error);
      alert("Error submitting ticket. Please try again.");
    } finally {
      isSubmitting.value = false;
    }
  }
};
</script>
