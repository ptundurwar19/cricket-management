/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        cricket: {
          dark: '#0B132B',
          navy: '#1C2541',
          teal: '#3A506B',
          cyan: '#48E5C2',
          gold: '#FFD166',
          coral: '#EF476F',
          green: '#06D6A0',
        }
      },
      animation: {
        'pulse-fast': 'pulse 1s cubic-bezier(0.4, 0, 0.6, 1) infinite',
        'bounce-short': 'bounce 0.6s ease-in-out infinite alternate',
      }
    },
  },
  plugins: [],
}
