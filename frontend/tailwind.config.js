/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./index.html", "./src/**/*.{vue,js,ts,jsx,tsx}"],
  theme: {
    extend: {
      colors: {
        theme: {
          DEFAULT: "#6c63ff",
          hover: "#7a73ff",
        },
      },
    },
  },
  plugins: [],
};
