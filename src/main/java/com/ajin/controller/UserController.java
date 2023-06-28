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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
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
//    @RequiresAuthentication
//    @RequestMapping("/index")
//    public Result index(){
//        User user = userService.getById(2);
//        return Result.succ(200,"操作成功",user);
//    }

    @GetMapping("/{id}")
    public Result getUser(@PathVariable(name = "id") Long id){
        User user = userService.getById(id);
        return Result.succ(user);
    }


    @GetMapping("/index")
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
        System.out.println(user);
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
    @PostMapping(value = "/upload")
    public void uploadFile(@RequestParam("files") MultipartFile[] files) throws IOException {
        // 1. 用数组MultipartFile[]来表示多文件,所以遍历数组,对其中的文件进行逐一操作
        for (MultipartFile file : files) {
            // 2. 通过一顿file.getXXX()的操作,获取文件信息。
            // 2.1 这里用文件名举个栗子
            String filename = file.getOriginalFilename();
            // 3. 接下来调用方法来保存文件到本地磁盘, 返回的是保存后的文件路径
            String filePath = savaFileByNio((FileInputStream) file.getInputStream(), filename);
            // 4. 保存文件信息到数据库
            // 4.1 搞个实体类，把你需要的文件信息保存到实体类中
            // 4.2 调用Service层或者Dao层，保存数据库即可。
        }
    }

    public static String savaFileByNio(FileInputStream fis, String fileName) {
        // 这个路径最后是在: 你的项目路径/FileSpace  也就是和src同级
//        String fileSpace = System.getProperty("user.dir") + File.separator + "FileSpace";
        String fileSpace = "/opt" + File.separator + "static";
        String path = fileSpace + File.separator + fileName;
        // 判断父文件夹是否存在
        File file = new File(path);
        if (file.getParentFile() != null || !file.getParentFile().isDirectory()) {
            file.getParentFile().mkdirs();
        }
        // 通过NIO保存文件到本地磁盘
        try {
            FileOutputStream fos = new FileOutputStream(path);
            FileChannel inChannel = fis.getChannel();
            FileChannel outChannel = fos.getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
            inChannel.close();
            outChannel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

}
