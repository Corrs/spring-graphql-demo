package com.yanxuan88.australiacallcenter.web;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanxuan88.australiacallcenter.annotation.Authenticated;
import com.yanxuan88.australiacallcenter.graphql.util.RelayUtil;
import com.yanxuan88.australiacallcenter.model.dto.LogLoginQueryDTO;
import com.yanxuan88.australiacallcenter.model.dto.OperationLogQueryDTO;
import com.yanxuan88.australiacallcenter.model.dto.PageDTO;
import com.yanxuan88.australiacallcenter.model.entity.SysLogLogin;
import com.yanxuan88.australiacallcenter.model.entity.SysLogOperation;
import com.yanxuan88.australiacallcenter.model.vo.LogLoginVO;
import com.yanxuan88.australiacallcenter.model.vo.OperationLogVO;
import com.yanxuan88.australiacallcenter.service.ILogLoginService;
import com.yanxuan88.australiacallcenter.service.ILogOperationService;
import graphql.relay.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;

/**
 * 登录日志控制器
 *
 * @author co
 * @since 2024-01-02 15:22:26
 */
@Controller
public class LogController {
    @Autowired
    private ILogLoginService logLoginService;
    @Autowired
    private ILogOperationService logOperationService;

    /**
     * 查询登录日志
     *
     * @return
     */
    @Authenticated
    @QueryMapping
    public Connection<LogLoginVO> loginLogs(@Argument LogLoginQueryDTO cond) {
        Page<SysLogLogin> page = logLoginService.page(new Page<>(cond.getCurrent(), cond.getLimit()),
                Wrappers.<SysLogLogin>lambdaQuery()
                        .between(SysLogLogin::getCreateTime, cond.getStartTime(), cond.getEndTime())
                        .eq(cond.getStatus() != null, SysLogLogin::getStatus, cond.getStatus())
                        .like(StringUtils.hasText(cond.getUsername()), SysLogLogin::getCreateName, cond.getUsername())
                        .orderByDesc(SysLogLogin::getCreateTime, SysLogLogin::getId));
        Page<LogLoginVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(page.getRecords().stream().map(e -> new LogLoginVO(e)).collect(Collectors.toList()));
        return RelayUtil.build(voPage);
    }

    /**
     * 查询操作日志
     *
     * @param p     分页
     * @param query 条件
     * @return relay
     */
    @Authenticated
    @QueryMapping
    public Connection<OperationLogVO> operationLogs(@Argument PageDTO p, @Argument OperationLogQueryDTO query) {
        Page<SysLogOperation> page = logOperationService.page(p.page(),
                Wrappers.<SysLogOperation>lambdaQuery()
                        .between(SysLogOperation::getCreateTime, query.getStartTime(), query.getEndTime())
                        .eq(query.getStatus() != null, SysLogOperation::getStatus, query.getStatus())
                        .orderByDesc(SysLogOperation::getCreateTime, SysLogOperation::getId));
        Page<OperationLogVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(page.getRecords().stream().map(e -> new OperationLogVO(e)).collect(Collectors.toList()));
        return RelayUtil.build(voPage);
    }

}
