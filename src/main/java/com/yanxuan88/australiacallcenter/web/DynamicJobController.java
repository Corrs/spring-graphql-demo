package com.yanxuan88.australiacallcenter.web;

import com.yanxuan88.australiacallcenter.annotation.SaAuthorize;
import com.yanxuan88.australiacallcenter.graphql.util.RelayUtil;
import com.yanxuan88.australiacallcenter.model.dto.AddDynamicJobDTO;
import com.yanxuan88.australiacallcenter.model.dto.DynamicJobQueryDTO;
import com.yanxuan88.australiacallcenter.model.dto.EditDynamicJobDTO;
import com.yanxuan88.australiacallcenter.model.dto.PageDTO;
import com.yanxuan88.australiacallcenter.model.vo.DynamicJobVO;
import com.yanxuan88.australiacallcenter.service.IDynamicJobService;
import graphql.relay.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 动态任务控制器
 *
 * @author caosh
 * @since 2024-01-26 18:59:30
 */
@Controller
public class DynamicJobController {

    @Autowired
    private IDynamicJobService dynamicJobService;

    @SaAuthorize
    @QueryMapping
    public Connection<DynamicJobVO> dynamicJobs(@Argument PageDTO page, @Argument DynamicJobQueryDTO query) {
        return RelayUtil.build(dynamicJobService.jobs(page, query));
    }

    /**
     * 删除任务
     *
     * @param id 任务id
     * @return true/false
     */
    @Valid
    @SaAuthorize
    @MutationMapping
    public boolean remDynamicJob(@Argument @NotNull(message = "任务标识不能为空")
                                 @Min(value = 1, message = "任务不存在") Long id) {
        return dynamicJobService.rem(id);
    }

    /**
     * 新增定时任务
     *
     * @param job 任务
     * @return true/false
     */
    @Valid
    @SaAuthorize
    @MutationMapping
    public boolean addDynamicJob(@Argument AddDynamicJobDTO job) {
        return dynamicJobService.add(job);
    }

    /**
     * 编辑定时任务
     *
     * @param job 任务
     * @return true/false
     */
    @Valid
    @SaAuthorize
    @MutationMapping
    public boolean editDynamicJob(@Argument EditDynamicJobDTO job) {
        return dynamicJobService.edit(job);
    }

    /**
     * 切换任务运行状态
     *
     * @param id 任务id
     * @return true/false
     */
    @Valid
    @SaAuthorize
    @MutationMapping
    public boolean switchJob(@Argument @NotNull(message = "任务标识不能为空")
                             @Min(value = 1, message = "任务不存在") Long id) {
        return dynamicJobService.switchJob(id);
    }
}
