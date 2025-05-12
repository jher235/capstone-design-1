package org.example.capstonedesign1.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import static org.example.capstonedesign1.global.constant.ThreadPoolConstant.I_O_TASK_THREAD_POOL_NAME;

@Configuration
public class ThreadPoolConfig {

    @Bean(name = I_O_TASK_THREAD_POOL_NAME)
    public ThreadPoolTaskExecutor taskExecutor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(30);
        executor.setMaxPoolSize(100);
        executor.setQueueCapacity(50);
        executor.initialize();
        return executor;
    }
}
