
// tailwind.config.js
export default {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        brand: {
          backgroundTheme: "#6c63ff",
          textTheme:"#717182"
        },
      },
    },
  },
  plugins: [],
};
