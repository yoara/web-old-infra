package org.yoara.framework.core.util.encrypt;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

/**
 * 提供AES加密组件服务<br>
 * Created by yoara on 2016/7/19.
 */
public class AESUtil extends AbstractEncryptUtil{
    private static final String KEY_ALGORITHM_AES = "AES";
    private static final String CHARACTER = "UTF-8";

    /**
     * AES加密秘钥位数
     */
    public enum AESSize{
        SIZE_128(128),SIZE_192(192),SIZE_256(256);
        AESSize(int size){
            this.size = size;
        }
        private int size;
        public int getSize(){
            return size;
        }
    }

    /**
     * 获得AES秘钥字节数组
     */
    public static byte[] makeKey(){
        return makeKey(AESSize.SIZE_128,null);
    }
    /**
     * 获得AES秘钥字节数组
     * @param size 秘钥块尺寸
     * @param seed 随机种子
     */
    public static byte[] makeKey(AESSize size,String seed){
        KeyGenerator keyGen = null;
        try {
            keyGen = KeyGenerator.getInstance(KEY_ALGORITHM_AES);
            int switchSize = size.getSize();
            if(seed==null){
                keyGen.init(switchSize);
            }else{
                keyGen.init(switchSize,new SecureRandom(seed.getBytes(CHARACTER)));
            }
        } catch (Exception e) {}
        SecretKey secretKey = keyGen.generateKey();
        return secretKey.getEncoded();
    }

    /**
     * 用字符串生成的私钥
     * @param secretKeyStr 私钥字符串
     * @return
     */
    public static SecretKeySpec getAESSecretKey(String secretKeyStr, boolean isHex) throws Exception{
        byte[] keyByte = isHex?hexStringToBytes(secretKeyStr):secretKeyStr.getBytes(CHARACTER);
        return new SecretKeySpec(keyByte, KEY_ALGORITHM_AES);
    }

    /**
     * 用字符串生成的私钥
     * @param keyByte 私钥数组
     * @return
     */
    public static SecretKeySpec getAESSecretKey(byte[] keyByte){
        return new SecretKeySpec(keyByte, KEY_ALGORITHM_AES);
    }

    /**
     * 对数据加密，默认UTF-8 加密
     * @param message 数据
     * @param key
     * @return
     */
    public static byte[] enCrypt(String message,SecretKeySpec key) throws Exception {
        Cipher enCipher = Cipher.getInstance(KEY_ALGORITHM_AES);
        enCipher.init(Cipher.ENCRYPT_MODE, key);
        return enCipher.doFinal(message.getBytes(CHARACTER));
    }

    /**
     * 对数据解密，默认UTF-8 加密
     * @return
     */
    public static String deCrypt(String enCode,SecretKeySpec key,boolean isHex) throws Exception {
        byte[] signCode = isHex?hexStringToBytes(enCode):enCode.getBytes(CHARACTER);
        return deCrypt(signCode,key);
    }

    /**
     * 对数据解密
     * @return
     */
    public static String deCrypt(byte[] enCode,SecretKeySpec key) throws Exception {
        Cipher deCipher = Cipher.getInstance(KEY_ALGORITHM_AES);
        deCipher.init(Cipher.DECRYPT_MODE, key);
        return new String(deCipher.doFinal(enCode),CHARACTER);
    }
}
