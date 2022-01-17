package com.lcz.elasticsearch_blog.controller;

import com.lcz.elasticsearch_blog.entity.es.EsBlog;
import com.lcz.elasticsearch_blog.entity.mysql.MySqlBlog;
import com.lcz.elasticsearch_blog.respository.es.EsBlogRepository;
import com.lcz.elasticsearch_blog.respository.mysql.MySqlBlogRespository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * @author : codingchao
 * @date : 2022-01-17 11:54
 * @Description:
 **/
@RestController
@Slf4j
public class DataController {

    @Autowired
    private MySqlBlogRespository mysqlBlogRepository;
    @Autowired
    private EsBlogRepository esBlogRepository;

    @GetMapping("/blogs")
    public Object blog(){
        List<MySqlBlog> mysqlBlogs = mysqlBlogRepository.queryAll();
        return mysqlBlogs;
    }

    @PostMapping("/search")
    public Object search(@RequestBody Param param){
        Map<String, Object> map = new HashMap<>();
        String type = param.getType();
        StopWatch watch = new StopWatch();
        watch.start();
        if(type.equalsIgnoreCase("mysql")){
            List<MySqlBlog> mysqlBlogs = mysqlBlogRepository.queryBlogs(param.getKeyword());
            map.put("list",mysqlBlogs);
        }else if(type.equalsIgnoreCase("es")){
            BoolQueryBuilder builder = QueryBuilders.boolQuery();
            builder.should(QueryBuilders.matchPhraseQuery("title",param.getKeyword()));
            builder.should(QueryBuilders.matchPhraseQuery("content",param.getKeyword()));
            String s = builder.toString();
            System.out.println("======");
            System.out.println(s);
            System.out.println("======");
            Page<EsBlog> esBlogs = (Page<EsBlog>) esBlogRepository.search(builder);
            List<EsBlog> content = esBlogs.getContent();
            map.put("list",content);
        }else {
            return ">>> 不知道 <<<";
        }
        watch.stop();
        long totalTimeMillis = watch.getTotalTimeMillis();
        map.put("duration",totalTimeMillis);
        return map;
    }

    @GetMapping("/blog/{id}")
    public Object blog(@PathVariable Integer id){
        Optional<MySqlBlog> byId = mysqlBlogRepository.findById(id);
        return byId.get();
    }


    @Data
    public static class Param{
        // String,es
        private String type;
        private String keyword;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }
    }
}