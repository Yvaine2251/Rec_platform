package com.platform.resolver;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;

/**
 * @author ErrorRua
 * @date 2022/11/06
 * @description:
 */
@Slf4j
@Component
public class PostRequestParamResolver implements HandlerMethodArgumentResolver {
    private static final String REQUEST_POST = "post";
    private static final String REQUEST_CONTENT = "application/json";
    private final static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    /**
     * @param parameter the method parameter to check
     * @return {@code true} if this resolver supports the supplied parameter;{@code false} otherwise;
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(PostRequestParam.class);
    }

    /**
     * @param parameter the method parameter to resolve. This parameter must
     * have previously been passed to {@link #supportsParameter} which must
     * have returned {@code true}.
     * @param mavContainer the ModelAndViewContainer for the current request
     * @param webRequest the current request
     * @param binderFactory a factory for creating {@link WebDataBinder} instances
     * @return object
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, @NotNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        String contentType = Objects.requireNonNull(servletRequest).getContentType();

        if (contentType == null || !contentType.contains(REQUEST_CONTENT)) {
            log.error("PostRequestParam' contentType must be[{}]", REQUEST_CONTENT);
            throw new RuntimeException("PostRequestParam' contentType must be application/json");
        }

        if (!REQUEST_POST.equalsIgnoreCase(servletRequest.getMethod())) {
            log.error("PostRequestParam' request type must be post");
            throw new RuntimeException("PostRequestParam' request type must be post ");
        }
        return this.bindRequestParams(parameter, servletRequest);
    }

    /**
     * Annotates that the parameters of {@code #PostRequestParam} are bound with the request.
     * @param parameter
     * @param servletRequest
     * @return object
     */
    private Object bindRequestParams(MethodParameter parameter, HttpServletRequest servletRequest) throws IOException {
        String requestBody = this.assembleRequestBody(servletRequest);
        JSONObject jsonObj =  JSONObject.parseObject(requestBody);
        if (jsonObj == null){
            throw new RuntimeException("Request body has no parameters.");
        }

        PostRequestParam postRequestParam = parameter.getParameterAnnotation(PostRequestParam.class);
        Class<?> parameterType = parameter.getParameterType();
        String parameterName = StringUtils.isBlank(postRequestParam.value())? parameter.getParameterName() : postRequestParam.value();
        Object value = jsonObj.get(parameterName);
        if (postRequestParam.required() && value == null) {
            log.error("PostRequestParam' require is true,[{}] must not be null!", parameterName);
            throw new RuntimeException("PostRequestParam' require is true,[{".concat(parameterName).concat("}] must not be null!"));
        }else{
            return ConvertUtils.convert(value,parameterType);
        }
    }

    /**
     * Assemble body from request.
     * @param request
     * @return String
     * @throws IOException
     */
    private String assembleRequestBody(HttpServletRequest request) throws IOException {
        String requestBody = new String(StreamUtils.copyToByteArray(request.getInputStream()),request.getCharacterEncoding());
        if(StringUtils.isNotBlank(requestBody)){
            threadLocal.set(requestBody);
        }else{
            requestBody = threadLocal.get();
        }
        return requestBody;
    }
}