package com.lcz.elasticsearch_blog.entity.es;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.*;
import java.util.Date;


/**
 * @author : codingchao
 * @date : 2022-01-17 11:37
 * @Description:
 **/
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * @program: estest
 * @description: ES实体类
 * @author: Mr.Huang
 * @create: 2020-03-30 21:21
 **/
@Data
@Document(indexName = "blog", type = "doc",
        useServerConfiguration = true, createIndex = false)
public class EsBlog {
    @Id
    private Integer id;
    @Field(type = FieldType.Text, analyzer = "ik_max_work")
    private String title;
    @Field(type = FieldType.Text, analyzer = "ik_max_work")
    private String author;
    @Field(type = FieldType.Text, analyzer = "ik_max_work")
    private String content;
    @Field(type = FieldType.Date, format = DateFormat.custom,
            pattern = "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis")
    @JsonAlias(value = "create_time")
    private Date createTime;
    @Field(type = FieldType.Date, format = DateFormat.custom,
            pattern = "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis")
    @JsonAlias(value = "update_time")
    private Date updateTime;

    public String getTitle() {
        return title;
    }
}
