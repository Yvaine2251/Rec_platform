package com.platform.exam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
//@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)// 下划线转为驼峰命名
public class QuestionAddDTO {
    // 题目描述
    String questionDescription;

    // 题目选项内容
    String questionOption;

    // 题目难度 0:容易  1：中等  2：难
    Integer questionDifficulty;

    // 题目类型 0：单选  1：多选  2：判断  3：填空
    Integer questionType;

    // 答案数量
    Integer questionAnswerNum;

    // 题目正确答案
    String rightAnswer;

    // 正确答案解析
    String questionAnswerExplain;

    // 所属课程id
    String courseId;

    // 关联知识点
    List<String> pointIds;
}
