package com.platform.filter;


import com.alibaba.fastjson.JSON;
import com.platform.entity.ResponseResult;
import com.platform.entity.LoginUser;
import com.platform.utils.JwtUtil;
import com.platform.utils.RedisCache;
import com.platform.utils.WebUtils;
import io.jsonwebtoken.Claims;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.Stream;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    /* 对于过滤器的实现，继承OncePerRequestFilter而不是实现Filter接口的原因是：
    默认的过滤器接口可能会出现问题，一个请求通过会被调用多次（可能是多线程的原因）
    OncePerRequestFilter是spring提供的过滤器父类 */
    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String token = request.getHeader("token");
        //如果没有token，直接放行
        if (!StringUtils.hasText(token)) {
            //放行
            filterChain.doFilter(request, response);
            //这里return的原因是不希望请求后面回来再走一遍，为空的话不需要执行后面的语句了
            return;
        }

        boolean match = Stream.of("/user/login", "/user/get-code", "/user/registry", "/resources/upload",
                        "/resources/downloadResource")
                .anyMatch(url -> request.getRequestURI().contains(url));

        if (match) {
            filterChain.doFilter(request, response);
            return;
        }

        //解析token
        String userId;
        try {
            //解析token
            System.out.println(token);
            Claims claims = JwtUtil.parseJWT(token);
            //获取解析token所得到的userId
            userId = claims.getSubject();
            request.setAttribute("userId", userId);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseResult result = new ResponseResult(500, "登录错误");
            //将结果返回前端
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
            // throw new RuntimeException("token非法"); 不能直接抛，统一异常捕捉不到
        }
        //从redis中获取用户信息（根据userId在redis中获取用户信息）
        String redisKey = "login:" + userId;
        LoginUser loginUser = redisCache.getCacheObject(redisKey);
        if (Objects.isNull(loginUser)) {
            ResponseResult result = new ResponseResult(500, "用户未登录");
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }
        //存入SecurityContextHolder
        //TODO 获取权限信息封装到Authentication
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }
}
