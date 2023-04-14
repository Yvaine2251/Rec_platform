package com.platform.statistics.vo;

import cn.hutool.core.lang.hash.Hash;
import io.swagger.models.auth.In;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Yvaine
 * @date 2023/3/28
 * @description 考试、作业情况分析VO
 */
@Data
public class AnalyseExamSituationVO {

    //所有学生成绩
    private HashMap<String,Integer> scores;

    //不同成绩层级对应的人数，如“优秀”——3
    private HashMap<String, Integer> scoreLevelNumbers;

    private ArrayList<String> userIds;

}
