package org.yoara.framework.component.web.common.security.encrypt;

import com.alibaba.fastjson.JSONObject;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yoara.framework.component.web.common.security.encrypt.helper.RSAEncryptHelper;
import org.yoara.framework.component.web.controller.BaseController;
import org.yoara.framework.core.util.encrypt.RSAUtil;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;

/**
 * 提供秘钥服务的Controller
 * Created by yoara on 2016/7/1.
 */
@Lazy
@RestController
@RequestMapping(value = "/common/encrypttoken/",produces = { "application/json;charset=UTF-8" })
public class EncryptTokenController extends BaseController {


    @RequestMapping(value = "initrascode")
    public String initRasCode() {
        JSONObject json = new JSONObject();

        KeyPair keyPair = RSAUtil.makeKeyPair();
        // 公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        String key = rsaEncryptHelper.putRSAPriveteKey(keyPair,60*10);
        json.put("status", "OK");
        json.put(RSAEncryptHelper.RSA_PARAM_KEY,key);
        json.put(RSAEncryptHelper.RSA_PUBLIC_MODULES,publicKey.getModulus().toString(16));
        json.put(RSAEncryptHelper.RSA_PUBLIC_EXPONENT,publicKey.getPublicExponent().toString(16));
        return json.toString();
    }

    @RequestMapping(value = "checkrascode")
    public String checkRasCode(){
        JSONObject json = new JSONObject();
        json.put("result", rsaCheck());
        return json.toString();
    }
}
