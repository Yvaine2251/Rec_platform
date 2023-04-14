package com.platform.points.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yjj
 * @date 2022/9/1-10:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointNodeVo {
    //知识点名
    String name;

    //知识点id
    String id;

    //知识点层级
    Integer category;
}
