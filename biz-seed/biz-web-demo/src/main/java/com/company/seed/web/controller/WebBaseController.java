package com.company.seed.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyPreFilter;
import com.company.seed.common.CommonConstants;
import com.company.seed.model.Pagination;
import com.company.seed.module.manager.model.ManagerModel;
import org.apache.commons.collections.CollectionUtils;
import org.yoara.framework.component.web.controller.BaseController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoara on 2016/5/23.
 */
public class WebBaseController extends BaseController {
    public ManagerModel getCurrentManager(){
        return (ManagerModel) getRequest().
                getSession().getAttribute(CommonConstants.CURRENT_LOGIN_MANAGER);
    }
    public ManagerModel getCurrentUser(){
        return (ManagerModel) getRequest().
                getSession().getAttribute(CommonConstants.CURRENT_LOGIN_USER);
    }
    protected JSONObject toList(Pagination page, PropertyPreFilter filter){
        JSONObject json = new JSONObject();
        json.put("recordCount", page.getRecordCount());
        json.put("currentPage", page.getCurrentPage());
        json.put("pageSize", page.getPageSize());
        json.put("pageCount", page.getPageCount());
        if (CollectionUtils.isNotEmpty(page.getItems()) && filter!=null) {
            final List<JSONObject> list = new ArrayList<>();
            for (Object obj : page.getItems()) {
                list.add(JSONObject.parseObject(JSON.toJSONString(obj,filter, features)));
            }
            json.put("items", list);
        }else{
            json.put("items", new ArrayList());
        }
        return json;
    }
}
