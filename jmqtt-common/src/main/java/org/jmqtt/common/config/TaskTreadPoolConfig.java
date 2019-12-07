package org.jmqtt.common.config;

/**
 * 异步任务线程池配置
 *
 * @author Alex Liu
 * @date 2019/12/06
 */
public class TaskTreadPoolConfig {

    private int corePoolSize = 80;

    private int maxPoolSize = 160;

    private int keepAliveSeconds = 200;

    private int queueCapacity = 3200;

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getKeepAliveSeconds() {
        return keepAliveSeconds;
    }

    public void setKeepAliveSeconds(int keepAliveSeconds) {
        this.keepAliveSeconds = keepAliveSeconds;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }
}
