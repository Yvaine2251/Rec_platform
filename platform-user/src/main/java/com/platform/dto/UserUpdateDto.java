package com.platform.dto;

import lombok.Data;

/**
 * @author yjj
 * @date 2022/7/21-21:20
 */
@Data
public class UserUpdateDto {

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 学校
     */
    private String school;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 个性签名
     */
    private String personalSignature;
}
