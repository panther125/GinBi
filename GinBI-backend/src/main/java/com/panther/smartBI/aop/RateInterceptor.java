package com.panther.smartBI.aop;

import com.panther.smartBI.annotation.RateCount;
import com.panther.smartBI.common.ErrorCode;
import com.panther.smartBI.exception.BusinessException;
import com.panther.smartBI.manager.RedissonLimiterManager;
import com.panther.smartBI.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Gin 琴酒
 * @data 2023/8/4 16:10
 */
@Aspect
@Component
@Slf4j
public class RateInterceptor {

    @Resource
    private RedissonLimiterManager redissonLimiterManager;

    @Resource
    private UserService userService;

    /**
     * 优雅的限流
     *
     * @param joinPoint 切入点
     * @param rateCount 限制次数
     * @return
     */
    @Around("@annotation(rateCount)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, RateCount rateCount) throws Throwable {
        String s = rateCount.count();

        // 获取原生的 HttpServletRequest
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String uid = userService.getLoginUser(request).getId().toString();
        boolean flag = redissonLimiterManager.doRateLimiter(uid,Long.parseLong(s));
        log.info(uid);
        if(!flag){
            throw new BusinessException(ErrorCode.VERY_MANY_REQUEST,"请求过于频繁");
        }
        return joinPoint.proceed();
    }
}
