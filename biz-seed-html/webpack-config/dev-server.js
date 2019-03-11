// dev server，定义反向代理
const devServer = {
  historyApiFallback: true, 
  hot: true,    //热部署
  inline: true, //inline模式
  stats: {
    colors: true
  },
  proxy: {      //配置代理
    '/biz-seed/*': {
      target:'http://localhost:9091',
      changeOrigin: true
    }
  }
};

module.exports = devServer;
