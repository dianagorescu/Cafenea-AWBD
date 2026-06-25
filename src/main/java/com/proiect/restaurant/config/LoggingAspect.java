package com.proiect.restaurant.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("within(com.proiect.restaurant.service..*)")
    public void servicePackage() {}

    @Around("servicePackage()")
    public Object logAroundServices(ProceedingJoinPoint joinPoint) throws Throwable {
        String signature = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();
        logger.debug("Entering {} with args={}", signature, args);
        try {
            Object result = joinPoint.proceed();
            logger.debug("Exiting {} with result={}", signature, result);
            return result;
        } catch (Throwable ex) {
            logger.error("Exception in {} with message={}", signature, ex.getMessage(), ex);
            throw ex;
        }
    }
}
