package com.platform.exam.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)// 下划线转为驼峰命名
public class ReleasePaperDTO {

    private String paperId;

//     private String classId;

    private List<String> studentIds;

    // 是否允许补交 0:不可以 1:可以
    private Integer isAllowMakeUp;

    // 及格分数
    private Integer passScore;

    // 考试开始时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startTime;

    // 考试结束时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;

    // 限制时间
    private Integer limitTime;

    // 限制进入时间
    private Integer limitEnterTime;

    // 限制提交时间
    private Integer limitSubmitTime;

    // 答案是否区分大小写
    private Integer isDistinguishCase;

    // 是否取历史最高成绩
    private Integer isGetHighScore;

    // 重做次数
    private Integer remakeTime;

    // 允许学生考后查看成绩
    private Integer isShowScore;

    // 是否允许考试后查看试卷
    private Integer isAllowShowPaper;

    // 是否允许学生考试后查看排名
    private Integer isShowRank;

    // 是否多选题未选全给一半分
    private Integer isMultiHalfScore;
}
