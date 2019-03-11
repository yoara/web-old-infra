package com.company.seed.module.manager.service.impl;

import com.company.seed.module.manager.dao.AuthorityDao;
import com.company.seed.module.manager.model.AuthorityLeafModel;
import com.company.seed.module.manager.model.AuthorityNodeModel;
import com.company.seed.module.manager.model.PositionAuthorityModel;
import com.company.seed.module.manager.model.enums.AuthorityAnnotationEnums;
import com.company.seed.module.manager.model.enums.AuthorityAnnotationNodeEnums;
import com.company.seed.module.manager.model.enums.ManagerAuthType;
import com.company.seed.module.manager.service.AuthorityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by yoara on 2016/4/25.
 */
@Service
public class AuthorityServiceImpl implements AuthorityService {
    @Resource
    private AuthorityDao authorityDaoMysql;

    @Override
    public Set<AuthorityAnnotationEnums> queryAuthLeafsByManagerId(String managerId) {
        return authorityDaoMysql.queryAuthLeafsByManagerId(managerId);
    }

    @Override
    public List<AuthorityLeafModel> selectAll() {
        return authorityDaoMysql.selectAll();
    }

    @Override
    public List<PositionAuthorityModel> selectAllPositionWithAuthority() {
        return authorityDaoMysql.selectAllPositionWithAuthority();
    }

    @Override
    public void modifyPositionWithAuth(String authorityId, String positionId, boolean set) {
        if(set){
            authorityDaoMysql.insertPositionWithAuth(authorityId,positionId);
        }else{
            authorityDaoMysql.deletePositionWithAuth(authorityId,positionId);
        }
    }

    @Override
    public void modifyManagerWithAuth(String authorityId, String managerId, boolean set) {
        //获取人员所在岗位中是否包含该权限
        boolean postionHasAuth = authorityDaoMysql.checkPositionHasAuthByAuthAndManager(authorityId, managerId);
        Map<String,Object> param = new HashMap<>();
        param.put("authorityId",authorityId);
        param.put("managerId",managerId);
        if(set){
            if(postionHasAuth){
                //岗位已经包含该权限，删除个人&权限表中的NOT关联
                param.put("type",ManagerAuthType.NOT);
                authorityDaoMysql.removeManagerAuthority(param);
            }else{
                //岗位未包含权限，新增权限GET关联
                param.put("type",ManagerAuthType.GET);
                authorityDaoMysql.insertManagerAuthority(param);
            }
        }else{
            if(postionHasAuth){
                //岗位已经包含该权限，新增个人&权限表中的NOT关联
                param.put("type",ManagerAuthType.NOT);
                authorityDaoMysql.insertManagerAuthority(param);
            }else{
                //岗位未包含权限，删除权限GET关联
                param.put("type",ManagerAuthType.GET);
                authorityDaoMysql.removeManagerAuthority(param);
            }
        }
    }

    @Transactional
    @Override
    public void initAllAuthority() {
        //删除全部权限叶子及权限节点数据
        authorityDaoMysql.clearAllAuthorityLeaf();
        authorityDaoMysql.clearAllAuthorityNode();

        AuthorityAnnotationEnums[] authorities = AuthorityAnnotationEnums.values();
        if(authorities.length>0){
            List<AuthorityLeafModel> leafs = new ArrayList<>();
            for(AuthorityAnnotationEnums authority:authorities){
                AuthorityLeafModel leaf = new AuthorityLeafModel();
                leaf.setName(authority.getDesc());
                leaf.setDefinition(authority);
                leaf.setNodeDefinition(authority.getNodeAuth());
                leafs.add(leaf);
            }
            List<AuthorityNodeModel> nodes = new ArrayList<>();
            for(AuthorityAnnotationNodeEnums
                    authorityNode:AuthorityAnnotationNodeEnums.values()){
                AuthorityNodeModel node = new AuthorityNodeModel();
                node.setName(authorityNode.getDesc());
                node.setKey(authorityNode);
                node.setParentKey(authorityNode.getParentNode());
                nodes.add(node);
            }

            //插入新生成的权限叶子及权限节点数据
            authorityDaoMysql.insertAllAuthorityLeaf(leafs);
            authorityDaoMysql.insertAllAuthorityNode(nodes);

            //清除已经失效的权限叶子相关绑定
            authorityDaoMysql.clearPositionWithAuthority(leafs);
        }
    }
}
