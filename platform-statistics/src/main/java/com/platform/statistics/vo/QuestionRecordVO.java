package com.platform.statistics.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionRecordVO {

    //已完成的题数
    private int completedQuestions;

    //错误的题数
    private int wrongQuestions;

    //总的题数
    private int totalQuestions;
}
