package com.yanxuan88.australiacallcenter.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.yanxuan88.australiacallcenter.annotation.SysLog;
import com.yanxuan88.australiacallcenter.common.Constant;
import com.yanxuan88.australiacallcenter.common.IDict;
import com.yanxuan88.australiacallcenter.exception.BizException;
import com.yanxuan88.australiacallcenter.mapper.SysMenuMapper;
import com.yanxuan88.australiacallcenter.model.dto.AddMenuDTO;
import com.yanxuan88.australiacallcenter.model.dto.EditMenuDTO;
import com.yanxuan88.australiacallcenter.model.entity.SysMenu;
import com.yanxuan88.australiacallcenter.model.enums.MenuTypeEnum;
import com.yanxuan88.australiacallcenter.model.vo.MenuVO;
import com.yanxuan88.australiacallcenter.service.IMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements IMenuService {
    @SysLog("新增菜单")
    @Override
    public boolean add(AddMenuDTO menu) {
        MenuTypeEnum type = IDict.getByCode(MenuTypeEnum.class, menu.getType());
        if (type == MenuTypeEnum.MENU) {
            if (!StringUtils.hasText(menu.getUrl())) {
                throw new BizException("菜单路由不能为空");
            }

            if (!StringUtils.hasText(menu.getIcon())) {
                throw new BizException("菜单图标不能为空");
            }
        } else {
            menu.setIcon("");
            menu.setUrl("");
        }
        SysMenu entity = new SysMenu()
                .setIcon(Strings.nullToEmpty(menu.getIcon()).trim())
                .setName(menu.getName().trim())
                .setParentId(menu.getParentId())
                .setPerms(Strings.nullToEmpty(menu.getPerms()).trim())
                .setUrl(Strings.nullToEmpty(menu.getUrl()).trim())
                .setType(menu.getType())
                .setSort(menu.getSort());
        return save(entity);
    }

    @Override
    public List<MenuVO> menus(Long userId) {
        List<SysMenu> menus = new ArrayList<>();
        if (userId == Constant.SUPER_ADMIN) {
            // 超级管理员，查所有
            menus.addAll(list());
        } else {
            // todo 需要搞完用户、角色后再写
        }
        return menus.stream()
                .map(e -> new MenuVO()
                        .setId(e.getId())
                        .setName(e.getName())
                        .setIcon(e.getIcon())
                        .setPerms(e.getPerms())
                        .setUrl(e.getUrl())
                        .setSort(e.getSort())
                        .setType(e.getType())
                        .setParentId(e.getParentId()))
                .collect(Collectors.toList());
    }

    @SysLog("删除菜单")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean rem(Long id) {
        boolean result = removeById(id);
        if (result) {
            remove(Wrappers.<SysMenu>lambdaQuery().eq(SysMenu::getParentId, id));
        }
        return result;
    }

    @SysLog("编辑菜单")
    @Override
    public boolean edit(EditMenuDTO menu) {
        SysMenu entity = getById(menu.getId());
        if (entity == null) {
            throw new BizException("菜单不存在");
        }
        MenuTypeEnum type = IDict.getByCode(MenuTypeEnum.class, menu.getType());
        if (type == MenuTypeEnum.MENU) {
            if (!StringUtils.hasText(menu.getUrl())) {
                throw new BizException("菜单路由不能为空");
            }

            if (!StringUtils.hasText(menu.getIcon())) {
                throw new BizException("菜单图标不能为空");
            }
        } else {
            menu.setIcon("");
            menu.setUrl("");
        }
        entity.setIcon(Strings.nullToEmpty(menu.getIcon()).trim())
                .setName(menu.getName().trim())
                .setParentId(menu.getParentId())
                .setPerms(Strings.nullToEmpty(menu.getPerms()).trim())
                .setUrl(Strings.nullToEmpty(menu.getUrl()).trim())
                .setType(menu.getType())
                .setSort(menu.getSort());
        return updateById(entity);
    }
}
