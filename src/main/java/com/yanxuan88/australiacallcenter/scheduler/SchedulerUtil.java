package com.yanxuan88.australiacallcenter.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yanxuan88.australiacallcenter.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

import static com.yanxuan88.australiacallcenter.common.Constant.DEFAULT_ZONE_OFFSET;

@Slf4j
@Component
public final class SchedulerUtil implements InitializingBean {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static Scheduler scheduler;
    @Autowired
    private SchedulerFactoryBean quartzScheduler;

    public static void addJob(String className, String name, String group, String description, String jobDataMap,
                              Integer triggerType, String triggerRule, LocalDateTime localDateTime) {
        // 检查任务是否已经存在
        if (isResume(name, group)) {
            throw new BizException("存在相同名称相同分组的任务");
        }
        // 构建JobDetail
        JobBuilder jobBuilder = JobBuilder.newJob(getClass(className)).withDescription(description).withIdentity(name, group);
        if (StringUtils.hasText(jobDataMap)) {
            try {
                Map<String, Object> dataMap = MAPPER.readValue(jobDataMap, MAPPER.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
                jobBuilder.usingJobData(new JobDataMap(dataMap));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        JobDetail jobDetail = jobBuilder.build();
        // 构建trigger
        TriggerBuilder triggerBuilder = TriggerBuilder.newTrigger().withIdentity(name, group)
                .withSchedule(TriggerTypeEnum.scheduleBuilder(triggerType, triggerRule));
        if (localDateTime != null) {
            triggerBuilder.startAt(Date.from(localDateTime.toInstant(DEFAULT_ZONE_OFFSET)));
        } else {
            triggerBuilder.startNow();
        }
        Trigger trigger = triggerBuilder.build();
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            log.error("新增定时任务失败", e);
            throw new BizException("新增定时任务失败");
        }
    }

    public static boolean deleteJob(String jobName, String jobGroup) {
        try {
            //停止触发
            scheduler.pauseTrigger(TriggerKey.triggerKey(jobName, jobGroup));
            //取消发布
            scheduler.unscheduleJob(TriggerKey.triggerKey(jobName, jobGroup));
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
            log.error("定时任务运行失败", e);
            throw new BizException("定时任务运行失败");
        }
    }

    public static void rescheduleJob(String name, String group, Integer triggerType, String triggerRule, LocalDateTime localDateTime) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(name, group);
            // 构建trigger
            TriggerBuilder triggerBuilder = TriggerBuilder.newTrigger().withIdentity(name, group)
                    .withSchedule(TriggerTypeEnum.scheduleBuilder(triggerType, triggerRule));
            if (localDateTime != null) {
                triggerBuilder.startAt(Date.from(localDateTime.toInstant(DEFAULT_ZONE_OFFSET)));
            } else {
                triggerBuilder.startNow();
            }
            Trigger trigger = triggerBuilder.build();
            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            log.error("更新定时任务失败", e);
            throw new BizException("更新定时任务失败");
        }
    }

    /**
     * 暂停所有任务
     *
     * @throws Exception
     */
    public static void pauseAllJob() {
        try {
            scheduler.pauseAll();
        } catch (SchedulerException e) {
            log.error("暂停全部定时任务失败", e);
            throw new BizException("暂停全部定时任务失败");
        }
    }

    /**
     * 唤醒所有任务
     *
     * @throws Exception
     */
    public static void resumeAllJob() {
        try {
            scheduler.resumeAll();
        } catch (SchedulerException e) {
            log.error("运行全部定时任务失败", e);
            throw new BizException("运行全部定时任务失败");
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

    private static boolean isResume(String jobName, String jobGroup) {
        try {
            return scheduler.checkExists(JobKey.jobKey(jobName, jobGroup));
        } catch (SchedulerException e) {
            log.error("检查任务是否存在失败", e);
            throw new BizException("检查任务是否存在失败");
        }
    }
}
