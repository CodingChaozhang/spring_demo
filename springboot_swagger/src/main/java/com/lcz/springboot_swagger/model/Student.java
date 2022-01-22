package com.lcz.springboot_swagger.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author : codingchao
 * @date : 2022-01-22 14:44
 * @Description:
 **/

@ApiModel(description = "学生实体类",value = "学生对象")
public class Student {
    /**
     * 姓名
     */
    @ApiModelProperty(notes = "name of the student",name = "name",required = true,value = "姓名")
    private String name;
    /**
     * 班级
     */
    @ApiModelProperty(notes = "class of the student",name="cls",required = true,value = "班级")
    private String cls;
    /**
     * 国家
     */
    @ApiModelProperty(notes = "country of the student",name = "country",required = true,value = "国家")
    private String country;

    public Student(){

    }
    public Student(String name, String cls, String country) {
        this.name = name;
        this.cls = cls;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
