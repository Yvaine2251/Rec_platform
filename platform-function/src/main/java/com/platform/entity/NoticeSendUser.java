package com.platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("fu_notice_send_user")
public class NoticeSendUser extends BaseEntity{

    @TableId(type = IdType.ASSIGN_ID)
    String id;

    //消息id
    String noticeId;

    //收件人id
    String userId;
}
