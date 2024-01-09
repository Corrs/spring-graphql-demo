package com.yanxuan88.australiacallcenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yanxuan88.australiacallcenter.model.entity.SysUserRole;

public interface IUserRoleService extends IService<SysUserRole> {
    void add(Long userId, Long roleId);

    void removeByUserId(Long userId);

}
