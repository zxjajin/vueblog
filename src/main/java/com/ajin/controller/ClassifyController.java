package com.ajin.controller;

import com.ajin.common.lang.Result;
import com.ajin.entity.Blog;
import com.ajin.entity.Classify;
import com.ajin.service.BlogService;
import com.ajin.service.ClassifyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ajin
 * @create 2023-06-26 2:43
 */
@RestController
@RequestMapping("/classify")
public class ClassifyController {
    @Autowired
    private ClassifyService classifyService;

    @Autowired
    private BlogService blogService;

    @GetMapping
    public Result getlist(){
        List<Classify> list = classifyService.list();
        return Result.succ(list);
    }

    @GetMapping("/wz/{id}")
    public Result getwz(@PathVariable("id") Long id){
        Classify classify = classifyService.getById(id);
        Assert.notNull(classify,"该分类不存在");
        return Result.succ(classify);
    }

    /**
     * 根据分类查询
     * @param id
     * @param currentPage
     * @return
     */
    @GetMapping("/{id}")
    public Result getClassify(@PathVariable("id") Long id, @RequestParam(defaultValue = "1") Integer currentPage){
        IPage<Blog> blogs = blogService.page(new Page<Blog>(currentPage, 5),
                new QueryWrapper<Blog>().eq("classify_id", id));
        return Result.succ(blogs);

    }

    /**
     * 修改分类
     * @param classify
     * @return
     */
    @PutMapping
    public Result update(@RequestBody Classify classify){
        boolean b = classifyService.updateById(classify);
        Assert.isTrue(b,"修改错误！");
        return Result.succ("修改成功");
    }

    /**
     * 增加分类
     */
    @PostMapping
    public Result add(@RequestBody Classify classify){
        boolean save = classifyService.save(classify);
        Assert.isTrue(save,"添加分类失败");
        return Result.succ("添加成功");
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") Long id){
        boolean b = classifyService.removeById(id);
        Assert.isTrue(b,"删除错误！");
        return Result.succ("删除成功");
    }

}
