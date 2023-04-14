package com.platform.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.course.dto.VideoRecordDTO;
import com.platform.course.entity.VideoRecord;
import com.platform.entity.ResponseResult;


/**
 * (StVideoResourceRecord)表服务接口
 *
 * @author makejava
 * @since 2023-03-29 00:28:56
 */
public interface VideoRecordService extends IService<VideoRecord> {

    ResponseResult setVideoRecord(VideoRecordDTO videoRecordDTO);

    ResponseResult getVideoRecord(String courseId, String resourceId);
}

