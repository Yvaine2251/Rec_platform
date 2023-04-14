package com.platform.statistics.controller;

import com.platform.entity.ResponseResult;
import com.platform.statistics.service.StatisticService;
import io.swagger.annotations.ApiOperation;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Resource
    private StatisticService statisticService;

    @ApiOperation("获取课程视频总播放时长")
    @GetMapping("/get-video-total-time")
    public ResponseResult getVideoTotalTime(@RequestParam("courseId") String courseId,@Nullable String userId){
        return statisticService.getVideoTotalTime(courseId,userId);
    }

    @ApiOperation("获取资源学习次数统计")
    @GetMapping("/study-count")
    public ResponseResult getStudyCount(@RequestParam String courseId,@Nullable String userId){
        return statisticService.getStudyCount(courseId,userId);
    }

    @ApiOperation("获取做题记录统计")
    @GetMapping("/question-record")
    public ResponseResult getQuestionRecord(@RequestParam String courseId,@Nullable String userId){
        return statisticService.getQuestionRecord(courseId,userId);
    }

    @ApiOperation("获取考试完成进度")
    @GetMapping("/exam-record")
    public ResponseResult getExamRecord(@RequestParam String courseId){
        return statisticService.getExamRecord(courseId);
    }
}