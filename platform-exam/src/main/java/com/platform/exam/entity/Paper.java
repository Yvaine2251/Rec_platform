package com.platform.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("ex_paper")
// 题目集（试卷库/作业库）详情
public class Paper extends BaseEntity {
    // 主键id
    @TableId(type= IdType.ASSIGN_ID)
    private String paperId;

    // 试卷名称
    private String paperName;

    // paper类型：0：作业  1：试卷
    Integer paperType;

    // 是否已发布，默认为0未发布
    private Integer isRelease;

    // 课程id
    private String courseId;

    // 是否允许补交 0:不可以 1:可以
    private Integer isAllowMakeUp;

    // 及格分数
    private Integer passScore;

    // 开始时间
    private LocalDateTime startTime;

    // 结束时间
    private LocalDateTime endTime;

    // 限制提交时间
    private Integer limitSubmitTime;

    // 限制进入考试时间  // 按分钟计时
    private Integer limitEnterTime;

    // 限制考试时间
    private Integer limitTime;

    // 答案是否区分大小写
    private Integer isDistinguishCase;

    // 是否多选题未选全给一半分
    private Integer isMultiHalfScore;

    // 是否取历史最高成绩
    private Integer isGetHighScore;

    // 重做次数
    private Integer remakeTime;

    // 是否允许查看试卷
    private Integer isAllowShowPaper;

    // 是否允许学生考试后查看成绩
    private Integer isShowScore;

    // 是否允许学生考试后查看排名
    private Integer isShowRank;
}
