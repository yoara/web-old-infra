package org.yoara.framework.component.web.common.security.checkrequest.policy;

/**
 * Created by yoara on 2015/12/28.
 * 判断结果返回对象
 */
public class CheckResult {
    //输入的字符串
    private String checkObject;
    //校验是否通过
    private boolean isValid = true;
    //返回消息
    private String msg;
    //替换的字符串
    private String replaceObject;
    //外部处理类
    private Object handler;

    public Object getHandler() {
        return handler;
    }

    public void setHandler(Object handler) {
        this.handler = handler;
    }

    public void setCheckObject(String checkObject) {
        this.checkObject = checkObject;
    }

    public String getReplaceObject() {
        return replaceObject;
    }

    public String getCheckObject() {
        return checkObject;
    }

    public void setReplaceObject(String replaceObject) {
        this.replaceObject = replaceObject;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setIsValid(boolean isValid) {
        this.isValid = isValid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
