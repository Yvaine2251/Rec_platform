package com.platform.course.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseVo {
    /**
     * 课程id
     */
    private String courseId;

    /**
     * 课程名
     */
    private String courseName;

    /**
     * 课程封面图片路径
     */
    private String courseCover;

    /**
     * 课程介绍
     */
    private String courseDescribe;
}
