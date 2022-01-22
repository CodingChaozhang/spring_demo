package com.lcz.springboot_swagger.controller;

import com.lcz.springboot_swagger.model.Student;
import com.sun.org.apache.xpath.internal.operations.Bool;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : codingchao
 * @date : 2022-01-22 14:51
 * @Description:
 **/
@Api(value = "Swagger2RestController",description = "学生服务")
@RestController
public class Swagger2RestController {

    /**
     * 组合所有学生的信息
     */
    List<Student> students = new ArrayList<>();
    {
        students.add(new Student("Sajal", "IV", "India"));
        students.add(new Student("Lokesh", "V", "India"));
        students.add(new Student("Kajal", "III", "USA"));
        students.add(new Student("Sukesh", "VI", "USA"));
    }

    /**
     * 返回所有学生信息
     * @return
     */
    @ApiOperation(value = "以列表形式返回学生信息",
            responseContainer="List",
            response = Student.class,
            tags = "getStudents")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Suceess|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping(value = "/getStudents")
    public List<Student> getStudents(){
        return students;
    }

    /**
     * 获取指定姓名的学生
     * @param name
     * @return
     */
    @ApiOperation(value = "获取指定名字的学生",
            response = Student.class,
            tags = "getStudentByName")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Suceess|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping(value = "/getStudentByName/{studentName}")
    public  Student getStudentByName(@RequestParam(value = "studentName") String name){
        return students.stream().filter(x->x.getName().equalsIgnoreCase(name)).collect(Collectors.toList()).get(0);
    }

    /**
     * 获取指定国家的学生
     * @param country
     * @return
     */
    @ApiOperation(value = "获取指定国家的学生",
            responseContainer="List",
            response = Student.class,
            tags = "getStudentByCountry")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Suceess|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping(value = "getStudentByCountry/{country}")
    public List<Student>  getStudentByCountry(@PathVariable(value = "country") String country){
        System.out.println("Searching Student in country : " + country);
        List<Student> studentsByCountry = students.stream().filter(x -> x.getCountry().equalsIgnoreCase(country))
                .collect(Collectors.toList());
        System.out.println(studentsByCountry);
        return studentsByCountry;
    }


    /**
     * 获取指定班级的学生
     * @param cls
     * @return
     */
    @ApiOperation(value = "获取指定班级的学生",
            responseContainer="List",
            response = Student.class,
            tags="getStudentByClass")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Suceess|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @RequestMapping(value = "/getStudentByClass/{cls}", method = RequestMethod.GET)
    public List<Student> getStudentByClass(@PathVariable(value = "cls") String cls) {
        return students.stream().filter(x -> x.getCls().equalsIgnoreCase(cls)).collect(Collectors.toList());
    }

    /**
     * 添加学生
     * @param student
     * @return
     */
    @ApiOperation(value = "添加学生",
            tags="addStudent")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Suceess|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @RequestMapping(value = "/addStudent", method = RequestMethod.POST, consumes = {"application/json"}, produces = {"application/json"})
    public Boolean addStudent(@RequestBody Student student){
        return students.add(student);
    }

    /**
     * 添加学生v2
     * @param name
     * @param cls
     * @param country
     * @return
     */
    @ApiOperation(value = "添加学生V2",
            tags="addStudentV2")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "姓名", paramType = "query"),
            @ApiImplicitParam(name = "cls", value = "班级", paramType = "query"),
            @ApiImplicitParam(name = "country", value = "国家", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Suceess|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @RequestMapping(value = "/addStudentV2", method = RequestMethod.GET)
    public Boolean addStudentV2(@RequestParam String name,
                                @RequestParam String cls,
                                @RequestParam String country) {
        Student student = new Student(name, cls, country);
        return students.add(student);
    }

    /**
     * 查找指定班级指定名字的学生
     * @param name
     * @param cls
     * @return
     */
    @ApiOperation(value = "查找指定班级指定名字的学生", tags = "getStudentByNameAndCls")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Suceess|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @RequestMapping(value = "getStudentByNameAndCls", method = RequestMethod.GET)
    public Student getStudentByNameAndCls(@RequestParam String name, @RequestParam String cls) {
        return students.stream()
                .filter(x -> x.getCls().equals(cls) && x.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList()).get(0);
    }

    /**
     * 删除指定姓名的学生
     * @param name
     * @return
     */
    @ApiOperation(value = "删除指定名字的学生", tags = "delStudentByName")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Suceess|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @RequestMapping(value = "delStudentByName", method = RequestMethod.GET)
    public Student delStudentByName(@RequestParam String name) {
        Student tempStudent = null;
        for (Student student : students) {
            if (student.getName().equalsIgnoreCase(name)) {
                tempStudent = student;
                break;
            }
        }
        students.remove(tempStudent);
        return tempStudent;
    }
}
