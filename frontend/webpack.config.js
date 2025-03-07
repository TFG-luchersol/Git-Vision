const path = require("path")

module.exports = {
  resolve: {
    alias: {
      "@CSS": path.resolve(__dirname, 'src/static/css')
    }
  },
  devServer: {
    allowedHosts: ["all"]
  }
};
