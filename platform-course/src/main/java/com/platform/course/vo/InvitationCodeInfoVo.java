package com.platform.course.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvitationCodeInfoVo {

    /**
     * 班级id
     */
    private String classId;

    /**
     * 课程名
     */
    private String courseName;

    /**
     * 课程封面路径
     */
    private String courseCover;

    /**
     * 老师名称
     */
    private String teacherName;
}
