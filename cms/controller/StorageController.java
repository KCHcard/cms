package com.cms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cms.common.QueryPageParam;
import com.cms.common.Result;
import com.cms.entity.Storage;
import com.cms.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Zhang MX
 * @since 2025-02-09
 */
@RestController
@RequestMapping("/storage")
public class StorageController {

    @Autowired
    private StorageService storageService;
    //新增
    @PostMapping("/save")
    public Result save(@RequestBody Storage storage){
        return storageService.save(storage) ? Result.success() : Result.fail();
    }

    //更新
    @PostMapping("/update")
    public Result update(@RequestBody Storage storage){
        return storageService.updateById(storage) ? Result.success() : Result.fail();
    }

    //删除
    @GetMapping("/del")
    public Result del(@RequestParam String id){
        return storageService.removeById(id) ? Result.success() : Result.fail();
    }

    //唯一性检验
    @GetMapping("/findByName")
    public Result findByName(@RequestParam String name){
        List list = storageService.lambdaQuery().eq(Storage::getName, name).list();
        return list.size()>0 ? Result.success(list):Result.fail();
    }

    //查询
    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query){
        HashMap param = query.getParam();

        String name = (String)param.get("name");

        Page<Storage> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<Storage> lambdaQueryWrapper = new LambdaQueryWrapper();
        if(StringUtils.isNotBlank(name) && !"null".equals(name)){
            lambdaQueryWrapper.like(Storage::getName, name);
        }

        IPage result = storageService.page(page, lambdaQueryWrapper);

        return Result.success(result.getRecords(), result.getTotal());
    }
}
