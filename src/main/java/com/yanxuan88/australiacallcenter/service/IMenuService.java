package com.yanxuan88.australiacallcenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yanxuan88.australiacallcenter.model.dto.AddMenuDTO;
import com.yanxuan88.australiacallcenter.model.dto.EditMenuDTO;
import com.yanxuan88.australiacallcenter.model.entity.SysMenu;
import com.yanxuan88.australiacallcenter.model.vo.MenuVO;

import java.util.List;

public interface IMenuService extends IService<SysMenu> {
    boolean add(AddMenuDTO menu);

    List<MenuVO> menus(Long userId);

    boolean rem(Long id);

    boolean edit(EditMenuDTO menu);
}
