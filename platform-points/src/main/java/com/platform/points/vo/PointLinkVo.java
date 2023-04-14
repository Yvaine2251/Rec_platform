package com.platform.points.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yjj
 * @date 2022/9/1-10:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointLinkVo {
    //前序知识点id
    String source;

    //后序知识点id
    String target;
}
