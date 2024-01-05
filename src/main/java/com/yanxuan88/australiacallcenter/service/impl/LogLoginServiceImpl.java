package com.yanxuan88.australiacallcenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanxuan88.australiacallcenter.mapper.SysLogLoginMapper;
import com.yanxuan88.australiacallcenter.model.entity.SysLogLogin;
import com.yanxuan88.australiacallcenter.service.ILogLoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 登录日志服务实现类
 *
 * @author co
 * @since 2024-01-02 11:09:58
 */
@Slf4j
@Service
public class LogLoginServiceImpl extends ServiceImpl<SysLogLoginMapper, SysLogLogin> implements ILogLoginService {

}
