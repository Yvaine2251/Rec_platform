package com.platform.exam.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("ex_paper_student")
public class PaperStudent extends BaseEntity {

    private String paperId;

    private String studentId;

    private String courseId;

    private Long firstEnterTime;

    private Integer isDone;

    private Integer hasRemakeTime;

    private Integer isReview;

    private Integer paperScore;

    //提交试卷答案版本
    private Integer submitVersion;
}
