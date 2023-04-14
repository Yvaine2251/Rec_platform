package com.platform.points.dto;

import lombok.Data;

/**
 * @author yjj
 * @date 2022/7/24-22:50
 */
@Data
public class PointUpdateLevelDto {

    //知识点id
    String pointId;

    //新的父id
    String pid;
}
