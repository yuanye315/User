// vite.config.js
import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';

export default defineConfig({
  plugins: [vue()],
  server: {
    proxy: {
      '/users': {
        target: 'http://localhost:8080', // 你的后端 API 地址
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/users/, '/users'),
      },
    },
  },
});

