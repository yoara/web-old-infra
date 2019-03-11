package com.company.seed.module.manager.dao;

import com.company.seed.module.manager.model.PositionModel;

import java.util.List;

/**
 * 岗位服务层Service
 * Created by Administrator on 2016-08-03
 */
public interface PositionDao {
    boolean insertPosition(PositionModel position);

    boolean updatePosition(PositionModel position);

    List<PositionModel> selectAll();
}
