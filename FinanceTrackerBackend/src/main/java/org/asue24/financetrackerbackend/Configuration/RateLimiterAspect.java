package org.asue24.financetrackerbackend.Configuration;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Aspect
public class RateLimiterAspect {
    private final RedisConfiguration redisConfiguration;

    @Autowired
    public RateLimiterAspect(RedisConfiguration redisConfiguration) {
        this.redisConfiguration = redisConfiguration;
    }
    @Around("@annotation(org.asue24.financetrackerbackend.annotations.Ratelimited)")
    public Object rateLimited(ProceedingJoinPoint joinPoint) throws Throwable {
        var request=((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes()).getRequest();
        var CLientkey=request.
    }
}
