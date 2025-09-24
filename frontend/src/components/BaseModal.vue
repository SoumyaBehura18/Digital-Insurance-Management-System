<template>
  <teleport to="body">
    <transition
      enter-active-class="duration-300 ease-out"
      enter-from-class="opacity-0"
      enter-to-class="opacity-100"
      leave-active-class="duration-200 ease-in"
      leave-from-class="opacity-100"
      leave-to-class="opacity-0"
    >
      <div
        v-if="isOpen"
        class="fixed inset-0 z-50 overflow-y-auto"
        @click="handleBackdropClick"
      >
        <!-- Backdrop -->
        <div
          class="fixed inset-0 bg-black bg-opacity-50 transition-opacity"
        ></div>

        <!-- Modal Container -->
        <div class="flex min-h-full items-center justify-center p-4">
          <transition
            enter-active-class="duration-300 ease-out"
            enter-from-class="opacity-0 scale-95"
            enter-to-class="opacity-100 scale-100"
            leave-active-class="duration-200 ease-in"
            leave-from-class="opacity-100 scale-100"
            leave-to-class="opacity-0 scale-95"
          >
            <div
              v-if="isOpen"
              :class="[
                'relative bg-white rounded-lg shadow-xl transform transition-all',
                sizeClasses,
              ]"
              @click.stop
            >
              <!-- Header -->
              <div
                class="flex items-center justify-between p-6 border-b border-gray-200"
              >
                <div>
                  <h3 class="text-lg font-semibold text-gray-900">
                    {{ title }}
                  </h3>
                  <p v-if="subtitle" class="text-sm text-gray-500 mt-1">
                    {{ subtitle }}
                  </p>
                </div>
                <button
                  @click="$emit('close')"
                  class="text-gray-400 hover:text-gray-600 transition-colors"
                >
                  <svg
                    class="w-6 h-6"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                  >
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      stroke-width="2"
                      d="M6 18L18 6M6 6l12 12"
                    ></path>
                  </svg>
                </button>
              </div>

              <!-- Content -->
              <div class="p-6">
                <slot></slot>
              </div>

              <!-- Footer (if provided) -->
              <div
                v-if="$slots.footer"
                class="px-6 py-4 bg-gray-50 border-t border-gray-200 rounded-b-lg"
              >
                <slot name="footer"></slot>
              </div>
            </div>
          </transition>
        </div>
      </div>
    </transition>
  </teleport>
</template>

<script setup>
import { computed, onMounted, onUnmounted } from "vue";

// Props
const props = defineProps({
  isOpen: {
    type: Boolean,
    required: true,
  },
  title: {
    type: String,
    required: true,
  },
  subtitle: {
    type: String,
    default: "",
  },
  size: {
    type: String,
    default: "md", // sm | md | lg | xl
    validator: (value) => ["sm", "md", "lg", "xl"].includes(value),
  },
  closeOnBackdrop: {
    type: Boolean,
    default: true,
  },
});

// Emits
const emit = defineEmits(["close"]);

// Computed
const sizeClasses = computed(() => {
  const sizes = {
    sm: "max-w-sm w-full",
    md: "max-w-md w-full",
    lg: "max-w-2xl w-full",
    xl: "max-w-4xl w-full",
  };
  return sizes[props.size];
});

// Methods
const handleBackdropClick = () => {
  if (props.closeOnBackdrop) {
    emit("close");
  }
};

const handleEscapeKey = (event) => {
  if (event.key === "Escape" && props.isOpen) {
    emit("close");
  }
};

// Lifecycle
onMounted(() => {
  document.addEventListener("keydown", handleEscapeKey);
  // Prevent body scroll when modal is open
  if (props.isOpen) {
    document.body.style.overflow = "hidden";
  }
});

onUnmounted(() => {
  document.removeEventListener("keydown", handleEscapeKey);
  // Restore body scroll
  document.body.style.overflow = "";
});

// Watch for modal open/close to manage body scroll
import { watch } from "vue";
watch(
  () => props.isOpen,
  (newValue) => {
    if (newValue) {
      document.body.style.overflow = "hidden";
    } else {
      document.body.style.overflow = "";
    }
  }
);
</script>
