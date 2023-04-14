package com.platform.points.vo;

import lombok.Data;

/**
 * @author yjj
 * @date 2022/10/6-10:42
 */
@Data
public class PointLevelVo extends PointNodeVo {

    //掌握分数（0-100）
    Integer level;
}
