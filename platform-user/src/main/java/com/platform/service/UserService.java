package com.platform.service;

import com.platform.dto.ForgetPasswordDTO;
import com.platform.entity.ResponseResult;
import com.platform.dto.UserRegistryDto;
import com.platform.dto.UserUpdateDto;

public interface UserService {

    // 用户注册
    ResponseResult registry(UserRegistryDto userRegistryDto);

    // 更新个人信息
    ResponseResult updateUserInfo(UserUpdateDto userUpdateDto);

    // 查询用户信息
    ResponseResult getUserInfo();

    /**
     * @description: 忘记密码
     * @param forgetPasswordDTO:
     * @return com.platform.entity.ResponseResult
     * @author ErrorRua
     * @date 2022/11/18
     */
    ResponseResult forgetPassword(ForgetPasswordDTO forgetPasswordDTO);


}
