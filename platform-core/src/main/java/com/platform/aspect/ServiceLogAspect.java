package com.platform.aspect;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;

/**
 * @author ErrorRua
 * @date 2022/11/23
 * @description:
 */
@Component
@Aspect
@Slf4j
public class ServiceLogAspect {
//    @Pointcut("execution(* com.platform.*.service..*.*(..))")
    public void pt() {

    }

//    @Around("pt()")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Object ret = null;
        try {
            log.info("========start========" + System.lineSeparator());
            log.info("Class Method  :{}. {}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName());
            System.out.println(Arrays.toString(joinPoint.getArgs()));
//            log.info("Request Args  :{}", JSON.toJSON(joinPoint.getArgs()));

            // log当前时间
            log.info("Time          :{}", new Date());
            ret = joinPoint.proceed();
        } finally {
            //系统换行符
            log.info("Response      :{}", JSON.toJSON(ret));
            log.info("Time          :{}", new Date());
            log.info("=======end=======" + System.lineSeparator());
        }
        return ret;
    }
}
