package com.company.seed.web.interceptor;

import com.company.seed.common.CommonConstants;
import com.company.seed.module.manager.model.ManagerModel;
import com.company.seed.module.user.model.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yoara.framework.component.web.bean.JsonCommonCodeEnum;
import org.yoara.framework.component.web.interceptor.ZBaseInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author yoara
 */
public class AccessInterceptor extends ZBaseInterceptorAdapter {
    private Logger loginLog = LoggerFactory.getLogger(this.getClass());
    //不需要登录的URL
    private List<String> skipUrl;
    //登录url
    private String loginUrl = "http://127.0.0.1:9091/login.html";
    /**超时或未登录，登录URL**/
    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        try {
            if (isSkipUrl(request)) {
                return toHandle(request, response, handler);
            }
            if (checkIsLogin(request)) {
                return toHandle(request, response, handler);
            } else {
                ResponseBody body = new ResponseBody();
                body.setCode(JsonCommonCodeEnum.E0008);
                body.setLoginUrl(loginUrl);
                printJsonMsg(response, body);
                return false;
            }
        } catch (Exception e) {
            loginLog.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 过滤不用拦截的请求
     **/
    private boolean isSkipUrl(HttpServletRequest request) {
        String urlPath = request.getRequestURL().toString();
        for (int i = 0; skipUrl != null && i < skipUrl.size(); i++) {
            if (urlPath.contains(skipUrl.get(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 通过执行下一步操作
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    private boolean toHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        return super.preHandle(request, response, handler);
    }

    /**
     * 验证当前是否登录
     * 和当前的登录信息是否有效
     * 验证当前用户是否有权限
     *
     * @return
     */
    private boolean checkIsLogin(HttpServletRequest request) {
        UserModel user = (UserModel) request.getSession().getAttribute(CommonConstants.CURRENT_LOGIN_USER);
        ManagerModel manager = (ManagerModel) request.getSession().getAttribute(CommonConstants.CURRENT_LOGIN_MANAGER);
        if ((user != null) || (manager != null)) {
            return true;
        }
        return false;
    }

    public void setSkipUrl(List<String> skipUrl) {
        this.skipUrl = skipUrl;
    }
}
