package com.platform.service.impl;

import com.platform.dto.UserEmailLoginDTO;
import com.platform.entity.ResponseResult;
import com.platform.dto.UserLoginDto;
import com.platform.entity.LoginUser;
import com.platform.exception.PlatformException;
import com.platform.security.EmailCodeAuthenticationToken;
import com.platform.service.LoginService;
import com.platform.util.SecurityUtils;
import com.platform.utils.JwtUtil;
import com.platform.utils.RedisCache;
import com.platform.vo.VerifyCodeVO;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 验证登录
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(UserLoginDto user) {
        //AuthenticationManager authenticate进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getName(), user.getPassword());

        //authenticate最终会调用UserDetailService中的方法来验证用户校验
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        //如果认证没通过，给出对应的提示
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("用户名或密码错误");
        }

        //如果认证通过了，使用userId生成一个jwt  jwt存入ResponseResult返回
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();

        //获取用户id
        String userId = loginUser.getUser().getUserId();

        //将用户id转换为token作为用户信息
        String jwt = JwtUtil.createJWT(userId);
        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);
        //把完整的用户信息存入redis  userId作为key
        redisCache.setCacheObject("login:" + userId, loginUser);
        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult loginByEmail(UserEmailLoginDTO userEmailLoginDTO) {
        EmailCodeAuthenticationToken authenticationToken =
                new EmailCodeAuthenticationToken(userEmailLoginDTO.getEmail(), userEmailLoginDTO.getPassword());

        //authenticate最终会调用UserDetailService中的方法来验证用户校验
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        //如果认证没通过，给出对应的提示
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("用户名或密码错误");
        }

        //如果认证通过了，使用userId生成一个jwt  jwt存入ResponseResult返回
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();

        //获取用户id
        String userId = loginUser.getUser().getUserId();

        //将用户id转换为token作为用户信息
        String jwt = JwtUtil.createJWT(userId);
        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);
        //把完整的用户信息存入redis  userId作为key
        redisCache.setCacheObject("login:" + userId, loginUser);
        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult logout() {

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
//        String userId = loginUser.getUser().getUserId();

        //获取SecurityContextHolder中的用户id
        String userId = SecurityUtils.getUserId();
        //删除redis中的值
        redisCache.deleteObject("login:" + userId);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getVerifyCode() {
        Captcha captcha = new SpecCaptcha(130, 48, 4);
        // 设置字体，有默认字体，可以不用设置
        try {
            captcha.setFont(Captcha.FONT_2);
        } catch (IOException | FontFormatException e) {
            throw new PlatformException(500, "验证码生成失败");
        }
        // 设置类型，纯数字、纯字母、字母数字混合
        captcha.setCharType(Captcha.TYPE_DEFAULT);

        // 将uuid作为redis的key
        String key = UUID.randomUUID().toString();
        String code = captcha.text().toLowerCase();

        // 将验证码存入redis
        redisCache.setCacheObject(key, code, 5, TimeUnit.MINUTES);

        VerifyCodeVO verifyCodeVO = new VerifyCodeVO();
        verifyCodeVO.setVerifyKey(key);
        verifyCodeVO.setCodeImg(captcha.toBase64());

        return ResponseResult.okResult(verifyCodeVO);
    }
}
