package com.platform.course.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("co_class_time_resource")
public class ClassTimeResource extends BaseEntity {

    @TableField("time_id")
    private String classTimeId;

    @TableField("resource_id")
    private String resourceId;
}
