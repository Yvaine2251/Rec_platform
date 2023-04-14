package com.platform.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeContentDTO {

    //标题
    private String headline;

    //发送人(消息类型)
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
}
