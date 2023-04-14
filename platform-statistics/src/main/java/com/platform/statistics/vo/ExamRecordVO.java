package com.platform.statistics.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamRecordVO {

    //已完成
    private int completedExam;
    //总数
    private int totalExam;
}
