package org.yoara.framework.component.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.yoara.framework.component.web.bean.JsonCommonCodeEnum;
import org.yoara.framework.component.web.bean.JsonReturnCodeEnum;
import org.yoara.framework.component.web.common.security.checkrequest.CheckRequestParamInterceptor;
import org.yoara.framework.component.web.common.security.encrypt.EncryptCheckResult;
import org.yoara.framework.component.web.common.security.encrypt.helper.RSAEncryptHelper;
import org.yoara.framework.component.web.common.util.CommonWebUtil;
import org.yoara.framework.component.web.common.validation.ValidationForm;
import org.yoara.framework.component.web.common.validation.ValidationResult;
import org.yoara.framework.component.web.common.validation.pool.ValidationPoolBean;
import org.yoara.framework.core.util.CommonStringUtil;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller 基类， 所有的controller 需要从此继承
 *
 * @author yoara
 */
public class BaseController {
    public static SerializerFeature[] features = {
            SerializerFeature.WriteMapNullValue,//输出值为null的字段
            SerializerFeature.WriteNullNumberAsZero, //数值字段如果为null,输出为0
            SerializerFeature.WriteNullStringAsEmpty, //字符类型字段如果为null,输出为""
            SerializerFeature.WriteNullListAsEmpty,//List字段如果为null,输出为[]
            SerializerFeature.WriteNullBooleanAsFalse,//Boolean字段如果为null,输出为false,而非null
            SerializerFeature.DisableCircularReferenceDetect};//禁止循环引用检测

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    protected ValidationPoolBean validationPoolBean;
    @Resource
    protected RSAEncryptHelper rsaEncryptHelper;
    /**
     * 校验传递的参数是否符合要求，传递的DO须通过JSR-303标注
     * @param param 待校验参数
     */
    protected ValidationResult validationParam(ValidationForm param){
        return validationPoolBean.validationParam(param);
    }


    /**
     * 校验RSA参数加密，省事儿模式{@link #rsaCheck(String, String, boolean)}
     * @return
     */
    protected EncryptCheckResult rsaCheck(){
        String encryptStr = getString(RSAEncryptHelper.RSA_PARAM_ENCRYPTSTR);
        boolean onlyMD5 = getBoolean(RSAEncryptHelper.RSA_PARAM_ONLYMD5,false);
        String key = getString(RSAEncryptHelper.RSA_PARAM_KEY);
        return rsaCheck(encryptStr,key,onlyMD5);
    }
    /**
     * 校验RSA参数加密
     * @param encryptStr 前端加密返回的加密字符串
     * @param onlyMD5 是否仅用md5加密
     * @param key 私钥缓存key
     * @return
     */
    protected EncryptCheckResult rsaCheck(String encryptStr, String key, boolean onlyMD5){
        //组装待验证参数串
        Map<String,String>  paramMap = rsaEncryptHelper.makeCheckMap(getRequest());
        if(paramMap==null){ //如果参数为空
            return EncryptCheckResult.EMPTY_ENCRYPTS;
        }
        if(encryptStr==null){ //如果加密字符串为空
            return EncryptCheckResult.EMPTY_ENCRYPTSTR;
        }
        if(key==null){ //如果key数为空
            return EncryptCheckResult.EMPTY_ENCRYPT_KEY;
        }
        return rsaEncryptHelper.rsaCheck(encryptStr,paramMap,key,onlyMD5);
    }

    /**
     * 全局的异常处理
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler({Exception.class})
    public String exception(Exception e) {
        log.error("Controller 错误信息:" + e.getMessage(), e);
        return returnWrong(JsonCommonCodeEnum.E0005);
    }

    public HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public ServletContext getServletContext() {
        return ContextLoader.getCurrentWebApplicationContext().getServletContext();
    }

    public int getInt(String name) {
        return getInt(name, 0);
    }

    public int getInt(String name, int defaultValue) {
        return CommonWebUtil.getInt(name,defaultValue);
    }

    /**
     * 查询request中的参数
     **/
    private String getRequestParameter(String name) {
        HttpServletRequest request = getRequest();
        if (request.getAttribute(CheckRequestParamInterceptor.PREFIX + name) != null) {
            return request.getAttribute(CheckRequestParamInterceptor.PREFIX + name).toString();
        }
        return request.getParameter(name);
    }


    public BigDecimal getBigDecimal(String name) {
        return getBigDecimal(name, null);
    }

    public BigDecimal getBigDecimal(String name, BigDecimal defaultValue) {
        return CommonWebUtil.getBigDecimal(name,defaultValue);
    }

    public String getString(String name) {
        return getString(name, null);
    }

    public String getString(String name, String defaultValue) {
        return CommonWebUtil.getString(name,defaultValue);
    }

    public String getClearHtmlElementString(String name) {
        return getClearHtmlElementString(name, null);
    }

    public String getClearHtmlElementString(String name, String defaultValue) {
        return CommonStringUtil.clearHtmlElement(getString(name, defaultValue));
    }

    public void outPrint(HttpServletResponse response, Object result) {
        try {
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.print(result.toString());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public Object outJson(HttpServletResponse response, String result) {
        try {
            response.setContentType("application/json;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(result);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public String[] getIpAddr(HttpServletRequest request) {
        return CommonWebUtil.getIpAddr(request);
    }

    public String redirect(HttpServletRequest request, HttpServletResponse response, String path, String ftlName) {
        try {
            response.sendRedirect(request.getContextPath() + path);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return ftlName;
    }

    public String redirect(String path) {
        return "redirect:" + path;
    }

    public String getMACAddress(String ip) {
        return CommonWebUtil.getMACAddress(ip);
    }

    public Cookie getCookie(String cookieName) {
        return CommonWebUtil.getCookie(cookieName);
    }

    /**
     * 获取日期类型参数值
     *
     * @param name   参数名
     * @param format 日期格式
     * @return
     */
    public Date getDate(String name, String format) {
        return CommonWebUtil.getDate(name,format);
    }

    public double getDouble(String name) {
        return getDouble(name, 0);
    }

    public double getDouble(String name, double defaultValue) {
        return CommonWebUtil.getDouble(name,defaultValue);
    }

    /**
     * 如果name是1 就返回true,否则返回false
     *
     * @param name
     * @param defaultValue
     * @return
     */
    public Boolean getBoolean(String name, boolean defaultValue) {
        return CommonWebUtil.getBoolean(name,defaultValue);
    }


    /**
     * 返回成功信息不带结果集
     **/
    protected String returnSuccessInfo() {
        return returnJsonInfoWithFilter(null, JsonCommonCodeEnum.C0000, null);
    }

    /**
     * 返回成功信息不带结果集
     **/
    protected String returnSuccessInfo(Object json) {
        return returnJsonInfoWithFilter(json, JsonCommonCodeEnum.C0000, null);
    }

    /**
     * 处理异常，返回出错信息
     **/
    protected String returnWrong(JsonReturnCodeEnum errCode) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", errCode.getStatus());
        result.put("message", errCode.getMessage());
        return JSON.toJSONString(result);
    }

    /**
     * 使用自定义消息
     **/
    protected String returnWithCustomMessage(String message, JsonReturnCodeEnum code) {
        Map<String, Object> result = new HashMap<>();
        result.put("status", code.getStatus());
        result.put("message", message);
        return JSON.toJSONString(result);
    }

    /**
     * 返回信息
     **/
    protected String returnJsonInfo(Object json, JsonReturnCodeEnum code) {
        return returnJsonInfoWithFilter(json, code, null);
    }

    /**
     * 处理成功，返回信息带过滤器过滤属性
     **/
    protected String returnJsonInfoWithFilter(Object json,
                                              JsonReturnCodeEnum code, SerializeFilter[] filters) {
        Map<String, Object> result = new HashMap<>();
        result.put("status", code.getStatus());
        result.put("message", code.getMessage());
        if (json != null) {
            result.put("result", json);
        }
        if (filters != null) {
            return JSON.toJSONString(result, filters, features);
        }
        return JSON.toJSONString(result, features);
    }
}
