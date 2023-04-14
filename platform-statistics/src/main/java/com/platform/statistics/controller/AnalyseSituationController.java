package com.platform.statistics.controller;

import com.platform.entity.ResponseResult;
import com.platform.statistics.dto.ExportDataDTO;
import com.platform.statistics.dto.LearningDataDTO;
import com.platform.statistics.service.AnalyseExamSituationService;
import com.platform.statistics.service.AnalysePointSituationService;
import com.platform.statistics.service.LearningSituationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Yvaine
 * @Date 2023/3/28
 * @description
 */
@RestController
@RequestMapping("/statistics")
public class AnalyseSituationController {


    @Autowired
    private AnalyseExamSituationService analyseExamSituationService;

    @Autowired
    private AnalysePointSituationService analysePointService;

    @Autowired
    private LearningSituationService learningSituationService;

    //    public ResponseResult showExamSituation(@RequestBody(required = false) AnalyseExamSituationDTO analyseExamSituationDTO) {
//    public ResponseResult showExamSituation(AnalyseExamSituationDTO analyseExamSituationDTO) {
//        return analyseExamSituationService.analyseExam(analyseExamSituationDTO);
//    }

    /**
     * 考试、作业情况统计
     * @param paperId
     * @return
     */
    @GetMapping("/analyzeExam")
    public ResponseResult showExamSituation(String paperId) {
        return analyseExamSituationService.analyseExam(paperId);
    }

    /**
     * 导出考试、作业情况
     * @param exportDataDTO
     * @return
     */
    @GetMapping("/exportExam")
    public ResponseResult exportData(ExportDataDTO exportDataDTO) {
        return analyseExamSituationService.exportExamData(exportDataDTO);
    }

    /**
     * 学生知识点掌握度统计分析
     * @param classId
     * @return
     */
    @GetMapping("/analyzePoint")
    public ResponseResult showPointSituation(String classId) {
        return analysePointService.analysePoint(classId);
    }

    /**
     * 导出学生学习情况
     * @param learningDataDTO
     * @return
     */
    @GetMapping("/exportLearn")
    public ResponseResult exportLearningSituation(LearningDataDTO learningDataDTO) {
        return learningSituationService.exportLearningSituation(learningDataDTO);
    }

    /**
     * 展示学生学习情况
     * @param learningDataDTO
     * @return
     */
    @GetMapping("/showLearningSituation")
    public ResponseResult showLearningSituation (LearningDataDTO learningDataDTO) {
        return learningSituationService.showLearningSituation(learningDataDTO);
    }
}
