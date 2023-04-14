package com.platform.statistics.service;

import com.platform.entity.ResponseResult;
import com.platform.statistics.dto.LearningDataDTO;

/**
 * @author Yvaine
 * @date 2023/3/30
 * @description
 */
public interface LearningSituationService {

    ResponseResult showLearningSituation(LearningDataDTO learningDataDTO);

    ResponseResult exportLearningSituation(LearningDataDTO learningDataDTO);
}
