package com.ajin.service.impl;

import com.ajin.entity.Blog;
import com.ajin.mapper.BlogMapper;
import com.ajin.service.BlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author AJin
 * @since 2023-05-11
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

}
