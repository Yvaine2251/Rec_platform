package com.platform.blog.dto;

import lombok.Data;

/**
 * @author ErrorRua
 * @date 2022/11/06
 * @description:
 */
@Data
public class ArticleCreateDTO {
    private String title;

    private String content;

    private String summary;

    private Integer isComment;
}
