package com.yanxuan88.australiacallcenter.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanxuan88.australiacallcenter.mapper.SysRoleMenuMapper;
import com.yanxuan88.australiacallcenter.model.dto.AssignPermsDTO;
import com.yanxuan88.australiacallcenter.model.entity.SysRoleMenu;
import com.yanxuan88.australiacallcenter.service.IRoleMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements IRoleMenuService {
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean assignPerms(AssignPermsDTO perms) {
        Long roleId = perms.getRoleId();
        return saveBatch(perms.getPerms().stream()
                .map(perm -> new SysRoleMenu()
                        .setMenuId(perm)
                        .setRoleId(roleId))
                .collect(Collectors.toList()));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeByRoleId(Long roleId) {
        remove(Wrappers.<SysRoleMenu>lambdaQuery().eq(SysRoleMenu::getRoleId, roleId));
    }

    @Override
    public List<Long> selectMenuIdByRoleId(Long roleId) {
        return list(Wrappers.<SysRoleMenu>lambdaQuery().select(SysRoleMenu::getMenuId).eq(SysRoleMenu::getRoleId, roleId))
                .stream()
                .map(SysRoleMenu::getMenuId)
                .collect(Collectors.toList());
    }
}
