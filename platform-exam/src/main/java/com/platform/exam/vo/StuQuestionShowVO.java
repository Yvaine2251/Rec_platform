package com.platform.exam.vo;

import lombok.Data;

/**
 * @author ErrorRua
 * @date 2022/10/22
 * @description: 学生题目库展示（缩略）
 */
@Data
public class StuQuestionShowVO {
	// 题目id
	private String questionId;

	// 题目描述
	private String questionDescription;

	// 题目难度   0:容易  1：中等  2：难
	private Integer questionDifficulty;

	// 题目类型 0：单选  1：多选  2：判断  3：填空
	private Integer questionType;
}
