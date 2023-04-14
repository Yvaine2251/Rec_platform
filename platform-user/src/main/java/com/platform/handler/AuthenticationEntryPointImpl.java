package com.platform.handler;

import com.alibaba.fastjson.JSON;
import com.platform.entity.ResponseResult;
import com.platform.utils.WebUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		ResponseResult result = new ResponseResult(403, "认证失败，请重新登录");
		//将结果返回前端
		WebUtils.renderString(response, JSON.toJSONString(result));
	}
}
