package com.platform.exam.dto;

import lombok.Data;

/**
 * @author yjj
 * @date 2022/8/13-15:08
 */
@Data
public class QuestionUpdateDTO extends QuestionAddDTO {
    // 题目id
    String questionId;
}
