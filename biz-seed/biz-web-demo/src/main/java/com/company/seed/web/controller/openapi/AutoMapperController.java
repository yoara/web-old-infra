package com.company.seed.web.controller.openapi;

import com.company.seed.model.Pagination;
import com.company.seed.module.user.dao.automapper.MapperDao;
import com.company.seed.module.user.model.UserModel;
import com.company.seed.web.controller.WebBaseController;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by yoara on 2016/3/3.
 */
@Lazy
@RestController
@RequestMapping(value = "/openapi/auto",produces = { "application/json;charset=UTF-8" })
public class AutoMapperController extends WebBaseController {
    @Resource
    private MapperDao testDao;

    @GetMapping(value = "test")
    public String test() {
        Pagination page = new Pagination();
        Pagination pageNoCount = new Pagination();
        pageNoCount.setQueryRecordCount(false);
        testDao.selectAll(page);
        testDao.selectAllNoCount(pageNoCount);
        List list = testDao.selectAll();
        UserModel user = testDao.selectById("1",null);
        return "ok";
    }
}
