package com.platform.exception;

import com.platform.constants.SystemConstant;
import com.platform.entity.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

import static com.platform.constants.SystemConstant.*;
/**
 * @author yjj
 * @date 2022/7/17-9:19
 * 全局异常处理类
 */
//@ResponseBody
//@ControllerAdvice
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 处理密码错误
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseResult badCredentialsException(BadCredentialsException e) {
        return ResponseResult.errorResult(HttpStatus.FORBIDDEN.value(), "用户名或密码错误");
    }

    //处理自定义异常
    @ExceptionHandler(PlatformException.class)
    public ResponseResult exception(PlatformException e) {
        log.error("出现了异常！{}", e.getMsg());
        return ResponseResult.errorResult(e.getCode(), e.getMsg());
    }

    @ExceptionHandler(Exception.class)
    public ResponseResult exception(Exception e) {
        log.error("异常：{}", e.getMessage());
        return ResponseResult.errorResult(GLOBAL_EXCEPTION, e.getMessage());
    }

    /**
     * @param e
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseResult handleValidException(MethodArgumentNotValidException e) {
        // 从异常对象中拿到ObjectError对象
        ObjectError objectError = e.getBindingResult().getAllErrors().get(0);
        // 然后提取错误提示信息进行返回
        return ResponseResult.errorResult(PARAMETER_VALIDATION, objectError.getDefaultMessage());
    }

    /**
     * 参数为单个参数或多个参数
     * @param e
     * @return
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseResult handleConstraintViolationException(ConstraintViolationException e) {
        // 从异常对象中拿到ObjectError对象
        String s = e.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList()).get(0);
        return ResponseResult.errorResult(PARAMETER_VALIDATION, s);
    }

//    @ExceptionHandler(ArithmeticException.class)
//    public ResponseResult exception(ArithmeticException e) {
//        e.printStackTrace();
//        return ResponseResult.errorResult(GLOBAL_EXCEPTION, "出现了ArithmeticException异常");
//    }
}
