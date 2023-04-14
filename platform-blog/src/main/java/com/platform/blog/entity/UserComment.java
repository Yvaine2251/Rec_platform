package com.platform.blog.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ErrorRua
 * @date 2022/11/05
 * @description:
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("us_user_comment")
public class UserComment {
    private String userId;
    private String commentId;
    private Integer isLike;
}
