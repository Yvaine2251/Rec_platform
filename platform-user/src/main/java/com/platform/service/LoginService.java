package com.platform.service;

import com.platform.dto.UserEmailLoginDTO;
import com.platform.entity.ResponseResult;
import com.platform.dto.UserLoginDto;

public interface LoginService {

    ResponseResult login(UserLoginDto userLoginDto);

    ResponseResult loginByEmail(UserEmailLoginDTO userEmailLoginDTO);

    ResponseResult logout();

    /**
     * @description: 获取验证码
     * @return
     * @author ErrorRua
     * @date 2022/11/17
     */
    ResponseResult getVerifyCode();
}
