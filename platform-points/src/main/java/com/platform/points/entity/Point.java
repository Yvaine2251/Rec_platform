package com.platform.points.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.platform.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * (CoPoint)表实体类
 *
 * @author makejava
 * @since 2022-04-25 10:46:21
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@EqualsAndHashCode
@TableName("co_point")
public class Point extends BaseEntity {

    //知识点id
    @TableId(value="id",type = IdType.ASSIGN_ID)
    private String pointId;

    //知识点名
    @TableField(value="name",fill= FieldFill.INSERT_UPDATE)
    private String pointName;

    //知识点Pid
    @TableField(value="pid")
    private String pointPid;

    //课程号
    @TableField(value = "course_id")
    private String courseId;

    ////子知识点列表
    //private List<Point> sonPoints;

    public Point(String pointName, String pointPid, String courseId) {
        this.pointName = pointName;
        this.pointPid = pointPid;
        this.courseId = courseId;
    }
}

