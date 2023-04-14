package com.platform.controller;

import com.platform.dto.*;
import com.platform.entity.ResponseResult;
import com.platform.exception.PlatformException;
import com.platform.service.EmailService;
import com.platform.service.LoginService;
import com.platform.service.UserService;
import com.platform.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.platform.enums.UserCodeEnum.*;


@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RedisCache redisCache;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody UserLoginDto userLoginDto) {
        if (!StringUtils.hasText(userLoginDto.getName())) {
            //必须要传用户名
            throw new PlatformException(REQUIRE_USERNAME.getCode(), REQUIRE_USERNAME.getMessage());
        }
        return loginService.login(userLoginDto);
    }

    @PostMapping("/login-email")
    public ResponseResult loginByEmail(@RequestBody UserEmailLoginDTO userLoginDto) {
        return loginService.loginByEmail(userLoginDto);
    }

    @PostMapping("/registry")
    public ResponseResult registry(@RequestBody @Valid UserRegistryDto userRegistryDto) {
        return userService.registry(userRegistryDto);
    }

    @PostMapping("/logout")
    public ResponseResult logout() {
        return loginService.logout();
    }

    @PutMapping("/update")
    public ResponseResult updateUserInfo(@RequestBody UserUpdateDto userUpdateDto) throws Exception {
        return userService.updateUserInfo(userUpdateDto);
    }

    @GetMapping("/show-detail")
    public ResponseResult getUserInfo() throws Exception {
        // String userId = JwtUtil.parseJWT(request.getHeader("token")).getSubject();
        return userService.getUserInfo();
    }

    // 获取验证码
    @GetMapping("/get-code")
    public ResponseResult getCode() {
        return loginService.getVerifyCode();
    }

    // 获取邮箱验证码
    @GetMapping("/get-email-code")
    public ResponseResult getEmailCode(@RequestParam String email) {
        return emailService.sendVerifiedCode(email);
    }

    // 忘记密码
    @PostMapping("/forget-password")
    public ResponseResult forgetPassword(@RequestBody ForgetPasswordDTO forgetPasswordDTO) {
        return userService.forgetPassword(forgetPasswordDTO);
    }
}
