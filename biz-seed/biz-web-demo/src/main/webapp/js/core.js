//全局路径
var ctx = "/biz-seed";


/******************************ajax**********************************/
var invalidSession = false;
// ajax 请求出错的情况下对 xhr 状态进行检查。
$(document).off('ajaxError').on('ajaxError', function(res, xhr) {
	if (xhr.status == 0) {
		return;
	} else if (xhr.status == 401 && !invalidSession) {
		invalidSession = !0;
		var result = JSON.parse(xhr.responseText);
		if(result.status=="E0008"){ //登录超时
			top.location.href = result.result;
		}else if(result.status=="E0007"){   //权限不足
			art.dialog.alert(result.message);
		}
	}
});
// session 过期则终止所有 ajax 请求。
$.ajaxPrefilter(function(options, originalOptions, xhr) {
	if (invalidSession) {
		xhr.abort();
	} else if (options.robot) {
		xhr.setRequestHeader('X-Robot', true);
	}
});
/******************************ajax**********************************/


/*****************************package********************************/
var coreEnvironment = {
	namespace_root : window,
	Package : function(ns) {
		var parent = coreEnvironment.namespace_root;
		var ns_a = ns.split('.');
		for ( var i = 0, j = ns_a.length; i < j; i++) {
			if (!parent[ns_a[i]]) {
				parent[ns_a[i]] = {};
			} else if (i == j - 1 && parent[ns_a[i]]) {
				try {
					console.log("namespace [ " + ns + " ] is exist!");
				} catch (e) {
				}
				return;
			}
			parent = parent[ns_a[i]];
		}
	},
	clone : function(obj) {
		function F() {
		}
		;
		F.prototype = obj;
		return new F;
	},
	extend : function(subClass, superClass) {
		var F = function() {
		};
		F.prototype = superClass.prototype;
		subClass.prototype = new F();
		subClass.prototype.constructor = subClass;
		subClass.superclass = superClass.prototype;
		if (superClass.prototype.constructor == Object.prototype.constructor) {
			superClass.prototype.constructor = superClass;
		}
	}
};
(function() {
	window.Package = coreEnvironment.Package;
	window.clone = coreEnvironment.clone;
})();

var _window = function() {
	var top = window, test = function(name) {
		try {
			var doc = window[name].document; // 跨域|无权限
			doc.getElementsByTagName; // chrome 本地安全限制
		} catch (e) {
			return false;
		}
		;

		return window[name].windowFlag
		// 框架集无法显示第三方元素
		&& doc.getElementsByTagName('frameset').length === 0;
	};

	if (test('top')) {
		top = window.top;
	} else if (test('parent')) {
		top = window.parent;
	}
	;
	return top;
}();
/*****************************package********************************/