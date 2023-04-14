package com.platform.statistics.service;

import com.platform.entity.ResponseResult;
import org.springframework.lang.Nullable;

public interface StatisticService {
//    ResponseResult getVideoTotalTime(String courseId);

    ResponseResult getVideoTotalTime(String courseId,@Nullable String userId);

//    ResponseResult getStudyCount(String courseId);
    ResponseResult getStudyCount(String courseId,@Nullable String userId);

//    ResponseResult getQuestionRecord(String courseId);
    ResponseResult getQuestionRecord(String courseId,@Nullable String userId);

    ResponseResult getExamRecord(String courseId);
}
