package com.company;


import org.yoara.framework.core.util.normal.SQLModelMapHelper;
import java.io.IOException;

/**
 * Created by yoara on 2016/8/1.
 */
public class AutoModelMapperCode {
    public static void main(String[] args) throws IOException {
        SQLModelMapHelper helper = new SQLModelMapHelper(
                "root","123456",
                "jdbc:mysql:replication://192.168.0.109,192.168.0.109/" +
                        "test?useUnicode=true&amp;characterEncoding=UTF-8&amp;zeroDateTimeBehavior=round",
                "test","t_base_manager","f",
                "com.company.seed.module","Manager","manager","岗位");
        helper.doMapper();
    }
}
