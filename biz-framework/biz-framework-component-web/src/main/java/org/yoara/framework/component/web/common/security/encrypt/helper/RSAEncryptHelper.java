package org.yoara.framework.component.web.common.security.encrypt.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.yoara.framework.component.web.cache.BridgeCache;
import org.yoara.framework.component.web.cache.CacheConstants;
import org.yoara.framework.component.web.common.security.encrypt.EncryptCheckResult;
import org.yoara.framework.core.util.encrypt.MD5Util;
import org.yoara.framework.core.util.encrypt.RSAUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

/**
 * 提供RSA秘钥存储，参数生成等应用层所需的一些工具方法
 * Created by yoara on 2016/7/19.
 */
@Component("rsaEncryptHelper")
public class RSAEncryptHelper extends AbstractEncryptHelper{
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private BridgeCache bridgeCache;
    /** 公钥modules **/
    public static final String RSA_PUBLIC_MODULES = "RSA_PUBLIC_MODULES";
    /** 公钥exponent **/
    public static final String RSA_PUBLIC_EXPONENT = "RSA_PUBLIC_EXPONENT";
    /** 待校验的参数名，值为字符串，多个用","分开 **/
    public static final String RSA_PARAM_ENCRYPTS = "RSA_PARAM_ENCRYPTS";
    /** 是否仅MD5加密，值为boolean **/
    public static final String RSA_PARAM_ONLYMD5 = "RSA_PARAM_ONLYMD5";
    /** 加密后的字符串 **/
    public static final String RSA_PARAM_ENCRYPTSTR = "RSA_PARAM_ENCRYPTSTR";
    /** 存储的键值 **/
    public static final String RSA_PARAM_KEY = "RSA_PARAM_KEY";
    /**
     * 获取需要加密的参数串
     * @param request
     * @return
     */
    public Map<String,String> makeCheckMap(HttpServletRequest request){
        String paramAttrs = request.getParameter(RSA_PARAM_ENCRYPTS);
        return makeCheckMap(request,paramAttrs);
    }

    /**
     * 将生成的RSA私钥存储至分布式缓存
     * @param keyPair 生成的加密键值对
     * @param expire 存储超时时间,单位为秒，最多不超过30分钟
     * @return 缓存的key
     */
    public String putRSAPriveteKey(KeyPair keyPair,int expire){
        // 公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        // 私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        if(expire>30*60){
            expire = 30*60;
        }
        String key = null;
        try {
            key = MD5Util.getMD5(
                    (publicKey.getModulus().toString()+publicKey.getPublicExponent().toString()).getBytes("UTF-8"));
            bridgeCache.put(CacheConstants.CACHE_ENCRYPT_RSA,key,privateKey,expire);
        } catch (UnsupportedEncodingException e) {}
        return key;
    }

    @Override
    protected EncryptCheckResult checkSign(String sign, String paramMD5, String key, boolean onlyMD5){
        if(onlyMD5){
            return sign.equals(paramMD5)? EncryptCheckResult.MATCHED: EncryptCheckResult.MISMATCHED;
        }
        RSAPrivateKey privateKey =
                (RSAPrivateKey)bridgeCache.get(CacheConstants.CACHE_ENCRYPT_RSA,key);
        if(privateKey==null){
            return EncryptCheckResult.INVALID_RSAKEY;
        }
        //将传递的sign解密成原始md5，与生成的md5比较
        try{
            String rsaSign = RSAUtil.deCrypt(sign,privateKey,true).toUpperCase();
            return rsaSign.equals(paramMD5)? EncryptCheckResult.MATCHED: EncryptCheckResult.MISMATCHED;
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return EncryptCheckResult.CHECK_FAIL;
        }
    }
}
