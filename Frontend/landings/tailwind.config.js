/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{html,js}"],
  theme: {
    colors: {
      transparent: "transparent",
      white: '#FFFFFF',
      uranium: '#ADD7F6',
      jordy:'#87BFFF',
      blue: '#3F8EFC',
      neon: '#2667FF',
      chrysler: '#3B28CC'
    },
    extend: {
      fontFamily: {
        inter: ['Inter', 'sans-serif'], // Add Inter to your fontFamily
      },
    },
  },
  plugins: [],
}

