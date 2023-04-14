package com.platform.enums;

import com.platform.constants.BaseCode;
import lombok.Getter;

/**
 * @author yjj
 * @date 2022/7/13-17:10
 */
@Getter
public enum UserCodeEnum implements BaseCode {

    REGISTRY_SUCCESS(200, "注册成功"),
    LOGIN_SUCCESS(200, "登录成功"),
    SUCCESS(200, "操作成功"),

    REQUIRE_USERNAME(501, "请输入用户名"),
    USERNAME_EXIST(502, "用户名已存在"),
    LOGIN_ERROR(503, "登录失败"),
    REGISTER_ERROR(504, "注册失败"),

    // 验证码过期
    VERIFY_CODE_EXPIRED(505, "验证码已过期"),
    // 验证码错误
    VERIFY_CODE_ERROR(506, "验证码错误"),
    // 邮箱验证码已过期
    EMAIL_VERIFY_CODE_EXPIRED(507, "邮箱验证码已过期"),
    // 邮箱验证码错误
    EMAIL_VERIFY_CODE_ERROR(507, "邮箱验证码错误"),

    EMAIL_SEND_ERROR(510, "邮件发送失败"),
    USER_NOT_EXIST(511, "用户不存在"),
    // 邮箱已注册
    EMAIL_EXIST(512, "邮箱已注册"),
    // 手机号已注册
    PHONE_EXIST(513, "手机号已注册"),
    SET_PASSWORD_ERROR(514, "设置密码失败"),
    // 请求过于频繁
    REQUEST_FREQUENTLY(515, "请求过于频繁"),
    ;

    private Integer code;
    private String message;

    UserCodeEnum(Integer code, String message) {
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
};
