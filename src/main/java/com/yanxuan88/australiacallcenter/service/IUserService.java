package com.yanxuan88.australiacallcenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yanxuan88.australiacallcenter.model.dto.AddUserDTO;
import com.yanxuan88.australiacallcenter.model.entity.SysUser;

import java.util.List;

public interface IUserService extends IService<SysUser> {
    SysUser queryByUsername(String username);

    List<SysUser> queryByDept(Long deptId);

    boolean resetPassword(Long userId);

    boolean add(AddUserDTO user);
}
