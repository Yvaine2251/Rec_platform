package com.platform.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.platform.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ErrorRua
 * @date 2022/11/05
 * @description: 文章实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("us_article")
public class Article extends BaseEntity {
    @TableId(type= IdType.ASSIGN_ID)
    private String articleId;

    private String title;

    private String content;

    // 摘要
    private String summary;

    private Integer isTop;

    // 状态（0已发布，1草稿）
    private Integer states;

    // TODO 准备删
    private Long viewCount;

    // 是否允许评论
    private Integer isComment;

    private String userId;
}
