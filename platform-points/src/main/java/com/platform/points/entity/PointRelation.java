package com.platform.points.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.entity.BaseEntity;
import com.platform.points.dto.PointRelationDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("co_point_relation")
public class PointRelation extends BaseEntity {

    @TableField(value="point_a_id")
    private String pointAId;

    @TableField(value = "point_b_id")
    private String pointBId;

    @TableField(value = "course_id")
    private String courseId;

    @TableField(value = "relation")
    private int relation;

    public PointRelation(String pointAId, String pointBId, String courseId) {
        this.pointAId = pointAId;
        this.pointBId = pointBId;
        this.courseId = courseId;
    }

    public PointRelation(PointRelationDto relationDto) {
        this.pointAId = relationDto.getPointAId();
        this.pointBId = relationDto.getPointBId();
//        this.setCreateTime(LocalDateTime.now());
//        this.setUpdateTime(LocalDateTime.now());
//        this.setDelFlag(0);
//        this.setVersion(1);
    }
}
