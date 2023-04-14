package com.platform.course.dto;


import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)//反序列,xx_xx转化为驼峰
public class CourseCreateDTO {

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
     * 是否为公共课程
     */
    private int isPublic;

    /**
     * 课程推荐理由
     */
    private String courseRecommendReason;
}
