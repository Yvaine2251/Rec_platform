package com.platform.course.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.platform.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@TableName("co_class_user")
public class ClassUser extends BaseEntity {
    /**
     * 用户id
     */
    private String userId;

    /**
     * 班级id
     */
    private String classId;

    /**
     * 学生角色
     */
    private Integer role;

}
