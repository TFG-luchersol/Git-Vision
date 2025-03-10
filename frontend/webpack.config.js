const path = require('path');
const HtmlWebpackPlugin = require("html-webpack-plugin");

module.exports = {
  mode: 'development',
  entry: './src/index.jsx', // o el archivo que sea tu punto de entrada
  output: {
    path: path.resolve(__dirname, 'dist'), // Directorio de salida
    filename: 'bundle.js', // Nombre del archivo de salida
    publicPath: "/",
  },
  plugins: [
    new HtmlWebpackPlugin({
      template: "./public/index.html", // Asegúrate de que este archivo existe
    }),
  ],
  devServer: {
    static: "./dist",
    port: 3000, 
    hot: true,
    open: true, 
    historyApiFallback: {
      disableDotRule: true, // 🔹 Permite rutas con puntos sin tratarlas como archivos
      rewrites: [{ from: /./, to: "/index.html" }], // 🔹 Redirige todo a index.html
    },
    proxy: [
      {
        context: ["/api"], // 🔹 Define qué rutas quieres redirigir
        target: "http://localhost:8080",
        changeOrigin: true,
        secure: false,
      },
    ],
  },
  module: {
    rules: [
      {
        test: /\.(js|jsx)$/, // Para procesar archivos JS/JSX
        exclude: /node_modules/,
        use: 'babel-loader',
      },
      {
        test: /\.css$/, // Para procesar archivos CSS
        use: ['style-loader', 'css-loader'], // Usa los loaders para CSS
      },
      {
        test: /\.(ts|tsx)$/, // Para procesar archivos TypeScript (.ts, .tsx)
        exclude: /node_modules/,
        use: 'ts-loader', // Usa ts-loader para archivos TypeScript
      },
      {
        test: /\.(png|jpg|jpeg|gif|svg)$/,
        type: "asset/resource", // Esto hace que Webpack copie las imágenes en "dist"
      },
    ],
  },
  resolve: {
    extensions: ['.js', '.jsx', '.ts', '.tsx', '.css'], // Para resolver extensiones sin escribirlas
    alias: {
      "@components": path.resolve(__dirname, "src/components"),
      "@screens": path.resolve(__dirname, "src/screens"),
      "@utils": path.resolve(__dirname, "src/utils"),
      "@css": path.resolve(__dirname, "src/static/css"),
      "@fonts": path.resolve(__dirname, "src/static/fonts"),
      "@services": path.resolve(__dirname, "src/services"),
    },
  },
};