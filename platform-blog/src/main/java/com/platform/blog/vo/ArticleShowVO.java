package com.platform.blog.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author ErrorRua
 * @date 2022/11/06
 * @description:
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleShowVO {
    private String articleId;

    private String title;
    // 摘要
    private String summary;

    private Integer isTop;

    // TODO 准备删
    private Long viewCount;

    private Long likes;

    private Long comments;

    private Long collects;

    private String updateTime;


}
