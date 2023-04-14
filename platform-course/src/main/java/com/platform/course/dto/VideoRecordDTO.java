package com.platform.course.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoRecordDTO {

    //课程id
    private String courseId;
    //资源id(视频id)
    private String resourceId;
    //上次观看时间
    private Time lastTime;
    //视频总时长
    private Time totalTime;
}
