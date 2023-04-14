package com.platform.course.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.entity.BaseEntity;
import lombok.Data;

/**
 * @author ErrorRua
 * @date 2022/12/15
 * @description:
 */
@Data
@TableName("co_study_record")
public class StudyRecord extends BaseEntity {

    @TableId
    private String recordId;
    private String resourceId;
    private String userId;
}
