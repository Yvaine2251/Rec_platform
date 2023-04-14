package com.platform.blog.vo;

import lombok.Data;

/**
 * @author ErrorRua
 * @date 2022/11/07
 * @description: 文章草稿
 */
@Data
public class ArticleDraftVO {
    private String articleId;

    private String title;
    // 摘要
    private String summary;
}
