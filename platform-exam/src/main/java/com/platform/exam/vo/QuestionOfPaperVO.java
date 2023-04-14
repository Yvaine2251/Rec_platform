package com.platform.exam.vo;

import lombok.Data;

/**
 * @author yjj
 * @date 2022/8/16-10:18
 */
@Data
public class QuestionOfPaperVO {
    // 题目id
    private String questionId;

    // 题目描述
    private String questionDescription;

    // 选项  （json格式）
    private String questionOption;

    // 题目类型
    private Integer questionType;

    // 题目分数
    private Integer questionScore;

    // 题目难度
    private Integer questionDifficulty;

    // 题目顺序
    private Integer questionOrder;

    // 学生答案
    private String studentAnswer;
}
