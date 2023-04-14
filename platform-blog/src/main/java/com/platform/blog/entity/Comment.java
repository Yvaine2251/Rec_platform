package com.platform.blog.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ErrorRua
 * @date 2022/11/05
 * @description: 评论
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("us_comment")
public class Comment {
    private String commentId;
    // 0:文章评论 1:话题评论
    private Integer type;
    private String content;

    // 评论的文章或话题的id
    private String blogId;

    private String userId;

    // 父评论id -1:无父评论
    private String rootId;


}
