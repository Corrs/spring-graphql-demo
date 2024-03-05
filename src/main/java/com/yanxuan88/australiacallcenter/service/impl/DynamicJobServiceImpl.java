package com.yanxuan88.australiacallcenter.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.yanxuan88.australiacallcenter.exception.BizException;
import com.yanxuan88.australiacallcenter.mapper.SysDynamicJobMapper;
import com.yanxuan88.australiacallcenter.model.dto.AddDynamicJobDTO;
import com.yanxuan88.australiacallcenter.model.dto.DynamicJobQueryDTO;
import com.yanxuan88.australiacallcenter.model.dto.EditDynamicJobDTO;
import com.yanxuan88.australiacallcenter.model.dto.PageDTO;
import com.yanxuan88.australiacallcenter.model.entity.SysDynamicJob;
import com.yanxuan88.australiacallcenter.model.vo.DynamicJobVO;
import com.yanxuan88.australiacallcenter.mysql.JdbcLockClient;
import com.yanxuan88.australiacallcenter.scheduler.SchedulerUtil;
import com.yanxuan88.australiacallcenter.service.IDynamicJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DynamicJobServiceImpl extends ServiceImpl<SysDynamicJobMapper, SysDynamicJob> implements IDynamicJobService {

    private static final String LOCK_KEY_PREFIX = "dynamicJob";
    @Autowired
    private JdbcLockClient jdbcLockClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
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
    @Transactional(rollbackFor = Exception.class)
    public boolean add(AddDynamicJobDTO job) {
        String jobName = job.getJobName().trim();
        String jobGroup = job.getJobGroup().trim();
        String lockKey = LOCK_KEY_PREFIX + ":" + jobName + ":" + jobGroup;
        return jdbcLockClient.doWithLock(lockKey, lock -> {
            SysDynamicJob one = getOne(Wrappers.<SysDynamicJob>lambdaQuery().eq(SysDynamicJob::getJobName, jobName).eq(SysDynamicJob::getJobGroup, jobGroup), false);
            if (one != null) {
                throw new BizException("存在相同名称相同分组的任务");
            }
            LocalDateTime firstRuntime = job.getFirstRuntime();
            String jobData = Strings.nullToEmpty(job.getJobData()).trim();
            Integer triggerType = job.getTriggerType();
            String triggerRule = Strings.nullToEmpty(job.getTriggerRule()).trim();
            String jobClassName = job.getJobClassName().trim();
            String description = job.getDescription();
            SysDynamicJob entity = new SysDynamicJob().setJobName(jobName).setJobGroup(jobGroup).setJobClassName(jobClassName).setDescription(description).setJobData(Strings.emptyToNull(jobData)).setTriggerType(triggerType).setTriggerRule(triggerRule).setCreateTime(LocalDateTime.now()).setFirstRuntime(firstRuntime).setStatus(job.getStatus());
            boolean result = save(entity);
            if (result) {
                SchedulerUtil.addJob(jobClassName, jobName, jobGroup, description, jobData, triggerType, triggerRule, firstRuntime);
                if (!Boolean.TRUE.equals(job.getStatus())) {
                    // 暂停任务
                    SchedulerUtil.pauseJob(jobName, jobGroup);
                }
            }
            return result;
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean edit(EditDynamicJobDTO job) {
        SysDynamicJob entity = getById(job.getId());
        if (entity == null) {
            throw new BizException("任务不存在");
        }
        LocalDateTime firstRuntime = job.getFirstRuntime();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oldRuntime = entity.getFirstRuntime();
        if (!firstRuntime.equals(oldRuntime) && firstRuntime.isBefore(now)) {
            throw new BizException("首次运行时间必须是未来时间");
        }

        Integer oldTriggerType = entity.getTriggerType();
        String oldTriggerRule = entity.getTriggerRule();
        Boolean oldStatus = entity.getStatus();
        String triggerRule = job.getTriggerRule().trim();
        Boolean status = job.getStatus();
        entity.setUpdateTime(now).setFirstRuntime(firstRuntime)
                .setDescription(Strings.nullToEmpty(job.getDescription()).trim()).setStatus(status)
                .setTriggerType(job.getTriggerType()).setTriggerRule(triggerRule);
        boolean result = updateById(entity);
        if (result) {
            String jobName = entity.getJobName();
            String jobGroup = entity.getJobGroup();
            if (!firstRuntime.equals(oldRuntime) || job.getTriggerType() != oldTriggerType || !triggerRule.equals(oldTriggerRule)) {
                SchedulerUtil.rescheduleJob(jobName, jobGroup, entity.getTriggerType(), entity.getTriggerRule(), firstRuntime);
            }
            if (!oldStatus.equals(status)) {
                if (Boolean.TRUE.equals(status)) SchedulerUtil.resumeJob(jobName, jobGroup);
                else SchedulerUtil.pauseJob(jobName, jobGroup);
            }
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean switchJob(Long id) {
        SysDynamicJob entity = getById(id);
        if (entity == null) {
            throw new BizException("任务不存在");
        }
        boolean result = update(Wrappers.<SysDynamicJob>lambdaUpdate().set(SysDynamicJob::getStatus, !entity.getStatus()).set(SysDynamicJob::getUpdateTime, LocalDateTime.now()).eq(SysDynamicJob::getId, id));
        if (Boolean.TRUE.equals(entity.getStatus())) {
            SchedulerUtil.pauseJob(entity.getJobName(), entity.getJobGroup());
        } else {
            SchedulerUtil.resumeJob(entity.getJobName(), entity.getJobGroup());
        }
        return result;
    }

    @Override
    public Page<DynamicJobVO> jobs(PageDTO page, DynamicJobQueryDTO query) {
        Page<SysDynamicJob> jobPage = page(page.page(), Wrappers.<SysDynamicJob>lambdaQuery()
                .eq(StringUtils.hasText(query.getName()), SysDynamicJob::getJobName, query.getName())
                .eq(StringUtils.hasText(query.getGroup()), SysDynamicJob::getJobGroup, query.getGroup())
                .orderByDesc(SysDynamicJob::getId));
        Page<DynamicJobVO> voPage = new Page<>(jobPage.getCurrent(), jobPage.getSize(), jobPage.getTotal());
        List<SysDynamicJob> records = jobPage.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            voPage.setRecords(records.stream()
                    .map(e -> new DynamicJobVO()
                            .setCreateTime(e.getCreateTime()).setUpdateTime(e.getUpdateTime())
                            .setJobClassName(e.getJobClassName()).setJobName(e.getJobName()).setJobGroup(e.getJobGroup())
                            .setJobData(e.getJobData()).setDescription(e.getDescription()).setId(e.getId()).setFirstRuntime(e.getFirstRuntime())
                            .setStatus(e.getStatus()).setTriggerRule(e.getTriggerRule()).setTriggerType(e.getTriggerType()))
                    .collect(Collectors.toList()));
        }
        return voPage;
    }
}
