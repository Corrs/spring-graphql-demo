package com.yanxuan88.australiacallcenter.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanxuan88.australiacallcenter.mapper.SysUserMapper;
import com.yanxuan88.australiacallcenter.model.entity.SysUser;
import com.yanxuan88.australiacallcenter.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements IUserService {
    @Override
    public SysUser queryByUsername(String username) {
        return getOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, username));
    }

    @Override
    public List<SysUser> queryByDept(Long deptId) {
        return list(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getDeptId, deptId));
    }
}
