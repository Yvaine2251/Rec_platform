package com.platform.points.mapper;


import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GraphSolveMapper {

    List<JSONObject> selectQuestionPoint(@Param("course_id")String courseId);

    List<JSONObject> selectQuestionStudent(@Param("course_id")String courseId);
}
