package com.platform.filter;

import com.platform.wrapper.RequestWrapper;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author ErrorRua
 * @date 2022/11/18
 * @description:
 */

@Component
public class JsonFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if("application/json".equals((httpServletRequest).getHeader("Content-Type"))) {
            RequestWrapper requestWrapper = new RequestWrapper(httpServletRequest);
            filterChain.doFilter(requestWrapper, httpServletResponse);
        } else {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }
}
