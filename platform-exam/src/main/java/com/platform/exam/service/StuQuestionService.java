package com.platform.exam.service;

import com.platform.entity.ResponseResult;
import com.platform.exam.dto.QuestionIdDTO;
import com.platform.exam.dto.QuestionSubmitDTO;
import com.platform.exam.dto.ShowQuestionDTO;

/**
 * @author ErrorRua
 * @date 2022/10/21
 * @description: 学生题目接口
 */

public interface StuQuestionService {

    /**
     * @description: 提交单个题目
     * @param questionSubmitDto:
     * @return com.platform.entity.ResponseResult
     * @author ErrorRua
     * @date 2022/10/23
     */
    ResponseResult submitQuestion(QuestionSubmitDTO questionSubmitDto);

    /**
     * @description: 展示学生题目库（缩略）
     * @param courseId:课程id
     * @return com.platform.entity.ResponseResult<com.platform.exam.vo.StuQuestionShowVO>
     * @author ErrorRua
     * @date 2022/10/22
     */
    ResponseResult showAllQuestion(String courseId);

    /**
     * @description: 展示一道题目
     * @param questionId:题目id
     * @return com.platform.entity.ResponseResult<com.platform.exam.vo.StuQuestionDetailVo>
     * @author ErrorRua
     * @date 2022/10/22
     */
    ResponseResult showOneQuestion(String questionId);

    /**
     * @description: 展示错题库
     * @param showQuestionDto:
     * @return com.platform.entity.ResponseResult
     * @author ErrorRua
     * @date 2022/10/23
     */
    ResponseResult showWrongQuestion(ShowQuestionDTO showQuestionDto);

    /**
     * @description: 展示收藏题目
     * @param showQuestionDto:
     * @return com.platform.entity.ResponseResult
     * @author ErrorRua
     * @date 2022/10/23
     */
    ResponseResult showCollectQuestion(ShowQuestionDTO showQuestionDto);

    /**
     * @description: 收藏题目
     * @param questionIdDto:
     * @return com.platform.entity.ResponseResult
     * @author ErrorRua
     * @date 2022/10/25
     */
    ResponseResult collectQuestion(String questionId);

    /**
     * @description: 取消收藏题目
     * @param questionId:
     * @return com.platform.entity.ResponseResult
     * @author ErrorRua
     * @date 2022/10/25
     */
    ResponseResult cancelCollect(String questionId);

    /**
     * @description: 删除错题记录
     * @param questionId:
     * @return com.platform.entity.ResponseResult
     * @author ErrorRua
     * @date 2022/11/2
     */
    ResponseResult deleteWrongQuestion(String questionId);

    /**
     * @description: 删除所有错题记录
     * @param courseId:
     * @return com.platform.entity.ResponseResult
     * @author ErrorRua
     * @date 2022/11/2
     */
    ResponseResult deleteAllWrongQuestion(String courseId);

    /**
     * @description: 推荐题目
     * @param courseId:
     * @return com.platform.entity.ResponseResult
     * @author ErrorRua
     * @date 2022/10/31
     */
    ResponseResult recommendQuestion(String courseId);
}
