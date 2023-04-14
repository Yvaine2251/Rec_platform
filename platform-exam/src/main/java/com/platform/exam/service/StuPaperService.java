package com.platform.exam.service;

import com.platform.entity.ResponseResult;
import com.platform.exam.dto.HomeworkSubmitDTO;
import com.platform.exam.dto.ShowPaperDTO;
import com.platform.exam.entity.PaperStudentAnswer;

/**
 * @author ErrorRua
 * @date 2022/10/21
 * @description: 学生试卷接口
 */

public interface StuPaperService {

	/**
	 * @description: 学生提交作业
	 * @param homeworkSubmitDto:
	 * @return com.platform.entity.ResponseResult
	 * @author ErrorRua
	 * @date 2022/10/24
	 */
	ResponseResult submitHomework(HomeworkSubmitDTO homeworkSubmitDto);

	/**
	 * @description: 学生提交考试
	 * @param paperId:
	 * @return com.platform.entity.ResponseResult
	 * @author ErrorRua
	 * @date 2022/10/24
	 */
	ResponseResult submitExam(String paperId);

	/**
	 * @description: 学生保存试卷
	 * @param homeworkSubmitDto:
	 * @return com.platform.entity.ResponseResult
	 * @author ErrorRua
	 * @date 2022/10/29
	 */
	ResponseResult savePaper(HomeworkSubmitDTO homeworkSubmitDto);


	/**
	 * @description: 考试单题提交
	 * @param paperStudentAnswer:
	 * @return com.platform.entity.ResponseResult
	 * @author ErrorRua
	 * @date 2022/10/24
	 */
	ResponseResult saveSingleQuestion(PaperStudentAnswer paperStudentAnswer);

	/**
	 * @description: 展示试卷详情
	 * @param paperId:
	 * @return com.platform.entity.ResponseResult
	 * @author ErrorRua
	 * @date 2022/10/22
	 */
	ResponseResult showPaperDetail(String paperId);

	/**
	 * @description: 学生查看考试列表
	 * @param showPaperDto:
	 * @return com.platform.entity.ResponseResult<java.util.List<com.platform.exam.vo.StuPaperListVO>>
	 * @author ErrorRua
	 * @date 2022/10/22
	 */
	ResponseResult showAllExam(ShowPaperDTO showPaperDto);

	/**
	 * @description: 学生查看作业列表
	 * @param showPaperDto:
	 * @return com.platform.entity.ResponseResult<java.util.List<com.platform.exam.vo.StuPaperListVO>>
	 * @author ErrorRua
	 * @date 2022/10/22
	 */
	ResponseResult showAllHomework(ShowPaperDTO showPaperDto);

	/**
	 * @description: 展示已完成试卷详情
	 * @param paperId:
	 * @return com.platform.entity.ResponseResult
	 * @author ErrorRua
	 * @date 2022/10/26
	 */
	ResponseResult showDonePaperDetail(String paperId);

}
