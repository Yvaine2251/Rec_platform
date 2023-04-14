package com.platform.points.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platform.points.entity.Point;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PointMapper extends BaseMapper<Point> {

    void insertBatchPoint(@Param("list") List<Point> list);
}
