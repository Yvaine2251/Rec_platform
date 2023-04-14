package com.platform.blog.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ErrorRua
 * @date 2022/11/05
 * @description: 关系
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("us_relation")
public class Relation {
    private String userId;
    private String followedUserId;
}
