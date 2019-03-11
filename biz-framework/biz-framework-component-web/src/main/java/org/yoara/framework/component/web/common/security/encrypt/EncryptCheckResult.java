package org.yoara.framework.component.web.common.security.encrypt;

/**
 * Created by yoara on 2016/7/20.
 */
public enum EncryptCheckResult {
    /**匹配**/
    MATCHED,
    /**不匹配**/
    MISMATCHED,
    /**校验参数为空**/
    EMPTY_ENCRYPTS,
    /**加密字符串为空**/
    EMPTY_ENCRYPTSTR,
    /**秘钥key为空**/
    EMPTY_ENCRYPT_KEY,
    /**失效的KEY**/
    INVALID_RSAKEY,
    /**解密失败**/
    CHECK_FAIL
}
