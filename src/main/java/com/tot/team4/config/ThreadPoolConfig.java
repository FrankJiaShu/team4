package com.tot.team4.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * @Author: weixy-h
 * @Date: 2023/9/1 00:11
 * @Desc:
 **/
@Configuration
public class ThreadPoolConfig {
    @Bean
    public ThreadPoolExecutor threadPoolExecutor(
            @Value("${thread.pool.coreSize}")
            Integer coreSize,
            @Value("${thread.pool.maxSize}")
            Integer maxSize,
            @Value("${thread.pool.keepalive}")
            Integer keepalive,
            @Value("${thread.pool.blockQueueSize}")
            Integer blockQueueSize
    ) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                coreSize,
                maxSize,
                keepalive,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(blockQueueSize),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );
        executor.prestartCoreThread();
        return executor;
    }
}
