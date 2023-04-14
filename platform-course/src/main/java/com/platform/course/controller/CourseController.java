package com.platform.course.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.platform.entity.ResponseResult;
import com.platform.course.dto.CourseUpdateDTO;
import com.platform.course.service.CourseService;
import com.platform.course.dto.CourseCreateDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @ApiOperation(value = "显示创建的课程")
    @GetMapping("/show-create")
    public ResponseResult showCreateCourse() throws Exception {
        return courseService.showCreateCourse();
    }

    //创建课程
    @ApiOperation(value = "创建课程")
    @PostMapping("/create")
    public ResponseResult createCourse(@RequestBody CourseCreateDTO courseCreateDto) throws Exception {
        return courseService.createCourse(courseCreateDto);
    }

    @ApiOperation(value = "修改课程信息")
    @PutMapping("/update")
    public ResponseResult updateCourse(@RequestBody CourseUpdateDTO courseUpdateDto) throws Exception {
        return courseService.updateCourse(courseUpdateDto);
    }

    @ApiOperation("删除课程")
    @DeleteMapping("/delete/{id}")
    public ResponseResult deleteCourse(@PathVariable("id") String userId) {
        return courseService.deleteCourse(userId);
    }

    @ApiOperation(value = "显示加入的课程")
    @GetMapping("/show-join")
    public ResponseResult showJoinCourse() throws Exception {
        return courseService.showJoinCourse();
    }

    @ApiOperation("回显单个课程信息")
    @GetMapping("/get-one")
    public ResponseResult selectOneCourse(String courseId) {
        return courseService.selectOneCourse(courseId);
    }

    @ApiOperation("随机推荐课程")
    @GetMapping("/get-random")
    public ResponseResult getRandomCourse(){
        return courseService.getRandomCourse();
    }

    @ApiOperation("推荐课程详细")
    @GetMapping("/get-random-detail/{courseId}")
    public ResponseResult getRandomCourseDetail(@PathVariable String courseId){
        return courseService.getRandomCourseDetail(courseId);
    }
}
