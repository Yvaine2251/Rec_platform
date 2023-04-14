package com.platform.points.dto;

import lombok.Data;

import java.util.List;

/**
 * @author yjj
 * @date 2022/8/13-8:57
 */
@Data
public class PointAddAfterDto {
    //知识点id
    String pointId;

    String courseId;

    //前序知识点id集合
    List<String> afterPointId;
}
