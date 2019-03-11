package org.yoara.framework.component.web.common.security.checkrequest.policy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yoara on 2015/12/28.
 * 标签判断策略
 */
public class TagCheckPolicy extends BaseCheckPolicy {
    private static ThreadLocal<Pattern> threadLocal
            = new ThreadLocal<Pattern>(){
        @Override
        protected Pattern initialValue() {
            return Pattern.compile(
                    "(<\\s*script\\s*)|(<\\s*embed\\s*)|(<\\s*style\\s*)|(<\\s*frame\\s*)|(<\\s*object\\s*)|(<\\s*iframe\\s*)" +
                            "|(<\\s*frameset\\s*)|(<\\s*meta\\s*)|(<\\s*xml\\s*)|(<\\s*applet\\s*)|(<\\s*link\\s*)|(<\\s*blink\\s*)" +
                            "|(<\\s*img\\s*)|(<\\s*xss\\s*)|(<\\s*html\\s*)|(<\\s*base\\s*)|(<\\s*body\\s*)|(<\\s*bgsound\\s*)" +
                            "|(\\s+on.*\\s*=)" +
                            "|(<\\s*a[\\s\\S]*>[\\s\\S]*<\\s*/\\s*a\\s*>)" +//<  [\\s\\S]*  任何字符     >
                            "|(\\s*javascript\\s*)|(\\s*vbscript\\s*)|(\\s*expression\\s*)" +
                            "|(\\s*alert\\s*\\()",
                    Pattern.CASE_INSENSITIVE);
        }
    };
    @Override
    public void doCheck(CheckResult result) {
        String params = result.getCheckObject().toString();
        Matcher matcher = threadLocal.get().matcher(params);
        if(matcher.find()){
            result.setReplaceObject(matcher.replaceAll(""));
            result.setIsValid(false);
            result.setMsg("包含特殊的字符类型"+ERR_MSG_TAG+"，请重新输入");
        }
    }
}
