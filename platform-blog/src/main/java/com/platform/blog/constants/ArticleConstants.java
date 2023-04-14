package com.platform.blog.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ErrorRua
 * @date 2022/11/06
 * @description: 文章常量
 */
@Getter
@AllArgsConstructor
public enum ArticleConstants {
    ARTICLE_SAVED(0, "草稿"),
    ARTICLE_PUBLISHED(1, "已发布"),

    IS_TOP(1, "置顶"),
    NOT_TOP(0, "未置顶"),
    ;

    private final Integer type;
    private final String typeName;
}
