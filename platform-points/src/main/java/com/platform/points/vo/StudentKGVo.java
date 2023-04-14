package com.platform.points.vo;

import lombok.Data;

import java.util.List;

/**
 * @author yjj
 * @date 2022/10/6-10:39
 */
@Data
public class StudentKGVo {

    //知识点掌握程度
    List<PointLevelVo> nodeData;

    //知识点关系信息
    List<PointLinkVo> linkData;
}
