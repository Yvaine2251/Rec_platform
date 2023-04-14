package com.platform.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeHeadlineVO {

    //消息id
    private String noticeId;

    //标题
    private String headline;

    //是否已读
    private String readFlag;

    //发送人(消息类型)
    private int sender;

    //创建时间
    private String createTime;
}
