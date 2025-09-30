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
                <option value="" disabled selected>
                  {{
                    !form.relatedPolicy
                      ? "Please select a relevant policy if applicable"
                      : "Select a claim if relevant"
                  }}
                </option>
                <option
                  v-for="claim in claimsById"
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
          :disabled="isSubmitting || !isFormDirty"
          @click="handleButtonClick"
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
import { reactive, ref, watch, computed } from "vue";
import BaseButton from "@/components/BaseButton.vue";
import { useStore } from "vuex";
import { useToast } from "vue-toast-notification";
const toast = useToast();

const props = defineProps({
  userId: {
    type: Number,
    required: true,
  },
  ticket: {
    type: Object,
    default: () => null,
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
const emit = defineEmits(["ticket-created", "ticket-updated", "cancel"]);
const store = useStore();

const isSubmitting = ref(false);

// Form state
const form = reactive({
  subject: props.ticket?.subject || "",
  description: props.ticket?.description || "",
  relatedPolicy: props.ticket?.relatedPolicy || "",
  relatedClaim: props.ticket?.relatedClaim || "",
});

const selectedPolicy = ref(form.relatedPolicy);

const isFormDirty = computed(() => {
  if (!props.ticket) return true; // New ticket is always dirty
  return (
    form.subject !== props.ticket.subject ||
    form.description !== props.ticket.description ||
    form.relatedPolicy !== props.ticket.relatedPolicy ||
    form.relatedClaim !== props.ticket.relatedClaim
  );
});

watch(
  () => form.relatedPolicy,
  (newVal) => {
    selectedPolicy.value = newVal;
    form.relatedClaim = ""; // reset claim when policy changes
  }
);

watch(
  () => props.ticket,
  async (newTicket) => {
    if (!newTicket || !newTicket.id) return;

    try {
      const response = await store.dispatch(
        "tickets/fetchTicketById",
        newTicket.id
      );

      if (response) {
        form.subject = response.subject || "";
        form.description = response.description || "";
        form.relatedPolicy = response.policyId || "";
        form.relatedClaim = response.claimId || "";
      }
    } catch (err) {
      console.error("Error fetching ticket:", err);
    }
  },
  { immediate: true }
);

const claimsById = computed(() => {
  if (!props.claims || !props.claims.length || !selectedPolicy.value) return [];
  const policyId = Number(selectedPolicy.value); // convert to number
  return props.claims.filter((claim) => claim.userPolicyId === policyId);
});

const handleSubmit = async () => {
  if (!isFormDirty.value) {
    toast.info("No changes detected. Nothing to update.");
    return;
  }

  if (props.userId) {
    if (form.subject === "" || form.description === "") return;
    isSubmitting.value = true;

    try {
      const newTicket = {
        subject: form.subject.trim(),
        description: form.description.trim(),
        policyId: form.relatedPolicy || null,
        claimId: form.relatedClaim || null,
        userId: props.userId,
      };

      if (props.ticket && props.ticket.id) {
        await store.dispatch("tickets/updateTicket", {
          ticketData: { ...newTicket, status: "OPEN" },
          ticketId: props.ticket.id,
        });
        emit("ticket-updated");
        toast.success("Ticket updated successfully.", {
          position: "top-right",
        });
      } else {
        const result = await store.dispatch("tickets/createTicket", newTicket);
        emit("ticket-created", newTicket);
        toast.success("Ticket created successfully.", {
          position: "top-right",
        });
      }

      Object.keys(form).forEach((key) => {
        form[key] = "";
      });
    } catch (error) {
      console.error("Error submitting ticket:", error);
      toast.error("Error submitting ticket. Please try again.", {
        position: "top-right",
      });
    } finally {
      isSubmitting.value = false;
    }
  }
};
</script>
