package com.yanxuan88.australiacallcenter;

import com.yanxuan88.australiacallcenter.model.dto.AddDynamicJobDTO;
import com.yanxuan88.australiacallcenter.scheduler.TestJob;
import com.yanxuan88.australiacallcenter.service.IDynamicJobService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;

import static com.yanxuan88.australiacallcenter.scheduler.TriggerTypeEnum.CRON;

@SpringBootTest
public class DynamicJobTest {
    @Autowired
    IDynamicJobService dynamicJobService;
    @Test
    void testAdd() {
        CountDownLatch latch = new CountDownLatch(1);
        AddDynamicJobDTO job = new AddDynamicJobDTO();
        job.setJobName("testJob");
        job.setJobGroup("testGroup");
        job.setJobData("{\"aa\": 1}");
        job.setTriggerType(CRON.getCode());
        job.setTriggerRule("{\"cron\": \"10 * * * * ?\"}");
        job.setStatus(true);
        job.setJobClassName(TestJob.class.getName());
        dynamicJobService.add(job);
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
