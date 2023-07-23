package com.qi.miaosha2.util.xiancche;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

@Configuration
public class ExecutorService {//自定义线程池要不了springboot自带的线程池是无边界的
   @Bean
    public static ThreadPoolExecutor newCachedThreadPool() {

        return new ThreadPoolExecutor(  20,
                30,
                60,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
    }



}
