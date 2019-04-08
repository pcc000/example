package com.tiefan.cps.base.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;

/**
 * @author chenyuejin
 *         create 2017-06-17 13:00
 */
public class LogAspect {

    @Around("within(com.tiefan.cps..*) && @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object rst;
        ThreadContext.refreshUUID();
        try {
            rst = pjp.proceed();
        } finally {
            ThreadContext.empty();
        }
        return rst;
    }
}
