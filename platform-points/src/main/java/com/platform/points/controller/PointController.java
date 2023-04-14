package com.platform.points.controller;

import com.platform.entity.ResponseResult;
import com.platform.points.dto.*;
import com.platform.points.service.PointService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/points")
public class PointController {

    @Autowired
    PointService pointService;

    @ApiOperation("添加同级知识点 or 添加子级知识点")
    @PostMapping("/add-point")
    ResponseResult addPoint(@RequestBody PointCreateDto createDto){
        return pointService.addPoints(createDto);
    }

    @ApiOperation(value="更新知识点名字")
    @PutMapping("/update-name")
    ResponseResult updatePointName(@RequestBody PointUpdateDto updateDto){
        return pointService.updatePointName(updateDto);
    }

    @ApiOperation(value="更新知识点层级")
    @PutMapping("/update-level")
    ResponseResult updatePointLevel(@RequestBody PointUpdateLevelDto updateDto){
        return pointService.updatePointLevel(updateDto);
    }

    @ApiOperation(value="删除知识点")
    @DeleteMapping(value="/delete")
    ResponseResult deletePoint(@RequestParam("pointIds[]") String[] pointIds){
        return pointService.deletePoints(pointIds);
    }

    @ApiOperation(value="显示课程知识点")
    @GetMapping("/show")
    ResponseResult getPointList(String courseId){
        return pointService.getPoints(courseId);
    }

    @PostMapping("/pre-points")
    ResponseResult addPrePoint(@RequestBody PointAddPreDto pointAddPreDto) {
        return pointService.addPrePoint(pointAddPreDto);
    }

    @PostMapping("/after-points")
    ResponseResult addAfterPoint(@RequestBody PointAddAfterDto pointAddAfterDto) {
        return pointService.addAfterPoint(pointAddAfterDto);
    }

    @Deprecated
    @ApiOperation("批量导入")
    @PostMapping("")
    public ResponseResult importGraph(String courseId, @RequestParam("excelFile") MultipartFile file) throws Exception {
        return pointService.uploadFile(courseId, file);
    }

    @ApiOperation("导入知识点excel")
    @PostMapping("/excel/import")
    public ResponseResult importExcel(String courseId, @RequestParam("excelFile") MultipartFile file) throws IOException {
        return pointService.importExcelFile(courseId, file);
    }

}
