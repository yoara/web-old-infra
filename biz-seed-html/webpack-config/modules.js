/**
loader配置项：

    test:     /\.(js|jsx)$/,//注意是正则表达式，不要加引号，匹配要处理的文件
    loader:   'eslint-loader',//要使用的loader，"-loader"可以省略
    include:  [path.resolve(__dirname, "src/app")],//把要处理的目录包括进来
    exclude:  [nodeModulesPath]//排除不处理的目录
**/

const path = require('path');
const config = require('./config');

const ExtractTextPlugin = require('extract-text-webpack-plugin');

// 定义所需的 loader
const modules = {
  // noParse: [
  //   config.srcDir + '/assets/vendor/qui/js/bootstrap.min',
  //   'node_modules/jquery/dist/jquery.min',
  //   config.srcDir + '/assets/vendors/qui/css/bootstrap.min.css'
  // ],
  loaders: [
    { //配合plugins.js_webpack.ProvidePlugin一起使用
      //引入第三方模式
      test: require.resolve(path.resolve('src/assets/vendors/jquery_1_11_1/jquery-1.11.1.min.js')),
      //test: require.resolve('jquery'),
      loader: 'expose?$!expose?jQuery'
    }, 
    {
      test: /\.html$/,
      include: config.srcDir,
      loader: 'dot-loader'
    }, {
      test: /\.css$/,
      include: config.srcDir,
      loader: ExtractTextPlugin.extract('happypack/loader?id=css')

      // loader: ExtractTextPlugin.extract('css?minimize&-autoprefixer!postcss')
    }, {
      test: /\.(png|jpg|gif)$/,
      include: config.srcDir,
      loader: 'file?name=static/img/[name].[ext]'
    },
    /*{
         // iconfont
         test: /\.(woff|woff2|svg|eot|ttf)\??.*$/,
         //include: path.resolve(config.srcDir, './assets/vendors/'),
         loaders: ['happypack/loader?id=iconfont']

         //loader: 'file?name=static/fonts/[name].[ext]'
       }*/
/*
    { test: /\.ttf\??.*$/, loader: 'happypack/loader?id=font-ttf' },
    { test: /\.eot\??.*$/, loader: 'happypack/loader?id=font-eot' },
    { test: /\.svg\??.*$/, loader: 'happypack/loader?id=font-svg' },
    { test: /\.woff\??.*$/, loader: 'happypack/loader?id=font-woff' },
    { test: /\.woff2\??.*$/, loader: 'happypack/loader?id=font-woff2' }

*/

    { test: /\.woff\??.*$/, loader: 'file?name=static/fonts/[name].[ext]&minetype=application/font-woff' },
    { test: /\.woff2\??.*$/, loader: 'file?name=static/fonts/[name].[ext]&minetype=application/font-woff' },
    { test: /\.ttf\??.*$/, loader: 'file?name=static/fonts/[name].[ext]&minetype=application/octet-stream' },
    { test: /\.eot\??.*$/, loader: 'file?name=static/fonts/[name].[ext]' },
    { test: /\.svg\??.*$/, loader: 'file?name=static/fonts/[name].[ext]&minetype=image/svg+xml' }

  ]
};

module.exports = modules;
