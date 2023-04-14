package com.platform.blog.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ErrorRua
 * @date 2022/11/05
 * @description: 用户博客关联
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("us_user_blog")
public class UserBlog {
    private String userId;
    private String blogId;

    // 博客类型 0 文章 1话题
    private Integer type;

    private Integer isLike;

    private Integer isCollect;
}
