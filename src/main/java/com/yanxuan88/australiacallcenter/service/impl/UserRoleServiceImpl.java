package com.yanxuan88.australiacallcenter.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanxuan88.australiacallcenter.mapper.SysUserRoleMapper;
import com.yanxuan88.australiacallcenter.model.entity.SysUserRole;
import com.yanxuan88.australiacallcenter.service.IUserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements IUserRoleService {
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(Long userId, Long roleId) {
        saveOrUpdate(new SysUserRole().setRoleId(roleId).setUserId(userId), Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, userId));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeByUserId(Long userId) {
        remove(Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, userId));
    }

}
