import { vi } from "vitest";

global.plugins = [
  {
    install(app) {
      app.config.globalProperties.$store = {
        dispatch: vi.fn(),
        commit: vi.fn(),
        state: { userPolicies: { policies: [] } },
        getters: {
          "userPolicies/getPolicies": [],
          "userPolicies/isLoading": false,
        },
      };
    },
  },
];
