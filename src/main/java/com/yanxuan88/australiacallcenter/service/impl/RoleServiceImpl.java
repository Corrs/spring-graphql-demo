package com.yanxuan88.australiacallcenter.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.yanxuan88.australiacallcenter.annotation.SysLog;
import com.yanxuan88.australiacallcenter.exception.BizException;
import com.yanxuan88.australiacallcenter.mapper.SysRoleMapper;
import com.yanxuan88.australiacallcenter.model.dto.AddRoleDTO;
import com.yanxuan88.australiacallcenter.model.dto.AssignPermsDTO;
import com.yanxuan88.australiacallcenter.model.dto.EditRoleDTO;
import com.yanxuan88.australiacallcenter.model.dto.PageDTO;
import com.yanxuan88.australiacallcenter.model.entity.SysRole;
import com.yanxuan88.australiacallcenter.model.vo.MenuVO;
import com.yanxuan88.australiacallcenter.model.vo.RoleVO;
import com.yanxuan88.australiacallcenter.service.IMenuService;
import com.yanxuan88.australiacallcenter.service.IRoleMenuService;
import com.yanxuan88.australiacallcenter.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements IRoleService {
    @Autowired
    private IMenuService menuService;
    @Autowired
    private IRoleMenuService roleMenuService;

    @Override
    public Page<RoleVO> page(PageDTO p, String roleName) {
        Page<SysRole> page = page(p.page(), Wrappers.<SysRole>lambdaQuery()
                .like(StringUtils.hasText(roleName), SysRole::getRoleName, roleName)
                .orderByDesc(SysRole::getId));

        Page<RoleVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(page.getRecords().stream()
                .map(e -> new RoleVO()
                        .setId(e.getId())
                        .setRoleName(e.getRoleName())
                        .setRemark(e.getRemark())
                        .setCreateTime(e.getCreateTime()))
                .collect(Collectors.toList()));
        return voPage;
    }

    @SysLog("新增角色")
    @Override
    public boolean add(AddRoleDTO role) {
        return save(
                new SysRole()
                        .setRoleName(role.getRoleName().trim())
                        .setRemark(Strings.nullToEmpty(role.getRemark()).trim())
        );
    }

    @SysLog("编辑角色")
    @Override
    public boolean edit(EditRoleDTO role) {
        SysRole entity = getById(role.getId());
        if (entity == null) {
            throw new BizException("角色不存在");
        }
        entity.setRoleName(role.getRoleName().trim())
                .setRemark(Strings.nullToEmpty(role.getRemark()).trim());
        return updateById(entity);
    }

    @SysLog("删除角色")
    @Override
    public boolean rem(Long id) {
        // todo 如果关联了用户，不能删除
        // todo 删除关联的 SysRoleMenu
        return false;
    }

    @SysLog("角色分配权限")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignPerms(AssignPermsDTO perms, Long userId) {
        Long roleId = perms.getRoleId();
        SysRole entity = getById(roleId);
        if (entity == null) {
            throw new BizException("角色不存在");
        }

        roleMenuService.removeByRoleId(roleId);
        List<Long> permsList = Optional.ofNullable(perms.getPerms()).orElseGet(Collections::emptyList)
                .stream().filter(Objects::nonNull).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(permsList)) {
            perms.setPerms(permsList);
            if (!menuService.menus(userId).stream().map(MenuVO::getId).collect(Collectors.toList())
                    .containsAll(permsList)) {
                throw new BizException("您分配了不存在的权限");
            }

            return roleMenuService.assignPerms(perms);
        }
        return true;
    }

    @Override
    public List<Long> rolePerms(Long roleId) {
        SysRole entity = getById(roleId);
        if (entity == null) {
            throw new BizException("角色不存在");
        }
        return roleMenuService.selectMenuIdByRoleId(roleId);
    }

    @Override
    public List<RoleVO> roleList() {
        return list().stream()
                .map(e -> new RoleVO()
                        .setId(e.getId())
                        .setRoleName(e.getRoleName())
                        .setRemark(e.getRemark())
                        .setCreateTime(e.getCreateTime()))
                .collect(Collectors.toList());
    }
}
