package com.platform.blog.vo;

import lombok.Data;

/**
 * @author ErrorRua
 * @date 2022/11/10
 * @description:
 */
@Data
public class ArticleDraftShowDetailVO {
    private String articleId;
    private String title;
    private String content;
    // 摘要
    private String summary;
}
