package com.platform.points.dto;

import lombok.Data;

import java.util.List;

@Data
public class PointDto {

    //知识点id
    String pointId;

    //课程id
    String courseId;

    //知识点名字
    String pointName;

    ////父知识点id
    //Long pointPid;

    //子知识点
    List<PointDto> pointDto;
}
