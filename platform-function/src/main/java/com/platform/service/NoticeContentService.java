package com.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.dto.NoticeContentDTO;
import com.platform.entity.NoticeContent;
import com.platform.entity.ResponseResult;

public interface NoticeContentService extends IService<NoticeContent> {

    ResponseResult sendNotice(String classId, NoticeContentDTO noticeContentDTO);

    ResponseResult getNoticeContent(String userId);

    ResponseResult getExamNotice(String noticeId);

    ResponseResult getHomeworkNotice(String noticeId);

    ResponseResult deleteNotice(String noticeId);
}
