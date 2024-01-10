package com.app.toaster.config.sqs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class GracefulShutdown implements SmartLifecycle, BeanFactoryAware {
    private final Map<String, SimpleMessageListenerContainer> messageListenerContainers;

    private boolean isRunning;
    private DefaultSingletonBeanRegistry beanFactory;


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultSingletonBeanRegistry) beanFactory;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable callback) {
        stop();
        callback.run();
    }

    @Override
    public void start() {
        this.isRunning = true;
    }

    @Override
    public void stop() {
        stopSqsListeners();
        this.isRunning = false;
        log.info("[GracefulShutdown 완료]");
    }

    private void stopSqsListeners() {
        this.messageListenerContainers.keySet().forEach(beanName -> beanFactory.destroySingleton(beanName));
    }


    @Override
    public boolean isRunning() {
        return this.isRunning;
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE; // 명시적으로 PHASE 설정
    }

}
