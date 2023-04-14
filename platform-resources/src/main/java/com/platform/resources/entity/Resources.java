package com.platform.resources.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.entity.BaseEntity;
import io.github.classgraph.json.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("co_resource")
public class Resources extends BaseEntity {
    /**
     * 资源id
     */
    @Id
    @TableId(type = IdType.ASSIGN_ID)
    private String resourceId;

    /**
     * 资源名
     */
    private String resourceName;

    /**
     * 课程id
     */
    private String courseId;

    /**
     *  资源类型
     */
    private Integer type;

    /**
     * 资源链接
     */
    private String resourceLink;

    /**
     *
     */
    @TableField("resource_uuid")
    private String resourceUUID;
}
