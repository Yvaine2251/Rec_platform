package com.platform.resources.resourceUtils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoRequest;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoResponse;
import com.aliyuncs.vod.model.v20170321.RefreshUploadVideoRequest;
import com.aliyuncs.vod.model.v20170321.RefreshUploadVideoResponse;

/**
 * @author ErrorRua
 * @date 2022/11/22
 * @description:
 */
public class AudioOrVideoCreateUploadUtil {

    public static CreateUploadVideoResponse createUploadVideo(String title, String fileName) throws Exception {

        DefaultAcsClient client = InitVodClient.initVodClient(OssProperties.ACCESS_KEY_ID, OssProperties.ACCESS_KEY_SECRET);

        CreateUploadVideoRequest request = new CreateUploadVideoRequest();
        request.setTitle(title);
        request.setFileName(fileName);

        return client.getAcsResponse(request);
    }

    public static RefreshUploadVideoResponse refreshUploadVideo(String videoId) throws Exception {
        DefaultAcsClient client = InitVodClient.initVodClient(OssProperties.ACCESS_KEY_ID, OssProperties.ACCESS_KEY_SECRET);
        RefreshUploadVideoRequest request = new RefreshUploadVideoRequest();
        //音频或视频ID
        request.setVideoId(videoId);
        return client.getAcsResponse(request);
    }
}
