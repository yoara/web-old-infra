package org.yoara.framework.component.web.common.security.checkrequest.policy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yoara on 2015/12/28.
 * sql语句过滤策略
 */
public class SQLCheckPolicy extends BaseCheckPolicy {
    private static ThreadLocal<Pattern> threadLocal
            = new ThreadLocal<Pattern>(){
        @Override
        protected Pattern initialValue() {
            return Pattern.compile(
                    "(\\s*select\\s+)|(\\s*insert\\s+)|(\\s*delete\\s+)|(\\s*from\\s+)" +
                            "|(\\s*drop\\s+)|(\\s*update\\s+)|(\\s*truncate\\s+)",
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
            result.setMsg("包含特殊的字符类型"+ERR_MSG_SQL+"，请重新输入");
        }
    }
}
