package com.platform.course.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ClassUserVo {

    /**
     * 学生id
     */
    private String userId;

    /**
     * 用户名
     */
    private String name;

    /**
     * 学号 （手机号）
     */
    private String mobile;

    /**
     * 班级名
     */
    private String className;

    /**
     * 加入时间
     */
    private LocalDateTime joinTime;

    /**
     * 角色
     */
    private Integer role;
}
