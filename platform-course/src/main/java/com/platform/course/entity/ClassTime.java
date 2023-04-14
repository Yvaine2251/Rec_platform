package com.platform.course.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("co_class_time")
public class ClassTime extends BaseEntity {

    /**
     * 课时id
     */
    @TableId(type= IdType.ASSIGN_ID)
    @TableField("id")
    private String classTimeId;

    /**
     * 课时名称
     */
    @TableField("name")
    private String name;

    /**
     * 课时所属章节id
     */
    @TableField("chapter_id")
    private String chapterId;

    /**
     * 作业id
     */
    @TableField("paper_id")
    private String paperId;

    //作业名称
    @TableField("paper_name")
    private String paperName;

    /**
     * 课时顺序
     */
    @TableField("class_time_order")
    private Integer classTimeOrder;
}
