package com.platform.exam.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Map;

/**
 * @author ErrorRua
 * @date 2022/10/24
 * @description: 试卷提交vo
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaperSubmitVO {
	// 试卷分数
	private Integer paperScore;

	// 排名
	private Map<String, Integer> rank;

	// 是否已批阅
	private Integer isReview;

	// 是否允许查看试卷
	private Integer isAllowShowPaper;

	// 是否允许学生考试后查看成绩
	private Integer isShowScore;

	// 是否允许学生考试后查看排名
	private Integer isShowRank;
}
