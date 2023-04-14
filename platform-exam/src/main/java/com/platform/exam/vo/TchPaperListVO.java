package com.platform.exam.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author yjj
 * @date 2022/9/13-17:01
 */
@Data
public class TchPaperListVO {
    // id
    private String paperId;

    // 试卷名称
    private String paperName;

    // paper类型：0：作业  1：试卷
    Integer paperType;

    // 是否已发布，默认为0未发布
    private Integer isRelease;

    // 课程id
//     private String courseId;

    // 开始时间
    private LocalDateTime startTime;

    // 结束时间
    private LocalDateTime endTime;
}
