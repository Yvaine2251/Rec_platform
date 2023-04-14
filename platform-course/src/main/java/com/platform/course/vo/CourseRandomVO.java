package com.platform.course.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseRandomVO {

    //课程id
    private String courseId;

    //课程名字
    private String courseName;

    //课程下班级里总学生
    private int courseStuCnt;

    //课程创建者的学校
    private String school;

    //课程简短描述
    private String courseSubDescribe;

    //课程推荐课程
    private String courseRecommendReason;
}
