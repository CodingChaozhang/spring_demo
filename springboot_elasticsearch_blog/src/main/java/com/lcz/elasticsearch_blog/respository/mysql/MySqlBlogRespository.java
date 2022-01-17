package com.lcz.elasticsearch_blog.respository.mysql;

import com.lcz.elasticsearch_blog.entity.mysql.MySqlBlog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author : codingchao
 * @date : 2022-01-17 10:39
 * @Description:
 **/
public interface MySqlBlogRespository extends JpaRepository<MySqlBlog,Integer> {
    @Query("select e from MySqlBlog e order by e.createTime desc")
    List<MySqlBlog> queryAll();
    @Query("select e from MySqlBlog e where e.title like concat('%',:keyword,'%') " +
            "or e.content like concat('%',:keyword,'%') order by e.createTime desc")
    List<MySqlBlog> queryBlogs(@Param("keyword") String keyword);
}
