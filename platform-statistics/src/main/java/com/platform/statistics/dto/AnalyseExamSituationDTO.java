package com.platform.statistics.dto;

import lombok.Data;

/**
 * @author Yvaine
 * @date 2023/3/28
 * @description 考试、作业情况分析DTO
 */
@Data
@Deprecated
public class AnalyseExamSituationDTO {

    //试卷id
    private String paperId;

    //试卷类型
    private Integer paperType;
}
