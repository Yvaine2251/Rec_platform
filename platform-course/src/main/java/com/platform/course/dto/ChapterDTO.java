package com.platform.course.dto;

import com.platform.course.vo.ClassTimeVo;
import lombok.Data;

import java.util.List;


@Data
public class ChapterDTO {

    String  chapterId;

    String  name;

    Integer chapterOrder;

    String courseId;

    // 课时
    List<ClassTimeVo> courTimes;

    // 子章节列表
    List<ChapterDTO> childChapters;
}
