package com.ajin.controller;


import cn.hutool.crypto.SecureUtil;
import com.ajin.common.lang.Result;
import com.ajin.entity.User;
import com.ajin.service.UserService;
import com.ajin.shiro.AccountProfile;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;


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
}
