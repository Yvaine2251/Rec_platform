package com.platform.exam.vo;

import lombok.Data;

/**
 * @agetIsShowScoreuthor ErrorRua
 * @date 2022/10/23
 * @description: 错题vo
 */
@Data
public class WrongQuestionVo {
	private String questionId;
	private String questionDescription;
	private Integer questionDifficulty;
	private Integer questionType;
	private String rightAnswer;
	private String lastWrongAnswer;
}
