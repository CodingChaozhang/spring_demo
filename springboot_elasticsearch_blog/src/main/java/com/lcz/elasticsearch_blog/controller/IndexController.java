package com.lcz.elasticsearch_blog.controller;

import com.lcz.elasticsearch_blog.entity.mysql.MySqlBlog;
import com.lcz.elasticsearch_blog.respository.mysql.MySqlBlogRespository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


/**
 * @author : codingchao
 * @date : 2022-01-17 11:27
 * @Description:
 **/
@Controller
@Slf4j
public class IndexController {
    @Autowired
    private MySqlBlogRespository mySqlBlogRespository;

    @RequestMapping("/")
    public String index(){
        List<MySqlBlog> all = mySqlBlogRespository.findAll();
        System.out.println(all.size());
        return "index.html";
    }
}
