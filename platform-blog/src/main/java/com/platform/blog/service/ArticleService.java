package com.platform.blog.service;

import com.platform.blog.dto.ArticleCreateDTO;
import com.platform.blog.dto.ArticleShowDTO;
import com.platform.entity.ResponseResult;

/**
 * @author ErrorRua
 * @date 2022/11/05
 * @description:
 */
public interface ArticleService {

    // TODO 发布草稿文章


    /**
     * @description: 获取文章列表
     * @param articleShowDto:
     * @return com.platform.entity.ResponseResult
     * @author ErrorRua
     * @date 2022/11/6
     */
    ResponseResult showArticleList(ArticleShowDTO articleShowDto);

    /**
     * @description: 获取草稿列表
     * @return com.platform.entity.ResponseResult
     * @author ErrorRua
     * @date 2022/11/7
     */
    ResponseResult showDraftList();

    /**
     * @description: 获取文章详情
     * @param articleId:
     * @return com.platform.entity.ResponseResult
     * @author ErrorRua
     * @date 2022/11/6
     */
    ResponseResult showArticleDetail(String articleId);

    /**
     * @description: 获取草稿详情
     * @param articleId:
     * @return com.platform.entity.ResponseResult
     * @author ErrorRua
     * @date 2022/11/10
     */
    ResponseResult showDraftDetail(String articleId);


    /**
     * @description: 发布文章
     * @param articleCreateDto:
     * @return com.platform.entity.ResponseResult
     * @author ErrorRua
     * @date 2022/11/6
     */
    ResponseResult publishArticle(ArticleCreateDTO articleCreateDto);

    /**
     * @description: 保存文章
     * @param articleCreateDto:
     * @return com.platform.entity.ResponseResult
     * @author ErrorRua
     * @date 2022/11/6
     */
    ResponseResult saveArticle(ArticleCreateDTO articleCreateDto);

    /**
     * @description: 修改文章
     * @param articleCreateDto:
     * @return com.platform.entity.ResponseResult
     * @author ErrorRua
     * @date 2022/11/6
     */
    ResponseResult updateArticle(ArticleCreateDTO articleCreateDto, String articleId);

    /**
     * @description: 删除文章
     * @param articleId:
     * @return com.platform.entity.ResponseResult
     * @author ErrorRua
     * @date 2022/11/6
     */
    ResponseResult deleteArticle(String articleId);

    /**
     * @description: 点赞文章
     * @param articleId:
     * @return com.platform.entity.ResponseResult
     * @author ErrorRua
     * @date 2022/11/6
     */
    ResponseResult likeArticle(String articleId);

    /**
     * @description: 取消点赞文章
     * @param articleId:
     * @return com.platform.entity.ResponseResult
     * @author ErrorRua
     * @date 2022/11/6
     */
    ResponseResult cancelLikeArticle(String articleId);

    /**
     * @description: 收藏文章
     * @param articleId:
     * @return com.platform.entity.ResponseResult
     * @author ErrorRua
     * @date 2022/11/6
     */
    ResponseResult collectArticle(String articleId);

    /**
     * @description: 取消收藏文章
     * @param articleId:
     * @return com.platform.entity.ResponseResult
     * @author ErrorRua
     * @date 2022/11/6
     */
    ResponseResult cancelCollectArticle(String articleId);

    /**
     * @description: 置顶文章
     * @param articleId:
     * @return com.platform.entity.ResponseResult
     * @author ErrorRua
     * @date 2022/11/6
     */
    ResponseResult topArticle(String articleId);

    /**
     * @description: 取消置顶文章
     * @param articleId:
     * @return com.platform.entity.ResponseResult
     * @author ErrorRua
     * @date 2022/11/6
     */
    ResponseResult cancelTopArticle(String articleId);
}
