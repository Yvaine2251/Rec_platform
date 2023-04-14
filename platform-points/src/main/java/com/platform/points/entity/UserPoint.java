package com.platform.points.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yjj
 * @date 2022/10/6-10:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("st_user_point")
public class UserPoint extends BaseEntity {

    private String userId;

    private String pointId;

    private String pointName;

    private String courseId;

    //掌握程度（0-100）
    private Integer level;
}
