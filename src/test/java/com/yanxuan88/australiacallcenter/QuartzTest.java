package com.yanxuan88.australiacallcenter;

import org.junit.jupiter.api.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.calendar.CronCalendar;
import org.quartz.impl.calendar.HolidayCalendar;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * Quartz Test
 */
public class QuartzTest {

    @Test
    void test01() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        JobDetail jobDetail = JobBuilder.newJob(HelloJob.class)
                .withIdentity("job", "group")
                .build();
        SimpleTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger", "group")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(10)
                        .repeatForever())
                .build();

        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        scheduler.scheduleJob(jobDetail, trigger);

        latch.await();

    }

    public static class HelloJob implements Job {

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println(context);
        }
    }

}
