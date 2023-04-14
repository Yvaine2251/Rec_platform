package com.platform.statistics.dto;

import lombok.Data;

/**
 * @author Yvaine
 * @version 1.0
 * @description EXCEL导出数据DTO
 */
@Data
public class ExportExamDataDTO {

    //试卷id
    private String paperId;

    //用户选择的存储路径
    private String path;
}
