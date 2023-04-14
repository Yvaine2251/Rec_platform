package com.platform.blog.vo;

import lombok.Data;

import java.util.List;

/**
 * @author ErrorRua
 * @date 2022/11/06
 * @description:
 */
@Data
public class ArticleShowListVO {
    private List<ArticleShowVO> articleShowVOList;

    private Long total;
}
