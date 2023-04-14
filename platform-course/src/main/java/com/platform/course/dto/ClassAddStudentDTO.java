package com.platform.course.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassAddStudentDTO {
    /**
     * 班级id
     */
    private String classId;

    /**
     * 学生姓名
     */
    private String name;

    /**
     * 学生手机号
     */
    private String mobile;
}
