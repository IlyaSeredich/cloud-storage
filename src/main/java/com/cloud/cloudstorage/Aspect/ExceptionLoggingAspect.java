package com.cloud.cloudstorage.Aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class ExceptionLoggingAspect {

    @Pointcut("execution(public * com.cloud.cloudstorage.*.*.*(..))")
    public void businessLogicMethods() {}

    @AfterThrowing(throwing = "ex", pointcut = "businessLogicMethods()")
    public void logException(JoinPoint jp, Exception ex) {
        String methodName = jp.getSignature().getName();
        String className = jp.getTarget().getClass().getSimpleName();

        log.error("Exception in {}.{}. Exception message: {}",
                className,
                methodName,
                ex.getMessage());
    }
}
