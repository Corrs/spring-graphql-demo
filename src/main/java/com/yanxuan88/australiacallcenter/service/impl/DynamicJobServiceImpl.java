package com.yanxuan88.australiacallcenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanxuan88.australiacallcenter.mapper.SysDynamicJobMapper;
import com.yanxuan88.australiacallcenter.model.entity.SysDynamicJob;
import com.yanxuan88.australiacallcenter.scheduler.SchedulerUtil;
import com.yanxuan88.australiacallcenter.service.IDynamicJobService;
import org.springframework.stereotype.Service;

@Service
public class DynamicJobServiceImpl extends ServiceImpl<SysDynamicJobMapper, SysDynamicJob>
        implements IDynamicJobService {
    @Override
    public boolean rem(Long id) {
        SysDynamicJob job = getById(id);
        if (job != null) {
            boolean result = removeById(id);
            if (result) {
                SchedulerUtil.deleteJob(job.getJobName(), job.getJobGroup());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean pause(Long id) {
        SysDynamicJob job = getById(id);
        if (job != null) {
            SchedulerUtil.pauseJob(job.getJobName(), job.getJobGroup());
        }
        return true;
    }

    @Override
    public boolean resume(Long id) {
        SysDynamicJob job = getById(id);
        if (job != null) {
            SchedulerUtil.resumeJob(job.getJobName(), job.getJobGroup());
        }
        return true;
    }
}
