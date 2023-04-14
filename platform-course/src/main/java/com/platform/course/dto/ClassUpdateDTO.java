package com.platform.course.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)//下划线转为驼峰命名
public class ClassUpdateDTO {


    /**
     * 班级id
     */
    private String classId;

    /**
     * 班级名
     */
    private String className;

}
