package com.platform.vo;

import lombok.Data;

import java.util.List;

@Data
public class UserInfoVo {

    private String userId;

    /**
     * 姓名
     */
    private String name;

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
     * 手机号
     */
    private String mobile;

    /**
     * 个性签名
     */
    private String personalSignature;

    /**
     * 头像路径地址
     */
    private String headPortrait;
}
