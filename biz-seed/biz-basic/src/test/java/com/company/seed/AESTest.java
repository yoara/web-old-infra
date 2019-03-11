package com.company.seed;

import com.company.seed.common.util.encrypt.AESUtil;
import junit.framework.TestCase;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by yoara on 2016/7/19.
 */
public class AESTest extends TestCase {
    private static final String KEY_ALGORITHM_AES = "AES";
    private static final String testMsg = "测试用例";

    public void testRSAOriginal() throws Exception {
        KeyGenerator keyGen = KeyGenerator
                .getInstance(KEY_ALGORITHM_AES);
        keyGen.init(AESUtil.AESSize.SIZE_128.getSize());

        // 秘钥
        SecretKey skey = keyGen.generateKey();
        byte[] keyBytes = skey.getEncoded();

        // 对数据加密
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, KEY_ALGORITHM_AES);
        Cipher enCipher = Cipher.getInstance(KEY_ALGORITHM_AES);
        enCipher.init(Cipher.ENCRYPT_MODE, keySpec);

        byte[] enCode = enCipher.doFinal(testMsg.getBytes("UTF-8"));
        System.out.println(enCode);
        System.out.println(new String(enCode));
        System.out.println("--------------------");

        // 对数据解密
        Cipher deCipher = Cipher.getInstance(KEY_ALGORITHM_AES);
        deCipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] edCode = deCipher.doFinal(enCode);
        System.out.println(edCode);
        System.out.println(new String(edCode,"UTF-8"));
    }

    public void testAESUtil() throws Exception {
        byte[] keyByte = AESUtil.makeKey();
        // 公钥
        SecretKeySpec keySpec = AESUtil.getAESSecretKey(keyByte);

        //对数据加密
        byte[] encode = AESUtil.enCrypt(testMsg,keySpec);
        //对数据解密
        System.out.println(AESUtil.deCrypt(encode,keySpec));
    }

    public void testAESDecode() throws Exception {
//        SecretKeySpec keySpec = AESUtil.getAESSecretKey(AESUtil.hexStringToBytes(""));
//
//        System.out.println(AESUtil.deCrypt("22d019cbfdf599b0aa5f5eb013f557efad6898e45622072f50987598d446d5e8" +
//                "6bb7f70690d20a8ec0c687e71688a14a3ef1eb53317556e807be6b1c0c1d70a2",keySpec,true));
    }
}
