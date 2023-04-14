package com.platform.exam.dto;

import com.platform.exam.entity.PaperStudentAnswer;
import lombok.Data;

import java.util.List;

/**
 * @author ErrorRua
 * @date 2022/10/24
 * @description: 试卷提交dto
 */
@Data
public class HomeworkSubmitDTO {
	private String paperId;
	private List<PaperStudentAnswer> answerList;
}
