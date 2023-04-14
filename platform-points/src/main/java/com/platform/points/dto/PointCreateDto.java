package com.platform.points.dto;

import com.platform.points.entity.Point;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@ApiModel(value="")
@Data
public class PointCreateDto {

    //知识点名称
    String pointName;

    //父id
    String pointPid;

    //课程 courseId
    String courseId;
}
