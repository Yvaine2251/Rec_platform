package com.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.dto.NoticeSendDTO;
import com.platform.entity.NoticeReceive;
import com.platform.entity.ResponseResult;


public interface NoticeReceiveService extends IService<NoticeReceive> {
    ResponseResult sendNotice(NoticeSendDTO noticeSendDTO);

    ResponseResult getReceiveNotice();

    ResponseResult deleteReceive(String noticeId);

    ResponseResult readNotice(String noticeId);
}
