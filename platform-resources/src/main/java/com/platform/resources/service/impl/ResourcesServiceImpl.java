package com.platform.resources.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.platform.entity.ResponseResult;
import com.platform.enums.ResourceType;
import com.platform.exception.PlatformException;
import com.platform.points.entity.Point;
import com.platform.points.entity.PointRelation;
import com.platform.points.mapper.PointMapper;
import com.platform.points.mapper.PointRelationMapper;

import com.platform.points.vo.PointRelationVo;
import com.platform.resources.dto.UploadVideoDTO;
import com.platform.resources.mapper.ResourceToPointMapper;
import com.platform.resources.mapper.ResourcesMapper;
import com.platform.resources.entity.ResourceToPoint;
import com.platform.resources.entity.Resources;
import com.platform.resources.resourceUtils.AudioOrVideoCreateUploadUtil;
import com.platform.resources.resourceUtils.ContentType;
import com.platform.resources.resourceUtils.InitVodClient;
import com.platform.resources.resourceUtils.OssProperties;
import com.platform.resources.service.ResourcesService;
import com.platform.resources.util.PdfConvertUtil;
import com.platform.resources.vo.ListResourcesVo;
import com.platform.resources.vo.ResourceOneVo;
import com.platform.utils.BeanCopyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.platform.enums.ResourceType.PPT;
import static com.platform.enums.ResourceType.PPTX;
import static com.platform.resources.enums.ResourcesCodeEnum.*;

@Slf4j
@Service
public class ResourcesServiceImpl implements ResourcesService {

    @Autowired
    private ResourcesMapper resourcesMapper;

    @Autowired
    private PointMapper pointMapper;

    @Autowired
    private PointRelationMapper pointRelationMapper;

    @Autowired
    private ResourceToPointMapper resourceToPointMapper;

    @Autowired
    private ResourcesService resourcesService;


    /**
     * 查看资源列表
     *
     * @param courseId
     * @return
     */
    @Override
    public ResponseResult listAllResources(String courseId) {
        //根据课程id查询到资源id
        LambdaQueryWrapper<Resources> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Resources::getCourseId, courseId)
                .orderByDesc(Resources::getUpdateTime);
        List<Resources> resources = resourcesMapper.selectList(queryWrapper);

        List<ListResourcesVo> resourcesVos = new ArrayList<>();
        for (Resources resourceOne : resources) {
            ListResourcesVo ResourcesVo = BeanCopyUtils.copyBean(resourceOne, ListResourcesVo.class);
            resourcesVos.add(ResourcesVo);
        }
        return ResponseResult.okResult(resourcesVos);
    }

    /**
     * 修改资源名称
     *
     * @param resourceId
     * @param newName
     * @return
     */
    @Override
    @Transactional
    public ResponseResult renameResource(String resourceId, String newName) {
        LambdaQueryWrapper<Resources> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Resources::getResourceId, resourceId);
        Resources resources = resourcesMapper.selectOne(queryWrapper);
        //更新资源名
        resources.setResourceName(newName);
        resourcesMapper.updateById(resources);
        return ResponseResult.okResult();
    }

    /**
     * 上传课程资源
     *
     * @param file
     * @param courseId
     * @param pointsId
     * @return
     */
    @Override
    @Transactional
    public ResponseResult uploadResources(MultipartFile file, String courseId, List<String> pointsId) {

        try {
            //获取文件上传的流
            InputStream inputStream = file.getInputStream();
            //获取文件名
            String originName = file.getOriginalFilename();
            //获取文件后缀
            String suffix = originName.substring(originName.lastIndexOf("."));
            //生成文件名
            String fileName = "";
            String uuid = UUID.randomUUID().toString().substring(0, 20);
            //文件名的拼接：课程id/uuid/
            fileName = courseId + "/" + uuid + originName;

//             把文件按照日期进行分类
//             String datePath = new DateTime().toString("yyyy/MM/dd");
//             fileName = datePath + "/" + fileName;

            //判断资源类型
            int type = 0;
            ResourceType[] values = ResourceType.values();
            for (ResourceType value : values) {
                if (suffix.equals(value.getTypeName())) {
                     type = value.getType();
                }
            }
            if (type == 0) {
                throw new PlatformException(FILE_FORMAT_NOT_SUPPORT.getCode(), FILE_FORMAT_NOT_SUPPORT.getMessage());
            }
            String resourcesLink = null;
             //如果文件类型没有问题，则将文件存入阿里云oss
            if (type < 10 || type >= 20) {
                // 文件为PPT
                if (type == PPT.getType()) {
                    inputStream = PdfConvertUtil.pptToPdf(inputStream);
                    fileName = fileName.replace(".ppt", ".pdf");
                    suffix = ".pdf";
                }
                if (type == PPTX.getType()) {
                    inputStream = PdfConvertUtil.pptToPdf(inputStream);
                    fileName = fileName.replace(".pptx", ".pdf");
                    suffix = ".pdf";
                }

                 //创建OSSClient实例。
                OSS ossClient = new OSSClientBuilder().build(OssProperties.END_POINT, OssProperties.ACCESS_KEY_ID, OssProperties.ACCESS_KEY_SECRET);
                try {
                    resourcesLink = "https://" + OssProperties.BUCKET_NAME + "." + OssProperties.END_POINT + "/" + fileName;
                    ObjectMetadata objectMetadata = new ObjectMetadata();
                    objectMetadata.setContentType(ContentType.getContentType(suffix));
                    objectMetadata.setHeader("Content-Disposition", "attachment");
//                    ossClient.putObject(OssProperties.BUCKET_NAME, fileName, inputStream, objectMetadata);

                    long start = System.currentTimeMillis();

                    ossClient.putObject(OssProperties.BUCKET_NAME, fileName, inputStream, objectMetadata);

                    long end = System.currentTimeMillis();
                    System.out.println("----------------------------------------------------------------");

                    log.info("上传文件耗时：{}ms", end - start);
                    System.out.println((end - start) / 1000.0);
                    System.out.println(end - start);
                    log.error(String.valueOf(((end - start) / 1000.0)));
                    System.out.println("----------------------------------------------------------------");

                } finally {
                    ossClient.shutdown();
                }
            } else {
                //上传视频到阿里云
                try {
                    String videoId = null;
                    fileName = file.getOriginalFilename();
                    //截取不包含后缀的名称
                    String title = fileName.substring(0, fileName.lastIndexOf("."));
                    //title：上传之后显示名称
                    //filename：上传文件原始名称
                    UploadStreamRequest request = new UploadStreamRequest(OssProperties.ACCESS_KEY_ID, OssProperties.ACCESS_KEY_SECRET, title, fileName, inputStream);

                    /* 存储区域(可选) */
                    request.setStorageLocation("outin-cbbc44500c8511ed9cfb00163e021072.oss-cn-shenzhen.aliyuncs.com");
                    /* 点播服务接入点 */
                    request.setApiRegionId("cn-shenzhen");

                    UploadVideoImpl uploader = new UploadVideoImpl();

                    long start = System.currentTimeMillis();

                    UploadStreamResponse response = uploader.uploadStream(request);

                    long end = System.currentTimeMillis();

                    System.out.println("----------------------------------------------------------------");

                    log.info("上传文件耗时：{}ms", end - start);
                    System.out.println((end - start) / 1000.0);
                    System.out.println(end - start);
                    log.error(String.valueOf(((end - start) / 1000.0)));
                    System.out.println("----------------------------------------------------------------");
                    videoId = response.getVideoId();
                    //视频资源的uuid存的是阿里云的视频id
                    uuid = videoId;
                    List<String> urlList = vodTest(videoId);
                    //视频资源
                    resourcesLink = urlList.get(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //将生成的文件链接和文件数据存入数据库
            Resources resources = new Resources();
            resources.setCourseId(courseId);
            resources.setResourceLink(resourcesLink);
            resources.setType(type);
            resources.setResourceName(originName);
            resources.setResourceUUID(uuid);

            int result = resourcesMapper.insert(resources);
            String resourceId = resources.getResourceId();
            ResourceOneVo resourceOneVo = BeanCopyUtils.copyBean(resources, ResourceOneVo.class);
//----- ----------根据知识点名称查询到知识点id，然后把关系存入知识点-资源表-------------------------
            if(pointsId != null && pointsId.size() > 0){
                for (String pointId : pointsId) {
                    //插入资源-知识点表
                    ResourceToPoint resourceToPoint = new ResourceToPoint();
                    resourceToPoint.setPointId(pointId);
                    resourceToPoint.setResourceId(resourceId);
                    resourceToPointMapper.insert(resourceToPoint);
                }
            }

            return ResponseResult.okResult(resourceOneVo);
        } catch (IOException e) {
            e.printStackTrace();
            throw new PlatformException(RESOURCES_UPLOAD_ERROR.getCode(), RESOURCES_UPLOAD_ERROR.getMessage());
        }
    }

    @Override
    public ResponseResult createUploadVideoAuth(String originName) {
        String suffix = originName.substring(originName.lastIndexOf("."));
        String title = originName.substring(0, originName.lastIndexOf("."));

        int type = 0;
        ResourceType[] values = ResourceType.values();
        for (ResourceType value : values) {
            if (suffix.equals(value.getTypeName())) {
                type = value.getType();
            }
        }

        if (type == 0) {
            throw new PlatformException(FILE_FORMAT_NOT_SUPPORT.getCode(), FILE_FORMAT_NOT_SUPPORT.getMessage());
        }

        try {
            CreateUploadVideoResponse uploadVideo = AudioOrVideoCreateUploadUtil.createUploadVideo(title, originName);
            return ResponseResult.okResult(uploadVideo);
        } catch (Exception e) {
            throw new PlatformException(RESOURCES_UPLOAD_ERROR.getCode(), RESOURCES_UPLOAD_ERROR.getMessage());
        }
    }

    @Override
    public ResponseResult refreshUploadVideoAuth(String videoId) {
        try {
            RefreshUploadVideoResponse refreshUploadVideo = AudioOrVideoCreateUploadUtil.refreshUploadVideo(videoId);
            return ResponseResult.okResult(refreshUploadVideo);
        } catch (Exception e) {
            throw new PlatformException(RESOURCES_UPLOAD_ERROR.getCode(), RESOURCES_UPLOAD_ERROR.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseResult uploadVideo(UploadVideoDTO uploadVideoDTO) {

        GetPlayInfoResponse response = null;
        try {
            DefaultAcsClient client = InitVodClient.initVodClient(OssProperties.ACCESS_KEY_ID, OssProperties.ACCESS_KEY_SECRET);

            //创建获取视频地址request和response
            GetPlayInfoRequest request = new GetPlayInfoRequest();

            //向request对象里面设置视频id
            request.setVideoId(uploadVideoDTO.getVideoId());

            //调用初始化对象里面的方法，传递request，获取数据
            response = client.getAcsResponse(request);

            //获取视频播放地址
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }


        String resourcesLink = response.getPlayInfoList().get(0).getPlayURL();
        String uuid = response.getVideoBase().getVideoId();
        String originName = response.getVideoBase().getTitle();

        String suffix = "." + response.getPlayInfoList().get(0).getFormat();
        int type = 0;
        ResourceType[] values = ResourceType.values();
        for (ResourceType value : values) {
            if (suffix.equals(value.getTypeName())) {
                type = value.getType();
            }
        }


        //将生成的文件链接和文件数据存入数据库
        Resources resources = new Resources();
        resources.setCourseId(uploadVideoDTO.getCourseId());
        resources.setResourceLink(resourcesLink);
        resources.setType(type);
        resources.setResourceName(originName);
        resources.setResourceUUID(uuid);

        int result = resourcesMapper.insert(resources);
        String resourceId = resources.getResourceId();
        ResourceOneVo resourceOneVo = BeanCopyUtils.copyBean(resources, ResourceOneVo.class);
        //---------------根据知识点名称查询到知识点id，然后把关系存入知识点-资源表-------------------------

        if (uploadVideoDTO.getRelatedPoints() != null && uploadVideoDTO.getRelatedPoints().size() > 0) {
            for (String pointId : uploadVideoDTO.getRelatedPoints()) {
                //插入资源-知识点表
                ResourceToPoint resourceToPoint = new ResourceToPoint();
                resourceToPoint.setPointId(pointId);
                resourceToPoint.setResourceId(resourceId);
                resourceToPointMapper.insert(resourceToPoint);
            }
        }
        return ResponseResult.okResult(resourceOneVo);
    }

    @Override
    public ResponseResult uploadAvatar(MultipartFile avatar) {
        InputStream inputStream = null;
        String resourcesLink = "";
        try {
            inputStream = avatar.getInputStream();
            String originalFilename = avatar.getOriginalFilename();
            String fileName = "avatar" + "/" + originalFilename;

            OSS ossClient = new OSSClientBuilder().build(OssProperties.END_POINT, OssProperties.ACCESS_KEY_ID, OssProperties.ACCESS_KEY_SECRET);
            try {
                resourcesLink = "https://" + OssProperties.BUCKET_NAME + "." + OssProperties.END_POINT + "/" + fileName;
                ossClient.putObject(OssProperties.BUCKET_NAME, fileName, inputStream);
            } finally {
                ossClient.shutdown();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseResult.okResult(resourcesLink);
    }

    @Override
    public ResponseResult getResourceById(String resourceId) {
        Resources resources = resourcesMapper.selectById(resourceId);
        if (resources == null) {
            throw new PlatformException(RESOURCES_NOTFOUND_ERROR.getCode(), RESOURCES_NOTFOUND_ERROR.getMessage());
        }
        ResourceOneVo resourceOneVo = BeanCopyUtils.copyBean(resources, ResourceOneVo.class);
        return ResponseResult.okResult(resourceOneVo);

    }

    /**
     * 删除资源
     *
     * @param resourceId
     * @return
     */
    @Override
    public ResponseResult delResourceById(String resourceId) {
        Resources resources = resourcesMapper.selectById(resourceId);
        Integer type = resources.getType();
        if (type >= 20) {
            //删除oss端资源
            delOssResourceById(resourceId);
        } else {
            //删除视频点播资源
            String videoId = resources.getResourceUUID();
            deleteVideo(videoId);
        }

        //从资源表中删除
        QueryWrapper<Resources> query = new QueryWrapper<>();
        query.eq("resource_id", resourceId);

        int result = resourcesMapper.delete(query);

        //从知识点-资源表中删除
        QueryWrapper<ResourceToPoint> queryResourceToPoint = new QueryWrapper<>();
        queryResourceToPoint.eq("resource_id", resourceId);
        int result2 = resourceToPointMapper.delete(queryResourceToPoint);

        return ResponseResult.okResult();
    }

    /**
     * 通过传如资源id，删除oss端资源
     * @param resourceId
     */
    public void delOssResourceById(String resourceId) {
        //从oss端删除
        OSS ossClient = new OSSClientBuilder().build(OssProperties.END_POINT, OssProperties.ACCESS_KEY_ID, OssProperties.ACCESS_KEY_SECRET);
        //从数据库中查询
        LambdaQueryWrapper<Resources> queryFromResource = new LambdaQueryWrapper<>();
        queryFromResource.eq(Resources::getResourceId, resourceId);
        try {
            // 填写需要删除的文件完整路径。文件完整路径中不能包含Bucket名称。
            Resources resources = resourcesMapper.selectOne(queryFromResource);
            String url = resources.getCourseId() + "/" + resources.getResourceUUID() + resources.getResourceName();
            ossClient.deleteObject(OssProperties.BUCKET_NAME, url);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ossClient.shutdown();
        }
    }

    @Override
    public ResponseResult searchResource(String resource_name) {
        LambdaQueryWrapper<Resources> lqw = new LambdaQueryWrapper<>();
        lqw.like(resource_name != null, Resources::getResourceName, resource_name);
        List<Resources> resources = resourcesMapper.selectList(lqw);
        ArrayList<ListResourcesVo> listResourcesVos = new ArrayList<>();
        for (Resources resource : resources) {
            ListResourcesVo listResourcesVo = new ListResourcesVo();
            BeanUtils.copyProperties(resource, listResourcesVo);
            listResourcesVos.add(listResourcesVo);
        }
        return ResponseResult.okResult(listResourcesVos);
    }

    //传入阿里云视频里的id，删除阿里云视频
    public ResponseResult deleteVideo(String videoId) {
        try {
            DefaultAcsClient client = InitVodClient.initVodClient(OssProperties.ACCESS_KEY_ID, OssProperties.ACCESS_KEY_SECRET);
            //创建删除视频request对象
            DeleteVideoRequest request = new DeleteVideoRequest();
            request.setVideoIds(videoId);
            //调用初始化对象方法实现删除
            client.getAcsResponse(request);
            return ResponseResult.okResult();
        } catch (Exception e) {
            e.printStackTrace();
            throw new PlatformException(200, "删除视频失败");
        }
    }

    public List<String> vodTest(String videoId) throws ClientException {
        //1 根据视频ID获取视频播放地址
        //创建初始化对象
        DefaultAcsClient client = InitVodClient.initVodClient(OssProperties.ACCESS_KEY_ID, OssProperties.ACCESS_KEY_SECRET);

        //创建获取视频地址request和response
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        GetPlayInfoResponse response = new GetPlayInfoResponse();

        //向request对象里面设置视频id
        request.setVideoId(videoId);

        //调用初始化对象里面的方法，传递request，获取数据
        response = client.getAcsResponse(request);
        ArrayList<String> playUrlList = new ArrayList<>();
        List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
        //播放地址
        for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
            playUrlList.add(playInfo.getPlayURL());
        }
        return playUrlList;
        //Base信息
//        System.out.print("VideoBase.Title = " + response.getVideoBase().getTitle() + "\n");
    }

    @Override
    public ResponseResult exportExcelFile(String courseId) {
        //存储所有数据
        List<PointRelationVo> relationVoList = new ArrayList<>();

        //存储每一个章节的儿子
        List<Point> pointSon = new ArrayList<>();

        //过滤courseId
        List<Point> pointList = pointMapper.selectList(new QueryWrapper<Point>().eq("course_id", courseId));
        List<PointRelation> pointRelationList = pointRelationMapper.selectList(new QueryWrapper<PointRelation>().eq("course_id", courseId).eq("relation", 0));

        //处理章节包含关系(-1)
        //章节查询 "555"
        List<Point> pointsChapter = pointList.stream().filter(item -> item.getPointPid().equals("555")).collect(Collectors.toList());
        //章节儿子
        for (Point pointFir : pointsChapter) {
            //每个章节的儿子,处理 pointsSection
            List<Point> pointsSection = pointList.stream().filter(item -> item.getPointPid().equals(pointFir.getPointId())).collect(Collectors.toList());
            for (Point pointSec : pointsSection) {
                relationVoList.add(new PointRelationVo(pointFir.getPointName(), pointSec.getPointName(), -1));
                pointSon.add(pointSec);
            }
        }
        //处理前后序
        for (PointRelation pointFif : pointRelationList) {
            QueryWrapper<Point> queryWrapperAId = new QueryWrapper<Point>().eq("id", pointFif.getPointAId()).eq("course_id", courseId);
            Point pointAId = pointMapper.selectOne(queryWrapperAId);
            QueryWrapper<Point> queryWrapperBId = new QueryWrapper<Point>().eq("id", pointFif.getPointBId()).eq("course_id", courseId);
            Point pointBId = pointMapper.selectOne(queryWrapperBId);
            relationVoList.add(new PointRelationVo(pointAId.getPointName(), pointBId.getPointName(), 0));
        }
        //处理知识点包含
        for (Point pointThi : pointSon) {
            List<Point> pointsPart = pointList.stream().filter(item -> item.getPointPid().equals(pointThi.getPointId())).collect(Collectors.toList());
            for (Point pointFou : pointsPart) {
                relationVoList.add(new PointRelationVo(pointThi.getPointName(), pointFou.getPointName(), 1));
            }
        }

        String fileName = System.currentTimeMillis() + ".xlsx";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        EasyExcel.write(byteArrayOutputStream, PointRelationVo.class)
                .sheet("sheet")
                .doWrite(relationVoList);

        MultipartFile file = new MockMultipartFile("file", fileName, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", byteArrayOutputStream.toByteArray());

        ResponseResult responseResult = uploadResources(file, courseId, null);
        ResourceOneVo data = (ResourceOneVo) responseResult.getData();
        String link = data.getResourceLink();

        return new ResponseResult(200, "成功", link);
    }
}
