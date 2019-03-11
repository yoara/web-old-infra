package org.yoara.framework.core.util.encrypt;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

/**
 * 提供RSA加密组件服务<br>
 * RSA加密数据最多不能超过 117 bytes
 * Created by yoara on 2016/7/19.
 */
public class RSAUtil extends AbstractEncryptUtil{
    private static final String KEY_ALGORITHM_RSA = "RSA";
    private static final String CHARACTER = "UTF-8";

    /**
     * 获得RSA秘钥对
     */
    public static KeyPair makeKeyPair(){
        return makeKeyPair(512);
    }
    /**
     * 获得RSA秘钥对
     * @param size 秘钥块尺寸
     */
    public static KeyPair makeKeyPair(int size){
        KeyPairGenerator keyPairGen = null;
        try {
            keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM_RSA);
        } catch (NoSuchAlgorithmException e) {}
        if(size>0){
            keyPairGen.initialize(size);
        }
        return keyPairGen.generateKeyPair();
    }

    /**
     * 用字符串生成的私钥
     * @param modulus 私钥的modulus
     * @param exponent 私钥的 modulus
     * @return
     */
    public static RSAPrivateKey getRSAPrivateKey(String modulus,String exponent) throws Exception{
        RSAPrivateKeySpec priKeySpec =
                new RSAPrivateKeySpec(new BigInteger(modulus),new BigInteger(exponent));
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM_RSA);
        return (RSAPrivateKey)keyFactory.generatePrivate(priKeySpec);
    }

    /**
     * 用字符串生成的公钥
     * @param modulus 公钥的modulus
     * @param exponent 公钥的 modulus
     * @return
     */
    public static RSAPublicKey getRSAPublicKey(String modulus, String exponent) throws Exception{
        RSAPublicKeySpec pubKeySpec =
                new RSAPublicKeySpec(new BigInteger(modulus),new BigInteger(exponent));
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM_RSA);
        return (RSAPublicKey)keyFactory.generatePublic(pubKeySpec);
    }

    /**
     * 对数据加密，默认UTF-8 加密
     * @param message 数据
     * @param publicKey
     * @return
     */
    public static byte[] enCrypt(String message,RSAPublicKey publicKey) throws Exception {
        Cipher enCipher = Cipher.getInstance(KEY_ALGORITHM_RSA);
        enCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return enCipher.doFinal(message.getBytes(CHARACTER));
    }

    /**
     * 对数据解密，默认UTF-8 加密
     * @return
     */
    public static String deCrypt(String enCode,RSAPrivateKey privateKey,boolean isHex) throws Exception {
        byte[] signCode = isHex?hexStringToBytes(enCode):enCode.getBytes(CHARACTER);
        return deCrypt(signCode,privateKey);
    }

    /**
     * 对数据解密
     * @return
     */
    public static String deCrypt(byte[] enCode,RSAPrivateKey privateKey) throws Exception {
        Cipher deCipher = Cipher.getInstance(KEY_ALGORITHM_RSA);
        deCipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(deCipher.doFinal(enCode),CHARACTER);
    }
}
