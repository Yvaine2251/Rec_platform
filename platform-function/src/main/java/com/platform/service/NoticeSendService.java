package com.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.entity.NoticeSend;
import com.platform.entity.ResponseResult;
import org.springframework.stereotype.Service;

public interface NoticeSendService extends IService<NoticeSend> {
    ResponseResult getSendNotice();

    ResponseResult deleteSend(String noticeId);
}
