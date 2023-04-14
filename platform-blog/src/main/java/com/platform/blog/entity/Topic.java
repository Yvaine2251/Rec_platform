package com.platform.blog.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ErrorRua
 * @date 2022/11/05
 * @description: 话题
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("us_topic")
public class Topic {
    private String topicId;
    private String title;
    private String content;
    private String userId;
    private String courseId;
}
