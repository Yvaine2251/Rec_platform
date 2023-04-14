package com.platform.points.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PointRelationDto {

    private String pointAId;

    private String pointBId;

    //0表示前后序关系，1表示包含关系  //new: -1表示章节包含的知识点
    private int relation;
}
