package com.platform.course.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * (CoCourseChapter)表实体类
 *
 * @author makejava
 * @since 2022-05-04 19:26:00
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("co_chapter")
public class Chapter extends BaseEntity {
    //章节id
    @TableId(type= IdType.ASSIGN_ID)
    private String id;

    //章节名称
    private String name;

    //父章节id
    private String pid;

    //课程id
    private String courseId;

    //章节顺序    小数靠前
    private Integer chapterOrder;

    //是否存在课时
    private Integer hasCoursetime;
}

