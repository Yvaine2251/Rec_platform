package com.platform.points.dto;

import com.platform.points.entity.Point;
import lombok.Data;

import java.util.List;

@Data
public class PointUpdateDto {

    //知识点id
    String pointId;

    //知识点名称
    String pointName;

    //所属课程id
//    String courseId;
}
