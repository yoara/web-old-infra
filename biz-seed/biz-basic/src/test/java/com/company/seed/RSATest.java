package com.company.seed;

import com.company.seed.common.util.encrypt.RSAUtil;
import junit.framework.TestCase;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;

/**
 * Created by yoara on 2016/7/19.
 */
public class RSATest extends TestCase {
    private static final String KEY_ALGORITHM_RSA = "RSA";
    private static final String testMsg = "测试用例";

    public void testRSAOriginal() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator
                .getInstance(KEY_ALGORITHM_RSA);
        keyPairGen.initialize(512);

        KeyPair keyPair = keyPairGen.generateKeyPair();

        // 公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        // 私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        // 用字符串生成的私钥
        RSAPrivateKeySpec priKeySpec = new RSAPrivateKeySpec(
                privateKey.getModulus(), privateKey.getPrivateExponent());
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM_RSA);
        RSAPrivateKey loadPrivateKey = (RSAPrivateKey)keyFactory.generatePrivate(priKeySpec);

        // 对数据加密
        Cipher enCipher = Cipher.getInstance(KEY_ALGORITHM_RSA);
        enCipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] enCode = enCipher.doFinal(testMsg.getBytes("UTF-8"));
        System.out.println(enCode);
        System.out.println(new String(enCode));
        System.out.println("--------------------");

        // 对数据解密
        Cipher deCipher = Cipher.getInstance(KEY_ALGORITHM_RSA);
        deCipher.init(Cipher.DECRYPT_MODE, loadPrivateKey);
        byte[] edCode = deCipher.doFinal(enCode);
        System.out.println(edCode);
        System.out.println(new String(edCode,"UTF-8"));
    }

    public void testRSAUtil() throws Exception {
        KeyPair keyPair = RSAUtil.makeKeyPair();
        // 公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        // 私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        //测试公钥
        RSAPublicKey loadPublicKey =
                RSAUtil.getRSAPublicKey(publicKey.getModulus()+"",publicKey.getPublicExponent()+"");
        //测试私钥
        RSAPrivateKey loadPrivateKey =
                RSAUtil.getRSAPrivateKey(privateKey.getModulus()+"",privateKey.getPrivateExponent()+"");

        //对数据加密
        byte[] encode = RSAUtil.enCrypt(testMsg,loadPublicKey);
        //对数据解密
        System.out.println(RSAUtil.deCrypt(encode,loadPrivateKey));
    }

    public void testRSAString() throws Exception {
        KeyPair keyPair = RSAUtil.makeKeyPair();
        // 公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        // 私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        System.out.println("public exponent:"+publicKey.getPublicExponent());
        System.out.println("public modulus:"+publicKey.getModulus());
        System.out.println("public exponent(16):"+publicKey.getPublicExponent().toString(16));
        System.out.println("public modulus(16):"+publicKey.getModulus().toString(16));
        System.out.println("-----------------------------------");
        System.out.println("private exponent:"+privateKey.getPrivateExponent());
        System.out.println("private modulus:"+privateKey.getModulus());
        System.out.println("private exponent(16):"+privateKey.getPrivateExponent().toString(16));
        System.out.println("private modulus(16):"+privateKey.getModulus().toString(16));
    }

    public void testRSADecode() throws Exception {
        RSAPrivateKey privateKey = RSAUtil.getRSAPrivateKey("6792011712800935044201912752736148522555403513938189395879544869289282145397805714178434767258316190315922822749557810671258647505735079165200505938519769",
                "5146785566643575936725138335999246348278780678230592822042051929423750399083535087088508283114976457003690642214117628217076957660598257051440857969084513"
                );
        System.out.println(RSAUtil.deCrypt("22d019cbfdf599b0aa5f5eb013f557efad6898e45622072f50987598d446d5e8" +
                "6bb7f70690d20a8ec0c687e71688a14a3ef1eb53317556e807be6b1c0c1d70a2",privateKey,true));
    }
}
