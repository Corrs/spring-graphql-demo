package com.yanxuan88.australiacallcenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanxuan88.australiacallcenter.mapper.SysUserRoleMapper;
import com.yanxuan88.australiacallcenter.model.entity.SysUserRole;
import com.yanxuan88.australiacallcenter.service.IUserRoleService;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements IUserRoleService {
}
