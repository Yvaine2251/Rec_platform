package com.platform.exam.listener;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.platform.exam.constants.LevelType;
import com.platform.exam.entity.QuestionPaper;
import com.platform.exam.entity.QuestionPoint;
import com.platform.exam.entity.QuestionUser;
import com.platform.exam.mapper.QuestionPaperMapper;
import com.platform.exam.mapper.QuestionPointMapper;
import com.platform.exam.mapper.QuestionUserMapper;
import com.platform.points.entity.UserPoint;
import com.platform.points.mapper.UserPointMapper;
import com.platform.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.Max;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class LevelEventListener {

    @Resource
    private QuestionPointMapper questionPointMapper;

    @Resource
    private QuestionPaperMapper questionPaperMapper;

    @Resource
    private QuestionUserMapper questionUserMapper;

    @Resource
    private UserPointMapper userPointMapper;

    //触发更新掌握度
    @Transactional
    @EventListener
    public void onApplicationEvent(LevelApplicationEvent event){
        //获取当前用户
        String userId = SecurityUtils.getUserId();
        //更新事件类型及id
        Integer type = event.getType();
        String id = event.getId();
        //存放这次需要更新的知识点
        HashSet<String> pointIds = new HashSet<>();
        if(type.equals(LevelType.SINGLE_QUESTION)){
            //查询对应id
            LambdaQueryWrapper<QuestionPoint> questionPointWrapper = new LambdaQueryWrapper<>();
            questionPointWrapper.eq(QuestionPoint::getQuestionId, id);
            List<QuestionPoint> questionPoints = questionPointMapper.selectList(questionPointWrapper);
            for(QuestionPoint questionPoint : questionPoints){
                pointIds.add(questionPoint.getPointId());
            }
        }else{
            //获取当前试卷或作业用到的所有pointId;
            //获取所有的questionId
            LambdaQueryWrapper<QuestionPaper> questionPaperWrapper = new LambdaQueryWrapper<>();
            questionPaperWrapper.eq(QuestionPaper::getPaperId, id);
            List<QuestionPaper> questionPapers = questionPaperMapper.selectList(questionPaperWrapper);
            for(QuestionPaper questionPaper : questionPapers){
                //遍历questionId获取pointId
                LambdaQueryWrapper<QuestionPoint> questionPointWrapper = new LambdaQueryWrapper<>();
                questionPointWrapper.eq(QuestionPoint::getQuestionId, questionPaper.getQuestionId());
                List<QuestionPoint> questionPoints = questionPointMapper.selectList(questionPointWrapper);
                for(QuestionPoint questionPoint : questionPoints){
                    pointIds.add(questionPoint.getPointId());
                }
            }
        }
        //处理每一个知识点
        for(String pointId : pointIds){
            //通过知识点获取到所有question
            LambdaQueryWrapper<QuestionPoint> questionPointWrapper = new LambdaQueryWrapper<>();
            questionPointWrapper
                    .eq(QuestionPoint::getPointId, pointId)
                    .select(QuestionPoint::getQuestionId);
            List<QuestionPoint> questionPoints = questionPointMapper.selectList(questionPointWrapper);
            List<String> questionIds = questionPoints.stream().map(QuestionPoint::getQuestionId).collect(Collectors.toList());
            //处理用户有关的question,获得做过的题，和是否做对
            int allQuestions = 0;
            int rightQuestions = 0;
            for(String questionId : questionIds){
                LambdaQueryWrapper<QuestionUser> queryWrapper = new LambdaQueryWrapper<QuestionUser>();
                queryWrapper
                        .eq(QuestionUser::getUserId, userId)
                        .eq(QuestionUser::getQuestionId, questionId);
                QuestionUser questionUser = questionUserMapper.selectOne(queryWrapper);
                if(questionUser != null){
                    if(questionUser.getLastSubmitIsCorrect().equals(LevelType.CORRECT)){
                        rightQuestions++;
                    }
                    allQuestions++;
                }
            }
            //获取初始化知识点掌握度
            LambdaQueryWrapper<UserPoint> userPointWrapper = new LambdaQueryWrapper<>();
            userPointWrapper
                    .eq(UserPoint::getUserId, userId)
                    .eq(UserPoint::getPointId, pointId);
            UserPoint userPoint = userPointMapper.selectOne(userPointWrapper);
            assert userPoint != null;

            //进行掌握度的计算
            //学生u对知识点k的掌握度
            double Kuk = (double)rightQuestions * LevelType.PERCENT / allQuestions;
            //M调整后掌握度
            double M = LevelType.LEVEL_PARAMETER * userPoint.getLevel() + Kuk;
            M = Math.min(LevelType.ONE_HUNDRED, M);
            //进行更新
            userPoint.setLevel((int)M);
            int count = userPointMapper.update(userPoint, userPointWrapper);
            assert count == 1;
        }
        log.info("知识点掌握度更新成功");
    }
}
