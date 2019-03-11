package org.yoara.framework.component.web.common.security.checkrequest.policy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by yoara on 2015/12/28.
 * 敏感词过滤策略
 */
public class SensitiveCheckPolicy extends BaseCheckPolicy{
    @Override
    public void doCheck(CheckResult result) {
        String params = result.getCheckObject().toString();
        List<String> list = new ArrayList<String>();
        if(!list.isEmpty()){
            Collections.sort(list, new ComparatorKey());
            for(String in:list){
                params = params.replaceAll(in, "*");
            }
            result.setReplaceObject(params);
            result.setIsValid(false);
            result.setMsg("包含特殊的字符类型"+ERR_MSG_SENSITIVE+"，请重新输入");
        }
    }
    private class ComparatorKey implements Comparator<String> {
        public int compare(String arg0, String arg1) {
            int flag = arg1.length() - arg0.length();
            return flag;
        }
    }
}
