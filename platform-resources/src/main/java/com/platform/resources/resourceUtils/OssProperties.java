package com.platform.resources.resourceUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author yjj
 * @date 2022/7/18-9:11
 */
//当项目一启动，spring接口，spring加载之后，执行接口一个方法
@Component
public class OssProperties {
    public static String END_POINT;
    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;
    public static String BUCKET_NAME;

    public static String TMP_DIR;


    @Value("${aliyun.oss.file.endpoint}")
    public void setEndPoint(String endPoint) {
        END_POINT = endPoint;
    }

    @Value("${aliyun.oss.file.keyId}")
    public void setAccessKeyId(String accessKeyId) {
        ACCESS_KEY_ID = accessKeyId;
    }

    @Value("${aliyun.oss.file.keySecret}")
    public void setAccessKeySecret(String accessKeySecret) {
        ACCESS_KEY_SECRET = accessKeySecret;
    }

    @Value("${aliyun.oss.file.bucketName}")
    public void setBucketName(String bucketName) {
        BUCKET_NAME = bucketName;
    }

}
