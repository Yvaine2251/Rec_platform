package com.platform.statistics.enums;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platform.constants.BaseCode;
import lombok.Data;

/**
 * @author Yvaine
 * @date
 * @description
 */
public enum StatisticsEnum implements BaseCode {

    ExportFail(501,"导出失败"),


    NullUserPoints(511,"查询不到知识点")

    ;

    StatisticsEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private Integer code;
    private String message;

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
