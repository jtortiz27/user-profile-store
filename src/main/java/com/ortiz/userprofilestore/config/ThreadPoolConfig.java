package com.ortiz.userprofilestore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ThreadPoolConfig {

    @Bean
    ExecutorService executorService() {
        return Executors.newFixedThreadPool(500);
    }
}
