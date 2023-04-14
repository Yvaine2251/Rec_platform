package com.platform.course.controller;

import com.platform.course.dto.VideoRecordDTO;
import com.platform.course.service.VideoRecordService;
import com.platform.entity.ResponseResult;
import com.platform.course.dto.ClassTimeAddDTO;
import com.platform.course.dto.ClassTimeUpdateDTO;
import com.platform.course.service.ClassTimeService;
import com.platform.resolver.PostRequestParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/ctime")
@Api("课时操作接口")
public class ClassTimeController {

    @Autowired
    private ClassTimeService classTimeService;

    @Resource
    private VideoRecordService videoRecordService;

    @ApiOperation("添加课时")
    @PostMapping("addClassTime")
    public ResponseResult addClassTime(@RequestBody ClassTimeAddDTO classTimeAddDto) {
        return classTimeService.addClassTime(classTimeAddDto);
    }

    @ApiOperation("删除课时")
    @DeleteMapping("deleteClassTime/{id}")
    public ResponseResult deleteClassTime(@PathVariable("id") String id) {
        return classTimeService.deleteClassTime(id);
    }

    @ApiOperation("更新课时")
    @PutMapping("updateClassTime")
    public ResponseResult updateClassTime(@RequestBody ClassTimeUpdateDTO classTimeUpdateDto) {
        return classTimeService.updateClassTime(classTimeUpdateDto);
    }

    // 课时资源学习记录
    @PostMapping("addStudyRecord")
    public ResponseResult addStudyRecord(@PostRequestParam String resourceId) {
        return classTimeService.addStudyRecord(resourceId);
    }


    @ApiOperation("保存视频记录")
    @PostMapping("/set-video-record")
    public ResponseResult setVideoRecord(@RequestBody VideoRecordDTO videoRecordDTO){
        return videoRecordService.setVideoRecord(videoRecordDTO);
    }

    @ApiOperation("获取上次视频记录")
    @GetMapping("/get-last-video-record")
    public ResponseResult getVideoRecord(@RequestParam("courseId") String courseId, @RequestParam("resourceId") String resourceId){
        return videoRecordService.getVideoRecord(courseId, resourceId);
    }
}
