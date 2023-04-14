package com.platform.course.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)//下划线转为驼峰命名
public class ClassCreateDTO {
    /**
     * 教师d
     */
    private String teacherId;

    /**
     * 班级名
     */
    private String className;

    /**
     * 课程id
     */
    private String courseId;
}
