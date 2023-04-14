package com.platform.exam.vo;


import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author yjj
 * @date 2022/8/13-12:30
 */
@Data
public class QuestionShowVO {
    // 题目id
    private String questionId;

    // 题目描述
    private String questionDescription;

    // 题目选项
    private String questionOption;

    // 题目答案
    private String rightAnswer;

    // 题目难度   0:容易  1：中等  2：难
    private Integer questionDifficulty;

    // 题目类型 0：单选  1：多选  2：判断  3：填空
    private Integer questionType;

    // 创建时间
    private LocalDateTime createTime;
}
