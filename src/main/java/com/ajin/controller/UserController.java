package com.ajin.controller;


import cn.hutool.crypto.SecureUtil;
import com.ajin.common.lang.Result;
import com.ajin.entity.User;
import com.ajin.service.UserService;
import com.ajin.shiro.AccountProfile;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import net.sf.saxon.expr.instruct.ForEach;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author AJin
 * @since 2023-05-11
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @RequiresAuthentication
    @RequestMapping("/index")
    public Result index(){
        User user = userService.getById(2);
        return Result.succ(200,"操作成功",user);
    }

    @GetMapping("index")
    public Result list(){
        List<User> list = userService.list();
        ArrayList<AccountProfile> userList = new ArrayList<>();
        for(User user : list){
            AccountProfile profile = new AccountProfile();
            BeanUtils.copyProperties(user,profile);
            userList.add(profile);
        }

        return Result.succ(userList);
    }

    @PostMapping("/save")
    public Result save(@Validated @RequestBody User user){
        User u = userService.getOne(new QueryWrapper<User>().eq("username", user.getUsername()));
        Assert.isNull(u,"用户名已经存在");
        if(StringUtils.isNotEmpty(user.getPassword())){
            user.setStatus(0);
            user.setCreated(LocalDateTime.now());
            user.setPassword(SecureUtil.md5(user.getPassword()));
            boolean save = userService.save(user);
            if(save){
                AccountProfile accountProfile = new AccountProfile();
                BeanUtils.copyProperties(user,accountProfile);
                return Result.succ(accountProfile);
            }
        }
        return Result.fail("密码不能为空");
    }

    @RequiresAuthentication
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable(name = "id") Long id){
        boolean flag = userService.removeById(id);
        Assert.isTrue(flag,"删除失败");
        return Result.succ(flag);
    }

    @RequiresAuthentication
    @PutMapping("/update")
    public Result update(@RequestBody User user){
        Long id = user.getId();
        if(id!=null && id!=0){
            boolean flag = userService.updateById(user);
            Assert.isTrue(flag,"修改失败");
            if(flag){
                return Result.succ(flag);
            }
        }
        return Result.fail("id不能为空");
    }
}
