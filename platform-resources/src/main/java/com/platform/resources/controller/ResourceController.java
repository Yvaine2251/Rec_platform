package com.platform.resources.controller;


import com.platform.annotation.SystemLog;
import com.platform.entity.ResponseResult;
import com.platform.resources.dto.UploadVideoDTO;
import com.platform.resources.service.ResourcesService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/resources")
public class ResourceController {

    @Autowired
    private ResourcesService resourcesService;

    /**
     * 查看资源列表
     *
     * @param courseId
     * @return
     */
    @GetMapping("/list-resources")
    @SystemLog(businessName = "查找该课程下所有资源")
    public ResponseResult listResources(String courseId) {
        return resourcesService.listAllResources(courseId);
    }

    /**
     * 修改资料名称
     *
     * @param resourceId
     * @param newName
     * @return
     */
    @PutMapping("/rename")
    public ResponseResult renameResource(String resourceId, String newName) {
        return resourcesService.renameResource(resourceId, newName);
    }

    /**
     * 上传课程资料
     *
     * @param file
     * @param courseId
     * @return
     *
     */
    @PostMapping("/upload-course")
    public ResponseResult uploadResource(MultipartFile file,
                                         @RequestParam(value = "relatePoints[]", required = false) List<String> relatedPoints,
                                         String courseId) {
        return resourcesService.uploadResources(file, courseId, relatedPoints);
    }

    /**
     * 获取上传视频凭证
     * @param originName
     * @return
     */
    @GetMapping("/upload-video-auth")
    public ResponseResult createUploadVideoAuth(String originName) {
        return resourcesService.createUploadVideoAuth(originName);
    }

    /**
     * 刷新视频上传凭证
     * @param videoId
     * @return
     */
    @GetMapping("/refresh-video-auth")
    public ResponseResult refreshUploadVideoAuth(String videoId) {
        return resourcesService.refreshUploadVideoAuth(videoId);
    }

    /**
     * 上传视频
     * @param videoId
     * @param courseId
     * @param relatedPoints
     * @return
     */
    @PostMapping("/upload-video")
    public ResponseResult uploadVideo(@RequestBody UploadVideoDTO uploadVideoDTO) {
        return resourcesService.uploadVideo(uploadVideoDTO);
    }

    /**
     * 删除资源
     *
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public ResponseResult deleteResource(@PathVariable String id) {
        return resourcesService.delResourceById(id);
    }

    /**
     * 搜索资源，根据资源名称模糊匹配
     *
     * @param searchName
     * @return
     */
    @GetMapping("/search")
    public ResponseResult searchResource(String searchName) {
        return resourcesService.searchResource(searchName);
    }

    /**
     * 上传用户头像或者课程封面
     *
     * @param avatar
     * @return
     */
    @PostMapping("/upload-avatar")
    public ResponseResult uploadAvatar(MultipartFile avatar) {
        return resourcesService.uploadAvatar(avatar);
    }

    @ApiOperation("导出知识点excel")
    @GetMapping("/points/export")
    public ResponseResult exportExcel(String courseId) throws Exception {
        return resourcesService.exportExcelFile(courseId);
    }

    // 获取资源详情
    @GetMapping("/get-resource/{resourceId}")
    public ResponseResult getResource(@PathVariable String resourceId) {
        return resourcesService.getResourceById(resourceId);
    }
}
