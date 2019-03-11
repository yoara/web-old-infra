package com.company.seed.module.manager.dao;

import com.company.seed.module.manager.model.AuthorityLeafModel;
import com.company.seed.module.manager.model.AuthorityNodeModel;
import com.company.seed.module.manager.model.PositionAuthorityModel;
import com.company.seed.module.manager.model.enums.AuthorityAnnotationEnums;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by yoara on 2016/4/25.
 */
public interface AuthorityDao {
    Set<AuthorityAnnotationEnums> queryAuthLeafsByManagerId(String managerId);

    List<AuthorityLeafModel> selectAll();

    List<PositionAuthorityModel> selectAllPositionWithAuthority();

    boolean insertPositionWithAuth(String authorityId, String positionId);

    boolean deletePositionWithAuth(String authorityId, String positionId);

    boolean clearAllAuthorityLeaf();

    boolean clearAllAuthorityNode();

    boolean insertAllAuthorityLeaf(List<AuthorityLeafModel> leafs);

    boolean insertAllAuthorityNode(List<AuthorityNodeModel> nodes);

    /**
     * 清除所有不在leafs中的权限相关绑定关系
     */
    void clearPositionWithAuthority(List<AuthorityLeafModel> leafs);

    /**
     * 通过权限id及managerid判断manager所属的岗位是否包含该权限
     * @param authorityId 岗位id
     * @param managerId 人员id
     * @return
     */
    boolean checkPositionHasAuthByAuthAndManager(String authorityId, String managerId);

    boolean removeManagerAuthority(Map<String, Object> param);

    boolean insertManagerAuthority(Map<String, Object> param);
}
