package com.platform.points.enums;

import com.platform.constants.BaseCode;

public enum PointCodeEnum implements BaseCode {

    //相当于 public static final PointCodeEnum PxxSxx = new PointCodeEnum(200,"操作成功）
    POINT_SUCCESS(200,"操作成功"),

    POINT_REPEAT(506, "修改失败，知识点名称已存在"),

    POINT_NOT_EXIST(507, "知识点不存在"),

    POINT_DIY_FAIL(505,"操作失败");

    private Integer code;
    private String message;

    PointCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }


}
