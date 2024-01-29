package com.yanxuan88.australiacallcenter.scheduler;

import com.yanxuan88.australiacallcenter.config.RedisClient;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class TestJob extends QuartzJobBean {
    @Autowired
    private RedisClient redisClient;
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobDetail jobDetail = context.getJobDetail();
        System.out.println(jobDetail.getJobDataMap());
        System.out.println(redisClient);
    }
}
