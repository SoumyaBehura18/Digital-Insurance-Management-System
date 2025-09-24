/* eslint-env node */
module.exports = {
  root: true,
  env: {
    browser: true,
    node: true,
    es2021: true,
    // 'jest/globals': true,
  },
  plugins: ['vue'],
  extends: [
    "eslint:recommended",
    "plugin:vue/vue3-recommended",
    // "plugin:jest/recommended",
  ],
  parserOptions: {
    ecmaVersion: 2021,
    sourceType: "module",
  },
  globals: {
    defineProps: "readonly",
    defineEmits: "readonly",
    defineExpose: "readonly",
    withDefaults: "readonly",
  },
  rules: {
    "no-unused-vars": "off",
    "tailwindcss/no-custom-classname": "off",
    "tailwindcss/classnames-order": "off",
    "tailwindcss/migration-from-tailwind-2": "off",

    // Vue-specific rules to disable
    "vue/html-self-closing": "off",
    "vue/max-attributes-per-line": "off",
    "vue/no-v-html": "off",
    "vue/attributes-order": "off",
    "vue/singleline-html-element-content-newline": "off",
    "vue/first-attribute-linebreak": "off",
    "vue/html-indent": "off",
    "vue/html-closing-bracket-newline":"off",
    "vue/attribute-hyphenation":"off"

  },
};

/* eslint-env node */
module.exports = {
  root: true,
  env: {
    browser: true,
    node: true,
    es2021: true,
    // 'jest/globals': true,
  },
  plugins: ['vue'],
  extends: [
    "eslint:recommended",
    "plugin:vue/vue3-recommended",
    // "plugin:jest/recommended",
  ],
  parserOptions: {
    ecmaVersion: 2021,
    sourceType: "module",
  },
  globals: {
    defineProps: "readonly",
    defineEmits: "readonly",
    defineExpose: "readonly",
    withDefaults: "readonly",
  },
  rules: {
    "no-unused-vars": "off",
    "tailwindcss/no-custom-classname": "off",
    "tailwindcss/classnames-order": "off",
    "tailwindcss/migration-from-tailwind-2": "off",

    // Vue-specific rules to disable
    "vue/html-self-closing": "off",
    "vue/max-attributes-per-line": "off",
    "vue/no-v-html": "off",
    "vue/attributes-order": "off",
    "vue/singleline-html-element-content-newline": "off",
    "vue/first-attribute-linebreak": "off",
    "vue/html-indent": "off",
    "vue/html-closing-bracket-newline":"off",
    "vue/attribute-hyphenation":"off",
    "vue/require-explicit-emits":"off",
    "vue/require-default-prop":"off",
    "vue/html-closing-bracket-spacing":"off"

  },
};
