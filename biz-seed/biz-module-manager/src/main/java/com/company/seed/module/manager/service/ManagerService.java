package com.company.seed.module.manager.service;

import com.company.seed.module.manager.model.ManagerModel;

import java.util.List;

/**
 * Created by yoara on 2016/4/25.
 * 人员操作service
 */
public interface ManagerService {
    boolean insertManager(ManagerModel manager);
    boolean updateManager(ManagerModel manager);

    ManagerModel authenticateManager(String userName, String passwordMD5);

    List<ManagerModel> selectAll();

    boolean saveManager(ManagerModel manager);


}
