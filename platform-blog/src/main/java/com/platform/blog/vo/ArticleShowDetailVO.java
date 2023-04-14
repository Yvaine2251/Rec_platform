package com.platform.blog.vo;

import com.platform.blog.entity.Article;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author ErrorRua
 * @date 2022/11/06
 * @description:
 */
@Data
public class ArticleShowDetailVO {

    private String articleId;
    private String title;
    private String content;
    // 摘要
    private String summary;

    private String userId;

    // TODO 准备删
    private Long viewCount;

    // 是否允许评论
    private Integer isComment;

    private Long likes;
    private Long comments;
    private Long collects;

    private Integer isLike;
    private Integer isCollect;

    private LocalDateTime updateTime;
}
