package com.platform.exam.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.entity.BaseEntity;
import lombok.Data;
/**
 * @author ErrorRua
 * @date 2022/10/24
 * @description: 试卷学生答案
 */
@Data
@TableName("ex_paper_student_answer")
public class PaperStudentAnswer extends BaseEntity {
	private String paperId;
	private String studentId;
	//提交试卷答案版本
	private Integer submitVersion;
	private String questionId;
	private String rightAnswer;
	private String studentAnswer;

	// 得到的分数
	private Integer score;
	// 是否正确 1正确 0错误 2未判题
	private Integer isRight;

}
