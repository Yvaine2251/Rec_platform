package com.platform.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.platform.entity.ResponseResult;
import com.platform.exam.constants.LevelType;
import com.platform.exam.dto.QuestionIdDTO;
import com.platform.exam.dto.QuestionSubmitDTO;
import com.platform.exam.dto.ShowQuestionDTO;
import com.platform.exam.entity.Question;
import com.platform.exam.entity.QuestionUser;
import com.platform.exam.listener.LevelApplicationEvent;
import com.platform.exam.mapper.QuestionMapper;
import com.platform.exam.mapper.QuestionUserMapper;
import com.platform.exam.service.StuQuestionService;
import com.platform.exam.utils.GetMulticaster;
import com.platform.exam.vo.*;
import com.platform.exception.PlatformException;
import com.platform.resources.vo.ResourceOneVo;
import com.platform.util.SecurityUtils;
import com.platform.utils.BeanCopyUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.platform.exam.constants.QuestionType.SUBJECTIVE;
import static com.platform.exam.enums.ExamCodeEnum.*;

/**
 * @author ErrorRua
 * @date 2022/10/22
 * @description:
 */
@Service
@Slf4j
public class StuQuestionServiceImpl implements StuQuestionService {

	@Autowired
	private QuestionMapper questionMapper;

	@Autowired
	private QuestionUserMapper questionUserMapper;

	@Resource
	private ApplicationContext applicationContext;

	@Override
	public ResponseResult submitQuestion(QuestionSubmitDTO questionSubmitDto) {
		LambdaQueryWrapper<Question> questionLambdaQueryWrapper = new LambdaQueryWrapper<>();
		questionLambdaQueryWrapper.eq(Question::getQuestionId, questionSubmitDto.getQuestionId());

		Question question = questionMapper.selectOne(questionLambdaQueryWrapper);

		if (Objects.isNull(question)) {
			throw new PlatformException(QUE_NOT_EXIST.getCode(), QUE_NOT_EXIST.getMessage());
		}

		// 创建返回vo
		QuestionSubmittedVO questionSubmittedVo = new QuestionSubmittedVO();

		String rightAnswer = question.getRightAnswer();
		String questionAnswerExplain = question.getQuestionAnswerExplain();

		questionSubmittedVo.setQuestionAnswer(rightAnswer);
		questionSubmittedVo.setQuestionAnswerExplain(questionAnswerExplain);

		// 判断答案是否正确
		boolean isRight = rightAnswer.equals(questionSubmitDto.getQuestionAnswer());
		questionSubmittedVo.setAnswerIsRight(isRight);

		// TODO 推荐相关资源
		ResourceOneVo resource = new ResourceOneVo();
		resource.setResourceLink("https:// platform-yjj.oss-cn-guangzhou.aliyuncs.com/1547211425930256386/04e5e50c-2cf9-4128-bPPT等分圆.pptx");
		resource.setResourceName("PPT等分圆.pptx");
		resource.setType(20);
		resource.setResourceId("1569683268209594369");
		questionSubmittedVo.setResource(resource);

		// TODO 推荐下一道题目
		String recommendQuestionId = recommendQuestionId(questionSubmitDto.getCourseId());
		questionSubmittedVo.setNextQuestionId(recommendQuestionId);

		// 将做题记录存入数据库
		String userId = SecurityUtils.getUserId();

		LambdaQueryWrapper<QuestionUser> questionUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
		questionUserLambdaQueryWrapper.eq(QuestionUser::getQuestionId, questionSubmitDto.getQuestionId())
				.eq(QuestionUser::getUserId, userId);

		QuestionUser questionUser = questionUserMapper.selectOne(questionUserLambdaQueryWrapper);
		if (Objects.isNull(questionUser)) {
			questionUser = new QuestionUser();
			questionUser.setQuestionId(questionSubmitDto.getQuestionId());
			questionUser.setUserId(userId);
// 			questionUser.setQuestionExistType(questionSubmitDto.getQuestionExistType());
			if (!isRight) {
				questionUser.setLastWrongAnswer(questionSubmitDto.getQuestionAnswer());
			}
			questionUser.setWrongTime(isRight ? 0 : 1);
			questionUser.setMakeTime(1);
			questionUser.setIsCollect(1);
			questionUserMapper.insert(questionUser);
		} else {
			if (!isRight) {
				questionUser.setLastWrongAnswer(questionSubmitDto.getQuestionAnswer());
			}
			questionUser.setWrongTime(isRight ? questionUser.getWrongTime() : questionUser.getWrongTime() + 1);
			questionUser.setMakeTime(questionUser.getMakeTime() + 1);

			LambdaUpdateWrapper<QuestionUser> questionUserLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
			questionUserLambdaUpdateWrapper.eq(QuestionUser::getQuestionId, questionSubmitDto.getQuestionId())
					.eq(QuestionUser::getUserId, userId);
			questionUserMapper.update(questionUser, questionUserLambdaUpdateWrapper);
		}

		//触发更新
		ApplicationEventMulticaster multicaster = GetMulticaster.getMulticaster();
		LevelApplicationEvent event = new LevelApplicationEvent(multicaster, questionSubmitDto.getQuestionId(), LevelType.SINGLE_QUESTION);
		applicationContext.publishEvent(event);

		return ResponseResult.okResult(questionSubmittedVo);
	}



	@Override
	public ResponseResult showAllQuestion(String courseId) {

		LambdaQueryWrapper<Question> questionQueryWrapper = new LambdaQueryWrapper<>();

		questionQueryWrapper.eq(Question::getCourseId, courseId)
				.select(Question::getQuestionId, Question::getQuestionDescription
						, Question::getQuestionType, Question::getQuestionDifficulty);

		List<Question> questions =
				questionMapper.selectList(questionQueryWrapper);

		List<StuQuestionShowVO> stuQuestionShowVOS = BeanCopyUtils.copyBeanList(questions, StuQuestionShowVO.class);

		return ResponseResult.okResult(stuQuestionShowVOS);
	}


	@Override
	public ResponseResult showOneQuestion(String questionId) {
		LambdaQueryWrapper<Question> questionQueryWrapper = new LambdaQueryWrapper<>();

		questionQueryWrapper.eq(Question::getQuestionId, questionId)
				.select(Question::getQuestionId, Question::getQuestionDescription
						, Question::getQuestionType, Question::getQuestionDifficulty
						, Question::getQuestionOption, Question::getQuestionAnswerNum);

		Question question = questionMapper.selectOne(questionQueryWrapper);

		if (Objects.isNull(question)) {
			throw new PlatformException(QUE_NOT_EXIST.getCode(), QUE_NOT_EXIST.getMessage());
		}

		return ResponseResult.okResult(BeanCopyUtils.copyBean(question, StuQuestionShowOneVO.class));
	}


	@Override
	public ResponseResult showWrongQuestion(ShowQuestionDTO showQuestionDto) {

		String userId = SecurityUtils.getUserId();
		// 查询所有错题
		LambdaQueryWrapper<QuestionUser> questionUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
		questionUserLambdaQueryWrapper.eq(QuestionUser::getUserId, userId)
				.gt(QuestionUser::getWrongTime, 0)
				.select(QuestionUser::getQuestionId, QuestionUser::getLastWrongAnswer);

		List<QuestionUser> questionUsers = questionUserMapper.selectList(questionUserLambdaQueryWrapper);

		// 查询课程中错题
		LambdaQueryWrapper<Question> questionQueryWrapper = new LambdaQueryWrapper<>();
		questionQueryWrapper.eq(Question::getCourseId, showQuestionDto.getCourseId())
				.in(Question::getQuestionId, questionUsers.stream().map(QuestionUser::getQuestionId).collect(Collectors.toList()))
				.select(Question::getQuestionId, Question::getQuestionDescription
						, Question::getQuestionType, Question::getQuestionDifficulty
						, Question::getQuestionOption, Question::getQuestionAnswerNum
						, Question::getRightAnswer);

		// 是否指定题目类型
		if (showQuestionDto.getQuestionType() != null) {
			questionQueryWrapper.eq(Question::getQuestionType, showQuestionDto.getQuestionType());
		}

		List<Question> questions = questionMapper.selectList(questionQueryWrapper);

		List<WrongQuestionVo> wrongQuestionVos = BeanCopyUtils.copyBeanList(questions, WrongQuestionVo.class);

		wrongQuestionVos.forEach(wrongQuestionVo -> {
			questionUsers.forEach(questionUser -> {
				if (wrongQuestionVo.getQuestionId().equals(questionUser.getQuestionId())) {
					wrongQuestionVo.setLastWrongAnswer(questionUser.getLastWrongAnswer());
				}
			});
		});

		return ResponseResult.okResult(wrongQuestionVos);
	}


	@Override
	public ResponseResult showCollectQuestion(ShowQuestionDTO showQuestionDto) {
		String userId = SecurityUtils.getUserId();

		// 查询所有收藏题目
		LambdaQueryWrapper<QuestionUser> questionUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
		questionUserLambdaQueryWrapper.eq(QuestionUser::getUserId, userId)
				.eq(QuestionUser::getIsCollect, 0)
				.select(QuestionUser::getQuestionId);

		List<QuestionUser> questionUsers = questionUserMapper.selectList(questionUserLambdaQueryWrapper);

		// 查询课程中收藏题目
		LambdaQueryWrapper<Question> questionQueryWrapper = new LambdaQueryWrapper<>();
		questionQueryWrapper.eq(Question::getCourseId, showQuestionDto.getCourseId())
				.in(Question::getQuestionId, questionUsers.stream().map(QuestionUser::getQuestionId).collect(Collectors.toList()))
				.select(Question::getQuestionId, Question::getQuestionDescription
						, Question::getQuestionType, Question::getQuestionDifficulty
						, Question::getQuestionOption, Question::getQuestionAnswerNum
						, Question::getRightAnswer);

		// 是否指定题目类型
		if (showQuestionDto.getQuestionType() != null) {
			questionQueryWrapper.eq(Question::getQuestionType, showQuestionDto.getQuestionType());
		}

		List<Question> questions = questionMapper.selectList(questionQueryWrapper);

		List<ShowCollectQuestionVO> collectQuestionVos = BeanCopyUtils.copyBeanList(questions, ShowCollectQuestionVO.class);


		return ResponseResult.okResult(collectQuestionVos);
	}



	@Override
	public ResponseResult collectQuestion(String questionId) {


		String userId = SecurityUtils.getUserId();
		// 查询题目是否存在
		LambdaQueryWrapper<Question> questionQueryWrapper = new LambdaQueryWrapper<>();
		questionQueryWrapper.eq(Question::getQuestionId, questionId);

		Question question = questionMapper.selectOne(questionQueryWrapper);
		if (Objects.isNull(question)) {
			throw new PlatformException(QUE_NOT_EXIST.getCode(), QUE_NOT_EXIST.getMessage());
		}

		// 查询是否有做题记录
		LambdaQueryWrapper<QuestionUser> questionUserQueryWrapper = new LambdaQueryWrapper<>();
		questionUserQueryWrapper.eq(QuestionUser::getUserId, userId)
				.eq(QuestionUser::getQuestionId, questionId);

		QuestionUser questionUser = questionUserMapper.selectOne(questionUserQueryWrapper);
		if (Objects.isNull(questionUser)) {
			// 没有做题记录
			QuestionUser questionUser1 = new QuestionUser();
			questionUser1.setQuestionId(questionId);
			questionUser1.setUserId(userId);
			questionUser1.setIsCollect(0);
			questionUser1.setWrongTime(0);
			questionUser1.setMakeTime(0);
			questionUser1.setLastWrongAnswer("");
			questionUserMapper.insert(questionUser1);
		} else {
			// 有做题记录， 判断是否已经收藏
			if (questionUser.getIsCollect() == 1) {
				throw new PlatformException(QUE_ALREADY_COLLECT.getCode(), QUE_ALREADY_COLLECT.getMessage());
			}
			questionUser.setIsCollect(0);
			questionUserMapper.update(questionUser, new LambdaQueryWrapper<QuestionUser>()
					.eq(QuestionUser::getUserId, userId)
					.eq(QuestionUser::getQuestionId, questionId));
		}

		return ResponseResult.okResult();
	}


	public ResponseResult cancelCollect(String questionId) {
		String userId = SecurityUtils.getUserId();
		// 查询题目是否存在
		LambdaQueryWrapper<Question> questionQueryWrapper = new LambdaQueryWrapper<>();
		questionQueryWrapper.eq(Question::getQuestionId, questionId);

		Question question = questionMapper.selectOne(questionQueryWrapper);
		if (Objects.isNull(question)) {
			throw new PlatformException(QUE_NOT_EXIST.getCode(), QUE_NOT_EXIST.getMessage());
		}

		// 查询是否有做题记录
		LambdaQueryWrapper<QuestionUser> questionUserQueryWrapper = new LambdaQueryWrapper<>();
		questionUserQueryWrapper.eq(QuestionUser::getUserId, userId)
				.eq(QuestionUser::getQuestionId, questionId);

		QuestionUser questionUser = questionUserMapper.selectOne(questionUserQueryWrapper);

		if (Objects.isNull(questionUser) || questionUser.getIsCollect() == 1) {
			throw new PlatformException(QUE_NOT_COLLECT.getCode(), QUE_NOT_COLLECT.getMessage());
		}

		// 取消收藏
		questionUser.setIsCollect(1);
		LambdaUpdateWrapper<QuestionUser> questionUserLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
		questionUserLambdaUpdateWrapper.eq(QuestionUser::getUserId, userId)
				.eq(QuestionUser::getQuestionId, questionId);
		questionUserMapper.update(questionUser, questionUserLambdaUpdateWrapper);

		return ResponseResult.okResult();

	}

	@Override
	public ResponseResult deleteWrongQuestion(String questionId) {
		LambdaQueryWrapper<QuestionUser> questionUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
		questionUserLambdaQueryWrapper.eq(QuestionUser::getUserId, SecurityUtils.getUserId())
				.eq(QuestionUser::getQuestionId, questionId);

		QuestionUser questionUser = questionUserMapper.selectOne(questionUserLambdaQueryWrapper);

		// 判断是否有做题记录
		if (Objects.isNull(questionUser)) {
			throw new PlatformException(QUE_NOT_EXIST.getCode(), QUE_NOT_EXIST.getMessage());
		}

		// 判断是否有错题记录
		if (questionUser.getWrongTime() == 0) {
			throw new PlatformException(QUE_NOT_WRONG.getCode(), QUE_NOT_WRONG.getMessage());
		}

		// 删除错题记录
		questionUser.setWrongTime(0);
		questionUser.setLastWrongAnswer("");

		questionUserMapper.update(questionUser, questionUserLambdaQueryWrapper);

		return ResponseResult.okResult();
	}

	@Override
	public ResponseResult deleteAllWrongQuestion(String courseId) {
		LambdaQueryWrapper<Question> questionLambdaQueryWrapper = new LambdaQueryWrapper<>();
		questionLambdaQueryWrapper.eq(Question::getCourseId, courseId);

		List<Question> questions = questionMapper.selectList(questionLambdaQueryWrapper);

		List<String> questionIds = questions.stream().map(Question::getQuestionId).collect(Collectors.toList());

		LambdaUpdateWrapper<QuestionUser> questionUserLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
		questionUserLambdaUpdateWrapper.eq(QuestionUser::getUserId, SecurityUtils.getUserId())
				.in(QuestionUser::getQuestionId, questionIds);

		QuestionUser questionUser = new QuestionUser();
		questionUser.setWrongTime(0);
		questionUser.setLastWrongAnswer("");

		questionUserMapper.update(questionUser, questionUserLambdaUpdateWrapper);

		return ResponseResult.okResult();
	}

	@Override
	public ResponseResult recommendQuestion(String courseId) {

		// 推荐题目
		String questionId = recommendQuestionId(courseId);

		return showOneQuestion(questionId);
	}

    /**
	 * 推荐题目
	 * @param courseId
	 * @return questionId
	 */
	private String recommendQuestionId(String courseId) {
		// 随机选择一题
		// TODO 以后应该由推荐模块来推荐
		LambdaQueryWrapper<Question> questionQueryWrapper = new LambdaQueryWrapper<>();
		questionQueryWrapper
				.eq(StringUtils.hasText(courseId), Question::getCourseId, courseId)
				//排除主观题
				.ne(Question::getQuestionType, SUBJECTIVE);

		Integer count = questionMapper.selectCount(questionQueryWrapper);
		if (count == 0) {
			throw new PlatformException(QUE_BANK_IS_EMPTY.getCode(), QUE_BANK_IS_EMPTY.getMessage());
		}
		// 随机数起始位置
		int randomCount =(int) (Math.random()*count);
		questionQueryWrapper.orderByDesc(Question::getCreateTime)
				.last("limit "+ randomCount +", 1");

		Question question = questionMapper.selectOne(questionQueryWrapper);

		return question.getQuestionId();
	}


}
