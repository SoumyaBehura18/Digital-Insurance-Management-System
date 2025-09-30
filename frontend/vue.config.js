const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  transpileDependencies: true,

  // Set the app title (used by htmlWebpackPlugin.options.title in index.html)
  pluginOptions: {
    html: {
      title: 'InsureCore'
    }
  },

  devServer: {
    proxy: {
      '/api': {
        // 👉 Change this to your backend server URL
        target: 'http://localhost:9090',   // e.g. http://127.0.0.1:9090 or https://api.insurecore.com
        changeOrigin: true,
        pathRewrite: { '^/api': '' }
      }
    }
  }
})
