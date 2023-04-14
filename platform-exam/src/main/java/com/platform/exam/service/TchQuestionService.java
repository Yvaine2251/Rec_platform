package com.platform.exam.service;

import com.platform.entity.ResponseResult;
import com.platform.exam.dto.QuestionAddDTO;
import com.platform.exam.dto.QuestionUpdateDTO;

public interface TchQuestionService {

    // 创建新题目
    ResponseResult createNewQuestion(QuestionAddDTO questionAddDto);

    // 展示题目
    ResponseResult showAllQuestion(String courseId);

    // 根据题目ID删除题目
    ResponseResult deleteQuestionById(String questionId);

    // 修改题目
    ResponseResult updateQuestion(QuestionUpdateDTO questionUpdateDto);

    // 显示单个题目详情
    ResponseResult showQuestion(String id);
}
