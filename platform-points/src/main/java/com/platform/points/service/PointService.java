package com.platform.points.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.entity.ResponseResult;
import com.platform.points.dto.*;
import com.platform.points.entity.Point;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface PointService extends IService<Point> {
    /**
     * 添加知识节点
     * @param pointCreateDto
     * @return
     */
    ResponseResult addPoints(PointCreateDto pointCreateDto);


    /**
     * 修改知识点的名称
     * @param updateDto
     * @return
     */
    ResponseResult updatePointName(PointUpdateDto updateDto);


    /**
     * 修改知识点的层级
     * @param updateDto  主要参数 ： pointPid 父节点  pointIds 子节点集合
     * @return
     */
    ResponseResult updatePointLevel(PointUpdateLevelDto updateDto);


    /**
     * 删除知识点集
     * @param ids 知识点id 集合，包含它们的子知识点
     * @return
     */
    ResponseResult deletePoints(String[] ids);


    /**
     * 获取知识点数据
     * @param courseId  课程id
     * @return
     */
    ResponseResult getPoints(String courseId);

    ResponseResult addPrePoint(PointAddPreDto pointAddPreDto);

    ResponseResult addAfterPoint(PointAddAfterDto pointAddAfterDto);

    /**
     * 以excel文件导入知识点关系
     *
     * @param courseId
     * @param file
     * @return
     * @throws Exception
     */
    ResponseResult uploadFile(String courseId, MultipartFile file) throws Exception;

    /*
    *  以excel文件导入知识点关系
    * */
    ResponseResult importExcelFile(String courseId, MultipartFile file) throws IOException;

    /*
    * 保存excel文件到数据库
    * */
    ResponseResult saveExcelFile(List<PointRelationDto> cachedDataList, String courseId);

    /*
    * 学习路径推荐
    * */
    ResponseResult getPath(String id, String courseId);
}
