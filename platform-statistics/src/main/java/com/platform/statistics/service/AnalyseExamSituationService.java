package com.platform.statistics.service;

import com.platform.entity.ResponseResult;
import com.platform.statistics.dto.ExportDataDTO;

/**
 * @author Yvaine
 * @date 2023/3/28
 * @description
 */
public interface AnalyseExamSituationService {

    /**
     * @return com.platform.entity.ResponseResult
     * @description: excel导出数据
     * @author Yvaine
     * @date 2023/3/28
     */
    ResponseResult exportExamData(ExportDataDTO exportDataDTO);

    //    ResponseResult analyseExam(AnalyseExamSituationDTO analyseExamSituationDTO);
    /**
     * @return com.platform.entity.ResponseResult
     * @description: 考试、作业情况统计
     * @author Yvaine
     * @date 2023/3/28
     */
    ResponseResult analyseExam(String paperId);
}
