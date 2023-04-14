package com.platform.exam.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.platform.entity.ResponseResult;
import com.platform.exam.dto.QuestionAddDTO;
import com.platform.exam.dto.QuestionUpdateDTO;
import com.platform.exam.entity.Question;
import com.platform.exam.entity.QuestionPoint;
import com.platform.exam.mapper.QuestionMapper;
import com.platform.exam.mapper.QuestionPointMapper;
import com.platform.exam.service.TchQuestionService;
import com.platform.exam.vo.TchQuestionShowOneVO;
import com.platform.exam.vo.QuestionShowVO;
import com.platform.exception.PlatformException;
import com.platform.points.entity.Point;
import com.platform.points.mapper.PointMapper;
import com.platform.utils.BeanCopyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import static com.platform.exam.enums.ExamCodeEnum.*;

@Service
public class TchQuestionServiceImpl implements TchQuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private PointMapper pointMapper;

    @Autowired
    private QuestionPointMapper questionPointMapper;

    // 创建新题目
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult createNewQuestion(QuestionAddDTO questionAddDto) {
        Question question = BeanCopyUtils.copyBean(questionAddDto, Question.class);
        int result = questionMapper.insert(question);
        if (result != 1) {
            return new ResponseResult(500, "创建题目失败，请检查格式");
        }
        // 将问题和知识点绑定
        int resultOfBinding = bindingPointsAndQuestions(question.getQuestionId(), questionAddDto.getPointIds());
        // 绑定成功的数量和原数量进行对比
        if (resultOfBinding != questionAddDto.getPointIds().size()) {
            return new ResponseResult(501, "绑定知识点失败");
        }
        return ResponseResult.okResult(question.getQuestionId());
    }

    // 展示所有题目
    @Override
    public ResponseResult showAllQuestion(String courseId) {
        LambdaQueryWrapper<Question> questionQueryWrapper = new LambdaQueryWrapper<>();
        questionQueryWrapper.eq(Question::getCourseId, courseId)
                .select(Question::getQuestionId, Question::getQuestionDescription, Question::getQuestionType,
                        Question::getQuestionDifficulty, Question::getCreateTime,
                        Question::getQuestionOption, Question::getRightAnswer);
        List<Question> questionList = questionMapper.selectList(questionQueryWrapper);
        List<QuestionShowVO> questionShowVOS = BeanCopyUtils.copyBeanList(questionList, QuestionShowVO.class);
        return ResponseResult.okResult(questionShowVOS);
    }

    // 根据题目id删除题目
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteQuestionById(String questionId) {
        // 删除题目表中记录
        QueryWrapper<Question> questionQueryWrapper = new QueryWrapper<>();
        questionQueryWrapper.eq("question_id", questionId);
        int result = questionMapper.delete(questionQueryWrapper);
        if (result != 1) {
            throw new PlatformException(QUE_NOT_EXIST.getCode(), QUE_NOT_EXIST.getMessage());
        }
        // 删除题目知识点中间表记录
        LambdaQueryWrapper<QuestionPoint> queLqw = new LambdaQueryWrapper<>();
        queLqw.eq(QuestionPoint::getQuestionId, questionId);
        questionPointMapper.delete(queLqw);

        return ResponseResult.okResult();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateQuestion(QuestionUpdateDTO questionUpdateDto) {
        // 1、更新题目表
        Question question = questionMapper.selectById(questionUpdateDto.getQuestionId());
        if (question == null) {
            throw new PlatformException(QUE_NOT_EXIST.getCode(), QUE_NOT_EXIST.getMessage());
        }
        BeanUtils.copyProperties(questionUpdateDto, question);
        questionMapper.updateById(question);

        // 2、删除题目知识点
        String questionId = questionUpdateDto.getQuestionId();
        LambdaQueryWrapper<QuestionPoint> queLqw = new LambdaQueryWrapper<>();
        queLqw.eq(QuestionPoint::getQuestionId, questionId);
        questionPointMapper.delete(queLqw);

        // 3、绑定题目知识点
        List<String> pointIds = questionUpdateDto.getPointIds();
        if (!pointIds.isEmpty()) {
            bindingPointsAndQuestions(questionId, questionUpdateDto.getPointIds());
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult showQuestion(String questionId) {
        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Question::getQuestionId, questionId);
        Question question = questionMapper.selectOne(wrapper);
        TchQuestionShowOneVO showOneVo = BeanCopyUtils.copyBean(question, TchQuestionShowOneVO.class);
        // 封装知识点
        LambdaQueryWrapper<QuestionPoint> quePointLqw = new LambdaQueryWrapper<>();
        quePointLqw.eq(QuestionPoint::getQuestionId, questionId)
                .select(QuestionPoint::getPointId);
        List<QuestionPoint> questionPoints = questionPointMapper.selectList(quePointLqw);
        ArrayList<String> list = new ArrayList<>();
        for (QuestionPoint point : questionPoints) {
//            list.add(point.getPointId());
            // 封装知识点名称
            Point point1 = pointMapper.selectById(point.getPointId());
            list.add(point1.getPointName());
        }
        showOneVo.setPoints(list);
        return ResponseResult.okResult(showOneVo);
    }

    // 执行绑定知识点和题目
    public Integer bindingPointsAndQuestions(String questionId, List<String> pointIds) {
        int result = 0;
        for (String pointId : pointIds) {
            QuestionPoint questionPoint = new QuestionPoint();
            questionPoint.setPointId(pointId);
            questionPoint.setQuestionId(questionId);
            int res = questionPointMapper.insert(questionPoint);
            result += res;
        }
        return result;
    }
}
