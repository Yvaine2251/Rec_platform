package com.platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.classgraph.json.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true) //加上父类进行比较是否相等
@TableName("fu_notice_receive")
public class NoticeReceive extends BaseEntity{

    @Id
    @TableId(type = IdType.ASSIGN_ID)
    String id;

    //通知id
    String noticeId;

    //发送人id
    String sendNoticeId;

    //接收人id
    String receiveNoticeId;

    //主题
    String theme;

    //内容
    String content;

    //是否已读
    int isRead;
}
