package com.panther.smartBI.manager;

import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 提供 redis limiter 的基础服务
 *
 * @author Gin 琴酒
 * @data 2023/8/1 17:29
 */
@Service
public class RedissonLimiterManager {

    @Resource
    private RedissonClient redissonClient;

    /**
     * 限流操作
     * @param key 每个用户对应不同的限流器
     */
    public boolean  doRateLimiter(String key,long count){
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
        /**
         * RateType:限流类型，可以是 OVERALL、OVER_QUEUE、OVER_THE_HASH 或 AVG_RATE。
         * 其中，OVERALL 是默认值，表示令牌桶算法；
         * OVER_QUEUE 是漏桶算法；OVER_THE_HASH 是哈希算法；AVG_RATE 是平均速率算法。
         *
         * rate:每秒产生的令牌数。
         * rateInterval:令牌有效期，单位为秒。
         * RateIntervalUnit:时间间隔单位，可以是 SECONDS、MILLISECONDS 或 NANOSECONDS。
         */
        rateLimiter.trySetRate (RateType.OVERALL, count, 1, RateIntervalUnit.SECONDS);
        // 每当一个操作来了后，请求一个令牌
        return rateLimiter.tryAcquire(1);
    }

}
