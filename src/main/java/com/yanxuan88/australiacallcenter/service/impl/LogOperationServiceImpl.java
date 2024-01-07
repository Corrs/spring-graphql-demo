package com.yanxuan88.australiacallcenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanxuan88.australiacallcenter.mapper.SysLogOperationMapper;
import com.yanxuan88.australiacallcenter.model.entity.SysLogOperation;
import com.yanxuan88.australiacallcenter.service.ILogOperationService;
import org.springframework.stereotype.Service;

@Service
public class LogOperationServiceImpl extends ServiceImpl<SysLogOperationMapper, SysLogOperation> implements ILogOperationService {
}
