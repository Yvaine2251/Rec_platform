package com.platform.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.platform.entity.ResponseResult;
import com.platform.course.entity.Class;
import com.platform.course.entity.ClassUser;
import com.platform.course.mapper.ClassMapper;
import com.platform.course.mapper.ClassUserMapper;
import com.platform.entity.User;
import com.platform.exam.dto.*;
import com.platform.exam.entity.*;
import com.platform.exam.mapper.*;
import com.platform.exam.service.TchPaperService;
import com.platform.exam.vo.*;
import com.platform.exception.PlatformException;
import com.platform.mapper.UserMapper;
import com.platform.points.entity.Point;
import com.platform.points.mapper.PointMapper;
import com.platform.utils.BeanCopyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.platform.exam.enums.ExamCodeEnum.*;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
public class TchPaperServiceImpl implements TchPaperService {

    @Autowired
    private PaperMapper paperMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionPaperMapper questionPaperMapper;

    @Autowired
    private QuestionPointMapper questionPointMapper;

    @Autowired
    private QuestionUserMapper questionUserMapper;

    @Autowired
    private PaperStudentMapper paperStudentMapper;

    @Autowired
    private PointMapper pointMapper;

    @Autowired
    private ClassMapper classMapper;

    @Autowired
    private ClassUserMapper classUserMapper;

    @Autowired
    private UserMapper userMapper;

    // 新建试卷
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult createPaper(PaperAddDTO paperAddDto) {
        // 新建作业对象
        Paper paper = BeanCopyUtils.copyBean(paperAddDto, Paper.class);
        int result = paperMapper.insert(paper);

        // 将题目和作业绑定(假设传入的题目顺序是按照数组的顺序)
        List<String> questionIds = paperAddDto.getQuestionIds();
        List<Integer> questionsScore = paperAddDto.getQuestionsScore();
        for (int i = 1; i <= questionIds.size(); i++) {
            QuestionPaper questionPaper = new QuestionPaper();
            questionPaper.setPaperId(paper.getPaperId());
            questionPaper.setQuestionId(questionIds.get(i - 1));
            questionPaper.setQuestionOrder(i);
            questionPaper.setQuestionScore(questionsScore.get(i - 1));
            questionPaperMapper.insert(questionPaper);
        }
        return ResponseResult.okResult();
    }



    // 发布作业
    @Override
    public ResponseResult releaseHomework(ReleaseHomeworkDTO releaseHomeworkDto) {

        // 先获取作业对象
        LambdaQueryWrapper<Paper> paperInfoQueryWrapper = new LambdaQueryWrapper<>();
        paperInfoQueryWrapper.eq(Paper::getPaperId, releaseHomeworkDto.getPaperId());
        Paper paper = paperMapper.selectOne(paperInfoQueryWrapper);

        // 绑定试题和学生
        for (String studentId : releaseHomeworkDto.getStudentIds()) {
            PaperStudent paperStudent = new PaperStudent();
            paperStudent.setPaperId(paper.getPaperId());
            paperStudent.setStudentId(studentId);
            paperStudent.setCourseId(paper.getCourseId());
            // 选择发布对象的同时赋予其重做次数（每提交一次就减1）
            paperStudent.setHasRemakeTime(releaseHomeworkDto.getRemakeTime());
            paperStudentMapper.insert(paperStudent);
        }

        // 将剩余属性进行拷贝
        BeanUtils.copyProperties(releaseHomeworkDto, paper);

        // 发布状态改为已发布
        paper.setIsRelease(1);
        // 将试卷类型改为作业
        paper.setPaperType(1);

        int result = paperMapper.updateById(paper);
        if (result != 1) {
            throw new PlatformException(PAPER_RELEASE_ERROR.getCode(), PAPER_RELEASE_ERROR.getMessage());
        }
        return ResponseResult.okResult();

    }

    // 发布试卷
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult releaseExam(ReleasePaperDTO releasePaperDto) {

        // 先获取待发布的作业对象
        LambdaQueryWrapper<Paper> paperInfoQueryWrapper = new LambdaQueryWrapper<>();
        paperInfoQueryWrapper.eq(Paper::getPaperId, releasePaperDto.getPaperId());
        Paper paper = paperMapper.selectOne(paperInfoQueryWrapper);

        // 绑定试题和学生
        for (String studentId : releasePaperDto.getStudentIds()) {
            PaperStudent paperStudent = new PaperStudent();
            paperStudent.setPaperId(paper.getPaperId());
            paperStudent.setStudentId(studentId);
            // 选择发布对象的同时赋予其重做次数（每提交一次就减1）
            paperStudent.setHasRemakeTime(releasePaperDto.getRemakeTime());
            paperStudent.setCourseId(paper.getCourseId());
            paperStudentMapper.insert(paperStudent);
        }

        // 将剩余属性进行拷贝
        BeanUtils.copyProperties(releasePaperDto, paper);

        // 将发布状态改为已发布
        paper.setIsRelease(1);
        // 将试卷类型改为考试
        paper.setPaperType(2);
        int result = paperMapper.updateById(paper);
        return ResponseResult.okResult();
    }


    @Override
    public ResponseResult showAllPaper(String courseId) {
        LambdaQueryWrapper<Paper> paperWrapper = new LambdaQueryWrapper<>();
        paperWrapper.eq(Paper::getCourseId, courseId).orderByDesc(Paper::getPaperType);
        List<Paper> paperList = paperMapper.selectList(paperWrapper);
        List<TchPaperListVO> tchPaperListVOS = BeanCopyUtils.copyBeanList(paperList, TchPaperListVO.class);
        return ResponseResult.okResult(tchPaperListVOS);
    }

    @Override
    public ResponseResult getTarget(String courseId, String paperId) {
        LambdaQueryWrapper<Class> classLqw = new LambdaQueryWrapper<>();
        classLqw.eq(Class::getCourseId, courseId);
        List<Class> classes = classMapper.selectList(classLqw);
        PaperTargetVO paperTargetVo = new PaperTargetVO();
        // 封装班级
        ArrayList<PaperClassVO> classList = new ArrayList<>();
        for (Class aClass : classes) {
            PaperClassVO paperClassVo = new PaperClassVO();
            // 找班级学生
            LambdaQueryWrapper<ClassUser> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ClassUser::getClassId, aClass.getClassId());
            List<ClassUser> classUsers = classUserMapper.selectList(wrapper);

            // 封装已经发布过的学生
            LambdaQueryWrapper<PaperStudent> paperStudentLqw = new LambdaQueryWrapper<>();
            paperStudentLqw.eq(PaperStudent::getCourseId, courseId).eq(PaperStudent::getPaperId, paperId);
            List<PaperStudent> paperStudents = paperStudentMapper.selectList(paperStudentLqw);
            List<String> studentIds = paperStudents.stream().map(PaperStudent::getStudentId).collect(Collectors.toList());

            // 封装学生
            ArrayList<PaperStudentVO> studentList = new ArrayList<>();
            for (ClassUser classUser : classUsers) {
                PaperStudentVO paperStudentVo = new PaperStudentVO();
                String userId = classUser.getUserId();
                // 查学生名字
                User user = userMapper.selectById(userId);
                paperStudentVo.setStudentId(userId);
                paperStudentVo.setStudentName(user.getName());

                if (studentIds.contains(userId)) {
                    paperStudentVo.setIsReleased(1);
                } else {
                    paperStudentVo.setIsReleased(0);
                }

                studentList.add(paperStudentVo);

            }
            paperClassVo.setClassName(aClass.getClassName());
            paperClassVo.setStudentList(studentList);
            classList.add(paperClassVo);
        }
        paperTargetVo.setClassList(classList);
        return ResponseResult.okResult(paperTargetVo);
    }

    @Override
    public ResponseResult updatePaper(PaperUpdateDTO paperUpdateDto) {
        Paper paper = BeanCopyUtils.copyBean(paperUpdateDto, Paper.class);
        paperMapper.updateById(paper);
        // 删除试卷题目中间表中记录
        String paperId = paper.getPaperId();
        LambdaQueryWrapper<QuestionPaper> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(QuestionPaper::getPaperId, paperId);
        questionPaperMapper.delete(wrapper);
        // 绑定试卷题目
        List<String> questionIds = paperUpdateDto.getQuestionIds();
        List<Integer> questionsScore = paperUpdateDto.getQuestionsScore();
        for (int i = 1; i <= questionIds.size(); i++) {
            QuestionPaper questionPaper = new QuestionPaper();
            questionPaper.setPaperId(paper.getPaperId());
            questionPaper.setQuestionId(questionIds.get(i - 1));
            questionPaper.setQuestionOrder(i);
            questionPaper.setQuestionScore(questionsScore.get(i - 1));
            questionPaperMapper.insert(questionPaper);
        }
        return ResponseResult.okResult();
    }

    // 查看试卷详情（教师）
    @Override
    public ResponseResult showPaperPreview(String paperId) {
        LambdaQueryWrapper<Paper> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Paper::getPaperId, paperId)
                .select(Paper::getPaperId, Paper::getPaperName);
        Paper paper = paperMapper.selectOne(wrapper);
        PaperShowPreviewVO paperShowPreviewVo = BeanCopyUtils.copyBean(paper, PaperShowPreviewVO.class);

        LambdaQueryWrapper<QuestionPaper> lqw = new LambdaQueryWrapper<>();
        lqw.eq(QuestionPaper::getPaperId, paperId)
                .select(QuestionPaper::getQuestionId, QuestionPaper::getQuestionOrder, QuestionPaper::getQuestionScore);
        List<QuestionPaper> questionPapers = questionPaperMapper.selectList(lqw);
        // 封装题目
        ArrayList<TchQuestionShowOneVO> tchQuestionShowOneVOS = new ArrayList<>();
        for (QuestionPaper questionPaper : questionPapers) {
            String questionId = questionPaper.getQuestionId();

            LambdaQueryWrapper<Question> questionLqw = new LambdaQueryWrapper<>();
            questionLqw.eq(Question::getQuestionId, questionId);
            Question question = questionMapper.selectOne(questionLqw);
            TchQuestionShowOneVO showOneVo = BeanCopyUtils.copyBean(question, TchQuestionShowOneVO.class);
            // 设置题目在试卷中顺序
            showOneVo.setQuestionOrder(questionPaper.getQuestionOrder());

            // 设置分数
            showOneVo.setQuestionScore(questionPaper.getQuestionScore());


            // 封装知识点
            LambdaQueryWrapper<QuestionPoint> quePointLqw = new LambdaQueryWrapper<>();
            quePointLqw.eq(QuestionPoint::getQuestionId, questionId)
                    .select(QuestionPoint::getPointId);
            List<QuestionPoint> questionPoints = questionPointMapper.selectList(quePointLqw);
            ArrayList<String> list = new ArrayList<>();
            for (QuestionPoint point : questionPoints) {
//                list.add(point.getPointId());
                // 返回知识点名字
                Point point1 = pointMapper.selectById(point.getPointId());
                list.add(point1.getPointName());
            }
            showOneVo.setPoints(list);
            tchQuestionShowOneVOS.add(showOneVo);
        }
        paperShowPreviewVo.setQuestionOfPaperVos(tchQuestionShowOneVOS);
        return ResponseResult.okResult(paperShowPreviewVo);
    }

    @Override
    @Transactional
    public ResponseResult deletePaper(String paperId) {
        paperMapper.deleteById(paperId);
        LambdaQueryWrapper<PaperStudent> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaperStudent::getPaperId, paperId);
        paperStudentMapper.delete(wrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult revokePaper(String paperId) {

        LambdaQueryWrapper<PaperStudent> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaperStudent::getPaperId, paperId);
        paperStudentMapper.delete(wrapper);

        LambdaQueryWrapper<Paper> paperLqw = new LambdaQueryWrapper<>();
        paperLqw.eq(Paper::getPaperId, paperId);
        Paper paper = paperMapper.selectOne(paperLqw);
        paper.setIsRelease(0);

        paperMapper.updateById(paper);

        return ResponseResult.okResult();
    }
}
