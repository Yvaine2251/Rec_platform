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
@TableName("co_class")
public class Class extends BaseEntity {

    /*雪花算法自动生成*/
    @Id
    @TableId(type = IdType.ASSIGN_ID)
    private String classId;

    /**
     * 课程id
     */
    private String courseId;

    /**
     * 教师id
     */
    private String teacherId;

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

    /**
     * 是否是公开班级
     */
    private int isPublic;
}
