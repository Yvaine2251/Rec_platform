package com.platform.course.controller;

import com.platform.entity.ResponseResult;
import com.platform.course.dto.ChapterAddDTO;
import com.platform.course.dto.ChapterUpdateDTO;
import com.platform.course.service.ChapterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chapter")
@Api(tags="章节操作接口")
public class ChapterController {

    @Autowired
    ChapterService chapterService;

    @ApiOperation(value="新建章节")
    @PostMapping("/addChapter")
    public ResponseResult addChapter(@RequestBody ChapterAddDTO chapterAddDto){
        return chapterService.addChapter(chapterAddDto);
    }

    @ApiOperation(value="更改章节名称")
    @PutMapping("/updateChapterName")
    public ResponseResult updateChapterName(@RequestBody ChapterUpdateDTO chapterUpdateDto){
        return chapterService.updateChapterName(chapterUpdateDto);
    }

    @ApiOperation(value="删除章节")
    @DeleteMapping("deleteChapter/{id}")
    public ResponseResult deleteChapter(@PathVariable("id") String chapterId){
        return chapterService.deleteChapter(chapterId);
    }

    @ApiOperation(value="获取章节列表")
    @GetMapping("getChapter")
    public ResponseResult getChapter(String courseId){
        return chapterService.getChapter(courseId);
    }
}
