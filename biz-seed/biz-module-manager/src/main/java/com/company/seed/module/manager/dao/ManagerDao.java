package com.company.seed.module.manager.dao;

import com.company.seed.module.manager.model.ManagerModel;

import java.util.List;

/**
 * Created by yoara on 2016/4/25.
 */
public interface ManagerDao {
    boolean insertManager(ManagerModel manager);

    boolean updateManager(ManagerModel manager);

    ManagerModel authenticateManager(String userName, String passwordMD5);

    List<ManagerModel> selectAll();

}
