package com.platform.statistics.dto;

import lombok.Data;
import org.springframework.lang.Nullable;

/**
 * @author Yvaine
 * @version 1.0
 * @description EXCEL导出数据DTO
 * 导出考试数据和导出学习情况共用
 */
@Data
public class ExportDataDTO {

    //试卷id
    private String paperId;

    //用户选择的存储路径
    private String path;

    //班级id，如果有就是教师端导出学生学习情况
    @Nullable
    private String classId;

}
