package com.platform.course.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.course.constants.CourseConstant;
import com.platform.course.dto.VideoRecordDTO;
import com.platform.course.entity.VideoRecord;
import com.platform.course.mapper.StudyRecordMapper;
import com.platform.course.mapper.VideoRecordMapper;
import com.platform.course.service.VideoRecordService;
import com.platform.entity.ResponseResult;
import com.platform.resources.mapper.ResourcesMapper;
import com.platform.util.SecurityUtils;
import com.platform.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Time;

/**
 * (StVideoRecord)表服务实现类
 *
 * @author makejava
 * @since 2023-03-29 00:28:57
 */
@Service("stVideoRecordService")
public class VideoRecordServiceImpl extends ServiceImpl<VideoRecordMapper, VideoRecord> implements VideoRecordService {

    @Resource
    private StudyRecordMapper studyRecordMapper;

    @Resource
    private ResourcesMapper resourcesMapper;

    //保存视频记录
    @Transactional
    @Override
    public ResponseResult setVideoRecord(VideoRecordDTO videoRecordDTO) {

        //获取用户id
        String userId = SecurityUtils.getUserId();
        //判断用户是否是第一次观看
        Integer count = lambdaQuery()
                .eq(VideoRecord::getUserId, userId)
                .eq(VideoRecord::getCourseId, videoRecordDTO.getCourseId())
                .eq(VideoRecord::getResourceId, videoRecordDTO.getResourceId())
                .count();
        //需要初始化或更新的基本数据
        VideoRecord videoRecordNew = BeanCopyUtils.
                copyBean(videoRecordDTO, VideoRecord.class);
        //userId
        videoRecordNew.setUserId(userId);
        //如果是第一次观看，则初始化一下
        if(count == 0){
            //studyTime
            videoRecordNew.setStudyTime(videoRecordDTO.getLastTime());
            //isComplete
            //如果上次观看时间(即已学习时间)等于总时长，则设置为1
            if(videoRecordDTO.getLastTime().equals(videoRecordDTO.getTotalTime())){
                videoRecordNew.setIsComplete(CourseConstant.ALREADY_COMPLETE);
            }else{
                videoRecordNew.setIsComplete(CourseConstant.NOT_COMPLETE);
            }
            save(videoRecordNew);
        }else{
            //如果不是第一次观看，则更新下数据
            //之前的数据
            VideoRecord videoRecordOld = lambdaQuery()
                    .eq(VideoRecord::getUserId, userId)
                    .eq(VideoRecord::getCourseId, videoRecordDTO.getCourseId())
                    .eq(VideoRecord::getResourceId, videoRecordDTO.getResourceId())
                    .one();
            //如果上次观看时间比已学习时间长，则更新已学习时间, 否则用回之前的数据
            //studyTime
            //取最长的时间
            Time time;
            if(videoRecordOld.getStudyTime().before(videoRecordDTO.getLastTime())){
                time = videoRecordDTO.getLastTime();
            }else{
                time = videoRecordOld.getStudyTime();
            }
            videoRecordNew.setStudyTime(time);
            //如果已学习时间等于总时长，则更新已完成
            //isComplete
            videoRecordNew.setIsComplete(videoRecordOld.getIsComplete());
            //如果原来是没完成的，就判断一下是否完成
            if((videoRecordOld.getIsComplete().equals(CourseConstant.NOT_COMPLETE)
                    && time.equals(videoRecordDTO.getTotalTime()))){
                videoRecordNew.setIsComplete(CourseConstant.ALREADY_COMPLETE);
            }
            lambdaUpdate()
                    .eq(VideoRecord::getUserId, userId)
                    .eq(VideoRecord::getCourseId, videoRecordDTO.getCourseId())
                    .eq(VideoRecord::getResourceId, videoRecordDTO.getResourceId())
                    .update(videoRecordNew);

        }

        return ResponseResult.okResult();
    }

    //获取视频记录
    @Override
    public ResponseResult getVideoRecord(String courseId, String resourceId) {
        //获取用户
        String userId = SecurityUtils.getUserId();
        //通过userId, courseId, resourceId 查询lastTime
        VideoRecord record = lambdaQuery()
                .eq(VideoRecord::getUserId, userId)
                .eq(VideoRecord::getCourseId, courseId)
                .eq(VideoRecord::getResourceId, resourceId)
                .one();
        assert record != null;
        return ResponseResult.okResult(record.getLastTime());
    }
}

