package com.yanxuan88.australiacallcenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yanxuan88.australiacallcenter.model.dto.AssignPermsDTO;
import com.yanxuan88.australiacallcenter.model.entity.SysRoleMenu;

import java.util.List;

public interface IRoleMenuService extends IService<SysRoleMenu> {
    boolean assignPerms(AssignPermsDTO perms);

    void removeByRoleId(Long roleId);

    List<Long> selectMenuIdByRoleId(Long roleId);
}
