package com.platform.course.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.platform.entity.BaseEntity;
import io.github.classgraph.json.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@TableName("co_course")
public class Course extends BaseEntity {
    /**
     * 课程id
     */
    @Id
    @TableId(type = IdType.ASSIGN_ID)
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

    /**
     * 课程创建者id
     */
    private String courseBuilderId;

    /**
     * 课程简短描述
     */
    private String courseSubDescribe;

    /**
     * 课程推荐理由
     */
    private String courseRecommendReason;

    /**
     * 是否为公共课程
     */
    private int isPublic;
}
