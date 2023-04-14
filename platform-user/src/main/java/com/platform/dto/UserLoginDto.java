package com.platform.dto;

import lombok.Data;

/**
 * @author yjj
 * @date 2022/7/21-21:18
 */
@Data
public class UserLoginDto {

    /**
     * 姓名
     */
    private String name;

    /**
     * 密码
     */
    private String password;
}
