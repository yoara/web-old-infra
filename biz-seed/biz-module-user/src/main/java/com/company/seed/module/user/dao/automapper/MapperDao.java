package com.company.seed.module.user.dao.automapper;


import com.company.seed.dao.basic.mybatis.mapper.annotation.CountSql;
import com.company.seed.model.Pagination;
import com.company.seed.module.user.model.UserModel;

import java.util.List;

/**
 * Created by yoara on 2017/4/5.
 */
public interface MapperDao {
    @CountSql("selectCount")
    void selectAllNoCount(Pagination page);

    @CountSql("selectCount")
    void selectAll(Pagination page);

    UserModel selectById(String id,String name);

    List selectAll();
}
