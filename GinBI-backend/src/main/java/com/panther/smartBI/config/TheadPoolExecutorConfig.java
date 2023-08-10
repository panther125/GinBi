package com.panther.smartBI.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Gin 琴酒
 * @data 2023/8/2 17:17
 */
@Configuration
public class TheadPoolExecutorConfig {

    @Bean
    public ThreadPoolExecutor threadPoolExecutor(){

        ThreadFactory threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(@NotNull Runnable r) {
                int count = 1;
                Thread thread = new Thread(r);
                thread.setName("线程"+count++);
                return thread;
            }
        };
        /**
         * corePoolSize : 核心线程数 （常驻线程）
         * maximumPoolSize : 池中最大的线程数
         * KeepAliveTime : 除核心线程外 新创建的线程 在空闲状态下的最大存活时间
         * TimeUnit : 最大存活时间的单位
         * BlockingQueue : 任务队列
         * threadFactor : 线程工厂
         * RejectedExecutionHandler : 拒接策略，但线程全部占满 任务队列也占满时 对任务的处理
         */
        return new ThreadPoolExecutor(2,
                4,60000
                , TimeUnit.SECONDS,new ArrayBlockingQueue<>(2),threadFactory);
    }

}
