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
public enum ArticleEnum {
    ARTICLE_NOT_EXIST(50000, "文章不存在"),
    ARTICLE_DRAFT_NOT_EXIST(50001, "草稿不存在"),
    ARTICLE_HAS_PUBLISHED(50002, "文章已发布"),
    NOT_OWNER(50001, "不是文章的所有者"),
    UPDATE_ARTICLE_FAIL(50002, "修改失败"),
    DELETE_ARTICLE_FAIL(50003, "删除失败"),

    TOP_OPERATION_FAIL(50004, "置顶操作失败"),

    ARTICLE_CREATE_FAIL(50005, "文章创建失败"),

    DRAFT_IS_FULL(50005, "草稿箱已满"),
    ;



    private final Integer code;
    private final String message;
}
