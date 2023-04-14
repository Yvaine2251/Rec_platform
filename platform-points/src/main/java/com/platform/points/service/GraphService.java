package com.platform.points.service;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.entity.ResponseResult;
import com.platform.points.entity.PointRelation;
import org.springframework.web.multipart.MultipartFile;


public interface GraphService extends IService<PointRelation> {
    /**
     * 通过课程号获取知识点图谱
     * @param courseId
     * @return
     */
    ResponseResult showGraph(String courseId);

    /**
     * 导出知识点关系excel文件
     * @return
     * @throws Exception
     */
    ResponseResult downloadRelationFile() throws Exception;

    ResponseResult showStudentKG(String courseId);

    ResponseResult connectSocket();

    JSONObject createCSV();

    JSONObject createPoint2Question();

    JSONObject createStudent2Question();
}
