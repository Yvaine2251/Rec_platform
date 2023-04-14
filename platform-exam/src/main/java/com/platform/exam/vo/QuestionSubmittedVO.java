package com.platform.exam.vo;

import com.platform.resources.vo.ResourceOneVo;
import lombok.Data;

/**
 * @author ErrorRua
 * @date 2022/10/23
 * @description: 题目提交vo
 */
@Data
public class QuestionSubmittedVO {
	// 是否正确
	private Boolean answerIsRight;
	// 下一道题目id
	private String nextQuestionId;
	private String questionAnswer;
	private String questionAnswerExplain;

	// 推荐资源
	private ResourceOneVo resource;
}
