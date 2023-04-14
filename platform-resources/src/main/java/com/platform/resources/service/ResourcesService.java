package com.platform.resources.service;

import com.platform.entity.ResponseResult;
import com.platform.resources.dto.UploadVideoDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ResourcesService {
    /**
     * 查找资源
     * @param courseId
     * @param
     * @return
     */
    ResponseResult listAllResources(String courseId);

    ResponseResult  renameResource(String resourceId, String newName);

    /**
     * 上传资源
     * @param courseId
     * @param pointId
     * @return
     */
    ResponseResult uploadResources(MultipartFile multipartFile, String courseId, List<String> pointId);

    /**
     * @description: 获取音/视频上传地址和凭证
     * @param originName:
     * @return com.platform.entity.ResponseResult
     * @author ErrorRua
     * @date 2022/11/22
     */
    ResponseResult createUploadVideoAuth(String originName);

    /**
     * @description: 刷新视频上传凭证
     * @param videoId:
     * @return com.platform.entity.ResponseResult
     * @author ErrorRua
     * @date 2022/11/22
     */
    ResponseResult refreshUploadVideoAuth(String videoId);

    ResponseResult uploadVideo(UploadVideoDTO uploadVideoDTO);

    /**
     * 删除资源
     * @param resourceId
     * @return
     */
    ResponseResult delResourceById(String resourceId);

    ResponseResult searchResource(String search_name);

    ResponseResult uploadAvatar(MultipartFile avatar);

    /**
     * @description: 获取资源信息
     * @param resourceId:
     * @return com.platform.entity.ResponseResult
     * @author ErrorRua
     * @date 2022/11/21
     */
    ResponseResult getResourceById(String resourceId);

    /*
     * 以excel文件导出知识点关系
     * */
    ResponseResult exportExcelFile(String courseId);


}
