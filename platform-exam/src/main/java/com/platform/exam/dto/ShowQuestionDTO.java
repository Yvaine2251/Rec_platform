package com.platform.exam.dto;

import lombok.Data;

/**
 * @author ErrorRua
 * @date 2022/10/24
 * @description: 展示收藏、错误题目dto
 */
@Data
public class ShowQuestionDTO {
	private String courseId;
	private String questionType;
}
