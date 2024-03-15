package io.spring.lbs.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ControllerAspect {
    long startTime;
    long endTime;
    long timeDiff;
    double transactionTime;

    @Before("@annotation(io.spring.lbs.aop.ControllerTrace)")
    public void logBeforeController(JoinPoint joinPoint) {
        startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().getName();

        log.info("----------------------------------------");
        log.info("Controller {} START", methodName);
    }

    @After("@annotation(io.spring.lbs.aop.ControllerTrace)")
    public void logAfterController(JoinPoint joinPoint) {
        endTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().getName();
        timeDiff = (endTime - startTime);
        transactionTime = timeDiff / 1000.0;

        log.info("Controller {} END", methodName);
        log.info("===== TRX TIME = {}s  =====", transactionTime);
        log.info("----------------------------------------");
        log.info("");
    }

}
