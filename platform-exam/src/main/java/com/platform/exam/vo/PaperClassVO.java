package com.platform.exam.vo;

import lombok.Data;

import java.util.List;

/**
 * @author yjj
 * @date 2022/9/13-17:33
 */
@Data
public class PaperClassVO {

    String className;

    // 学生列表
    List<PaperStudentVO> studentList;
}
