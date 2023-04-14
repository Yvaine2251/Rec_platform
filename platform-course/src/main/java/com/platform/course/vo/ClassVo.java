package com.platform.course.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ClassVo {

    /**
     * 班级id
     */
    private String classId;

    /**
     * 邀请码
     */
    private String classInvitationCode;

    /**
     * 班级名
     */
    private String className;

    /**
     * 班级人数
     */
    private Integer studentNumber;
}
