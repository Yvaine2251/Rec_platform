package com.platform.exam.service;


import com.platform.entity.ResponseResult;
import com.platform.exam.dto.*;

public interface TchPaperService {

    // 创建试卷/作业
    ResponseResult createPaper(PaperAddDTO paperAddDto);

    // 发布试卷
    ResponseResult releaseExam(ReleasePaperDTO releasePaperDto);

    // 发布作业
    ResponseResult releaseHomework(ReleaseHomeworkDTO releaseHomeworkDto);

    ResponseResult showAllPaper(String courseId);

    ResponseResult getTarget(String courseId, String paperId);

    ResponseResult updatePaper(PaperUpdateDTO paperUpdateDto);

    // 查看试卷详情（教师）
    ResponseResult showPaperPreview(String paperId);

    ResponseResult deletePaper(String paperId);

    ResponseResult revokePaper(String paperId);
}
