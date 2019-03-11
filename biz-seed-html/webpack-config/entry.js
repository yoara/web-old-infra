//node 路径处理模块path
const path = require('path');
const config = require('./config');
var pages = require('./pages');
var entry = {};

// 遍历所有入口文件
pages.forEach(function(page) {
	//获取绝对路径
  	entry[page] = path.resolve(config.srcDir + '/pages', page + '/index.js');
});

module.exports = entry;
