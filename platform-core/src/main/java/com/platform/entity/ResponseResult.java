package com.platform.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.platform.enums.HttpCodeEnum;

/**
 * 封装返回结果
 *
 * @param <T>
 */

@JsonInclude(JsonInclude.Include.NON_NULL)  //jackSon 中注解，字段为 null 时不返回
public class ResponseResult<T> {
    /**
     * 状态码
     */
    private Integer code;
    /**
     * 提示信息，如果有错误时，前端可以获取该字段进行提示
     */
    private String msg;
    /**
     * 查询到的结果数据
     */
    private T data;

    public ResponseResult() {
        this.code = HttpCodeEnum.SUCCESS.getCode();
        this.msg = HttpCodeEnum.SUCCESS.getMsg();
    }

    //不需要
    public ResponseResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

//    public ResponseResult(Integer code, T data) {
//        this.code = code;
//        this.data = data;
//    }

    public ResponseResult(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static ResponseResult errorResult(int code, String msg) {
        ResponseResult result = new ResponseResult();
        return result.error(code, msg);
    }

//    public static ResponseResult errorResult(HttpCodeEnum enums, String msg){
//        return setHttpCodeEnum(enums,msg);
//    }

    public ResponseResult<?> error(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
        return this;
    }

    public static ResponseResult okResult() {
        ResponseResult result = new ResponseResult();
        return result;
    }

    //注释
    public static ResponseResult okResult(int code, String msg) {
        ResponseResult result = new ResponseResult();
        return result.ok(code, null, msg);
    }

    public static ResponseResult okResult(Object data) {
        ResponseResult result = okResult();
//        ResponseResult result = setHttpCodeEnum(HttpCodeEnum.SUCCESS, HttpCodeEnum.SUCCESS.getMsg());
        if (data != null) {
            result.setData(data);
        }
        return result;
    }

//    public static ResponseResult setHttpCodeEnum(HttpCodeEnum enums) {
//        return okResult(enums.getCode(), enums.getMsg());
//    }

//    private static ResponseResult setHttpCodeEnum(HttpCodeEnum enums, String msg) {
//        return okResult(enums.getCode(), msg);
//    }

//    public ResponseResult<?> ok(Integer code, T data) {
//        this.code = code;
//        this.data = data;
//        return this;
//    }

    public ResponseResult<?> ok(Integer code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        return this;
    }

    public ResponseResult<?> ok(T data) {
        this.data = data;
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
