package com.yanxuan88.australiacallcenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.yanxuan88.australiacallcenter.annotation.SysLog;
import com.yanxuan88.australiacallcenter.mapper.SysMenuMapper;
import com.yanxuan88.australiacallcenter.model.dto.AddMenuDTO;
import com.yanxuan88.australiacallcenter.model.entity.SysMenu;
import com.yanxuan88.australiacallcenter.service.IMenuService;
import org.springframework.stereotype.Service;

@Service
public class MenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements IMenuService {
    @SysLog("新增菜单")
    @Override
    public boolean add(AddMenuDTO menu) {
        SysMenu entity = new SysMenu()
                .setIcon(menu.getIcon())
                .setName(menu.getName().trim())
                .setParentId(menu.getParentId())
                .setPerms(Strings.nullToEmpty(menu.getPerms()).trim())
                .setUrl(Strings.nullToEmpty(menu.getUrl()).trim())
                .setType(menu.getType())
                .setSort(menu.getSort());
        return save(entity);
    }
}
