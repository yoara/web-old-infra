package com.company.seed.module.manager.dao.impl;

import com.company.seed.dao.basic.mybatis.BaseDao;
import com.company.seed.module.manager.dao.AuthorityDao;
import com.company.seed.module.manager.model.AuthorityLeafModel;
import com.company.seed.module.manager.model.AuthorityNodeModel;
import com.company.seed.module.manager.model.PositionAuthorityModel;
import com.company.seed.module.manager.model.enums.AuthorityAnnotationEnums;
import com.company.seed.module.manager.model.enums.ManagerAuthType;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by yoara on 2016/4/25.
 */
@Repository
public class AuthorityDaoMysqlImpl extends BaseDao implements AuthorityDao {
    private static String SQL_PREFIX = "com.company.seed.module.manager.dao.AuthorityDao.";

    @Override
    public Set<AuthorityAnnotationEnums> queryAuthLeafsByManagerId(String managerId) {
        Map<String,Object> params = new HashMap<>();
        params.put("managerId",managerId);
        //获取从岗位上继承的权限
        List positionAuthList = selectList(SQL_PREFIX+"queryAuthLeafsByManagerIdFromPosition",params);
        Set<AuthorityAnnotationEnums> authSet = new HashSet<>();
        if(positionAuthList!=null){
            authSet.addAll(positionAuthList);
        }
        //从个人权限设置上并集权限
        List personAuthList = selectList(SQL_PREFIX+"queryAuthLeafsByManagerId",params);
        if(personAuthList!=null){
            for(Object pa:personAuthList){
                Map<String,String> paResult = (Map<String,String>)pa;
                if(ManagerAuthType.NOT.name().equals(paResult.get("FType"))){
                    authSet.remove(AuthorityAnnotationEnums.valueOf(paResult.get("FDefinition")));
                }else{
                    authSet.add(AuthorityAnnotationEnums.valueOf(paResult.get("FDefinition")));
                }
            }
        }
        return authSet;
    }

    @Override
    public List<AuthorityLeafModel> selectAll() {
        return (List<AuthorityLeafModel>)selectList(SQL_PREFIX+"selectAll");
    }

    @Override
    public List<PositionAuthorityModel> selectAllPositionWithAuthority() {
        return (List<PositionAuthorityModel>)selectList(SQL_PREFIX+"selectAllPositionWithAuthority");
    }

    @Override
    public boolean insertPositionWithAuth(String authorityId, String positionId) {
        Map<String,Object> param = new HashMap<>();
        param.put("authorityId",authorityId);
        param.put("positionId",positionId);
        return insert(SQL_PREFIX+"insertPositionWithAuth",param)>0;
    }

    @Override
    public boolean deletePositionWithAuth(String authorityId, String positionId) {
        Map<String,Object> param = new HashMap<>();
        param.put("authorityId",authorityId);
        param.put("positionId",positionId);
        return insert(SQL_PREFIX+"deletePositionWithAuth",param)>0;
    }

    @Override
    public boolean clearAllAuthorityLeaf() {
        return delete(SQL_PREFIX+"clearAllAuthorityLeaf",null)>0;
    }

    @Override
    public boolean clearAllAuthorityNode() {
        return delete(SQL_PREFIX+"clearAllAuthorityNode",null)>0;
    }

    @Override
    public boolean insertAllAuthorityLeaf(List<AuthorityLeafModel> leafs) {
        return insert(SQL_PREFIX+"insertAllAuthorityLeaf",leafs)>0;
    }

    @Override
    public boolean insertAllAuthorityNode(List<AuthorityNodeModel> nodes) {
        return insert(SQL_PREFIX+"insertAllAuthorityNode",nodes)>0;
    }

    @Override
    public void clearPositionWithAuthority(List<AuthorityLeafModel> leafs) {
        delete(SQL_PREFIX+"clearPositionWithAuthority",leafs);
    }

    @Override
    public boolean checkPositionHasAuthByAuthAndManager(String authorityId, String managerId) {
        Map<String,Object> param = new HashMap<>();
        param.put("authorityId",authorityId);
        param.put("managerId",managerId);
        return ((int)selectOne(SQL_PREFIX+"checkPositionHasAuthByAuthAndManager",param))>0;
    }

    @Override
    public boolean removeManagerAuthority(Map<String, Object> param) {
        return delete(SQL_PREFIX+"removeManagerAuthority",param)>0;
    }

    @Override
    public boolean insertManagerAuthority(Map<String, Object> param) {
        return insert(SQL_PREFIX+"insertManagerAuthority",param)>0;
    }
}
