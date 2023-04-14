package com.platform.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.platform.entity.ResponseResult;
import com.platform.exam.constants.LevelType;
import com.platform.exam.dto.HomeworkSubmitDTO;
import com.platform.exam.dto.ShowPaperDTO;
import com.platform.exam.entity.*;
import com.platform.exam.listener.LevelApplicationEvent;
import com.platform.exam.mapper.*;
import com.platform.exam.service.StuPaperService;
import com.platform.exam.utils.GetMulticaster;
import com.platform.exam.vo.*;
import com.platform.exception.PlatformException;
import com.platform.util.SecurityUtils;
import com.platform.utils.BeanCopyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.platform.exam.constants.PaperType.*;
import static com.platform.exam.constants.QuestionType.*;
import static com.platform.exam.enums.ExamCodeEnum.*;

/**
 * @author ErrorRua
 * @date 2022/10/21
 * @description:
 */
@Service
@Slf4j
public class StuPaperServiceImpl implements StuPaperService {

	@Autowired
	private PaperMapper paperMapper;

	@Autowired
	private QuestionPaperMapper questionPaperMapper;

	@Autowired
	private QuestionMapper questionMapper;

	@Autowired
	private PaperStudentMapper paperStudentMapper;

	@Autowired
	private QuestionUserMapper questionUserMapper;

	@Autowired
	private PaperStudentAnswerMapper paperStudentAnswerMapper;

	@Resource
	private ApplicationContext applicationContext;


	// TODO 这是一坨屎山
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseResult submitHomework(HomeworkSubmitDTO homeworkSubmitDto) {
		String userId = SecurityUtils.getUserId();
		// 先在试卷-学生表中查找是否有该学生的记录
		PaperStudent paperStudent = checkPaperRecordExists(homeworkSubmitDto.getPaperId());

		// 判断是否还能重做
		if (paperStudent.getHasRemakeTime() == 0) {
			throw new PlatformException(PAPER_CANNOT_REMAKE.getCode(), PAPER_CANNOT_REMAKE.getMessage());
		}

		LambdaQueryWrapper<Paper> paperLqw = new LambdaQueryWrapper<>();
		paperLqw.eq(Paper::getPaperId, homeworkSubmitDto.getPaperId());
		Paper paper = paperMapper.selectOne(paperLqw);

		// 检查试卷状态
		checkPaperStatus(paper);

		// 判题
		AtomicInteger score = new AtomicInteger();
		// 试卷中不带主观题则直接判分
		AtomicReference<Integer> isReview = new AtomicReference<>(1);
		homeworkSubmitDto.getAnswerList().forEach(answer -> {
			// 先在试卷-题目表中查找是否有该题目的记录
			LambdaQueryWrapper<QuestionPaper> questionPaperLambdaQueryWrapper = new LambdaQueryWrapper<>();
			questionPaperLambdaQueryWrapper.eq(QuestionPaper::getPaperId, homeworkSubmitDto.getPaperId())
					.eq(QuestionPaper::getQuestionId, answer.getQuestionId());

			QuestionPaper questionPaper = questionPaperMapper.selectOne(questionPaperLambdaQueryWrapper);

			// 如果没有记录，说明题目不存在
			if (Objects.isNull(questionPaper)) {
				throw new PlatformException(QUE_NOT_EXIST.getCode(), QUE_NOT_EXIST.getMessage());
			}

			// 如果有记录，说明题目存在，进行判题
			// 先在题目表中查找该题目的正确答案
			LambdaQueryWrapper<Question> questionLambdaQueryWrapper = new LambdaQueryWrapper<>();
			questionLambdaQueryWrapper.eq(Question::getQuestionId, answer.getQuestionId());

			Question question = questionMapper.selectOne(questionLambdaQueryWrapper);
			if (Objects.isNull(question)) {
				throw new PlatformException(QUE_NOT_EXIST.getCode(), QUE_NOT_EXIST.getMessage());
			}

			// 客观题
			// 如果正确答案和学生答案相同
			int isRight = 0;
			if(!Objects.equals(question.getQuestionType(), SUBJECTIVE.getType()) ){

				if (Objects.equals(question.getQuestionType(), MULTIPLE_CHOICE.getType()) && paper.getIsMultiHalfScore() == 1) {
					// 如果是多选题，需要将正确答案和学生答案都转换为数组
					String[] rightAnswers = question.getRightAnswer().split("#");
					String[] studentAnswers = answer.getStudentAnswer().split("#");


					for (String studentAnswer : studentAnswers) {
						// 如果学生答案中有正确答案中没有的选项，则判错
						if (!Arrays.asList(rightAnswers).contains(studentAnswer)) {
							isRight = 0;
							break;
						} else {
							isRight = 1;
						}
					}
					// 未选全
					if (rightAnswers.length != studentAnswers.length) {
						isRight = 2;
					}

					if (isRight == 1) {
						score.addAndGet(questionPaper.getQuestionScore());
						answer.setScore(questionPaper.getQuestionScore());
						answer.setIsRight(1);
					} else if (isRight == 2) {
						score.addAndGet(questionPaper.getQuestionScore() / 2);
						answer.setScore(questionPaper.getQuestionScore() / 2);
						answer.setIsRight(0);
					} else {
						answer.setScore(0);
						answer.setIsRight(0);
					}

				} else {
					if (Objects.equals(question.getQuestionType(), FILL_BLANK.getType())) {
						// 如果是填空题，需要将正确答案都转换为数组
						isRight = Arrays.stream(question.getRightAnswer().split("#"))
								.anyMatch(rightAnswer -> {
									if (paper.getIsDistinguishCase() == 0) {
										return Objects.equals(rightAnswer.toLowerCase(), answer.getStudentAnswer().toLowerCase());
									} else {
										return Objects.equals(rightAnswer, answer.getStudentAnswer());
									}
								}) ? 1 : 0;
					} else {
						if (paper.getIsDistinguishCase() == 0) {
							isRight = question.getRightAnswer().equalsIgnoreCase(answer.getStudentAnswer()) ? 1 : 0;
						} else {
							isRight = question.getRightAnswer().equals(answer.getStudentAnswer()) ? 1 : 0;
						}
					}

					if (isRight == 1) {
						// 得分加上该题目的分值
						score.addAndGet(questionPaper.getQuestionScore());
						answer.setScore(questionPaper.getQuestionScore());
						answer.setIsRight(1);
					} else {
						answer.setIsRight(0);
						answer.setScore(0);
					}
				}
			}
			// 主观题
			if(question.getQuestionType() == 4){
				answer.setIsRight(3);
				answer.setScore(0);
				// 如果有主观题，则需要教师批改
				isReview.set(0);
			}
			answer.setRightAnswer(question.getRightAnswer());


			// 记录做题记录
			LambdaQueryWrapper<QuestionUser> questionUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
			questionUserLambdaQueryWrapper.eq(QuestionUser::getQuestionId, answer.getQuestionId())
					.eq(QuestionUser::getUserId, userId);

			QuestionUser questionUser = questionUserMapper.selectOne(questionUserLambdaQueryWrapper);

			if (Objects.isNull(questionUser)) {
				questionUser = new QuestionUser();
				questionUser.setQuestionId(answer.getQuestionId());
				questionUser.setUserId(userId);
				if (isRight == 0) {
					questionUser.setLastWrongAnswer(answer.getStudentAnswer());
				}
				questionUser.setWrongTime(isRight == 0 ? 0 : 1);
				questionUser.setMakeTime(1);
				questionUser.setIsCollect(1);
				questionUserMapper.insert(questionUser);
			} else {
				if (isRight == 0) {
					questionUser.setLastWrongAnswer(answer.getStudentAnswer());
				}
				questionUser.setWrongTime(isRight != 0 ? questionUser.getWrongTime() : questionUser.getWrongTime() + 1);
				questionUser.setMakeTime(questionUser.getMakeTime() + 1);

				LambdaUpdateWrapper<QuestionUser> questionUserLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
				questionUserLambdaUpdateWrapper.eq(QuestionUser::getQuestionId, answer.getQuestionId())
						.eq(QuestionUser::getUserId, userId);
				questionUserMapper.update(questionUser, questionUserLambdaUpdateWrapper);
			}


			// 设置提交试卷的版本
			answer.setSubmitVersion(paperStudent.getSubmitVersion() + 1);
			// 设置试卷Id
			answer.setPaperId(homeworkSubmitDto.getPaperId());
			// 设置学生Id
			answer.setStudentId(userId);

			// 答案写入数据库
			paperStudentAnswerMapper.insert(answer);

		});

		// 记录试卷做题记录
		paperStudent.setHasRemakeTime(paperStudent.getHasRemakeTime() - 1);
		if (paper.getIsGetHighScore() == 0 || paper.getIsGetHighScore() == 1 && score.get() >= paperStudent.getPaperScore()) {
			paperStudent.setPaperScore(score.get());
			paperStudent.setSubmitVersion(paperStudent.getSubmitVersion() + 1);
		}
		paperStudent.setIsDone(1);
		paperStudent.setIsReview(isReview.get());

		LambdaUpdateWrapper<PaperStudent> paperStudentLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
		paperStudentLambdaUpdateWrapper.eq(PaperStudent::getPaperId, homeworkSubmitDto.getPaperId())
				.eq(PaperStudent::getStudentId, userId);
		paperStudentMapper.update(paperStudent, paperStudentLambdaUpdateWrapper);

		// 处理返回结果
		PaperSubmitVO paperSubmitVo = new PaperSubmitVO();
		if (1 == paper.getIsShowScore() && 1 == isReview.get()) {
			paperSubmitVo.setPaperScore(score.get());
		}
		if (1 == paper.getIsShowRank()) {
			Map<String, Integer> rank = getRank(paperSubmitVo.getPaperScore(), homeworkSubmitDto.getPaperId());
			paperSubmitVo.setRank(rank);
		}
		paperSubmitVo.setIsAllowShowPaper(paper.getIsAllowShowPaper());
		paperSubmitVo.setIsShowRank(paper.getIsShowRank());
		paperSubmitVo.setIsShowScore(paper.getIsShowScore());
		paperSubmitVo.setIsReview(isReview.get());

		// TODO 删除上一次提交的答案 学习通会留着
		//触发掌握度的更新
		ApplicationEventMulticaster multicaster = GetMulticaster.getMulticaster();
		LevelApplicationEvent event = new LevelApplicationEvent(multicaster, homeworkSubmitDto.getPaperId(), LevelType.HOMEWORK_EXAM);
		applicationContext.publishEvent(event);

		return ResponseResult.okResult(paperSubmitVo);
	}


	@Override
	public ResponseResult submitExam(String paperId) {
		String userId = SecurityUtils.getUserId();
		// 先在试卷-学生表中查找是否有该学生的记录
		PaperStudent paperStudent = checkPaperRecordExists(paperId);
		// 判断是否还能重做
		if (paperStudent.getHasRemakeTime() == 0 && paperStudent.getIsDone() == 1) {
			throw new PlatformException(PAPER_CANNOT_REMAKE.getCode(), PAPER_CANNOT_REMAKE.getMessage());
		}

		LambdaQueryWrapper<Paper> paperLqw = new LambdaQueryWrapper<>();
		paperLqw.eq(Paper::getPaperId, paperId);
		Paper paper = paperMapper.selectOne(paperLqw);

		// 检查试卷状态
		checkPaperStatusSubmit(paper);

		// 拿出保存的答案
		LambdaQueryWrapper<PaperStudentAnswer> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(PaperStudentAnswer::getPaperId, paperId)
				.eq(PaperStudentAnswer::getStudentId, userId)
				.eq(PaperStudentAnswer::getSubmitVersion, paperStudent.getSubmitVersion());

		List<PaperStudentAnswer> paperStudentAnswerList = paperStudentAnswerMapper.selectList(queryWrapper);


		// 判题
		AtomicInteger score = new AtomicInteger();
		// 试卷中不带主观题则直接判分
		AtomicReference<Integer> isReview = new AtomicReference<>(1);
		paperStudentAnswerList.forEach(answer -> {
			// 先在试卷-题目表中查找是否有该题目的记录
			LambdaQueryWrapper<QuestionPaper> questionPaperLambdaQueryWrapper = new LambdaQueryWrapper<>();
			questionPaperLambdaQueryWrapper.eq(QuestionPaper::getPaperId, paperId)
					.eq(QuestionPaper::getQuestionId, answer.getQuestionId());

			QuestionPaper questionPaper = questionPaperMapper.selectOne(questionPaperLambdaQueryWrapper);

			// 如果没有记录，说明题目不存在
			if (Objects.isNull(questionPaper)) {
				throw new PlatformException(QUE_NOT_EXIST.getCode(), QUE_NOT_EXIST.getMessage());
			}

			// 如果有记录，说明题目存在，进行判题
			// 先在题目表中查找该题目的正确答案
			LambdaQueryWrapper<Question> questionLambdaQueryWrapper = new LambdaQueryWrapper<>();
			questionLambdaQueryWrapper.eq(Question::getQuestionId, answer.getQuestionId());

			Question question = questionMapper.selectOne(questionLambdaQueryWrapper);
			if (Objects.isNull(question)) {
				throw new PlatformException(QUE_NOT_EXIST.getCode(), QUE_NOT_EXIST.getMessage());
			}

			// 客观题
			// 如果正确答案和学生答案相同
			int isRight = 0;
			if(!Objects.equals(question.getQuestionType(), SUBJECTIVE.getType()) ){

				if (Objects.equals(question.getQuestionType(), MULTIPLE_CHOICE.getType()) && paper.getIsMultiHalfScore() == 1) {
					// 如果是多选题，需要将正确答案和学生答案都转换为数组
					String[] rightAnswers = question.getRightAnswer().split("#");
					String[] studentAnswers = answer.getStudentAnswer().split("#");


					for (String studentAnswer : studentAnswers) {
						// 如果学生答案中有正确答案中没有的选项，则判错
						if (!Arrays.asList(rightAnswers).contains(studentAnswer)) {
							isRight = 0;
							break;
						} else {
							isRight = 1;
						}
					}
					// 未选全
					if (rightAnswers.length != studentAnswers.length) {
						isRight = 2;
					}

					if (isRight == 1) {
						score.addAndGet(questionPaper.getQuestionScore());
						answer.setScore(questionPaper.getQuestionScore());
						answer.setIsRight(1);
					} else if (isRight == 2) {
						score.addAndGet(questionPaper.getQuestionScore() / 2);
						answer.setScore(questionPaper.getQuestionScore() / 2);
						answer.setIsRight(0);
					} else {
						answer.setScore(0);
						answer.setIsRight(0);
					}

				} else {
					if (Objects.equals(question.getQuestionType(), FILL_BLANK.getType())) {
						// 如果是填空题，需要将正确答案都转换为数组
						isRight = Arrays.stream(question.getRightAnswer().split("#"))
								.anyMatch(rightAnswer -> {
									if (paper.getIsDistinguishCase() == 0) {
										return Objects.equals(rightAnswer.toLowerCase(), answer.getStudentAnswer().toLowerCase());
									} else {
										return Objects.equals(rightAnswer, answer.getStudentAnswer());
									}
								}) ? 1 : 0;
					} else {
						if (paper.getIsDistinguishCase() == 0) {
							isRight = question.getRightAnswer().equalsIgnoreCase(answer.getStudentAnswer()) ? 1 : 0;
						} else {
							isRight = question.getRightAnswer().equals(answer.getStudentAnswer()) ? 1 : 0;
						}
					}

					if (isRight == 1) {
						// 得分加上该题目的分值
						score.addAndGet(questionPaper.getQuestionScore());
						answer.setScore(questionPaper.getQuestionScore());
						answer.setIsRight(1);
					} else {
						answer.setIsRight(0);
						answer.setScore(0);
					}
				}
			}
			// 主观题
			if(question.getQuestionType() == 4){
				answer.setIsRight(3);
				answer.setScore(0);
				// 如果有主观题，则需要教师批改
				isReview.set(0);
			}
			answer.setRightAnswer(question.getRightAnswer());


			// 记录做题记录
			LambdaQueryWrapper<QuestionUser> questionUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
			questionUserLambdaQueryWrapper.eq(QuestionUser::getQuestionId, answer.getQuestionId())
					.eq(QuestionUser::getUserId, userId);

			QuestionUser questionUser = questionUserMapper.selectOne(questionUserLambdaQueryWrapper);

			if (Objects.isNull(questionUser)) {
				questionUser = new QuestionUser();
				questionUser.setQuestionId(answer.getQuestionId());
				questionUser.setUserId(userId);
				if (isRight == 0) {
					questionUser.setLastWrongAnswer(answer.getStudentAnswer());
				}
				questionUser.setWrongTime(isRight == 0 ? 0 : 1);
				questionUser.setMakeTime(1);
				questionUser.setIsCollect(1);
				questionUserMapper.insert(questionUser);
			} else {
				if (isRight == 0) {
					questionUser.setLastWrongAnswer(answer.getStudentAnswer());
				}
				questionUser.setWrongTime(isRight != 0 ? questionUser.getWrongTime() : questionUser.getWrongTime() + 1);
				questionUser.setMakeTime(questionUser.getMakeTime() + 1);

				LambdaUpdateWrapper<QuestionUser> questionUserLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
				questionUserLambdaUpdateWrapper.eq(QuestionUser::getQuestionId, answer.getQuestionId())
						.eq(QuestionUser::getUserId, userId);
				questionUserMapper.update(questionUser, questionUserLambdaUpdateWrapper);
			}

			// 答案写入数据库
			LambdaQueryWrapper<PaperStudentAnswer> paperStudentAnswerLambdaQueryWrapper = new LambdaQueryWrapper<>();
			paperStudentAnswerLambdaQueryWrapper.eq(PaperStudentAnswer::getPaperId, paperId)
					.eq(PaperStudentAnswer::getQuestionId, answer.getQuestionId())
					.eq(PaperStudentAnswer::getStudentId, userId)
					.eq(PaperStudentAnswer::getSubmitVersion, answer.getSubmitVersion());

			paperStudentAnswerMapper.update(answer, paperStudentAnswerLambdaQueryWrapper);

		});

		// 记录试卷做题记录
		paperStudent.setHasRemakeTime(paperStudent.getHasRemakeTime() - 1);
		if (paper.getIsGetHighScore() == 0 || paper.getIsGetHighScore() == 1 && score.get() >= paperStudent.getPaperScore()) {
			paperStudent.setPaperScore(score.get());
			paperStudent.setSubmitVersion(paperStudent.getSubmitVersion() + 1);
		}
		paperStudent.setIsDone(1);
		paperStudent.setIsReview(isReview.get());

		LambdaUpdateWrapper<PaperStudent> paperStudentLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
		paperStudentLambdaUpdateWrapper.eq(PaperStudent::getPaperId, paperId)
				.eq(PaperStudent::getStudentId, userId);
		paperStudentMapper.update(paperStudent, paperStudentLambdaUpdateWrapper);

		// 处理返回结果
		PaperSubmitVO paperSubmitVo = new PaperSubmitVO();
		if (1 == paper.getIsShowScore() && 1 == isReview.get()) {
			paperSubmitVo.setPaperScore(score.get());
		}
		if (1 == paper.getIsShowRank()) {
			Map<String, Integer> rank = getRank(paperSubmitVo.getPaperScore(), paperId);
			paperSubmitVo.setRank(rank);
		}
		paperSubmitVo.setIsAllowShowPaper(paper.getIsAllowShowPaper());
		paperSubmitVo.setIsShowRank(paper.getIsShowRank());
		paperSubmitVo.setIsShowScore(paper.getIsShowScore());
		paperSubmitVo.setIsReview(isReview.get());

		// TODO 删除上一次提交的答案 学习通会留着

		//触发掌握度更新
		ApplicationEventMulticaster multicaster = GetMulticaster.getMulticaster();
		LevelApplicationEvent event = new LevelApplicationEvent(multicaster, paperId, LevelType.HOMEWORK_EXAM);
		applicationContext.publishEvent(event);

		return ResponseResult.okResult(paperSubmitVo);

	}

	@Override
	public ResponseResult savePaper(HomeworkSubmitDTO homeworkSubmitDto) {
		String userId = SecurityUtils.getUserId();
		// 先在试卷-学生表中查找是否有该学生的记录
		PaperStudent paperStudent = checkPaperRecordExists(homeworkSubmitDto.getPaperId());

		// 判断是否还能重做
		if (paperStudent.getHasRemakeTime() == 0) {
			throw new PlatformException(PAPER_CANNOT_REMAKE.getCode(), PAPER_CANNOT_REMAKE.getMessage());
		}



		LambdaQueryWrapper<Paper> paperLqw = new LambdaQueryWrapper<>();
		paperLqw.eq(Paper::getPaperId, homeworkSubmitDto.getPaperId());
		Paper paper = paperMapper.selectOne(paperLqw);

		// 检查试卷状态
		checkPaperStatus(paper);

		// 考试不能保存
		if (Objects.equals(paper.getPaperType(), EXAM.getType())) {
			throw new PlatformException(PAPER_CANNOT_SAVE.getCode(), PAPER_CANNOT_SAVE.getMessage());
		}


		homeworkSubmitDto.getAnswerList().forEach(answer -> {
			answer.setSubmitVersion(paperStudent.getSubmitVersion() + 1);
			// 设置试卷Id
			answer.setPaperId(homeworkSubmitDto.getPaperId());
			// 设置学生Id
			answer.setStudentId(userId);
			// 答案写入数据库
			paperStudentAnswerMapper.insert(answer);
		});

		paperStudent.setSubmitVersion(paperStudent.getSubmitVersion() + 1);
		paperStudent.setIsDone(2);

		LambdaUpdateWrapper<PaperStudent> paperStudentLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
		paperStudentLambdaUpdateWrapper.eq(PaperStudent::getPaperId, homeworkSubmitDto.getPaperId())
				.eq(PaperStudent::getStudentId, userId);
		paperStudentMapper.update(paperStudent, paperStudentLambdaUpdateWrapper);
		return ResponseResult.okResult();
	}

	@Override
	public ResponseResult saveSingleQuestion(PaperStudentAnswer paperStudentAnswer) {
		String userId = SecurityUtils.getUserId();
		// 先在试卷-学生表中查找是否有该学生的记录
		PaperStudent paperStudent = checkPaperRecordExists(paperStudentAnswer.getPaperId());

		LambdaQueryWrapper<Paper> paperLqw = new LambdaQueryWrapper<>();
		paperLqw.eq(Paper::getPaperId, paperStudentAnswer.getPaperId());
		Paper paper = paperMapper.selectOne(paperLqw);

		// 检查试卷状态
		checkPaperStatusSubmit(paper);

		paperStudentAnswer.setStudentId(userId);
		// 保存答案
		LambdaQueryWrapper<PaperStudentAnswer> paperStudentAnswerLqw = new LambdaQueryWrapper<>();
		paperStudentAnswerLqw.eq(PaperStudentAnswer::getPaperId, paperStudentAnswer.getPaperId())
				.eq(PaperStudentAnswer::getQuestionId, paperStudentAnswer.getQuestionId())
				.eq(PaperStudentAnswer::getStudentId, userId)
				.eq(PaperStudentAnswer::getSubmitVersion, paperStudent.getSubmitVersion());
		
		if (paperStudentAnswerMapper.selectOne(paperStudentAnswerLqw) == null) {
			paperStudentAnswerMapper.insert(paperStudentAnswer);
		} else {
			paperStudentAnswerMapper.update(paperStudentAnswer, paperStudentAnswerLqw);
		}

		return ResponseResult.okResult();
	}

	@Override
	public ResponseResult showPaperDetail(String paperId) {
		// 处理保存未提交状态 和 作业提交未截至还能修改答案的情况

		// 先在试卷-学生表中查找是否有该学生的记录
		PaperStudent paperStudent = checkPaperRecordExists(paperId);

		// 判断是否还能重做
		if (paperStudent.getHasRemakeTime() == 0 && paperStudent.getIsDone() == 1) {
			throw new PlatformException(PAPER_CANNOT_REMAKE.getCode(), PAPER_CANNOT_REMAKE.getMessage());
		}
		//判断是否已批阅
		if (paperStudent.getIsReview() == 1 && paperStudent.getIsDone() == 0) {
			throw new PlatformException(PAPER_HAS_REVIEW.getCode(), PAPER_HAS_REVIEW.getMessage());
		}

		LambdaQueryWrapper<Paper> paperLqw = new LambdaQueryWrapper<>();
		paperLqw.eq(Paper::getPaperId, paperId);
		Paper paper = paperMapper.selectOne(paperLqw);

		// 检查试卷状态
		checkPaperStatus(paper);

		// 计算考试剩余时间
		Long remainTime = null;
		if (Objects.equals(paper.getPaperType(), EXAM.getType()) && paper.getLimitTime() != null) {
			if (paperStudent.getFirstEnterTime() != null) {
				remainTime = paper.getLimitTime() * 60 * 1000 - (System.currentTimeMillis() - paperStudent.getFirstEnterTime());
			} else {
				remainTime = (long) (paper.getLimitTime() * 60 * 1000);
				// 记录第一次进入考试的时间
				paperStudent.setFirstEnterTime(System.currentTimeMillis());
				LambdaQueryWrapper<PaperStudent> paperStudentLqw = new LambdaQueryWrapper<>();
				paperStudentLqw.eq(PaperStudent::getPaperId, paperId)
						.eq(PaperStudent::getStudentId, SecurityUtils.getUserId());
				paperStudentMapper.update(paperStudent, paperStudentLqw);
			}
		}


		LambdaQueryWrapper<QuestionPaper> queLqw = new LambdaQueryWrapper<>();
		queLqw.eq(QuestionPaper::getPaperId, paperId);
		List<QuestionPaper> questionList = questionPaperMapper.selectList(queLqw);

		PaperShowVO paperShowVo = BeanCopyUtils.copyBean(paper, PaperShowVO.class);

		//存有学生未完成试卷或作业提交未截至
		List<PaperStudentAnswer> paperStudentAnswerList = null;
		if (paperStudent.getIsDone() != 0) {
			LambdaQueryWrapper<PaperStudentAnswer> paperStudentAnswerLqw = new LambdaQueryWrapper<>();
			paperStudentAnswerLqw.eq(PaperStudentAnswer::getPaperId, paperId)
					.eq(PaperStudentAnswer::getStudentId, SecurityUtils.getUserId())
					.eq(PaperStudentAnswer::getSubmitVersion, paperStudent.getSubmitVersion());
			paperStudentAnswerList = paperStudentAnswerMapper.selectList(paperStudentAnswerLqw);
		}

		// 试卷类型为考试，设置答案版本
		if (Objects.equals(paper.getPaperType(), EXAM.getType())) {
			// 已经提交过试卷或第一次进入考试
			if (paperStudent.getIsDone() != 2) {
				paperShowVo.setSubmitVersion(paperStudent.getSubmitVersion() + 1);
				paperStudent.setSubmitVersion(paperStudent.getSubmitVersion() + 1);
			} else {
				// 有保存未提交的试卷
				paperShowVo.setSubmitVersion(paperStudent.getSubmitVersion());
			}
		}

		// 试卷满分
		Integer paperFullScore = 0;
		List<QuestionOfPaperVO> questionOfPaperVOS = new ArrayList<>();
		for (QuestionPaper questionPaper : questionList) {
			// 根据题目id查找题目
			LambdaQueryWrapper<Question> questionLqw = new LambdaQueryWrapper<>();
			questionLqw.eq(Question::getQuestionId, questionPaper.getQuestionId());
			Question question = questionMapper.selectOne(questionLqw);

			// 新建返回试卷对象并赋值
			QuestionOfPaperVO questionOfPaperVO = BeanCopyUtils.copyBean(question, QuestionOfPaperVO.class);

			questionOfPaperVO.setQuestionOrder(questionPaper.getQuestionOrder());
			questionOfPaperVO.setQuestionScore(questionPaper.getQuestionScore());

			// 计算试卷满分
			paperFullScore += questionPaper.getQuestionScore();

			//将学生答案存入Vo
			if (paperStudent.getIsDone() != 0) {
				paperStudentAnswerList.forEach(paperStudentAnswer -> {
					if (paperStudentAnswer.getQuestionId().equals(question.getQuestionId())) {
						questionOfPaperVO.setStudentAnswer(paperStudentAnswer.getStudentAnswer());
					}
				});
			}

			// 将封装好的题目放入题目集中
			questionOfPaperVOS.add(questionOfPaperVO);
		}

		// 将题目排序
		questionOfPaperVOS.sort(Comparator.comparing(QuestionOfPaperVO::getQuestionOrder));

		paperShowVo.setQuestionOfPaperVOS(questionOfPaperVOS);

		// 将剩余时间放入试卷对象中
		paperShowVo.setRemainTime(remainTime);
		// 首次考试时间
		paperShowVo.setFirstEnterTime(paperStudent.getFirstEnterTime());

		paperStudent.setIsDone(2);
		LambdaQueryWrapper<PaperStudent> paperStudentLqw = new LambdaQueryWrapper<>();
		paperStudentLqw.eq(PaperStudent::getPaperId, paperId)
				.eq(PaperStudent::getStudentId, SecurityUtils.getUserId());
		paperStudentMapper.update(paperStudent, paperStudentLqw);

		paperShowVo.setStudentName(SecurityUtils.getLoginUser().getUsername());
		paperShowVo.setFullScore(paperFullScore);
		paperShowVo.setQuestionNum(questionList.size());


		return ResponseResult.okResult(paperShowVo);
	}


	@Override
	public ResponseResult showAllExam(ShowPaperDTO showPaperDto) {
		return showPaperList(showPaperDto, EXAM.getType());
	}


	@Override
	public ResponseResult showAllHomework(ShowPaperDTO showPaperDto) {
		return showPaperList(showPaperDto, HOMEWORK.getType());
	}


	@Override
	public ResponseResult showDonePaperDetail(String paperId) {
		// 先在试卷-学生表中查找是否有该学生的记录
		PaperStudent paperStudent = checkPaperRecordExists(paperId);

		// 获取试卷
		LambdaQueryWrapper<Paper> paperLqw = new LambdaQueryWrapper<>();
		paperLqw.eq(Paper::getPaperId, paperId);
		Paper paper = paperMapper.selectOne(paperLqw);

		// 判断试卷是否存在
		if (Objects.isNull(paper)) {
			throw new PlatformException(PAPER_NOT_EXIST.getCode(), PAPER_NOT_EXIST.getMessage());
		}
		// 判断试卷是否发布
		if (paper.getIsRelease() == 0) {
			throw new PlatformException(PAPER_NOT_RELEASE.getCode(), PAPER_NOT_RELEASE.getMessage());
		}

		if (paper.getIsAllowShowPaper() == 0 && Objects.equals(paper.getPaperType(), EXAM.getType())) {
			throw new PlatformException(PAPER_CANNOT_REVIEW.getCode(), PAPER_CANNOT_REVIEW.getMessage());
		}

		// 获取学生做试卷的答案记录
		LambdaQueryWrapper<PaperStudentAnswer> paperStudentAnswerLqw = new LambdaQueryWrapper<>();
		paperStudentAnswerLqw.eq(PaperStudentAnswer::getPaperId, paperId)
				.eq(PaperStudentAnswer::getStudentId, paperStudent.getStudentId())
				.eq(PaperStudentAnswer::getSubmitVersion, paperStudent.getSubmitVersion());
		List<PaperStudentAnswer> paperStudentAnswers = paperStudentAnswerMapper.selectList(paperStudentAnswerLqw);

		PaperShowDoneVO paperShowDoneVo = BeanCopyUtils.copyBean(paper, PaperShowDoneVO.class);

		List<QuestionAnswerOfPaperVO> questionAnswerOfPaperVOS = paperStudentAnswers.stream().map(paperStudentAnswer -> {
			// 根据题目id查找题目
			LambdaQueryWrapper<Question> questionLqw = new LambdaQueryWrapper<>();
			questionLqw.eq(Question::getQuestionId, paperStudentAnswer.getQuestionId());
			Question question = questionMapper.selectOne(questionLqw);

			LambdaQueryWrapper<QuestionPaper> questionPaperLqw = new LambdaQueryWrapper<>();
			questionPaperLqw.eq(QuestionPaper::getPaperId, paperId)
					.eq(QuestionPaper::getQuestionId, paperStudentAnswer.getQuestionId());
			QuestionPaper questionPaper = questionPaperMapper.selectOne(questionPaperLqw);

			// 新建返回已完成试卷对象并赋值
			QuestionAnswerOfPaperVO questionAnswerOfPaperVo = BeanCopyUtils.copyBean(question, QuestionAnswerOfPaperVO.class);

			questionAnswerOfPaperVo.setQuestionOrder(questionPaper.getQuestionOrder());
			questionAnswerOfPaperVo.setQuestionScore(questionPaper.getQuestionScore());

			questionAnswerOfPaperVo.setStudentAnswer(paperStudentAnswer.getStudentAnswer());

			if (paperStudent.getIsReview() == 1) {
				questionAnswerOfPaperVo.setIsRight(paperStudentAnswer.getIsRight());
			}

			if (paper.getIsShowScore() == 1 && paperStudent.getIsReview() == 1) {
				questionAnswerOfPaperVo.setScore(paperStudentAnswer.getScore());
			}
			if (paper.getIsAllowShowPaper() == 0 || paperStudent.getIsReview() == 0) {
				questionAnswerOfPaperVo.setRightAnswer(null);
				questionAnswerOfPaperVo.setIsRight(null);
				questionAnswerOfPaperVo.setQuestionAnswerExplain(null);
			}
			return questionAnswerOfPaperVo;
		}).collect(Collectors.toList());

		paperShowDoneVo.setQuestionAnswerOfPaperVOS(questionAnswerOfPaperVOS);
		if (paper.getIsShowScore() == 1 && paperStudent.getIsReview() == 1) {
			paperShowDoneVo.setPaperScore(paperStudent.getPaperScore());
		}
// 		paperShowDoneVo.setRank(paperStudent.getRank());
		paperShowDoneVo.setIsReview(paperStudent.getIsReview());

		// 展示排名
		if (paper.getIsShowRank() == 1 && paperStudent.getIsReview() == 1) {
			Map<String, Integer> rank = getRank(paperShowDoneVo.getPaperScore(), paperId);
			paperShowDoneVo.setRank(rank);
		}

		return ResponseResult.okResult(paperShowDoneVo);
	}

	/**
	 * @description: 学生查看试卷列表
	 * @param showPaperDto:
	 * @param paperType: 试卷类型 2-考试 1-作业
	 * @return com.platform.entity.ResponseResult<java.util.List<com.platform.exam.vo.StuPaperListVO>> 返回试卷列表
	 * @throws
	 * @date 2022/10/22
	 */
	private ResponseResult showPaperList(ShowPaperDTO showPaperDto, Integer paperType) {
		String userId = SecurityUtils.getUserId();

		// 查找该学生的所有试卷
		LambdaQueryWrapper<PaperStudent> paperStudentLambdaQueryWrapper = new LambdaQueryWrapper<>();
		paperStudentLambdaQueryWrapper
				.eq(PaperStudent::getStudentId, userId)
				.eq(Objects.nonNull(showPaperDto.getCourseId()), PaperStudent::getCourseId, showPaperDto.getCourseId())
				.select(PaperStudent::getPaperId, PaperStudent::getHasRemakeTime, PaperStudent::getIsDone, PaperStudent::getIsReview);

		// 是否带完成状态
		if (Objects.nonNull(showPaperDto.getIsDone())) {
			paperStudentLambdaQueryWrapper.eq(PaperStudent::getIsDone, showPaperDto.getIsDone());
		}

		// 如果是考试， 需要判断是否展示试卷，成绩，排名
		if (paperType.equals(EXAM.getType())) {
			// 没有排名
			paperStudentLambdaQueryWrapper.select(PaperStudent::getPaperScore, PaperStudent::getPaperId, PaperStudent::getHasRemakeTime, PaperStudent::getIsDone, PaperStudent::getIsReview);
		}

		List<PaperStudent> paperStudentList = paperStudentMapper.selectList(paperStudentLambdaQueryWrapper);

		if (paperStudentList.isEmpty()) {
			return ResponseResult.okResult(new int[]{});
		}

		LambdaQueryWrapper<Paper> paperLambdaQueryWrapper = new LambdaQueryWrapper<>();
		paperLambdaQueryWrapper.in(Paper::getPaperId, paperStudentList.stream().map(PaperStudent::getPaperId).toArray())
				.eq(Paper::getPaperType, paperType)
				.eq(Paper::getIsRelease, 1)
				.select(Paper::getPaperId, Paper::getPaperName, Paper::getStartTime, Paper::getEndTime);

		// 如果是考试， 需要判断是否展示试卷，成绩，排名
		if (Objects.equals(paperType, EXAM.getType())) {
			paperLambdaQueryWrapper.select(Paper::getIsAllowShowPaper, Paper::getIsShowRank, Paper::getIsShowScore,
					Paper::getPaperId, Paper::getPaperName, Paper::getStartTime, Paper::getEndTime);
		}

		List<Paper> papers = paperMapper.selectList(paperLambdaQueryWrapper);

		List<StuPaperListVO> stuPaperListVos = BeanCopyUtils.copyBeanList(papers, StuPaperListVO.class);

		stuPaperListVos.forEach(stuPaperListVo -> {
			paperStudentList.forEach(paperStudent -> {
				if (stuPaperListVo.getPaperId().equals(paperStudent.getPaperId())) {
					stuPaperListVo.setHasRemakeTime(paperStudent.getHasRemakeTime());
					stuPaperListVo.setIsDone(paperStudent.getIsDone());
					stuPaperListVo.setIsReview(paperStudent.getIsReview());

					// 计算排名
					if (Objects.equals(paperType, EXAM.getType())) {
						Map<String, Integer> rank = getRank(stuPaperListVo.getPaperScore(), stuPaperListVo.getPaperId());
						stuPaperListVo.setRank(rank);
					}
				}
			});
		});

		return ResponseResult.okResult(stuPaperListVos);
	}


	/**
	 * @description: 检查试卷状态
	 * @param paper:
	 * @return com.platform.entity.ResponseResult
	 * @author ErrorRua
	 * @date 2022/10/25
	 */
	private void checkPaperStatus(Paper paper, boolean isSubmit) {
		// 判断试卷是否存在
		if (Objects.isNull(paper)) {
			throw new PlatformException(PAPER_NOT_EXIST.getCode(), PAPER_NOT_EXIST.getMessage());
		}
		// 判断试卷是否发布
		if (paper.getIsRelease() == 0) {
			throw new PlatformException(PAPER_NOT_RELEASE.getCode(), PAPER_NOT_RELEASE.getMessage());
		}
		// 判断试卷是否已经结束
		if (paper.getEndTime().isBefore(LocalDateTime.now()) && !isSubmit) {
			throw new PlatformException(PAPER_END.getCode(), PAPER_END.getMessage());
		}
		// 判断试卷是否已经开始
		if (paper.getStartTime().isAfter(LocalDateTime.now())) {
			throw new PlatformException(PAPER_NOT_START.getCode(), PAPER_NOT_START.getMessage());
		}
		// 判断是否还能进入
		if (!Objects.isNull(paper.getLimitEnterTime()) &&
				paper.getStartTime().plusMinutes(paper.getLimitEnterTime()).isBefore(LocalDateTime.now()) && !isSubmit) {
			throw new PlatformException(PAPER_CANNOT_ENTER.getCode(), PAPER_CANNOT_ENTER.getMessage());
		}

	}

	private void checkPaperStatus(Paper paper) {
		checkPaperStatus(paper, false);
	}

	private void checkPaperStatusSubmit(Paper paper) {
		checkPaperStatus(paper, true);
	}


	/**
	 * @description: 检查试卷记录是否存在
	 * @param paperId:
	 * @return com.platform.exam.entity.PaperStudent
	 * @author ErrorRua
	 * @date 2022/10/25
	 */
	private PaperStudent checkPaperRecordExists(String paperId) {
		String userId = SecurityUtils.getUserId();

		LambdaQueryWrapper<PaperStudent> paperStudentLambdaQueryWrapper = new LambdaQueryWrapper<>();
		paperStudentLambdaQueryWrapper.eq(PaperStudent::getPaperId, paperId)
				.eq(PaperStudent::getStudentId, userId);

		PaperStudent paperStudent = paperStudentMapper.selectOne(paperStudentLambdaQueryWrapper);

		// 如果没有记录，说明试卷未发放给该学生或试卷不存在
		if (Objects.isNull(paperStudent)) {
			throw new PlatformException(PAPER_NOT_EXIST.getCode(), PAPER_NOT_EXIST.getMessage());
		}
		return paperStudent;
	}

	/**
	 * @description: 计算排名
	 * @param score:
	 * @param paperId:
	 * @return java.util.Map<java.lang.String,java.lang.Integer>
	 * @author ErrorRua
	 * @date 2022/10/31
	 */
	private Map<String, Integer> getRank(Integer score, String paperId) {
		Map<String, Integer> rankMap = new HashMap<>(2);
		LambdaQueryWrapper<PaperStudent> paperStudentLambdaQueryWrapper = new LambdaQueryWrapper<>();
		paperStudentLambdaQueryWrapper.select(PaperStudent::getPaperScore)
				.eq(PaperStudent::getPaperId, paperId)
				.eq(PaperStudent::getIsDone, 1)
				.eq(PaperStudent::getIsReview, 1)
				.ne(PaperStudent::getStudentId, SecurityUtils.getUserId());

		Integer total = paperStudentMapper.selectCount(paperStudentLambdaQueryWrapper) + 1;
		rankMap.put("total", total);

		paperStudentLambdaQueryWrapper.gt(PaperStudent::getPaperScore, score);

		Integer rank = paperStudentMapper.selectCount(paperStudentLambdaQueryWrapper) + 1;
		rankMap.put("rank", rank);

		return rankMap;
	}
}
