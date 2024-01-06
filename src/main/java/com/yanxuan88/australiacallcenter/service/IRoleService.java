package com.yanxuan88.australiacallcenter.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yanxuan88.australiacallcenter.model.dto.AddRoleDTO;
import com.yanxuan88.australiacallcenter.model.dto.EditRoleDTO;
import com.yanxuan88.australiacallcenter.model.dto.PageDTO;
import com.yanxuan88.australiacallcenter.model.entity.SysRole;
import com.yanxuan88.australiacallcenter.model.vo.RoleVO;

public interface IRoleService extends IService<SysRole> {
    Page<RoleVO> page(PageDTO p, String roleName);
    boolean add(AddRoleDTO role);

    boolean edit(EditRoleDTO role);

    boolean rem(Long id);
}
