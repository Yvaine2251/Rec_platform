package com.platform.statistics.service;

import com.platform.entity.ResponseResult;

/**
 * @author Yvaine
 * @date 2023/3/30
 * @description
 */
public interface AnalysePointSituationService {

    /**
     * @return com.platform.entity.ResponseResult
     * @description: 学生知识点掌握度分析
     * @author Yvaine
     * @date 2023/3/30
     */
    ResponseResult analysePoint(String userId);
}
