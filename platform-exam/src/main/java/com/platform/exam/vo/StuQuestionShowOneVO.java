package com.platform.exam.vo;

import lombok.Data;

/**
 * @author ErrorRua
 * @date 2022/10/22
 * @description:
 */
@Data
public class StuQuestionShowOneVO {
	private String questionId;

	// 题目描述
	private String questionDescription;

	// 选项  （json格式）
	private String questionOption;

	// 题目难度   0:容易  1：中等  2：难
	private Integer questionDifficulty;

	// 题目类型 0：单选  1：多选  2：判断  3：填空
	private Integer questionType;

	// 答案数量 (多选题会用到)
	private Integer questionAnswerNum;

}
