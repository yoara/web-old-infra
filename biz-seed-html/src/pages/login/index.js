var common = require('common');
var config = require('config');
var tpl = require('./index.html');
var header = require('components/header/index');
var footer = require('components/footer/index');
require('./index.css');


// 替换页面
$("#app").append(tpl);

//替换模板
$("#header").append(header.tpl);
$("#footer").append(footer.tpl);

//具体处理
var me = {
	init: function(){
		this.initEvent();
		this.initInfo();
	},
	initInfo: function(){

	},
	initEvent: function(){
		$("button[key=userLogin]").on("click", function(){
			debugger
			var data = {
				username:$("#inputUsername").val(),
				password:$("#inputPassword").val()
			}
			$.ajax({
				url: config.ctx + "/login/user/loginin",
				data:data,
				dataType:"json",
				success: function (data) {
					$("#resultDiv").show();
					if(data.result=='ok'){
						$("#result").text("done");
					}else{
						$("#result").text(data.msg);
					}
				},
				beforeSend:function(){
					$("#resultDiv").hide();
				}
			});
		});
	}
};

$(document).ready(function(){
	me.init();
});
