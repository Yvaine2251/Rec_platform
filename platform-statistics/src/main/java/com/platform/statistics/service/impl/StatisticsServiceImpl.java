package com.platform.statistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.platform.course.entity.StudyRecord;
import com.platform.course.entity.VideoRecord;
import com.platform.course.mapper.StudyRecordMapper;
import com.platform.course.service.VideoRecordService;
import com.platform.entity.ResponseResult;
import com.platform.exam.entity.Paper;
import com.platform.exam.entity.PaperStudent;
import com.platform.exam.entity.Question;
import com.platform.exam.entity.QuestionUser;
import com.platform.exam.mapper.PaperMapper;
import com.platform.exam.mapper.PaperStudentMapper;
import com.platform.exam.mapper.QuestionMapper;
import com.platform.exam.mapper.QuestionUserMapper;
import com.platform.resources.entity.Resources;
import com.platform.resources.mapper.ResourcesMapper;
import com.platform.statistics.dto.LearningDataDTO;
import com.platform.statistics.service.StatisticService;
import com.platform.statistics.vo.ExamRecordVO;
import com.platform.statistics.vo.QuestionRecordVO;
import com.platform.statistics.vo.StudyCountVO;
import com.platform.util.SecurityUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticsServiceImpl implements StatisticService {

    @Resource
    private VideoRecordService videoRecordService;

    @Resource
    private ResourcesMapper resourcesMapper;

    @Resource
    private StudyRecordMapper studyRecordMapper;

    @Resource
    private QuestionMapper questionMapper;

    @Resource
    private QuestionUserMapper questionUserMapper;

    @Resource
    private PaperMapper paperMapper;

    @Resource
    private PaperStudentMapper paperStudentMapper;

    //获取单个用户单个课程总播放时长
    @Override
    public ResponseResult getVideoTotalTime(String courseId, @Nullable String userId) {
        //获取用户
        if (userId == null) {
            userId = SecurityUtils.getUserId();
        }
        //通过userId, courseId 查询所有视频的已学习时间
        long sum = getSumTime(userId, courseId);
        return ResponseResult.okResult(new Time(sum));
    }

    //通过 userId 和 courseId 获取 总的时间
    private long getSumTime(String userId, String courseId) {
        List<VideoRecord> records = videoRecordService.lambdaQuery()
                .eq(VideoRecord::getUserId, userId)
                .eq(VideoRecord::getCourseId, courseId)
                .list();
        //获取当前学生该课程的总时间
        long sum = 0;
        for (VideoRecord record : records) {
            Time studyTime = record.getStudyTime();
            long seconds = getSeconds(studyTime);
            sum += seconds;
        }
        return sum;
    }

    //获取Time对应到的long类型的秒数
    private long getSeconds(Time time) {
        String studyTime = time.toString();
        String[] split = studyTime.split(":");
        long hour = Long.parseLong(split[0]);
        long minute = Long.parseLong(split[1]);
        long seconds = Long.parseLong(split[2]);
        seconds += minute * 60 + hour * 3600;
        return seconds;
    }

    @Override
    public ResponseResult getStudyCount(String courseId, @Nullable String userId) {
        //获取当前用户
        if (userId == null) {
            userId = SecurityUtils.getUserId();
        }
        StudyCountVO studyCountVO = getStudyCountVO(userId, courseId);

        return ResponseResult.okResult(studyCountVO);
    }

    //通过传入 userId 和 courseId 获取到单个用户在单个课程的资源学习次数，封装进行返回
    @NotNull
    private StudyCountVO getStudyCountVO(String userId, String courseId) {
        //获取用户观看过的视频资源
        List<VideoRecord> videoRecords = videoRecordService.lambdaQuery()
                .eq(VideoRecord::getUserId, userId)
                .eq(VideoRecord::getCourseId, courseId)
                .list();
        //获取所有的资源记录
        LambdaQueryWrapper<StudyRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(StudyRecord::getUserId, userId);
        //通过 courId 进行过滤
        List<StudyRecord> studyRecords = studyRecordMapper.selectList(queryWrapper);
        int totalRecords = (int) studyRecords.stream().filter(studyRecord -> {
            Resources resources = resourcesMapper.selectById(studyRecord.getResourceId());
            return resources.getCourseId().equals(courseId);
        }).count();
        //已完成的其它资源记录
        int otherRecords = totalRecords - videoRecords.size();
        //有效的视频资源记录
        long validVideo = videoRecords.stream().filter(videoRecord -> {
            Time studyTime = videoRecord.getStudyTime();
            Time totalTime = videoRecord.getTotalTime();
            return 3 * getSeconds(studyTime) > 2 * getSeconds(totalTime);
        }).count();

        //获取总资源
        LambdaQueryWrapper<Resources> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Resources::getCourseId, courseId);
        int count = resourcesMapper.selectCount(wrapper);

        //封装数据
        StudyCountVO studyCountVO = new StudyCountVO();
        studyCountVO.setCompletedCount(otherRecords + (int) validVideo);
        studyCountVO.setTotalCount(count);
        return studyCountVO;
    }

    @Override
    public ResponseResult getQuestionRecord(String courseId, @Nullable String userId) {
        //获取当前用户
        if (userId == null) {
            userId = SecurityUtils.getUserId();
        }

        QuestionRecordVO questionRecordVO = getQuestionRecordVO(courseId, userId);

        return ResponseResult.okResult(questionRecordVO);
    }

    //通过传入 userId 和 courseId 获取做题记录统计
    @NotNull
    private QuestionRecordVO getQuestionRecordVO(String courseId, String userId) {
        QuestionRecordVO questionRecordVO = new QuestionRecordVO();
        //通过userId 获取所有的题目
        LambdaQueryWrapper<QuestionUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(QuestionUser::getUserId, userId)
                .select(QuestionUser::getUserId, QuestionUser::getQuestionId, QuestionUser::getWrongTime);
        List<QuestionUser> questionUsers = questionUserMapper.selectList(queryWrapper);

        //通过判断是否是对应课程进行过滤，获取该课程做过的题
        List<QuestionUser> collect = questionUsers.stream().filter(questionUser -> {
            Question question = questionMapper.selectById(questionUser.getQuestionId());
            return question != null && question.getCourseId().equals(courseId);
        }).collect(Collectors.toList());

        //保存做过的题的数量
        questionRecordVO.setCompletedQuestions(collect.size());

        //通过判断是否有错误次数，获取该课程所有错题数量
        int wrongCount = (int) collect.stream()
                .filter(item -> item.getWrongTime() != 0).count();
        //保存错题数量
        questionRecordVO.setWrongQuestions(wrongCount);

        //获取该课程所有题目
        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Question::getCourseId, courseId);
        int totalQuestions = questionMapper.selectCount(wrapper);
        //保存题库总数
        questionRecordVO.setTotalQuestions(totalQuestions);
        return questionRecordVO;
    }

    @Override
    public ResponseResult getExamRecord(String courseId) {
        //获取用户id
        String userId = SecurityUtils.getUserId();
        //通过userId 找出 所有 paper
        LambdaQueryWrapper<PaperStudent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PaperStudent::getStudentId, userId);
        List<PaperStudent> paperStudents = paperStudentMapper.selectList(queryWrapper);
        //通过 paperId 联查 进行 paper_type 的过滤
        List<PaperStudent> collect = paperStudents.stream().filter(paperStudent -> {
            Paper paper = paperMapper.selectById(paperStudent.getPaperId());
            return paper.getPaperType().equals(2);
        }).collect(Collectors.toList());
        //总数
        int total = collect.size();
        //已完成
        int completed = (int) collect.stream().filter(item -> {
            return item.getIsDone() == 1;
        }).count();

        return ResponseResult.okResult(new ExamRecordVO(completed, total));
    }
}
