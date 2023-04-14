package com.platform.enums;


/**
 * @author yjj
 * @date 2022/9/11-19:11
 */
public enum HttpCodeEnum {
    SUCCESS(200, "操作成功");
    int code;
    String msg;
    HttpCodeEnum(int code, String message) {
        this.code = code;
        this.msg = message;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
