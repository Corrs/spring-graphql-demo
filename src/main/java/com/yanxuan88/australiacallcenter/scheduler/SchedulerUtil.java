package com.yanxuan88.australiacallcenter.scheduler;

import com.yanxuan88.australiacallcenter.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public final class SchedulerUtil implements InitializingBean {
    private static Scheduler scheduler;
    @Autowired
    private SchedulerFactoryBean quartzScheduler;


    public static void addJob(String className, String name, String group) {
        ;
        // 新增job
        JobDetail jobDetail = JobBuilder.newJob(getClass(className))
                .withIdentity(name, group)
                .build();
//        scheduler.addJob(jobDetail, false);

    }

    public static boolean deleteJob(String jobName, String jobGroup) {
        try {
            boolean b = scheduler.deleteJob(JobKey.jobKey(jobName, jobGroup));
            if (!b) {
                throw new BizException("定时任务删除失败");
            }
            return true;
        } catch (SchedulerException e) {
            log.error("定时任务删除失败", e);
            throw new BizException("定时任务删除失败");
        }
    }

    public static void pauseJob(String jobName, String jobGroup) {
        try {
            scheduler.pauseJob(JobKey.jobKey(jobName, jobGroup));
        } catch (SchedulerException e) {
            throw new BizException("定时任务暂停失败");
        }
    }

    public static void resumeJob(String jobName, String jobGroup) {
        try {
            scheduler.resumeJob(JobKey.jobKey(jobName, jobGroup));
        } catch (SchedulerException e) {
            throw new BizException("定时任务运行失败");
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        scheduler = quartzScheduler.getScheduler();
    }

    private static Class<? extends Job> getClass(String className) {
        try {
            return (Class<? extends Job>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new BizException("类【" + className + "】不存在");
        }
    }
}
