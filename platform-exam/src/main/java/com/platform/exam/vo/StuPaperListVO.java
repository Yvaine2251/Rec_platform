package com.platform.exam.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author ErrorRua
 * @date 2022/10/24
 * @description: 考试/作业 列表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StuPaperListVO extends PaperSubmitVO {
	// id
	private String paperId;

	// 试卷名称
	private String paperName;

	// 是否已完成，默认为0未完成
	private Integer isDone;

	// 剩余重做次数
	private Integer hasRemakeTime;

	// 开始时间
	private LocalDateTime startTime;

	// 结束时间
	private LocalDateTime endTime;

}
