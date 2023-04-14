package com.platform.statistics.vo;

import com.platform.course.entity.ClassUser;
import lombok.Data;

import java.sql.Time;
import java.util.HashMap;

/**
 * @author Yvaine
 * @date 2020/3/30
 * @description 学生的视频播放时间，资源学习次数，做题记录
 */
@Data
public class LearningSituationVO {

    private String userName;

//    学生的视频播放时间
    private HashMap<ClassUser,Time> videoTotalTimeMap;

//    学生的资源学习次数
    private HashMap<ClassUser,StudyCountVO> studyCountVOMap;

//    学生的做题记录
    private HashMap<ClassUser,QuestionRecordVO> questionRecordVOMap;

}
