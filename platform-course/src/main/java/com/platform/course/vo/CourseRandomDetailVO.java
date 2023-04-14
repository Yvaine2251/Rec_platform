package com.platform.course.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseRandomDetailVO {

    //课程总人数
    private int courseStuCnt;

    //课程学校
    private String school;

    //教师信息
    private CourseRandomTeacherVO teacher;

    //课程简短描述
    private String courseSubDescribe;

    //课程描述
    private String courseDescribe;
}
