package org.jmqtt.persistent.asyncTask;

import org.jmqtt.common.config.TaskTreadPoolConfig;
import org.jmqtt.common.log.LoggerName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Alex Liu
 * @date 2019/12/06
 */

@Configuration
@EnableAsync
public class TaskExecutePool {
    private Logger logger = LoggerFactory.getLogger(LoggerName.BROKER);

    private TaskTreadPoolConfig config = new TaskTreadPoolConfig();

    @Bean
    public Executor brokerAsyncPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(config.getCorePoolSize());
        executor.setMaxPoolSize(config.getMaxPoolSize());
        executor.setQueueCapacity(config.getQueueCapacity());
        executor.setKeepAliveSeconds(config.getKeepAliveSeconds());
        executor.setThreadNamePrefix("broker-task-");

        logger.info("corePoolSize:" + config.getCorePoolSize() +"|maxPoolSize:" + config.getMaxPoolSize() +"|queueCapacity:" + config.getQueueCapacity() + "|keepAliveSeconds:" + config.getKeepAliveSeconds());

        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
