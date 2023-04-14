package com.platform.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.platform.course.entity.StudyRecord;
import com.platform.course.mapper.StudyRecordMapper;
import com.platform.entity.ResponseResult;
import com.platform.course.dto.ClassTimeAddDTO;
import com.platform.course.dto.ClassTimeUpdateDTO;
import com.platform.course.entity.Chapter;
import com.platform.course.entity.ClassTime;
import com.platform.course.entity.ClassTimeResource;
import com.platform.course.mapper.ChapterMapper;
import com.platform.course.mapper.ClassTimeMapper;
import com.platform.course.mapper.ClassTimeResourceMapper;
import com.platform.course.service.ClassTimeService;
import com.platform.exception.PlatformException;
import com.platform.resources.service.ResourcesService;
import com.platform.util.SecurityUtils;
import com.platform.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.platform.course.enums.CourseCodeEnum.*;


@Service
public class ClassTimeServiceImpl implements ClassTimeService {

    @Autowired
    private ClassTimeMapper classTimeMapper;

    @Autowired
    private ChapterMapper chapterMapper;

    @Autowired
    private ClassTimeResourceMapper classTimeResourceMapper;

    @Autowired
    private ResourcesService resourcesService;

    @Autowired
    private StudyRecordMapper studyRecordMapper;


    /**
     * 添加课时
     * @param classTimeAddDto
     * @return
     */
    @Override
    @Transactional
    public ResponseResult addClassTime(ClassTimeAddDTO classTimeAddDto) {

        //首先查看当前章节下，有多少个课时
        LambdaQueryWrapper<ClassTime> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ClassTime::getChapterId, classTimeAddDto.getChapterId());
        List<ClassTime> classTimesOfChapter = classTimeMapper.selectList(wrapper);
        int count = classTimesOfChapter.size();

        //插入课时
        ClassTime classTime = BeanCopyUtils.copyBean(classTimeAddDto, ClassTime.class);
        classTime.setClassTimeOrder(count + 1);
        int result = classTimeMapper.insert(classTime);
        if (result == 0) {
            throw new PlatformException(CLASS_TIME_ADD_ERROR.getCode(), CLASS_TIME_ADD_ERROR.getMessage());
        }
        String classTimeId = classTime.getClassTimeId();
        //拿到id后插入知识点，添加资源-课时关系
        List<String> resourceIds = classTimeAddDto.getResourceIds();
        for (String resourceId : resourceIds) {
            ClassTimeResource classTimeResource = new ClassTimeResource();
            classTimeResource.setClassTimeId(classTimeId);
            classTimeResource.setResourceId(resourceId);
            classTimeResourceMapper.insert(classTimeResource);
        }
        return ResponseResult.okResult(classTimeId);
    }


    @Override
    @Transactional
    public ResponseResult deleteClassTime(String classTimeId) {
        LambdaQueryWrapper<ClassTime> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ClassTime::getClassTimeId, classTimeId);
        ClassTime classTimeOne = classTimeMapper.selectOne(wrapper);

        if (classTimeOne == null) {
            throw new PlatformException(CLASS_TIME_NOT_EXIST.getCode(), CLASS_TIME_NOT_EXIST.getMessage());
        }

        //获取当前课时的顺序
        int classTimeOrder = classTimeOne.getClassTimeOrder();

        //删除课时
        int result = classTimeMapper.delete(wrapper);
        if (result == 0) {
            throw new PlatformException(CLASS_TIME_DELETE_ERROR.getCode(), CLASS_TIME_DELETE_ERROR.getMessage());
        }

        //删除该课时后，调整课时顺序，首先获取所有顺序在该课时后面的课时
        wrapper.gt(ClassTime::getClassTimeOrder, classTimeOrder);
        List<ClassTime> classTimes = classTimeMapper.selectList(wrapper);
        //所有顺序在该课时后面的，全部往前移动一位
        for (ClassTime classTime : classTimes) {
            classTime.setClassTimeOrder(classTime.getClassTimeOrder() - 1);
            //移动后直接更新
            classTimeMapper.updateById(classTime);
        }

        //删除课时资源表
        LambdaQueryWrapper<ClassTimeResource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ClassTimeResource::getClassTimeId, classTimeId);
        classTimeResourceMapper.delete(queryWrapper);

        //获取被删除课时的章节id
        String chapterId = classTimeOne.getChapterId();
        LambdaQueryWrapper<Chapter> chapterQueryWrapper = new LambdaQueryWrapper<>();
        chapterQueryWrapper.eq(Chapter::getId, chapterId);

        //先查询看看当前章节是否还有课时存在
        wrapper.clear();
        wrapper.eq(ClassTime::getChapterId, chapterId);
        List<ClassTime> classTimeList = classTimeMapper.selectList(wrapper);
        //如果不存在课时
        if (classTimeList == null) {
            Chapter chapterOne = chapterMapper.selectOne(chapterQueryWrapper);
            chapterOne.setHasCoursetime(0);
            chapterMapper.updateById(chapterOne);
        }
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult updateClassTime(ClassTimeUpdateDTO classTimeUpdateDto) {
        ClassTime classTime = BeanCopyUtils.copyBean(classTimeUpdateDto, ClassTime.class);
        classTimeMapper.updateById(classTime);

        //更新课时资源表，先删除，再添加
        LambdaQueryWrapper<ClassTimeResource> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ClassTimeResource::getClassTimeId, classTimeUpdateDto.getClassTimeId());
        classTimeResourceMapper.delete(wrapper);
        for (String resourceId : classTimeUpdateDto.getResourceIds()) {
            ClassTimeResource classTimeResource = new ClassTimeResource();
            classTimeResource.setResourceId(resourceId);
            classTimeResource.setClassTimeId(classTimeUpdateDto.getClassTimeId());
            classTimeResourceMapper.insert(classTimeResource);
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addStudyRecord(String resourceId) {

        // 获取当前用户id
        String userId = SecurityUtils.getUserId();
        // 获取当前用户的学习记录
        LambdaQueryWrapper<StudyRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StudyRecord::getUserId, userId);
        wrapper.eq(StudyRecord::getResourceId, resourceId);
        StudyRecord studyRecord = studyRecordMapper.selectOne(wrapper);
        // 如果学习记录不存在，添加学习记录
        if (studyRecord == null) {
            studyRecord = new StudyRecord();
            studyRecord.setUserId(userId);
            studyRecord.setResourceId(resourceId);
            studyRecordMapper.insert(studyRecord);
        }

        return ResponseResult.okResult();


    }
}
