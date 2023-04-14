package com.platform.points.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointGraphVo {

    //知识点信息
    List<PointNodeVo> nodeData;

    //知识点关系信息
    List<PointLinkVo> linkData;
}
