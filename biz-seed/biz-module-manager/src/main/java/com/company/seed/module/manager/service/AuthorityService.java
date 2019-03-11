package com.company.seed.module.manager.service;

import com.company.seed.module.manager.model.AuthorityLeafModel;
import com.company.seed.module.manager.model.PositionAuthorityModel;
import com.company.seed.module.manager.model.enums.AuthorityAnnotationEnums;

import java.util.List;
import java.util.Set;

/**
 * Created by yoara on 2016/4/25.
 * 权限操作service
 */
public interface AuthorityService {
    /**
     * 传入managerId，查询该manager对应的所有权限叶子
     * @param managerId
     * @return
     */
    Set<AuthorityAnnotationEnums> queryAuthLeafsByManagerId(String managerId);

    /**
     * 获取所有权限叶子
     * @return
     */
    List<AuthorityLeafModel> selectAll();

    /**
     * 获取所有岗位并携带相应的权限
     * @return
     */
    List<PositionAuthorityModel> selectAllPositionWithAuthority();

    /**
     * 单改岗位&权限绑定信息
     * @param authorityId
     * @param positionId
     * @param set
     */
    void modifyPositionWithAuth(String authorityId, String positionId, boolean set);

    /**
     * 单改人员&岗位绑定信息
     * @param authorityId
     * @param managerId
     * @param set
     */
    void modifyManagerWithAuth(String authorityId, String managerId, boolean set);

    /**
     * 通过枚举定义初始化权限
     */
    void initAllAuthority();
}
