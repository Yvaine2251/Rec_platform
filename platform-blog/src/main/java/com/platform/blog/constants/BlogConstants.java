package com.platform.blog.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ErrorRua
 * @date 2022/11/06
 * @description:
 */
@Getter
@AllArgsConstructor
public enum BlogConstants {
    ARTICLE(0, "文章"),
    TOPIC(1, "话题"),

    NOT_LIKE(0, "未点赞"),
    IS_LIKE(1, "已点赞"),

    NOT_COLLECT(0, "未收藏"),
    IS_COLLECT(1, "已收藏"),

    ;

    private final Integer type;
    private final String typeName;
}
