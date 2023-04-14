package com.platform.points.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.platform.points.constants.PointConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointRelationVo {

    @ExcelProperty({PointConstant.POINT_HEADLINE,"知识点1"})
    private String pointAId;

    @ExcelProperty({PointConstant.POINT_HEADLINE ,"知识点2"})
    private String pointBId;

    //0表示前后序关系，1表示包含关系  //new: -1表示章节包含的知识点
    @ExcelProperty({PointConstant.POINT_HEADLINE,"关系"})
    private int relation;
}
