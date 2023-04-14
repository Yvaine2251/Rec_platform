package com.platform.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 试卷表
@TableName("ex_question")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Question extends BaseEntity {

    // 主键id
    @TableId(type= IdType.ASSIGN_ID)
    private String questionId;

    // 题目描述
    private String questionDescription;

    // 选项  （json格式）
    private String questionOption;

    // 题目难度   0:容易  1：中等  2：难
    private Integer questionDifficulty;

    // 题目类型 0：单选  1：多选  2：判断  3：填空
    private Integer questionType;

    // 答案数量 (多选题会用到)
    private Integer questionAnswerNum;

    // 问题正确答案
    private String rightAnswer;

    // 问题描述
    private String questionAnswerExplain;

    // 所属课程id
    private String courseId;
}
