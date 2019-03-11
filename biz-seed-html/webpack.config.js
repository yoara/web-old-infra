var webpack = require('webpack');

const _webpack = {
  //devtool: 'source-map',
  devServer: require('./webpack-config/dev-server'),	//加载webpack-server-dev组件
  entry: require('./webpack-config/entry'),				//模块加载入口js
  module: require('./webpack-config/modules'),			//配置要使用的loader。对文件进行一些相应的处理
  output: require('./webpack-config/output'),			//定义打包编译文件输出
  plugins: require('./webpack-config/plugins'),
  resolve: {
  	//模块加载入口
    modulesDirectories: ['node_modules', 'src', 'src/pages', 'src/assets']
  },
};

module.exports = _webpack;