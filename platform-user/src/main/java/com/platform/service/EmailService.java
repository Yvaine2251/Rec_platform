package com.platform.service;

import com.platform.entity.ResponseResult;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author ErrorRua
 * @date 2022/11/18
 * @description:
 */
public interface EmailService {

    /**
     * @param email:
     * @return com.platform.entity.ResponseResult
     * @description: 发送验证码
     * @author ErrorRua
     * @date 2022/11/18
     */
    ResponseResult sendVerifiedCode(String email);

    /**
     * @param email:
     * @param subject:
     * @param text:
     * @return void
     * @description: 发送邮件
     * @author ErrorRua
     * @date 2022/11/18
     */
    void sendEmail(String email, String subject, String text);

    /**
     * @description: 通过邮箱登陆
     * @param email:
     * @return org.springframework.security.core.userdetails.UserDetails
     * @author ErrorRua
     * @date 2022/11/24
     */
    UserDetails loadUserByEmail(String email);
}