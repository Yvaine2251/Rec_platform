package com.platform.dto;

import lombok.Data;

/**
 * @author ErrorRua
 * @date 2022/11/18
 * @description:
 */
@Data
public class ForgetPasswordDTO {
    /**
     * 邮箱
     */
    private String email;
    /**
     * 验证码
     */
    private String verifyCode;
    /**
     * 新密码
     */
    private String newPassword;
}
