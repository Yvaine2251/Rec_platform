package com.platform.resources.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("co_resource_point")
public class ResourceToPoint extends BaseEntity {

    @TableField("point_id")
    String pointId;

    @TableField("resource_id")
    String resourceId;
}
