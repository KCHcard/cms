package com.cms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cms.common.QueryPageParam;
import com.cms.common.Result;
import com.cms.entity.Members;
import com.cms.service.MembersService;
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
@RequestMapping("/members")
public class MembersController {
    @Autowired
    private MembersService membersService;
    //新增
    @PostMapping("/save")
    public Result save(@RequestBody Members members){
        return membersService.save(members) ? Result.success() : Result.fail();
    }

    //更新
    @PostMapping("/update")
    public Result update(@RequestBody Members members){
        return membersService.updateById(members) ? Result.success() : Result.fail();
    }

    //删除
    @GetMapping("/del")
    public Result del(@RequestParam String id){
        return membersService.removeById(id) ? Result.success() : Result.fail();
    }

    //查询
    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query){
        HashMap param = query.getParam();

        String id = (String)param.get("id");

        Page<Members> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<Members> lambdaQueryWrapper = new LambdaQueryWrapper();
        if(StringUtils.isNotBlank(id) && !"null".equals(id)){
            lambdaQueryWrapper.like(Members::getId, id);
        }

        IPage result = membersService.page(page, lambdaQueryWrapper);

        return Result.success(result.getRecords(), result.getTotal());
    }
}
