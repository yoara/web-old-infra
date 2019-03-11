package com.company.seed.module.manager.service.impl;

import com.company.seed.module.manager.dao.ManagerDao;
import com.company.seed.module.manager.model.ManagerModel;
import com.company.seed.module.manager.service.ManagerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by yoara on 2016/4/25.
 */
@Service
public class ManagerServiceImpl implements ManagerService {
    @Resource
    private ManagerDao managerDaoMysql;

    @Override
    public boolean insertManager(ManagerModel manager) {
        return managerDaoMysql.insertManager(manager);
    }

    @Override
    public boolean updateManager(ManagerModel manager) {
        return managerDaoMysql.updateManager(manager);
    }

    @Override
    public ManagerModel authenticateManager(String userName, String passwordMD5) {
        return managerDaoMysql.authenticateManager(userName, passwordMD5);
    }

    @Override
    public List<ManagerModel> selectAll() {
        return managerDaoMysql.selectAll();
    }

    @Override
    public boolean saveManager(ManagerModel manager) {
        if (StringUtils.isEmpty(manager.getId())) {
            return insertManager(manager);
        } else {
            return updateManager(manager);
        }
    }
}