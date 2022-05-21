const path = require('path')

module.exports = {
  publicPath: '/',
  css: {
    loaderOptions: {
      stylus: {
        'resolve url': true,
        import: [
          './src/assets/styl/theme'
        ]
      }
    }
  },
  pluginOptions: {
  },
  configureWebpack: {
    resolve: {
      alias: {
        styl: path.join(__dirname, 'src/assets/styl')
      }
    },
    externals: {
    }
  },
  devServer: {
    proxy: {
      '/': {
        target: 'http://localhost:8888',
        changeOrigin: true
      }
    },
    port: 8888
  }
}
