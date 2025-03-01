package com.cms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cms.common.QueryPageParam;
import com.cms.common.Result;
import com.cms.entity.Lists;
import com.cms.service.ListsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Zhang MX
 * @since 2025-02-10
 */
@RestController
@RequestMapping("/lists")
public class ListsController {
    @Autowired
    private ListsService listsService;
    //新增
    @PostMapping("/save")
    public Result save(@RequestBody Lists lists){
        return listsService.save(lists) ? Result.success() : Result.fail();
    }

    //更新
    @PostMapping("/update")
    public Result update(@RequestBody Lists lists){
        return listsService.updateById(lists) ? Result.success() : Result.fail();
    }

    //删除
    @GetMapping("/del")
    public Result del(@RequestParam String id){
        return listsService.removeById(id) ? Result.success() : Result.fail();
    }

    //查询
    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query){
        HashMap param = query.getParam();

        String id = (String)param.get("id");
        String listcondition = (String)param.get("listcondition");

        Page<Lists> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<Lists> lambdaQueryWrapper = new LambdaQueryWrapper();
        if(StringUtils.isNotBlank(id) && !"null".equals(id)){
            lambdaQueryWrapper.like(Lists::getId, id);
        }
        if(StringUtils.isNotBlank(listcondition) && !"null".equals(listcondition)){
            lambdaQueryWrapper.like(Lists::getListcondition, listcondition);
        }

        IPage result = listsService.page(page, lambdaQueryWrapper);

        return Result.success(result.getRecords(), result.getTotal());
    }
}
