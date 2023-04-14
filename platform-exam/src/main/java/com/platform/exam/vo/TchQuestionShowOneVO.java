package com.platform.exam.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yjj
 * @date 2022/9/5-11:23
 */
@Data
public class TchQuestionShowOneVO {
    // 题目id
    private String questionId;

    // 题目描述
    private String questionDescription;

    // 选项  （json格式）
    private String questionOption;

    // 题目难度   0:容易  1：中等  2：难
    private Integer questionDifficulty;

    // 题目类型 0：单选  1：多选  2：判断  3：填空
    private Integer questionType;

    // 题目分数
    private Integer questionScore;

    // 答案数量 (多选题会用到)
    private Integer questionAnswerNum;

    // 问题正确答案
    private String rightAnswer;

    // 题目答案解析
    private String questionAnswerExplain;

    // 创建时间
    private LocalDateTime createTime;

    // 题目在试卷中顺序
    private Integer questionOrder;

    // 关联知识点
    private List<String> points;



    // 所属课程id
//     private String courseId;
}
