package com.platform.course.controller;

import com.platform.entity.ResponseResult;
import com.platform.course.dto.*;
import com.platform.course.service.ClassService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/class")
@Api(tags = "班级操作接口")
public class ClassController {

    @Autowired
    private ClassService classService;

    @ApiOperation(value = "创建班级")
    @PostMapping("create")
    public ResponseResult createClass(@RequestBody ClassCreateDTO classCreateDto) throws Exception {
        return classService.createClass(classCreateDto);
    }

    @ApiOperation(value = "显示班级列表")
    @GetMapping("/show")
    public ResponseResult showClass(String courseId) {
//        if (!StringUtils.hasText(courseId)) {
//            throw new PlatformException(999, "i love");
//        }
        return classService.showClass(courseId);
    }

    @ApiOperation(value = "邀请码查看班级信息")
    @GetMapping("/invitation-code")
    public ResponseResult invitationCodeInfo(String classInvitationCode) {
        return classService.invitationCodeInfo(classInvitationCode);
    }

    @ApiOperation(value = "加入班级")
    @PostMapping("/join")
    public ResponseResult joinClass(String classId, String ability, String expect) throws Exception {
        return classService.joinClass(classId, ability, expect);
    }

    @ApiOperation(value = "删除班级")
    @DeleteMapping("/delete/{id}")
    public ResponseResult deleteClass(@PathVariable("id") String classId) throws Exception {
        return classService.deleteClass(classId);
    }

    @ApiOperation(value = "修改班级名")
    @PutMapping("/update-name")
    public ResponseResult updateClassName(@RequestBody ClassUpdateDTO classUpdateDto) throws Exception {
        return classService.updateClassName(classUpdateDto);
    }

    @ApiOperation(value = "添加学生")
    @PostMapping("/add-student")
    public ResponseResult addStudent(@RequestBody ClassAddStudentDTO classAddStudent) throws Exception {
        return classService.addStudent(classAddStudent);
    }

    @ApiOperation("移除学生")
    @DeleteMapping("/remove-student")
    public ResponseResult removeStudentFromClass(@RequestBody ClassUserDTO classUserDto) throws Exception {
        return classService.removeStudentFromClass(classUserDto);
    }

    @ApiOperation(value = "显示班级学生列表，班级管理页面")
    @GetMapping("/show-student")
    public ResponseResult classUserByClassId(String classId) {
        return classService.classUserByClassId(classId);
    }

    @ApiOperation(value = "退出课程（班级）")
    @DeleteMapping("/quit")
    public ResponseResult quitCourse(String classId) {
        return classService.quitCourse(classId);
    }

    @ApiOperation("通过首页推荐进入公共班级")
    @GetMapping("/join-public/{courseId}")
    public ResponseResult joinPublicClass(@PathVariable("courseId") String courseId){
        return classService.joinPublicClass(courseId);
    }
}
