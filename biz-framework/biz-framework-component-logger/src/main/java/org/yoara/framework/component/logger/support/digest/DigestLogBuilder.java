package org.yoara.framework.component.logger.support.digest;


import com.alibaba.fastjson.JSON;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;
import org.yoara.framework.component.logger.support.logger.LoggerBuilder;
import org.yoara.framework.component.logger.support.webaccess.RequestContext;
import org.yoara.framework.component.logger.support.webaccess.RequestContextHolder;
import org.yoara.framework.core.util.spring.SpringPropertyReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static eu.bitwalker.useragentutils.BrowserType.ROBOT;

public abstract class DigestLogBuilder {

    public static final String WEB_ACCESS_PARAMS = "web.access.params";

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    public static String buildStartDiagnosisDigest(HttpServletRequest request){

        RequestContext digestContext = RequestContextHolder.getDigestContext();
        Map<String, String> parameterMap = WebUtil.getParamterMap(request);

        StringBuffer sb = new StringBuffer();
        sb.append(getValue(digestContext.getLoginAccount()));
        sb.append(DigestLogSymbol.LOG_DELIMITER);
        sb.append(request.getRequestURI());
        sb.append(DigestLogSymbol.LOG_DELIMITER);
        sb.append(getValue(buildParameterDigest(parameterMap)));

        return sb.toString();
    }

    /**
     * 访问时间#请求标示#应用名#登录账户#执行状态#执行时间#请求域名#请求URI#请求参数#响应结果#refere(来源)#客户端IP#服务端IP#操作系统#浏览器信息#cookieId#sessionId#宽x高
     */
    public static String buildWebAccessDigest(HttpServletRequest request, HttpServletResponse response,
                                              Boolean success, String result) throws Exception {
        String userAgent = request.getHeader(WebUtil.USER_AGENT_NAME);
        UserAgent clientAgent = UserAgent.parseUserAgentString(userAgent);
        if(clientAgent.getBrowser().getBrowserType().equals(ROBOT)){

        }
        RequestContext digestContext = RequestContextHolder.getDigestContext();

        long startTime = digestContext.getTimeKey();
        long endTime = System.currentTimeMillis();

        String screenResolution = null;
        if(WebUtil.getString(request, "width") != null && WebUtil.getString(request, "height") != null){
            screenResolution = WebUtil.getString(request, "width") + "x" + WebUtil.getString(request, "height");
        }
        StringBuilder digestBuilder = new StringBuilder();
        digestBuilder.append(getValue(new Date(startTime)));
        digestBuilder.append(DigestLogSymbol.LOG_SEP_JIN);
        digestBuilder.append(getValue(RequestContextHolder.getDigestContext().getRequestId()));
        digestBuilder.append(DigestLogSymbol.LOG_SEP_JIN);
        digestBuilder.append(getValue(LoggerBuilder.LOGGER_APP_NAME));
        digestBuilder.append(DigestLogSymbol.LOG_SEP_JIN);
        digestBuilder.append(getValue(digestContext == null ? null : digestContext.getLoginAccount()));
        digestBuilder.append(DigestLogSymbol.LOG_SEP_JIN);
        digestBuilder.append(getValue(success));
        digestBuilder.append(DigestLogSymbol.LOG_SEP_JIN);
        digestBuilder.append(getValue(endTime - startTime));
        digestBuilder.append(DigestLogSymbol.LOG_SEP_JIN);
        digestBuilder.append(getValue(WebUtil.getHostContext(request)));
        digestBuilder.append(DigestLogSymbol.LOG_SEP_JIN);
        //URI
        digestBuilder.append(replaceUnNeed(getValue(request.getRequestURI())));
        digestBuilder.append(DigestLogSymbol.LOG_SEP_JIN);
        if(SpringPropertyReader.getProperty(WEB_ACCESS_PARAMS)!=null){
            Map<String, String> parameterMap = WebUtil.getParamterMap(request);
            digestBuilder.append(replaceUnNeed(getValue(buildParameterDigest(parameterMap))));
        }else{
            digestBuilder.append(getValue(""));
        }
        digestBuilder.append(DigestLogSymbol.LOG_SEP_JIN);
        digestBuilder.append(replaceUnNeed(getValue(result)));
        digestBuilder.append(DigestLogSymbol.LOG_SEP_JIN);
        //Referer，外网/stat请求的referer是reqeust中的referer参数
        digestBuilder.append(replaceUnNeed(getValue(request.getHeader("Referer"))));
        digestBuilder.append(DigestLogSymbol.LOG_SEP_JIN);
        digestBuilder.append(StringUtils.join(WebUtil.getIpAddr(request),","));
        digestBuilder.append(DigestLogSymbol.LOG_SEP_JIN);
        digestBuilder.append(getValue(InetAddress.getLocalHost().getHostAddress()));
        digestBuilder.append(DigestLogSymbol.LOG_SEP_JIN);
        digestBuilder.append(getValue(clientAgent==null ? null : clientAgent.getOperatingSystem().getName()));
        digestBuilder.append(DigestLogSymbol.LOG_SEP_JIN);
        digestBuilder.append(getValue(clientAgent==null ? null : clientAgent.getBrowser().getName()));
        digestBuilder.append(DigestLogSymbol.LOG_SEP_JIN);
        digestBuilder.append(getValue(WebUtil.getCookieId(request)));
        digestBuilder.append(DigestLogSymbol.LOG_SEP_JIN);
        digestBuilder.append(getValue(digestContext.getSessionId()));
        digestBuilder.append(DigestLogSymbol.LOG_SEP_JIN);
        digestBuilder.append(getValue(screenResolution));

        return digestBuilder.toString();
    }

    public static String buildParameterDigest(Map<String, String> parameterMap) {
        StringBuilder paramsDigestBuilder = new StringBuilder();
        if (!CollectionUtils.isEmpty(parameterMap)) {
            paramsDigestBuilder.append(DigestLogSymbol.LOG_PARAM_PREFIX);
            Set<String> keySet = parameterMap.keySet();
            for(String key : keySet){
                String value = parameterMap.get(key);
                paramsDigestBuilder.append(key + "=" + value).append(DigestLogSymbol.LOG_SEP);
            }
            paramsDigestBuilder.substring(0, paramsDigestBuilder.length() - 1);
            paramsDigestBuilder.append(DigestLogSymbol.LOG_PARAM_SUFFIX);
        } else {
            paramsDigestBuilder.append(DigestLogSymbol.LOG_DEFAULT);
        }

        return paramsDigestBuilder.toString();
    }

    public static String buildMqDigest(String mqKey, String msgId,
                  long startTime, String role, String type, Boolean successed, String msg, Object param){
        long endTime = System.currentTimeMillis();

        StringBuffer digestBuffer = new StringBuffer();
        digestBuffer.append(getValue(new Date(startTime)));
        digestBuffer.append(DigestLogSymbol.LOG_SEP_JIN);
        digestBuffer.append(getValue(LoggerBuilder.LOGGER_APP_NAME));
        digestBuffer.append(DigestLogSymbol.LOG_SEP_JIN);
        digestBuffer.append(getValue(role));
        digestBuffer.append(DigestLogSymbol.LOG_SEP_JIN);
        digestBuffer.append(getValue(type));
        digestBuffer.append(DigestLogSymbol.LOG_SEP_JIN);
        digestBuffer.append(getValue(mqKey));
        digestBuffer.append(DigestLogSymbol.LOG_SEP_JIN);
        digestBuffer.append(getValue(msgId));
        digestBuffer.append(DigestLogSymbol.LOG_SEP_JIN);
        digestBuffer.append(getValue(successed));
        digestBuffer.append(DigestLogSymbol.LOG_SEP_JIN);
        digestBuffer.append(getValue(msg));
        digestBuffer.append(DigestLogSymbol.LOG_SEP_JIN);
        digestBuffer.append(getValue(endTime - startTime));
        digestBuffer.append(DigestLogSymbol.LOG_SEP_JIN);
        digestBuffer.append(getValue(JSON.toJSONString(param)));
        return digestBuffer.toString();
    }

    private static String getValue(String value) {
        return StringUtils.isBlank(value) ? DigestLogSymbol.LOG_DEFAULT : value;
    }

    private static String getValue(Long value) {
        return value == null ? DigestLogSymbol.LOG_DEFAULT : value.toString();
    }

    private static String getValue(Boolean value) {
        return value == null ? DigestLogSymbol.LOG_DEFAULT : value.toString();
    }

    private static String getValue(Date value) {
        return value == null ? DigestLogSymbol.LOG_DEFAULT : simpleDateFormat
                .format(value);
    }

    private static String replaceUnNeed(String str) {
        if (str!=null) {
            Pattern p = Pattern.compile("\t|\r|\n|#");
            Matcher m = p.matcher(str);
            String dest = m.replaceAll("");
            return dest;
        }
        return str;
    }

}