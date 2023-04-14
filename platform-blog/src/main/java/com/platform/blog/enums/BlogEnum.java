package com.platform.blog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ErrorRua
 * @date 2022/11/06
 * @description:
 */
@Getter
@AllArgsConstructor
public enum BlogEnum {
    NOT_COLLECT_YET(50000, "并未收藏"),
    NOT_LIKE_YET(50000, "并未点赞"),
    ALREADY_LIKE(50001, "已经点赞过了"),
    ALREADY_COLLECT(50001, "已经收藏过了"),

    CANNOT_COLLECTION(50002, "不能收藏自己的文章"),
    ;



    private final Integer code;
    private final String message;
}
