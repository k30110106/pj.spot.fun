/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{js,jsx,ts,tsx}"],
  theme: {
    darkMode: 'media',
    // screens: {
    //   'tablet': '640px',
    //   // => @media (min-width: 640px) { ... }

    //   'laptop': '1024px',
    //   // => @media (min-width: 1024px) { ... }

    //   'desktop': '1920px',
    //   // => @media (min-width: 1280px) { ... }
    // },
    extend: {
      colors: {
        'custom-cyan': '#25E2B6'
      },
    },
  },
  plugins: [],
}
