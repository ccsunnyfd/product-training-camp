package com.productcamp.demo.controller;

import com.productcamp.demo.model.Course;
import com.productcamp.demo.model.RespBean;
import com.productcamp.demo.service.CourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CourseController
 *
 * @version 1.0
 */
@RestController
@RequestMapping(value = "api/course")
@Api(value = "课程章节的api接口", tags={"课程章节相关"})
public class CourseController {
    private CourseService courseService;

    @Autowired
    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("list")
    @ApiOperation(value = "根据产品id获取课程章节信息")
    public Map<String, Object> getCoursesByProdId(@RequestParam(value = "prodId") Long prodId ) {
        Map<String, Object> map = new HashMap<>();
        List<Course> res = null;
        RespBean respBean;
        try {
            res = courseService.getCoursesByProductId(prodId);
            respBean = new RespBean("success", "获取课程章节成功");
        } catch (Exception e) {
            e.printStackTrace();
            respBean = new RespBean("failure", "获取课程章节失败");
        }

        map.put("status", respBean.getStatus());
        map.put("msg", respBean.getMsg());
        map.put("data", res);
        return map;
    }

    @PostMapping("add")
    @ApiOperation(value = "添加新的课程章节")
    public RespBean addNewCourse(@RequestBody Course newCourse) {
        Long newId = courseService.addNewCourse(newCourse);
        if (newId != null) {
            return new RespBean("success", "添加应用案例成功");
        } else {
            return new RespBean("error", "添加应用案例失败");
        }
    }
}
