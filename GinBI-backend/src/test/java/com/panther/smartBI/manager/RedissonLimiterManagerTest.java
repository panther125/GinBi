package com.panther.smartBI.manager;

import com.panther.smartBI.annotation.RateCount;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
class RedissonLimiterManagerTest {

    @Resource
    private RedissonLimiterManager manager;

    @Test
    void TestDoRateLimiter() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
           manager.doRateLimiter("key",2);
            log.info("success");
        }
        Thread.sleep(1000);
        for (int i = 0; i < 5; i++) {
            manager.doRateLimiter("key",2);
            log.info("success");
        }
    }

    @RateCount(count = "2")
    public void tryQuery(){
        log.info("success");
    }

    @Test
    void TestAnnoRateLimiter() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            tryQuery();
        }
        Thread.sleep(1000);
        for (int i = 0; i < 5; i++) {
            tryQuery();
        }
    }

}