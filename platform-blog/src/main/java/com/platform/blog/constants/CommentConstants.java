package com.platform.blog.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ErrorRua
 * @date 2022/11/05
 * @description:
 */

@Getter
@AllArgsConstructor
public enum CommentConstants  {
    /**
     * 评论类型
     */
    COMMENT_TYPE_ARTICLE(1, "文章"),
    COMMENT_TYPE_TOPIC(2, "话题"),


    ROOT_COMMENT(-1, "根评论");



    private final Integer type;
    private final String typeName;

}
