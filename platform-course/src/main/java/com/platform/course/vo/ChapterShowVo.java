package com.platform.course.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.platform.course.entity.ClassTime;
import io.swagger.models.auth.In;
import lombok.Data;

import java.util.List;

/**
 * @author yjj
 * @date 2022/9/13-10:32
 */
@Data
public class ChapterShowVo {
    //章节id
    private String id;

    //章节名称
    private String name;

    //章节顺序
    private Integer chapterOrder;

    //父章节id
    private String pid;

    //课程id
//    private String courseId;

    //课时列表
    List<ClassTimeVo> courTimes;

    //子章节
    List<ChapterShowVo> childChapters;
}
