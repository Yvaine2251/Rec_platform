package com.platform.points.vo;

import com.platform.points.entity.Point;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointShowVo {

    //知识点id
    String pointId;

    //知识点名字
    String pointName;

    //知识点父id
    String pointPid;

    //子知识点
    List<PointShowVo> children;

    //前序知识点
    List<PointIdNameVo> prePoints;

    //后序知识点
    List<PointIdNameVo> afterPoints;

    public PointShowVo(Point point) {
        pointId = point.getPointId();
        pointName = point.getPointName();
        pointPid = point.getPointPid();
    }
}
