package com.platform.course.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author yjj
 * @date 2022/7/19-9:21
 */
@Data
public class ClassTimeOneVO {
    //添加的课时名称
    private String name;

    //添加课时所属的章节
    private String chapterId;

    //添加课时时添加的知识点
    private List<String> pointIds;

    //上传资源所获取的链接
    private String resourceLink;
}
