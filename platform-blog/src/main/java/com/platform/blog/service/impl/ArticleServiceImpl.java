package com.platform.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.blog.dto.ArticleCreateDTO;
import com.platform.blog.dto.ArticleShowDTO;
import com.platform.blog.entity.Article;
import com.platform.blog.entity.Comment;
import com.platform.blog.entity.UserBlog;
import com.platform.blog.mapper.ArticleMapper;
import com.platform.blog.mapper.CommentMapper;
import com.platform.blog.mapper.UserBlogMapper;
import com.platform.blog.service.ArticleService;
import com.platform.blog.vo.*;
import com.platform.entity.ResponseResult;
import com.platform.exception.PlatformException;
import com.platform.util.SecurityUtils;
import com.platform.utils.BeanCopyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.platform.blog.constants.ArticleConstants.*;
import static com.platform.blog.constants.BlogConstants.*;
import static com.platform.blog.enums.ArticleEnum.*;
import static com.platform.blog.enums.BlogEnum.*;

/**
 * @author ErrorRua
 * @date 2022/11/05
 * @description:
 */
@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private UserBlogMapper userBlogMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public ResponseResult showArticleList(ArticleShowDTO articleShowDto) {
        // 分页查询
        Page<Article> articlePage = new Page<>(articleShowDto.getCurrentPage(), articleShowDto.getPageSize());

        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.orderByDesc(Article::getUpdateTime)
                .eq(Article::getStates, ARTICLE_PUBLISHED.getType())
                .eq(Article::getUserId, SecurityUtils.getUserId());

        articlePage = articleMapper.selectPage(articlePage, articleLambdaQueryWrapper);

        List<ArticleShowVO> articleShowVOS =
                BeanCopyUtils.copyBeanList(articlePage.getRecords(), ArticleShowVO.class);

        // 获取点赞数, 收藏数, 评论数
        articleShowVOS.forEach(articleShowVO -> {
            Map<String, Long> articleLikeAndCollectAndComment = getArticleLikeAndCollectAndComments(articleShowVO.getArticleId());

            articleShowVO.setLikes(articleLikeAndCollectAndComment.get("likes"));
            articleShowVO.setCollects(articleLikeAndCollectAndComment.get("collects"));
            articleShowVO.setComments(articleLikeAndCollectAndComment.get("comments"));
        });


        ArticleShowListVO articleShowListVO = new ArticleShowListVO();

        articleShowListVO.setArticleShowVOList(articleShowVOS);
        articleShowListVO.setTotal(articlePage.getTotal());

        return ResponseResult.okResult(articleShowListVO);
    }

    @Override
    public ResponseResult showDraftList() {
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.eq(Article::getStates, ARTICLE_SAVED.getType())
                .eq(Article::getUserId, SecurityUtils.getUserId());

        List<ArticleDraftVO> articleDraftVOS =
                BeanCopyUtils.copyBeanList(articleMapper.selectList(articleLambdaQueryWrapper), ArticleDraftVO.class);
        return ResponseResult.okResult(articleDraftVOS);
    }

    // TODO 要不要传作者信息呢?
    @Override
    public ResponseResult showArticleDetail(String articleId) {

        Article article = articleMapper.selectById(articleId);

        // 文章不存在
        if (Objects.isNull(article)) {
            throw new PlatformException(ARTICLE_NOT_EXIST.getCode(), ARTICLE_NOT_EXIST.getMessage());
        }

        // 文章未发布
        if (!Objects.equals(article.getStates(), ARTICLE_PUBLISHED.getType())) {
            throw new PlatformException(ARTICLE_NOT_EXIST.getCode(), ARTICLE_NOT_EXIST.getMessage());
        }

        ArticleShowDetailVO articleShowDetailVO = BeanCopyUtils.copyBean(article, ArticleShowDetailVO.class);

        // 获取点赞数, 收藏数, 评论数
        Map<String, Long> articleLikeAndCollectAndComment = getArticleLikeAndCollectAndComments(articleId);

        articleShowDetailVO.setLikes(articleLikeAndCollectAndComment.get("likes"));
        articleShowDetailVO.setCollects(articleLikeAndCollectAndComment.get("collects"));
        articleShowDetailVO.setComments(articleLikeAndCollectAndComment.get("comments"));

        // 获取用户对该文章的点赞, 收藏状态
        UserBlog userBlog = userBlogMapper.selectOne(new LambdaQueryWrapper<UserBlog>()
                .eq(UserBlog::getUserId, SecurityUtils.getUserId())
                .eq(UserBlog::getBlogId, articleId)
                .eq(UserBlog::getType, ARTICLE.getType()));

        if (Objects.nonNull(userBlog)) {
            articleShowDetailVO.setIsLike(userBlog.getIsLike());
            articleShowDetailVO.setIsCollect(userBlog.getIsCollect());
        } else {
            articleShowDetailVO.setIsLike(0);
            articleShowDetailVO.setIsCollect(0);
        }

        return ResponseResult.okResult(articleShowDetailVO);
    }

    @Override
    public ResponseResult showDraftDetail(String articleId) {

        Article article = articleMapper.selectById(articleId);

        // 文章不存在
        if (Objects.isNull(article)) {
            throw new PlatformException(ARTICLE_DRAFT_NOT_EXIST.getCode(), ARTICLE_DRAFT_NOT_EXIST.getMessage());
        }

        // 文章已发布
        if (Objects.equals(article.getStates(), ARTICLE_PUBLISHED.getType())) {
            throw new PlatformException(ARTICLE_HAS_PUBLISHED.getCode(), ARTICLE_HAS_PUBLISHED.getMessage());
        }

        // 不是作者
        if (!Objects.equals(article.getUserId(), SecurityUtils.getUserId())) {
            throw new PlatformException(NOT_OWNER.getCode(), NOT_OWNER.getMessage());
        }

        ArticleDraftShowDetailVO articleDraftShowDetailVO = BeanCopyUtils.copyBean(article, ArticleDraftShowDetailVO.class);

        return ResponseResult.okResult(articleDraftShowDetailVO);
    }

    @Override
    public ResponseResult publishArticle(ArticleCreateDTO articleCreateDto) {
        return createArticle(articleCreateDto, ARTICLE_PUBLISHED.getType());
    }

    @Override
    public ResponseResult saveArticle(ArticleCreateDTO articleCreateDto) {
        return createArticle(articleCreateDto, ARTICLE_SAVED.getType());
    }

    @Override
    public ResponseResult updateArticle(ArticleCreateDTO articleCreateDto, String articleId) {
        Article article = articleMapper.selectById(articleId);

        // 文章不存在
        if (Objects.isNull(article)) {
            throw new PlatformException(ARTICLE_NOT_EXIST.getCode(), ARTICLE_NOT_EXIST.getMessage());
        }

        // 文章不是作者本人
        if (!Objects.equals(article.getUserId(), SecurityUtils.getUserId())) {
            throw new PlatformException(NOT_OWNER.getCode(), NOT_OWNER.getMessage());
        }

        article.setTitle(articleCreateDto.getTitle());
        article.setContent(articleCreateDto.getContent());
        article.setSummary(articleCreateDto.getSummary());
        article.setIsComment(articleCreateDto.getIsComment());

        article.setStates(ARTICLE_PUBLISHED.getType());

        if (articleMapper.updateById(article) == 0) {
            throw new PlatformException(UPDATE_ARTICLE_FAIL.getCode(), UPDATE_ARTICLE_FAIL.getMessage());
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteArticle(String articleId) {
        Article article = articleMapper.selectById(articleId);

        // 文章不存在
        if (Objects.isNull(article)) {
            throw new PlatformException(ARTICLE_NOT_EXIST.getCode(), ARTICLE_NOT_EXIST.getMessage());
        }

        // 文章不是作者本人
        if (!Objects.equals(article.getUserId(), SecurityUtils.getUserId())) {
            throw new PlatformException(NOT_OWNER.getCode(), NOT_OWNER.getMessage());
        }

        if (articleMapper.deleteById(articleId) == 0) {
            throw new PlatformException(DELETE_ARTICLE_FAIL.getCode(), DELETE_ARTICLE_FAIL.getMessage());
        }
        return ResponseResult.okResult();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseResult likeArticle(String articleId) {
        return likeOrCollectArticle(articleId, true);
    }

    @Override
    public ResponseResult cancelLikeArticle(String articleId) {

        return cancelLikeOrCollectArticle(articleId, true);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseResult collectArticle(String articleId) {
        return likeOrCollectArticle(articleId, false);
    }

    @Override
    public ResponseResult cancelCollectArticle(String articleId) {
        return cancelLikeOrCollectArticle(articleId, false);
    }

    @Override
    public ResponseResult topArticle(String articleId) {
        return topOrCancelTopArticle(articleId, true);
    }

    @Override
    public ResponseResult cancelTopArticle(String articleId) {
        return topOrCancelTopArticle(articleId, false);
    }

    private Map<String, Long> getArticleLikeAndCollectAndComments(String articleId) {

        LambdaQueryWrapper<UserBlog> userBlogLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userBlogLambdaQueryWrapper.eq(UserBlog::getType, ARTICLE.getType())
                .eq(UserBlog::getBlogId, articleId);

        List<UserBlog> userBlogs = userBlogMapper.selectList(userBlogLambdaQueryWrapper);

        long likes = userBlogs.stream().filter(userBlog -> Objects.equals(userBlog.getIsLike(), IS_LIKE.getType())).count();
        long collects = userBlogs.stream().filter(userBlog -> Objects.equals(userBlog.getIsCollect(), IS_COLLECT.getType())).count();

        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        commentLambdaQueryWrapper.eq(Comment::getBlogId, articleId)
                .eq(Comment::getType, ARTICLE.getType());

        long comments = commentMapper.selectCount(commentLambdaQueryWrapper);

        return new HashMap<String, Long>() {{
            put("likes", likes);
            put("collects", collects);
            put("comments", comments);
        }};

    }

    private ResponseResult createArticle(ArticleCreateDTO articleCreateDto, Integer states) {

        if (Objects.equals(ARTICLE_SAVED.getType(), states)) {
            LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
            articleLambdaQueryWrapper.eq(Article::getUserId, SecurityUtils.getUserId())
                    .eq(Article::getStates, ARTICLE_SAVED.getType());

            if (articleMapper.selectCount(articleLambdaQueryWrapper) > 9) {
                throw new PlatformException(DRAFT_IS_FULL.getCode(), DRAFT_IS_FULL.getMessage());
            }
        }


        Article article = BeanCopyUtils.copyBean(articleCreateDto, Article.class);
        article.setUserId(SecurityUtils.getUserId());
        article.setStates(states);

        if (articleMapper.insert(article) == 0) {
            throw new PlatformException(ARTICLE_CREATE_FAIL.getCode(), ARTICLE_CREATE_FAIL.getMessage());
        }

        ArticleCreateVO articleCreateVO = new ArticleCreateVO();
        articleCreateVO.setArticleId(article.getArticleId());

        return ResponseResult.okResult(articleCreateVO);
    }


    /**
     * @description: 点赞或收藏文章
     * @param articleId:
     * @param isLike: true: 点赞, false: 收藏
     * @return com.platform.entity.ResponseResult
     * @author ErrorRua
     * @date 2022/11/6
     */
    private ResponseResult likeOrCollectArticle(String articleId, boolean isLike) {
        Article article = articleMapper.selectById(articleId);

        // 文章不存在
        if (Objects.isNull(article) || !article.getStates().equals(ARTICLE_PUBLISHED.getType())) {
            throw new PlatformException(ARTICLE_NOT_EXIST.getCode(), ARTICLE_NOT_EXIST.getMessage());
        }
        // 不能收藏自己的文章
        if (article.getUserId().equals(SecurityUtils.getUserId()) && !isLike) {
            throw new PlatformException(CANNOT_COLLECTION.getCode(), CANNOT_COLLECTION.getMessage());
        }

        UserBlog userBlog = userBlogMapper.selectOne(new LambdaQueryWrapper<UserBlog>()
                .eq(UserBlog::getUserId, SecurityUtils.getUserId())
                .eq(UserBlog::getBlogId, articleId)
                .eq(UserBlog::getType, ARTICLE.getType()));

        if (Objects.isNull(userBlog)) {
            userBlog = new UserBlog();
            userBlog.setUserId(SecurityUtils.getUserId());
            userBlog.setBlogId(articleId);
            userBlog.setType(ARTICLE.getType());
            userBlog.setIsLike(NOT_LIKE.getType());
            userBlog.setIsCollect(NOT_COLLECT.getType());
            userBlogMapper.insert(userBlog);
        } else {
            if (isLike && Objects.equals(userBlog.getIsLike(), IS_LIKE.getType())) {
                throw new PlatformException(ALREADY_LIKE.getCode(), ALREADY_LIKE.getMessage());
            }
            if (!isLike && Objects.equals(userBlog.getIsCollect(), IS_COLLECT.getType())) {
                throw new PlatformException(ALREADY_COLLECT.getCode(), ALREADY_COLLECT.getMessage());
            }
        }

        if (isLike) {
            userBlog.setIsLike(IS_LIKE.getType());
        } else {
            userBlog.setIsCollect(IS_COLLECT.getType());
        }

        LambdaQueryWrapper<UserBlog> userBlogLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userBlogLambdaQueryWrapper.eq(UserBlog::getBlogId, articleId)
                .eq(UserBlog::getType, ARTICLE.getType())
                .eq(UserBlog::getUserId, SecurityUtils.getUserId());

        userBlogMapper.update(userBlog, userBlogLambdaQueryWrapper);

        return ResponseResult.okResult();
    }

    /**
     * @description: 取消点赞或收藏文章
     * @param articleId:
     * @param isLike: true: 点赞, false: 收藏
     * @return com.platform.entity.ResponseResult
     * @author ErrorRua
     * @date 2022/11/6
     */
    private ResponseResult cancelLikeOrCollectArticle(String articleId, boolean isLike) {
        Article article = articleMapper.selectById(articleId);

        // 文章不存在
        if (Objects.isNull(article) || !article.getStates().equals(ARTICLE_PUBLISHED.getType())) {
            throw new PlatformException(ARTICLE_NOT_EXIST.getCode(), ARTICLE_NOT_EXIST.getMessage());
        }

        UserBlog userBlog = userBlogMapper.selectOne(new LambdaQueryWrapper<UserBlog>()
                .eq(UserBlog::getUserId, SecurityUtils.getUserId())
                .eq(UserBlog::getBlogId, articleId)
                .eq(UserBlog::getType, ARTICLE.getType()));

        if (Objects.isNull(userBlog)) {
            if (isLike) {
                throw new PlatformException(NOT_LIKE_YET.getCode(), NOT_LIKE_YET.getMessage());
            } else {
                throw new PlatformException(NOT_COLLECT_YET.getCode(), NOT_COLLECT_YET.getMessage());
            }
        }

        if (isLike) {
            userBlog.setIsLike(NOT_LIKE.getType());
        } else {
            userBlog.setIsCollect(NOT_COLLECT.getType());
        }

        LambdaQueryWrapper<UserBlog> userBlogLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userBlogLambdaQueryWrapper.eq(UserBlog::getBlogId, articleId)
                .eq(UserBlog::getType, ARTICLE.getType())
                .eq(UserBlog::getUserId, SecurityUtils.getUserId());

        userBlogMapper.update(userBlog, userBlogLambdaQueryWrapper);

        return ResponseResult.okResult();
    }

    // TODO 置顶操作是要由管理员来做还是作者自己来做
    private ResponseResult topOrCancelTopArticle(String articleId, boolean isTop) {
        Article article = articleMapper.selectById(articleId);

        // 文章不存在
        if (Objects.isNull(article)) {
            throw new PlatformException(ARTICLE_NOT_EXIST.getCode(), ARTICLE_NOT_EXIST.getMessage());
        }

        // 只有文章作者才能置顶或取消置顶
        if (!article.getUserId().equals(SecurityUtils.getUserId())) {
            throw new PlatformException(NOT_OWNER.getCode(), NOT_OWNER.getMessage());
        }
        if (Objects.equals(article.getIsTop(), IS_TOP.getType())) {
            throw new PlatformException(TOP_OPERATION_FAIL.getCode(), TOP_OPERATION_FAIL.getMessage());
        }

        article.setIsTop(isTop ? IS_TOP.getType() : NOT_TOP.getType());

        if (articleMapper.updateById(article) == 0) {
            throw new PlatformException(TOP_OPERATION_FAIL.getCode(), TOP_OPERATION_FAIL.getMessage());
        }
        return ResponseResult.okResult();
    }
}
