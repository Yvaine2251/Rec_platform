package com.platform.exam.dto;

import lombok.Data;

/**
 * @author ErrorRua
 * @date 2022/10/22
 * @description: 题目提交dto
 */
@Data
public class QuestionSubmitDTO {
	private String questionId;
	private String questionType;
	private String questionAnswer;
	private Integer questionExistType;

	private String courseId;
}
