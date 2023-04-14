package com.platform.course.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)//下划线转为驼峰命名
public class CourseUpdateDTO {

    /**
     * 课程id
     */
    private String courseId;

    /**
     * 课程名
     */
    private String courseName;

    /**
     * 课程封面
     */
    private String courseCover;

    /**
     * 课程描述
     */
    private String courseDescribe;

    /**
     * 课程简短描述
     */
    private String courseSubDescribe;

    /**
     * 是否为公开课程
     */
    private int isPublic;

    /**
     * 课程推荐理由
     */
    private String courseRecommendReason;
}
