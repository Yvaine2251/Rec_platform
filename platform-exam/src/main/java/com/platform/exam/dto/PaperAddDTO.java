package com.platform.exam.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)// 下划线转为驼峰命名
public class PaperAddDTO {
    // 试卷名称
    String paperName;

    // 课程id
    String courseId;

    // 试题分数集
    List<Integer> questionsScore;

    // 试题id集
    List<String> questionIds;
}
