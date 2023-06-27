package com.ajin.controller;

import com.ajin.common.lang.Result;
import com.ajin.entity.Classify;
import com.ajin.service.ClassifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping
    public Result getlist(){
        List<Classify> list = classifyService.list();
        return Result.succ(list);
    }


}
