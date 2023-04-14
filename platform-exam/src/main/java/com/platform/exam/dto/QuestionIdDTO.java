package com.platform.exam.dto;

import lombok.Data;

/**
 * @author ErrorRua
 * @date 2022/10/25
 * @description: 接受单个题目id
 */
// TODO 能用自定义注解类解决post传单个字符串
@Data
public class QuestionIdDTO {
	private String questionId;
}
