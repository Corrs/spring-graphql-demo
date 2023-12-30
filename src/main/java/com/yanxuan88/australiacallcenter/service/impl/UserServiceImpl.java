package com.yanxuan88.australiacallcenter.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanxuan88.australiacallcenter.mapper.SysUserMapper;
import com.yanxuan88.australiacallcenter.model.entity.SysUser;
import com.yanxuan88.australiacallcenter.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements IUserService {
    @Override
    public SysUser queryByUsername(String username) {
        log.info("根据用户名查询用户数据，username={}", username);
        SysUser result = getOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, username));
        log.info("用户数据，user={}", result);
        return result;
    }
}
