package com.platform.exam.dto;

import lombok.Data;

/**
 * @author ErrorRua
 * @date 2022/10/24
 * @description: 展示 考试/作业 列表 Dto
 */
@Data
public class ShowPaperDTO {
	private String courseId;
	private Integer isDone;
}
