package org.yoara.framework.component.web.common.security.encrypt.helper;

import org.springframework.stereotype.Component;
import org.yoara.framework.component.web.cache.BridgeCache;
import org.yoara.framework.component.web.cache.CacheConstants;
import org.yoara.framework.component.web.common.security.encrypt.EncryptCheckResult;
import org.yoara.framework.core.util.encrypt.AESUtil;

import javax.annotation.Resource;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.UUID;

/**
 * 提供RSA秘钥存储，参数生成等应用层所需的一些工具方法
 * Created by yoara on 2016/7/19.
 */
@Component("aesEncryptHelper")
public class AESEncryptHelper extends AbstractEncryptHelper{
    @Resource
    private BridgeCache bridgeCache;
    /** 待校验的参数名，值为字符串，多个用","分开 **/
    public static final String AES_PARAM_ENCRYPTS = "AES_PARAM_ENCRYPTS";
    /** 是否仅MD5加密，值为boolean **/
    public static final String AES_PARAM_ONLYMD5 = "AES_PARAM_ONLYMD5";
    /** 加密后的字符串 **/
    public static final String AES_PARAM_ENCRYPTSTR = "AES_PARAM_ENCRYPTSTR";
    /** 存储的键值 **/
    public static final String AES_PARAM_KEY = "AES_PARAM_KEY";
    /**
     * 获取需要加密的参数串
     * @param request
     * @return
     */
    public Map<String,String> makeCheckMap(HttpServletRequest request){
        String paramAttrs = request.getParameter(AES_PARAM_ENCRYPTS);
        return makeCheckMap(request,paramAttrs);
    }

    /**
     * 将生成的AES秘钥存储至分布式缓存
     * @param keyByte 生成的加密秘钥数组
     * @param expire 存储超时时间,单位为秒，最多不超过30分钟
     * @return 缓存的key
     */
    public String putAESSecretKeySpec(byte[] keyByte,int expire){
        SecretKeySpec keySpec = AESUtil.getAESSecretKey(keyByte);
        if(expire>30*60){
            expire = 30*60;
        }
        String key = UUID.randomUUID().toString();
        bridgeCache.put(CacheConstants.CACHE_ENCRYPT_AES,key,keySpec,expire);
        return key;
    }

    @Override
    protected EncryptCheckResult checkSign(String sign, String paramMD5, String key, boolean onlyMD5){
        if(onlyMD5){
            return sign.equals(paramMD5)? EncryptCheckResult.MATCHED: EncryptCheckResult.MISMATCHED;
        }
        SecretKeySpec keySpec = (SecretKeySpec)bridgeCache.get(CacheConstants.CACHE_ENCRYPT_AES,key);
        if(keySpec==null){
            return EncryptCheckResult.INVALID_RSAKEY;
        }
        //将传递的sign解密成原始md5，与生成的md5比较
        try{
            String rsaSign = AESUtil.deCrypt(sign,keySpec,true).toUpperCase();
            return rsaSign.equals(paramMD5)? EncryptCheckResult.MATCHED: EncryptCheckResult.MISMATCHED;
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return EncryptCheckResult.CHECK_FAIL;
        }
    }
}
