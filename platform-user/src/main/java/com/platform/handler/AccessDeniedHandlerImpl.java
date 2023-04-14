package com.platform.handler;


import com.alibaba.fastjson.JSON;
import com.platform.entity.ResponseResult;
import com.platform.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

		ResponseResult result = new ResponseResult(403, "访问被拒绝");
		//将结果返回前端
		WebUtils.renderString(response, JSON.toJSONString(result));
	}
}
