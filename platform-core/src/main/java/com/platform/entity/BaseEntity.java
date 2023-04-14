package com.platform.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 定义基础实体类，固定 id，创建时间、乐观锁、更新时间、逻辑删除
 */
@Data
public class BaseEntity {

    //乐观锁
    @Version
    @TableField(fill= FieldFill.INSERT)
    private Integer version;

    /*逻辑删除*/
    /*@TableLogic*/
    @TableField(fill= FieldFill.INSERT)
    private Integer delFlag;

    //创建时间
    @TableField(fill= FieldFill.INSERT)
    private LocalDateTime createTime;

    //修改时间
    @TableField(fill=FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
