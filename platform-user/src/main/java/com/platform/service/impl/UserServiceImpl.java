package com.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.platform.dto.ForgetPasswordDTO;
import com.platform.entity.ResponseResult;
import com.platform.dto.UserRegistryDto;
import com.platform.dto.UserUpdateDto;
import com.platform.entity.User;
import com.platform.exception.PlatformException;
import com.platform.mapper.UserMapper;
import com.platform.service.EmailService;
import com.platform.service.UserService;
import com.platform.util.SecurityUtils;
import com.platform.utils.BeanCopyUtils;
import com.platform.utils.RedisCache;
import com.platform.vo.UserInfoVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.util.Objects;

import static com.platform.enums.UserCodeEnum.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisCache redisCache;



    /**
     * 处理用户注册
     *
     * @param userRegistryDto
     * @return
     */

    // TODO 注册成功自动登录返回token
    @Override
    @Transactional
    public ResponseResult registry(UserRegistryDto userRegistryDto) {
        User user = new User();
        // 姓名，邮箱和手机都不能重复
        // TODO 我猜这里高并发会爆炸
        if (userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getName, userRegistryDto.getName())) != null) {
            throw new PlatformException(USERNAME_EXIST.getCode(), USERNAME_EXIST.getMessage());
        }
        if (userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail, userRegistryDto.getEmail())) != null) {
            throw new PlatformException(EMAIL_EXIST.getCode(), EMAIL_EXIST.getMessage());
        }
        if (userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getMobile, userRegistryDto.getMobile())) != null) {
            throw new PlatformException(PHONE_EXIST.getCode(), PHONE_EXIST.getMessage());
        }

        // 2.校验邮箱验证码
        checkEmailCode(userRegistryDto.getEmail(), userRegistryDto.getEmailVerifyCode());

        //3.将用户存入数据库
        BeanUtils.copyProperties(userRegistryDto, user);
        user.setPassword(bCryptPasswordEncoder.encode(userRegistryDto.getPassword()));
        int result = userMapper.insert(user);
        if (result == 0) {
            throw new PlatformException(REGISTER_ERROR.getCode(), REGISTER_ERROR.getMessage());
        }
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult updateUserInfo(UserUpdateDto userUpdateDto) {
        String userId = SecurityUtils.getUserId();
        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(User::getUserId, userId);
        User user = userMapper.selectOne(userWrapper);
        BeanUtils.copyProperties(userUpdateDto, user);
        int i = userMapper.updateById(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getUserInfo() {
        String userId = SecurityUtils.getUserId();
        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(User::getUserId, userId);
        User user = userMapper.selectOne(userWrapper);
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult forgetPassword(ForgetPasswordDTO forgetPasswordDTO) {
        // 1.校验邮箱验证码
        checkEmailCode(forgetPasswordDTO.getEmail(), forgetPasswordDTO.getVerifyCode());

        // 2.根据邮箱查询用户
        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(User::getEmail, forgetPasswordDTO.getEmail());
        User user = userMapper.selectOne(userWrapper);
        if (user == null) {
            throw new PlatformException(USER_NOT_EXIST.getCode(), USER_NOT_EXIST.getMessage());
        }
        // 3.修改密码
        user.setPassword(bCryptPasswordEncoder.encode(forgetPasswordDTO.getNewPassword()));
        int i = userMapper.updateById(user);
        if (i == 0) {
            throw new PlatformException(SET_PASSWORD_ERROR.getCode(), SET_PASSWORD_ERROR.getMessage());
        }
        return ResponseResult.okResult();
    }

    /**
     * 校验邮箱验证码
     *
     * @param email
     * @param code
     * @return
     */
    private void checkEmailCode(String email, String code) {
        String emailCode = redisCache.getCacheObject(email);
        if (Objects.isNull(emailCode)) {
            throw new PlatformException(EMAIL_VERIFY_CODE_EXPIRED.getCode(), EMAIL_VERIFY_CODE_EXPIRED.getMessage());
        }
        if (!emailCode.equalsIgnoreCase(code)) {
            throw new PlatformException(EMAIL_VERIFY_CODE_ERROR.getCode(), EMAIL_VERIFY_CODE_ERROR.getMessage());
        }
        redisCache.deleteObject(email);
    }
}
