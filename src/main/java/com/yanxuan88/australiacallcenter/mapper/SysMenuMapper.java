package com.yanxuan88.australiacallcenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yanxuan88.australiacallcenter.model.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    List<SysMenu> selectUserMenus(@Param("userId") Long userId);
}
