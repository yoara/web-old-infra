//https://github.com/isaacs/node-glob
//用于返回匹配指定模式的文件名或目录
const glob = require('glob');
const config = require('./config');

// 遍历所有入口文件
const pages = new glob.Glob('!(_)*', {
  cwd: config.srcDir + '/pages',
  sync: true
}).found;

module.exports = pages;
