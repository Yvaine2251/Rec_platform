package com.platform.course.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.entity.ResponseResult;
import com.platform.course.dto.ChapterAddDTO;
import com.platform.course.dto.ChapterUpdateDTO;
import com.platform.course.entity.Chapter;


public interface ChapterService extends IService<Chapter> {

    ResponseResult addChapter(ChapterAddDTO chapterAddDto);

    ResponseResult updateChapterName(ChapterUpdateDTO chapterUpdateDto);

    ResponseResult deleteChapter(String chapterId);

    ResponseResult getChapter(String courseId);
}
