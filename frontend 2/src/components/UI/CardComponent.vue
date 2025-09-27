<template>
  <!-- Main Card -->
  <div
    data-slot="card"
    class="bg-card text-card-foreground flex flex-col gap-6 rounded-xl border"
    v-bind="$attrs"
  >
    <slot />
  </div>
</template>

<script>
import { h } from "vue";

export default {
  name: "CardComponent",
};

// Sub-components attached to default export
export const CardHeader = {
  name: "CardHeader",
  render() {
    return h(
      "div",
      {
        "data-slot": "card-header",
        class:
          "@container/card-header grid auto-rows-min grid-rows-[auto_auto] items-start gap-1.5 px-6 pt-6 has-data-[slot=card-action]:grid-cols-[1fr_auto] [.border-b]:pb-6",
        ...this.$attrs,
      },
      this.$slots.default()
    );
  },
};

export const CardTitle = {
  name: "CardTitle",
  render() {
    return h(
      "h4",
      { "data-slot": "card-title", class: "leading-none", ...this.$attrs },
      this.$slots.default()
    );
  },
};

export const CardDescription = {
  name: "CardDescription",
  render() {
    return h(
      "p",
      { "data-slot": "card-description", class: "text-muted-foreground", ...this.$attrs },
      this.$slots.default()
    );
  },
};

export const CardAction = {
  name: "CardAction",
  render() {
    return h(
      "div",
      {
        "data-slot": "card-action",
        class: "col-start-2 row-span-2 row-start-1 self-start justify-self-end",
        ...this.$attrs,
      },
      this.$slots.default()
    );
  },
};

export const CardContent = {
  name: "CardContent",
  render() {
    return h(
      "div",
      { "data-slot": "card-content", class: "px-6 [&:last-child]:pb-6", ...this.$attrs },
      this.$slots.default()
    );
  },
};

export const CardFooter = {
  name: "CardFooter",
  render() {
    return h(
      "div",
      { "data-slot": "card-footer", class: "flex items-center px-6 pb-6 [.border-t]:pt-6", ...this.$attrs },
      this.$slots.default()
    );
  },
};
</script>
