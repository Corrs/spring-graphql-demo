package com.yanxuan88.australiacallcenter.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.yanxuan88.australiacallcenter.annotation.SysLog;
import com.yanxuan88.australiacallcenter.exception.BizException;
import com.yanxuan88.australiacallcenter.mapper.SysRoleMapper;
import com.yanxuan88.australiacallcenter.model.dto.AddRoleDTO;
import com.yanxuan88.australiacallcenter.model.dto.EditRoleDTO;
import com.yanxuan88.australiacallcenter.model.dto.PageDTO;
import com.yanxuan88.australiacallcenter.model.entity.SysRole;
import com.yanxuan88.australiacallcenter.model.vo.RoleVO;
import com.yanxuan88.australiacallcenter.service.IRoleService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;

@Service
public class RoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements IRoleService {
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
}
