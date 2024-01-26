package com.yanxuan88.australiacallcenter.scheduler;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Component;

/**
 * 定时任务工厂类
 *
 * @author co
 * @since 2024-01-26 14:27:59
 */
public class MyJobFactory extends AdaptableJobFactory {
    /**
     * spring 容器 bean工厂
     */
    AutowireCapableBeanFactory capableBeanFactory;

    public void setCapableBeanFactory(AutowireCapableBeanFactory capableBeanFactory) {
        this.capableBeanFactory = capableBeanFactory;
    }

    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        Object jobInstance = super.createJobInstance(bundle);
        // 将job实例注入到spring容器中
        capableBeanFactory.autowireBean(jobInstance);
        return jobInstance;
    }
}
