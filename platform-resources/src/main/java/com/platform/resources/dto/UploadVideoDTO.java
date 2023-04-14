package com.platform.resources.dto;

import lombok.Data;

import java.util.List;

/**
 * @author ErrorRua
 * @date 2022/11/23
 * @description:
 */
@Data
public class UploadVideoDTO {
    private String videoId;
    private String courseId;
    private List<String> relatedPoints;
}
