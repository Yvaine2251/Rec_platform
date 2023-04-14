package com.platform.exam.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yjj
 * @date 2022/8/16-9:52
 * 给学生展示
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaperShowVO {
    // 试卷/作业id
    String paperId;

    // 试卷/作业名字
    String paperName;

    // 题目集
    List<QuestionOfPaperVO> questionOfPaperVOS;

    // 初次进入时间戳
    private Long firstEnterTime;

    // 剩余时间
    private Long remainTime;

    // 限制提交时间
    private Integer limitSubmitTime;

    // 提交试卷答案版本
    private Integer submitVersion;

    // 开始时间
    private LocalDateTime startTime;

    // 结束时间
    private LocalDateTime endTime;

    // 学生姓名
    private String studentName;

    // 试卷满分
    private Integer fullScore;

    // 题目数量
    private Integer questionNum;
}
