var config = require('config');
var invalidSession = false;

require('vendors/bs_3_3_1/css/bootstrap.min.css');
require('vendors/bs_3_3_1/js/bootstrap.min');
require('css/base.css');

// setup the default parameter for all of the ajax requests
$.ajaxSetup({
  cache: false,
  xhrFields: {
    withCredentials: true
  }
});

/*$(document).on('ajaxBeforeSend', function(e, xhr) {
  var userInfo = amplify.store.sessionStorage('userInfo');
  if (userInfo) {
    xhr.setRequestHeader('sid', userInfo.sessionId);
  }

}); */

// whenever an ajax request completes with an error, check the xhr status;
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

// abort the all the ajax requests when the session is expired.
$.ajaxPrefilter(function(options, originalOptions, xhr) {
  if (invalidSession) {
    xhr.abort();
  } else if (options.robot) {
    xhr.setRequestHeader('X-Robot', true);
  } else if (options.loading) {
    //options.layerId = layer.msg('加载中...', {icon: 16});
  }
});

$(document).off('ajaxComplete').on('ajaxComplete', function(e, req, options) {
  if (options.loading) {
    //layer.close(options.layerId);
  }
});
