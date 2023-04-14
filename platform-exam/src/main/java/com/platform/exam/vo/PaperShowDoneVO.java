package com.platform.exam.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author ErrorRua
 * @date 2022/10/26
 * @description:
 */

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaperShowDoneVO extends PaperSubmitVO {
	// 试卷/作业id
	private String paperId;

	// 试卷/作业名字
	private String paperName;

	// 题目集(带答案）
	private List<QuestionAnswerOfPaperVO> questionAnswerOfPaperVOS;
}
