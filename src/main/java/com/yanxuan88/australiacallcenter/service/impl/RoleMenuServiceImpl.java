package com.yanxuan88.australiacallcenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanxuan88.australiacallcenter.mapper.SysRoleMenuMapper;
import com.yanxuan88.australiacallcenter.model.entity.SysRoleMenu;
import com.yanxuan88.australiacallcenter.service.IRoleMenuService;
import org.springframework.stereotype.Service;

@Service
public class RoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements IRoleMenuService {
}
