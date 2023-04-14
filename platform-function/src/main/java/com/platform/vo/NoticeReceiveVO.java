package com.platform.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeReceiveVO {

    //消息id
    String noticeId;

    //发送者
    String sendName;

    //主题
    String theme;

    //内容
    String content;
}
