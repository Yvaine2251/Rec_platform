package com.platform.points.controller;

import com.platform.entity.ResponseResult;
import com.platform.points.dto.PointRelationDto;
import com.platform.points.service.GraphService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@ApiOperation("知识图谱")
@RequestMapping("/points/KG")
public class GraphController {
    @Autowired
    private GraphService graphService;

    @ApiOperation("显示课程知识图谱")
    @GetMapping("/show")
    public ResponseResult showKG(String courseId){
        return graphService.showGraph(courseId);
    }

    //显示个人知识图谱
    @GetMapping("/student")
    public ResponseResult showStudentKG(String courseId) {
        return graphService.showStudentKG(courseId);
    }

    @Deprecated
    @ApiOperation("导出文件")
    @GetMapping("")
    public ResponseResult exportGraph() throws Exception{
        return graphService.downloadRelationFile();
    }

    @GetMapping("/TTT")
    public ResponseResult tryConnect(){

        //graphService.connectSocket();
        //graphService.createCSV();
        //graphService.createPoint2Question();
        graphService.createStudent2Question();
        return null;
    }
}
