package com.platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("fu_notice_content")
public class NoticeContent extends BaseEntity {

    //消息id
    @TableId(type = IdType.ASSIGN_ID)
    private String noticeId;

    //用户id
    private String userId;

    //标题
    private String headline;

    //发送人
    private int sender;

    //课程名称
    private String className;

    //作业名称
    private String homeworkName;

    //考试名称
    private String examName;

    //开始时间
    private String startTime;

    //结束时间
    private String endTime;

    //答题时长
    private String answerTime;

    //提示
    private String tip;

    //是否已读
    private int readFlag;
}
