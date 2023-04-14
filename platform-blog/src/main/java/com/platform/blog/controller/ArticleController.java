package com.platform.blog.controller;

import com.platform.blog.dto.ArticleCreateDTO;
import com.platform.blog.dto.ArticleShowDTO;
import com.platform.blog.service.ArticleService;
import com.platform.entity.ResponseResult;

import com.platform.resolver.PostRequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author ErrorRua
 * @date 2022/11/05
 * @description:
 */

@RestController
@RequestMapping("/article")
public class ArticleController {


    @Autowired
    private ArticleService articleService;

    // 查询文章列表
    @GetMapping("/list")
    public ResponseResult showArticleList(ArticleShowDTO articleShowDto) {
        return articleService.showArticleList(articleShowDto);
    }

    // 查询草稿列表
    @GetMapping("/draft")
    public ResponseResult showDraftList() {
        return articleService.showDraftList();
    }

    // 查询文章详情
    @GetMapping("/detail/{articleId}")
    public ResponseResult showArticleDetail(@PathVariable String articleId) {
        return articleService.showArticleDetail(articleId);
    }

    // 查询草稿详情
    @GetMapping("/detail/draft/{articleId}")
    public ResponseResult showDraftDetail(@PathVariable String articleId) {
        return articleService.showDraftDetail(articleId);
    }

    // 发布文章
    @PostMapping("/publish")
    public ResponseResult publishArticle(@RequestBody ArticleCreateDTO articleCreateDto) {
        return articleService.publishArticle(articleCreateDto);
    }

    // 保存文章
    @PostMapping("/save")
    public ResponseResult saveArticle(@RequestBody ArticleCreateDTO articleCreateDto) {
        return articleService.saveArticle(articleCreateDto);
    }

    // 修改文章
    @PutMapping("/update/{articleId}")
    public ResponseResult updateArticle(@RequestBody ArticleCreateDTO articleCreateDto, @PathVariable String articleId) {
        return articleService.updateArticle(articleCreateDto, articleId);
    }

    // 删除文章
    @DeleteMapping("/delete/{articleId}")
    public ResponseResult deleteArticle(@PathVariable String articleId) {
        return articleService.deleteArticle(articleId);
    }

    // 点赞文章
    @PostMapping("/like")
    public ResponseResult likeArticle(@PostRequestParam String articleId) {
        return articleService.likeArticle(articleId);
    }

    // 取消点赞文章
    @DeleteMapping("/cancel-like/{articleId}")
    public ResponseResult cancelLikeArticle(@PathVariable String articleId) {
        return articleService.cancelLikeArticle(articleId);
    }

    // 收藏文章
    @PostMapping("/collect")
    public ResponseResult collectArticle(@PostRequestParam String articleId) {
        return articleService.collectArticle(articleId);
    }

    // 取消收藏文章
    @DeleteMapping("/cancel-collect/{articleId}")
    public ResponseResult cancelCollectArticle(@PathVariable String articleId) {
        return articleService.cancelCollectArticle(articleId);
    }

    // 置顶文章
    @PostMapping("/top")
    public ResponseResult topArticle(@PostRequestParam String articleId) {
        return articleService.topArticle(articleId);
    }

    // 取消置顶文章
    @DeleteMapping("/cancel-top/{articleId}")
    public ResponseResult cancelTopArticle(@PathVariable String articleId) {
        return articleService.cancelTopArticle(articleId);
    }

}
