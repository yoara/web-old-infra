package org.yoara.framework.component.web.common.security.encrypt.helper;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yoara.framework.component.web.common.security.encrypt.EncryptCheckResult;
import org.yoara.framework.core.util.encrypt.MD5Util;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yoara on 2016/7/27.
 */
public abstract class AbstractEncryptHelper {
    protected Logger log = LoggerFactory.getLogger(this.getClass());

    protected static final String CHARACTER = "UTF-8";


    /**
     * 校验（加密）签名字符串
     * @param sign 客户端传递过来的已签名的字符串
     * @param o 参数对象
     * @param key 获取私钥的key值，key值依赖分布式缓存
     * @param onlyMD5 如果true，则只校验MD5签名
     */
    public EncryptCheckResult rsaCheck(String sign, Object o, String key, boolean onlyMD5){
        try {
            String paramMD5 = getSign(o);
            return checkSign(sign, paramMD5,key,onlyMD5);
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(),e);
            return EncryptCheckResult.CHECK_FAIL;
        }
    }

    /**
     * 校验（加密）签名字符串
     * @param sign 客户端传递过来的已签名的字符串
     * @param map 参数Map
     * @param key 获取私钥的key值
     * @param onlyMD5 如果true，则只校验MD5签名
     */
    public EncryptCheckResult rsaCheck(String sign, Map<String,String> map, String key, boolean onlyMD5){
        String paramMD5 = getSign(map);
        return checkSign(sign, paramMD5,key,onlyMD5);
    }

    /**
     * 参数排序并用md5签名
     * @param o 要参与签名的数据对象
     * @return 签名
     * @throws IllegalAccessException
     */
    private String getSign(Object o) throws IllegalAccessException {
        ArrayList<String> list = getParamListByObject(o);
        return returnSign(list);
    }

    /**
     * 参数排序并用md5签名
     * @param map 要参与签名的数据对象
     * @return 签名
     * @throws IllegalAccessException
     */
    private String getSign(Map<String,String> map){
        ArrayList<String> list = getParamListByMap(map);
        return returnSign(list);
    }

    private String returnSign(ArrayList<String> list) {
        int size = list.size();
        String [] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i ++) {
            sb.append(arrayToSort[i]);
        }
        //将最后一个分割字符去掉
        sb.deleteCharAt(sb.length()-1);
        String result = sb.toString();
        try {
            result = MD5Util.getMD5(result.getBytes(CHARACTER)).toUpperCase();
        } catch (UnsupportedEncodingException e) {}
        return result;
    }

    private ArrayList<String> getParamListByMap(Map<String, String> map) {
        ArrayList<String> list = new ArrayList<String>();
        for(Map.Entry<String,String> entry:map.entrySet()){
            if(entry.getValue()!=""){
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        return list;
    }

    private ArrayList<String> getParamListByObject(Object o) throws IllegalAccessException {
        ArrayList<String> list = new ArrayList<String>();
        Class cls = o.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            if (f.get(o) != null && f.get(o) != "") {
                list.add(f.getName() + "=" + f.get(o) + "&");
            }
        }
        return list;
    }

    /**
     * 获取需要加密的参数串
     * @param request
     * @return
     */
    protected Map<String,String> makeCheckMap(HttpServletRequest request,String paramAttrs){
        if(StringUtils.isEmpty(paramAttrs)){
            return null;
        }
        String[] paramKeys = paramAttrs .split(",");
        Map<String,String[]> allParams = request.getParameterMap();
        Map<String,String> checkParam = new HashMap<>();
        for(String paramKey:paramKeys){
            String[] params = allParams.get(paramKey);
            if(params!=null){
                checkParam.put(paramKey, StringUtils.join(params,","));
                continue;
            }
        }
        return checkParam;
    }

    protected abstract EncryptCheckResult checkSign(String sign, String paramMD5, String key, boolean onlyMD5);
}
