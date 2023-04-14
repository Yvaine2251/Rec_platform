package com.platform.controller;

import com.platform.entity.ResponseResult;
import com.platform.exam.constants.LevelType;
import com.platform.exam.listener.LevelApplicationEvent;
import com.platform.exam.utils.GetMulticaster;
import com.platform.points.service.PointService;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/recommend")
public class RecommendController {

    @Resource
    private PointService pointService;

    @ApiOperation("学习路径推荐")
    @GetMapping("/learningPath")
    public ResponseResult getPath(@RequestParam("pointId") String id, String courseId) {
        return pointService.getPath(id, courseId);
    }
}
