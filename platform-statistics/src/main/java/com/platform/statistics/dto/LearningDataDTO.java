package com.platform.statistics.dto;

import lombok.Data;
import org.springframework.lang.Nullable;

/**
 * @author Yvaine
 * @version 1.0
 */
@Data
public class LearningDataDTO {
    //课程id
    private String courseId;

    //用户选择的存储路径
    private String path;

    //班级id
    @Nullable
    private String classId;
}
