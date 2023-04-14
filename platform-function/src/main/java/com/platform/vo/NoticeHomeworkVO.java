package com.platform.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeHomeworkVO {

    //标题
    private String headline;

    //发送人(消息类型)
    private int sender;

    //创建时间
    private String createTime;

    //课程名称
    private String className;

    //作业名称
    private String homeworkName;

    //开始时间
    private String startTime;

    //结束时间
    private String endTime;
}
