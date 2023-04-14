package com.platform.exam.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("ex_question_paper")
// 试卷题目中间表 n-n
public class QuestionPaper extends BaseEntity {

    // 试卷id
    private String paperId;

    // 题目id
    private String questionId;

    // 题目序号
    private Integer questionOrder;

    // 题目分数
    private Integer questionScore;
}
