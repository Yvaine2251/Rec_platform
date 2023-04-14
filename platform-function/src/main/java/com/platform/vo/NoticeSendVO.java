package com.platform.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeSendVO {

    //消息id
    String noticeId;

    //主题
    String theme;

    //内容
    String content;

    //收到的用户
    List<String> receiveNames;
}
