package com.platform.points.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yjj
 * @date 2022/9/1-11:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointNodeTempVo {
    //知识点id
    String pointId;

    //知识点名
    String pointName;

    //父知识点id
    String pointPid;

    //知识点层级
    Integer level;
}
