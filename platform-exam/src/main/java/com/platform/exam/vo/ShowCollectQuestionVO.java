package com.platform.exam.vo;

import lombok.Data;

/**
 * @author ErrorRua
 * @date 2022/10/24
 * @description: 收藏题目vo
 */
@Data
public class ShowCollectQuestionVO {
	// 题目id
	private String questionId;

	// 题目描述
	private String questionDescription;

	// 题目选项
	private String questionOption;

	// 题目答案
	private String rightAnswer;

	// 题目难度   0:容易  1：中等  2：难
	private Integer questionDifficulty;

	// 题目类型 0：单选  1：多选  2：判断  3：填空
	private Integer questionType;
}
