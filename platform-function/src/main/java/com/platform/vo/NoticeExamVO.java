package com.platform.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeExamVO {

    //标题
    private String headline;

    //发送人(消息类型)
    private int sender;

    //创建时间
    private String createTime;

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
