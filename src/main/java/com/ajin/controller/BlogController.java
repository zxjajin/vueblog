package com.ajin.controller;


import cn.hutool.core.bean.BeanUtil;
import com.ajin.common.lang.Result;
import com.ajin.entity.Blog;
import com.ajin.service.BlogService;
import com.ajin.util.ShiroUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
public class BlogController {

    @Autowired
    BlogService blogService;

    @GetMapping("/blogs")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage){

        Page page = new Page(currentPage,5);
        IPage pageDate = blogService.page(page, new QueryWrapper<Blog>().orderByDesc("created"));
        return Result.succ(pageDate);
    }

    @GetMapping("/blog/{id}")
    public Result detail(@PathVariable(name = "id") Long id){
        Blog blog = blogService.getById(id);
        Assert.notNull(blog,"该博客已被删除");
        return Result.succ(blog);
    }

    @RequiresAuthentication
    @PostMapping("blog/edit")
    public Result edit(@Validated @RequestBody Blog blog){

        Blog temp = null;
        if(blog.getId() != null){
            temp = blogService.getById(blog.getId());
            //只能编辑自己的文章
            Assert.isTrue(temp.getUserId() == ShiroUtil.getProfile().getId().longValue(),"没有权限编辑");
        }else{
            temp = new Blog();
            temp.setUserId(ShiroUtil.getProfile().getId());
            temp.setCreated(LocalDateTime.now());
            temp.setStatus(0);
            temp.setId(0L);
        }
        BeanUtil.copyProperties(blog,temp,"id","userId","created","status");
        blogService.saveOrUpdate(temp);
        return Result.succ(temp);
    }

    @RequiresAuthentication
    @DeleteMapping("blog/{id}")
    public Result delete(@PathVariable(name = "id") Long id){
            boolean b = blogService.removeById(id);
            Assert.isTrue(b,"删除失败");
        return Result.succ("删除的id为："+id);
    }

}
