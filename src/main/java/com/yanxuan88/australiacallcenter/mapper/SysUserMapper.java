package com.yanxuan88.australiacallcenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanxuan88.australiacallcenter.model.dto.UserQueryDTO;
import com.yanxuan88.australiacallcenter.model.entity.SysUser;
import com.yanxuan88.australiacallcenter.model.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    Page<UserVO> users(Page<SysUser> page, @Param("query") UserQueryDTO query);

    Page<UserVO> allUsers(@Param("page") Page<SysUser> page, @Param("query") UserQueryDTO query);

    Integer saUpdate(@Param("entity") SysUser entity);
}
