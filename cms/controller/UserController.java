package com.cms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cms.common.QueryPageParam;
import com.cms.common.Result;
import com.cms.entity.Menu;
import com.cms.entity.User;
import com.cms.service.MenuService;
import com.cms.service.UserService;
import freemarker.template.utility.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Zhang MX
 * @since 2025-02-03
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private MenuService menuService;


    @GetMapping("/list")
    public List<User> list(){return userService.list();}


    //唯一性检验
    @GetMapping("/findByAccount")
    public Result findByAccount(@RequestParam String account){
        List list = userService.lambdaQuery().eq(User::getAccount, account).list();
        return list.size()>0 ? Result.success(list):Result.fail();
    }

    //登录
    @PostMapping("/login")
    public Result login(@RequestBody User user){
        List list = userService.lambdaQuery()
                .eq(User::getAccount, user.getAccount())
                .eq(User::getPassword, user.getPassword()).list();

        if (list.size()>0){
            User user1 = (User)list.get(0);
            List menuList = menuService.lambdaQuery().like(Menu::getMenuright, user1.getRoleId()).list();
            HashMap res = new HashMap();
            res.put("user", user1);
            res.put("menu", menuList);
            return Result.success(res);
        }

        return Result.fail();
    }

    //增
    @PostMapping("/save")
    public Result save(@RequestBody User user){
        return userService.save(user) ? Result.success() : Result.fail();
    }

    //更新
    @PostMapping("/update")
    public Result update(@RequestBody User user){
        return userService.updateById(user) ? Result.success() : Result.fail();
    }

    //删除
    @GetMapping("/del")
    public Result del(@RequestParam String id){
        return userService.removeById(id) ? Result.success() : Result.fail();
    }

    //改
    @PostMapping("/mod")
    public boolean mod(@RequestBody User user){return userService.updateById(user);}

    //增改
    @PostMapping("/saveOrMod")
    public boolean saveOrMod(@RequestBody User user){return userService.saveOrUpdate(user);}

    //删
    @GetMapping("/delete")
    public boolean delete(Integer id){return userService.removeById(id);}

    //模糊查询(匹配查询)
    @PostMapping("/listP")
    public Result listP(@RequestBody User user){
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper();
        if(StringUtils.isNotBlank(user.getUsername())){
            lambdaQueryWrapper.like/*eq*/(User::getUsername, user.getUsername());//用户名模糊查找(匹配)
        }
        return Result.success(userService.list(lambdaQueryWrapper));
    }

    @PostMapping("/listPageC")
    public Result listPageC(@RequestBody QueryPageParam query){
        HashMap param = query.getParam();
        String username = (String) param.get("username");
        String sex = (String) param.get("sex");
        String roleId = (String) param.get("roleId");
        System.out.println("account==="+(String)param.get("account"));

        Page<User> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper();
        if(StringUtils.isNotBlank(username) && !"null".equals(username)){
            lambdaQueryWrapper.like(User::getUsername, username);
        }
        if(StringUtils.isNotBlank(sex)){
            lambdaQueryWrapper.like(User::getSex, sex);
        }
        if(StringUtils.isNotBlank(roleId)){
            lambdaQueryWrapper.like(User::getRoleId, roleId);
        }


       IPage result = userService.page(page, lambdaQueryWrapper);
       System.out.println("total==="+result.getTotal());

        return Result.success(result.getRecords(), result.getTotal());
    }
}
