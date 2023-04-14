package com.platform.exam.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ErrorRua
 * @date 2022/10/25
 * @description:
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuestionAnswerOfPaperVO extends QuestionOfPaperVO {

	private String questionId;
	private String rightAnswer;
	private String studentAnswer;

	// 得到的分数
	private Integer score;
	// 是否正确 1正确 0错误 2未判题
	private Integer isRight;


	// 答案数量 (多选题会用到)
	private Integer questionAnswerNum;

	// 问题描述
	private String questionAnswerExplain;
}
