package com.platform.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.platform.entity.ResponseResult;
import com.platform.exception.PlatformException;
import com.platform.utils.RedisCache;
import com.platform.utils.WebUtils;
import com.platform.wrapper.RequestWrapper;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static com.platform.enums.UserCodeEnum.VERIFY_CODE_ERROR;
import static com.platform.enums.UserCodeEnum.VERIFY_CODE_EXPIRED;

/**
 * @author ErrorRua
 * @date 2022/11/17
 * @description:
 */
@Component
public class VerifyFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        if (request.getRequestURI().equals("/user/login")
                && request.getMethod().equalsIgnoreCase("post")
            || request.getRequestURI().equals("/user/register")
                && request.getMethod().equalsIgnoreCase("post")
                || request.getRequestURI().equals("/user/login-email")
                && request.getMethod().equalsIgnoreCase("post") ) {


            if (request instanceof RequestWrapper) {
                System.out.println(123123);
            }
            ServletInputStream inputStream = request.getInputStream();
            String s = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

            JSONObject jsonObject = JSON.parseObject(s);
            String verifyCode = jsonObject.getString("verifyCode");
            String verifyKey = jsonObject.getString("verifyKey");


            Object validateCode = redisCache.getCacheObject(verifyKey);

            if (Objects.isNull(validateCode)) {
                ResponseResult result = new ResponseResult(VERIFY_CODE_EXPIRED.getCode(), VERIFY_CODE_EXPIRED.getMessage());
                WebUtils.renderString(response, JSON.toJSONString(result));
                return;
            }

            if (!validateCode.toString().equalsIgnoreCase(verifyCode)) {
                ResponseResult result = new ResponseResult(VERIFY_CODE_ERROR.getCode(), VERIFY_CODE_ERROR.getMessage());
                WebUtils.renderString(response, JSON.toJSONString(result));
                return;
            }

            redisCache.deleteObject(verifyKey);
        }


        filterChain.doFilter(request,response);
    }
}
