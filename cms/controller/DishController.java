package com.cms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cms.common.QueryPageParam;
import com.cms.common.Result;
import com.cms.entity.Dish;
import com.cms.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    //新增
    @PostMapping("/save")
    public Result save(@RequestBody Dish dish){
        return dishService.save(dish) ? Result.success() : Result.fail();
    }

    //上传头像
    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        // 文件保存逻辑
        return "File uploaded successfully: " + file.getOriginalFilename();
    }

    //更新
    @PostMapping("/update")
    public Result update(@RequestBody Dish dish){
        return dishService.updateById(dish) ? Result.success() : Result.fail();
    }

    //删除
    @GetMapping("/del")
    public Result del(@RequestParam String id){
        return dishService.removeById(id) ? Result.success() : Result.fail();
    }

    //唯一性检验
    @GetMapping("/findByName")
    public Result findByName(@RequestParam String name){
        List list = dishService.lambdaQuery().eq(Dish::getName, name).list();
        return list.size()>0 ? Result.success(list):Result.fail();
    }

    //查询
    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query){
        HashMap param = query.getParam();

        String name = (String)param.get("name");

        Page<Dish> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper();
        if(StringUtils.isNotBlank(name) && !"null".equals(name)){
            lambdaQueryWrapper.like(Dish::getName, name);
        }

        IPage result = dishService.page(page, lambdaQueryWrapper);

        return Result.success(result.getRecords(), result.getTotal());
    }
}

