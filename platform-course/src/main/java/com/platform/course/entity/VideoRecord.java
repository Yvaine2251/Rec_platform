package com.platform.course.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("co_video_resource_record")
public class VideoRecord extends BaseEntity {
    //id@TableId
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    //用户id
    private String userId;
    //课程id
    private String courseId;
    //资源id(视频id)
    private String resourceId;
    //上次观看时间
    private Time lastTime;
    //已学习时间
    private Time studyTime;
    //视频总时长
    private Time totalTime;
    //0:没完成，1：已完成
    private Integer isComplete;



}

